package top.bingk.jtable.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.rpc.RpcContext;
import top.bingk.jtable.config.Equation;
import top.bingk.jtable.config.Operator;
import top.bingk.jtable.config.SQLOperation;
import top.bingk.jtable.service.BusinessException;
import top.bingk.jtable.service.DatabaseException;
import top.bingk.jtable.service.IllegalAgumentException;
import top.bingk.jtable.service.Service;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.ActiveRecordException;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;

@SuppressWarnings({"unchecked", "rawtypes"})
public class Table<T extends Model> implements Service<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(Table.class);

    private Set<String> keySet = new HashSet<String>();
    private StringBuilder queryBody = new StringBuilder();
    private String queryPrimaryKey = "";
    private String[] primaryKey;
    private String queryTableName = "";
    private T tableModel;

    private Map<String, String> keyMap = new HashMap<String, String>();

    // 特殊业务需求，多系统下区别systemId。
    private static final String SYSTEM_ID = "systemId";
    private boolean hasSystemId = false;

    // 每次同时操作数据的最大值
    private static int eachAlterQuantity = 100;

    @SuppressWarnings("unused")
    private Table() {}

    public Table(Class<T> modelClass) {
        String tableName = getUnderLineClassName(modelClass);
        List<Record> columns = findColumns(tableName);
        if (columns == null || columns.size() == 0) {
            throw new RuntimeException("Columns initialization failed. Possible reason: The table does not exist.");
        }

        Set<String> primaryKeySet = new HashSet<String>();
        if (this.keySet.size() == 0) {
            for (Record column : columns) {
                String columnName = column.getStr("columnName");
                this.keySet.add(columnName);
                if (column.get("primaryKey") != null && column.getStr("primaryKey").equals("PRI")) {
                    primaryKeySet.add(columnName);
                    continue;
                }
                this.queryBody.append("`").append(columnName).append("`").append(", ");
                this.keyMap.put(columnName, "`" + columnName + "` = ?");
            }
        }

        if (primaryKeySet.size() > 0) {
            this.primaryKey = primaryKeySet.toArray(new String[primaryKeySet.size()]);
        }
        if (this.primaryKey.length == 0) {
            throw new RuntimeException("primaryKeys initialization failed.");
        }
        this.queryPrimaryKey = getPrimaryValue(this.primaryKey, 0, ",");
        this.queryTableName = "`" + tableName + "`";
        if (isFieldExist(SYSTEM_ID)) {
            this.hasSystemId = true;
        }

        try {
            this.tableModel = modelClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("表格对象model实例初始化失败", e);
            }
            throw new RuntimeException("primaryKeys initialization failed.", e);
        }
    }

    public static <T extends Model> Table<T> newInstance(Class<T> modelClass) {
        if (TableManager.hasTable(modelClass)) {
            return (Table<T>)TableManager.get(modelClass);
        }
        Table<T> table = new Table<T>(modelClass);
        TableManager.set(modelClass, table);
        return table;
    }

    @Override
    public boolean save(T model) throws BusinessException {
        try {
            return model.save();
        } catch (ActiveRecordException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("数据插入失败", e);
            }
            throw new DatabaseException(e);
        }
    }

    @Override
    public boolean saveForAny(Object... args) throws BusinessException {
        try {
            SqlPara sql = new SqlPara();
            String values = getWhereIn(args.length);
            for (Object arg : args) {
                sql.addPara(arg);
            }
            StringBuilder sqlBuilder =
                buildSql(SQLOperation.INSERT, SQLOperation.INTO, this.queryTableName, SQLOperation.VALUES, values);
            sql.setSql(sqlBuilder.toString());
            return Db.update(sql) == 1;
        } catch (ActiveRecordException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("数据插入失败", e);
            }
            throw new DatabaseException(e);
        }
    }

    @Override
    public boolean save(T model, Object... noRepetition) throws BusinessException {
        if (findForAny(noRepetition).size() > 0) {
            throw new DatabaseException(1098, new IllegalArgumentException());
        }
        try {
            return model.save();
        } catch (ActiveRecordException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("数据插入失败", e);
            }
            throw new DatabaseException(e);
        }
    }

    @Override
    public boolean save(T model, T noRepetition) throws BusinessException {
        if (findList(noRepetition).size() > 0) {
            throw new DatabaseException(1098, new IllegalArgumentException());
        }
        try {
            return model.save();
        } catch (ActiveRecordException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("数据插入失败", e);
            }
            throw new DatabaseException(e);
        }
    }

    @Override
    public boolean saveWithTransaction(T model, Model... models) throws BusinessException {
        try {
            return Db.tx(() -> {

                if (!model.save()) {
                    throw new SQLException("添加对象失败", "fail", 2102);
                }
                for (Model model2 : models) {
                    if (!model2.save()) {
                        throw new SQLException("添加附加对象失败", "fail", 2106);
                    }
                }
                return true;

            });
        } catch (ActiveRecordException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("数据插入失败", e);
            }
            throw new DatabaseException(e);
        }
    }

    @Override
    public boolean saveWithTransactionForList(T model, List<? extends Model>... models) throws BusinessException {
        try {
            return Db.tx(new IAtom() {

                @Override
                public boolean run() throws SQLException {
                    if (!model.save()) {
                        throw new SQLException("添加对象失败", "fail", 2102);
                    }
                    for (List<? extends Model> model2 : models) {
                        int[] batchSaveResult = Db.batchSave(model2, 100);
                        for (int result : batchSaveResult) {
                            if (result != 1) {
                                throw new SQLException("添加对象失败", "fail", 2102);
                            }
                        }
                    }
                    return true;
                }

            });
        } catch (ActiveRecordException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("数据插入失败", e);
            }
            throw new DatabaseException(e);
        }
    }

    @Override
    public boolean saveAsGroupForAny(List<Object>... args) throws BusinessException {
        List<T> saveList = new ArrayList<T>();
        for (int i = 0; i < args.length; i++) {
            T model = this.getCondition();
            int j = 0;
            for (String key : this.keySet) {
                model.put(key, args[i].get(j));
                j++;
            }
            saveList.add(model);
        }
        try {
            return saveAsGroup(saveList);
        } catch (ActiveRecordException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("数据插入失败", e);
            }
            throw new DatabaseException(e);
        }
    }

    @Override
    public boolean saveAsGroup(List<T> modelList, Object... noRepetition) throws BusinessException {
        if (findForAny(noRepetition).size() != 0) {
            throw new DatabaseException(1098, new IllegalArgumentException());
        }
        try {
            int[] saveResult = Db.batchSave(modelList, eachAlterQuantity);
            for (int result : saveResult) {
                if (result != 1) {
                    throw new DatabaseException(2102, new IllegalArgumentException());
                }
            }
            return true;
        } catch (ActiveRecordException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("数据插入失败", e);
            }
            throw new DatabaseException(e);
        }
    }

    @Override
    public boolean saveAsGroup(List<T> modelList, T noRepetition) throws BusinessException {
        if (findList(noRepetition).size() != 0) {
            throw new DatabaseException(1098, new IllegalArgumentException());
        }
        try {
            int[] saveResult = Db.batchSave(modelList, eachAlterQuantity);
            for (int result : saveResult) {
                if (result != 1) {
                    throw new DatabaseException(2102, new IllegalArgumentException());
                }
            }
            return true;
        } catch (ActiveRecordException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("数据插入失败", e);
            }
            throw new DatabaseException(e);
        }
    }

    @Override
    public boolean saveAsGroup(List<T> modelList) throws BusinessException {
        try {
            int[] saveResult = Db.batchSave(modelList, eachAlterQuantity);
            for (int result : saveResult) {
                if (result != 1) {
                    throw new DatabaseException(2102, new IllegalArgumentException());
                }
            }
            return true;
        } catch (ActiveRecordException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("数据插入失败", e);
            }
            throw new DatabaseException(e);
        }
    }

    @Override
    public boolean saveAsGroupWithTransactionForList(List<T> modelList, List<? extends Model>... modelLists)
        throws BusinessException {
        try {
            return Db.tx(new IAtom() {

                @Override
                public boolean run() throws SQLException {
                    int[] saveResult = Db.batchSave(modelList, 100);
                    for (int result : saveResult) {
                        if (result != 1) {
                            throw new SQLException("添加对象失败", "fail", 2102);
                        }
                    }
                    for (List<? extends Model> list : modelLists) {
                        int[] save2Result = Db.batchSave(list, 100);
                        for (int result : save2Result) {
                            if (result != 1) {
                                throw new SQLException("添加附加对象失败", "fail", 2102);
                            }
                        }
                    }
                    return true;
                }

            });
        } catch (ActiveRecordException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("数据插入失败", e);
            }
            throw new DatabaseException(e);
        }
    }

    @Override
    public boolean saveAsGroupWithTransaction(List<T> modelList, Model... modelLists) throws BusinessException {
        try {
            return Db.tx(new IAtom() {

                @Override
                public boolean run() throws SQLException {
                    int[] saveResult = Db.batchSave(modelList, 100);
                    for (int result : saveResult) {
                        if (result != 1) {
                            throw new SQLException("添加对象失败", "fail", 2102);
                        }
                    }
                    for (Model model : modelLists) {
                        boolean saveResult2 = model.save();
                        if (!saveResult2) {
                            throw new SQLException("添加附加对象失败", "fail", 2102);
                        }
                    }
                    return true;
                }

            });
        } catch (ActiveRecordException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("数据插入失败", e);
            }
            throw new DatabaseException(e);
        }
    }

    @Override
    public boolean deleteForAny(T condition) throws BusinessException {
        SqlPara sql = accumulate(getDeleteBody(), getWhereQuery(condition, null, null));
        condition.remove(Condition.CONDITION_BUILDER);
        try {
            return Db.update(sql) > 0;
        } catch (ActiveRecordException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("数据删除失败", e);
            }
            throw new DatabaseException(e);
        }
    }

    @Override
    public boolean delete(T model) throws BusinessException {
        try {
            return model.delete();
        } catch (ActiveRecordException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("数据删除失败", e);
            }
            throw new DatabaseException(e);
        }
    }

    @Override
    public boolean deleteWithTransaction(T model, Model... models) throws BusinessException {
        try {
            return Db.tx(new IAtom() {

                @Override
                public boolean run() throws SQLException {
                    boolean saveResult = model.delete();
                    if (!saveResult) {
                        throw new SQLException("删除对象失败", "fail", 2110);
                    }
                    for (Model model : models) {
                        boolean saveResult2 = model.delete();
                        if (!saveResult2) {
                            throw new SQLException("删除附加对象失败", "fail", 2114);
                        }
                    }
                    return true;
                }

            });
        } catch (ActiveRecordException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("数据删除失败", e);
            }
            throw new DatabaseException(e);
        }
    }

    @Override
    public boolean deleteWithTransactionForList(T model, List<? extends Model>... modelLists) throws BusinessException {
        try {
            return Db.tx(new IAtom() {

                @Override
                public boolean run() throws SQLException {
                    boolean saveResult = model.delete();
                    if (!saveResult) {
                        throw new SQLException("删除对象失败", "fail", 2110);
                    }
                    for (List<? extends Model> model : modelLists) {
                        boolean saveResult2;
                        try {
                            saveResult2 = deleteAsGroupExtra(model);
                            if (!saveResult2) {
                                throw new SQLException("删除附加对象失败", "fail", 2114);
                            }
                        } catch (BusinessException e) {
                            if (LOGGER.isErrorEnabled()) {
                                LOGGER.error("数据删除失败", e);
                            }
                            throw new SQLException("删除附加对象失败", "fail", 2114);
                        }
                    }
                    return true;
                }

            });
        } catch (ActiveRecordException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("数据删除失败", e);
            }
            throw new DatabaseException(e);
        }
    }

    @Override
    public boolean delete(Object... primaryKey) throws BusinessException {
        SqlPara sql = accumulate(getDeleteBody(), getPrimaryWhereQuery(primaryKey));
        try {
            return Db.update(sql) == 1;
        } catch (ActiveRecordException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("数据删除失败", e);
            }
            throw new DatabaseException(e);
        }
    }

    @Override
    public boolean deleteAsGroup(List<T> modelList) throws BusinessException {
        List<SqlPara> sqlParaList = new ArrayList<SqlPara>();
        for (String primaryKey : this.primaryKey) {
            sqlParaList.add(getPrimaryWhereQuery(primaryKey, modelList.stream().map(e -> e.get(primaryKey)).toArray()));
        }
        SqlPara sql = accumulate(getDeleteBody(), sqlParaList);
        try {
            return Db.update(sql) > 0;
        } catch (ActiveRecordException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("数据删除失败", e);
            }
            throw new DatabaseException(e);
        }
    }

    private boolean deleteAsGroupExtra(List<? extends Model> modelList) throws BusinessException {
        List<SqlPara> sqlParaList = new ArrayList<SqlPara>();
        for (String primaryKey : this.primaryKey) {
            sqlParaList.add(getPrimaryWhereQuery(primaryKey, modelList.stream().map(e -> e.get(primaryKey)).toArray()));
        }
        SqlPara sql = accumulate(getDeleteBody(), sqlParaList);
        try {
            return Db.update(sql) > 0;
        } catch (ActiveRecordException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("数据删除失败", e);
            }
            throw new DatabaseException(e);
        }
    }

    @Override
    public boolean deleteAsGroupWithTransaction(List<T> modelList, Model... models) throws BusinessException {
        try {
            return Db.tx(new IAtom() {

                @Override
                public boolean run() throws SQLException {
                    boolean saveResult;
                    try {
                        saveResult = deleteAsGroup(modelList);
                        if (!saveResult) {
                            throw new SQLException("删除对象失败", "fail", 2110);
                        }
                    } catch (BusinessException e) {
                        if (LOGGER.isErrorEnabled()) {
                            LOGGER.error("数据删除失败", e);
                        }
                        throw new SQLException("删除对象失败", "fail", 2110);
                    }
                    for (Model model : models) {
                        boolean saveResult2 = model.delete();
                        if (!saveResult2) {
                            throw new SQLException("删除附加对象失败", "fail", 2114);
                        }
                    }
                    return true;
                }

            });
        } catch (ActiveRecordException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("数据删除失败", e);
            }
            throw new DatabaseException(e);
        }
    }

    @Override
    public boolean deleteAsGroupWithTransactionForList(List<T> modelList, List<? extends Model>... modelLists)
        throws BusinessException {
        try {
            return Db.tx(new IAtom() {

                @Override
                public boolean run() throws SQLException {
                    boolean saveResult;
                    try {
                        saveResult = deleteAsGroup(modelList);
                        if (!saveResult) {
                            throw new SQLException("删除对象失败", "fail", 2110);
                        }
                    } catch (BusinessException e) {
                        if (LOGGER.isErrorEnabled()) {
                            LOGGER.error("数据删除失败", e);
                        }
                        throw new SQLException("删除对象失败", "fail", 2110);
                    }

                    for (List<? extends Model> model : modelLists) {
                        boolean saveResult2;
                        try {
                            saveResult2 = deleteAsGroupExtra(model);
                            if (!saveResult2) {
                                throw new SQLException("删除附加对象失败", "fail", 2114);
                            }
                        } catch (BusinessException e) {
                            if (LOGGER.isErrorEnabled()) {
                                LOGGER.error("数据删除失败", e);
                            }
                            throw new SQLException("删除附加对象失败", "fail", 2114);
                        }

                    }
                    return true;
                }

            });
        } catch (ActiveRecordException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("数据删除失败", e);
            }
            throw new DatabaseException(e);
        }
    }

    @Override
    public boolean deleteAsGroup(Object[]... primaryKeySet) throws BusinessException {
        List<SqlPara> paraList = new ArrayList<SqlPara>();
        for (Object[] keySet : primaryKeySet) {
            paraList.add(getPrimaryWhereQuery(keySet));
        }
        SqlPara sql = accumulate(getDeleteBody(), paraList);
        try {
            return Db.update(sql) > 0;
        } catch (ActiveRecordException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("数据删除失败", e);
            }
            throw new DatabaseException(e);
        }
    }

    @Override
    @Deprecated
    public boolean deleteAll() throws BusinessException {
        try {
            return Db.update(getDeleteBody()) > 0;
        } catch (ActiveRecordException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("表格删除失败", e);
            }
            throw new DatabaseException(e);
        }
    }

    @Override
    public boolean update(T model) throws BusinessException {
        try {
            return model.update();
        } catch (ActiveRecordException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("数据更新失败", e);
            }
            throw new DatabaseException(e);
        }
    }

    @Override
    public boolean update(T model, T conditation) throws BusinessException {
        SqlPara sql = accumulate(getUpdateBody(model), getWhereQuery(conditation, null, null));
        try {
            return Db.update(sql) == 1;
        } catch (ActiveRecordException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("数据更新失败", e);
            }
            throw new DatabaseException(e);
        }
    }

    @Override
    public boolean updateWithTransaction(T model, Model... models) throws BusinessException {
        try {
            return Db.tx(new IAtom() {

                @Override
                public boolean run() throws SQLException {
                    boolean saveResult = model.update();
                    if (!saveResult) {
                        throw new SQLException("修改对象失败", "fail", 2118);
                    }
                    for (Model model : models) {
                        boolean saveModelsResult = model.update();
                        if (!saveModelsResult) {
                            throw new SQLException("修改附加对象失败", "fail", 2122);
                        }
                    }
                    return true;
                }

            });
        } catch (ActiveRecordException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("数据更新失败", e);
            }
            throw new DatabaseException(e);
        }
    }

    @Override
    public boolean updateWithTransactionForList(T model, List<? extends Model>... models) throws BusinessException {
        try {
            return Db.tx(new IAtom() {

                @Override
                public boolean run() throws SQLException {
                    boolean saveResult = model.update();
                    if (!saveResult) {
                        throw new SQLException("修改对象失败", "fail", 2118);
                    }
                    for (List<? extends Model> list : models) {
                        int[] saveModelsResult = Db.batchUpdate(list, 100);
                        for (int result : saveModelsResult) {
                            if (result != 1) {
                                throw new SQLException("修改附加对象失败", "fail", 2122);
                            }
                        }
                    }
                    return true;
                }

            });
        } catch (ActiveRecordException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("数据更新失败", e);
            }
            throw new DatabaseException(e);
        }
    }

    @Override
    public boolean updateAsGroup(List<T> modelList) throws BusinessException {
        try {
            int[] saveModelsResult = Db.batchUpdate(modelList, eachAlterQuantity);
            for (int result : saveModelsResult) {
                if (result != 1) {
                    throw new DatabaseException(2118, new IllegalArgumentException());
                }
            }
            return true;
        } catch (ActiveRecordException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("数据更新失败", e);
            }
            throw new DatabaseException(e);
        }
    }

    @Override
    public boolean updateAsGroup(List<T> modelList, T conditation) throws BusinessException {
        SqlPara sql = new SqlPara();
        StringBuilder modelSql = new StringBuilder();
        for (T model : modelList) {
            SqlPara updateSql = getUpdateBody(model);
            SqlPara whereSql = getWhereQuery(conditation, null, null);
            modelSql.append(updateSql.getSql());
            modelSql.append(" ").append(SQLOperation.WHERE).append(" 1");
            modelSql.append(whereSql.getSql());
            modelSql.append(";");
            addPara(sql, updateSql);
            addPara(sql, updateSql);
        }
        sql.setSql(modelSql.toString());
        try {
            return Db.update(sql) == modelList.size();
        } catch (ActiveRecordException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("数据更新失败", e);
            }
            throw new DatabaseException(e);
        }
    }

    @Override
    public boolean updateAsGroupWithTransaction(List<T> modelList, Model... models) throws BusinessException {
        try {
            return Db.tx(new IAtom() {

                @Override
                public boolean run() throws SQLException {
                    int[] saveResult = Db.batchUpdate(modelList, 100);
                    for (int result : saveResult) {
                        if (result != 1) {
                            throw new SQLException("修改对象失败", "fail", 2118);
                        }
                    }
                    for (Model model : models) {
                        boolean saveModelsResult = model.update();
                        if (!saveModelsResult) {
                            throw new SQLException("修改附加对象失败", "fail", 2122);
                        }
                    }
                    return true;
                }

            });
        } catch (ActiveRecordException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("数据更新失败", e);
            }
            throw new DatabaseException(e);
        }
    }

    @Override
    public boolean updateAsGroupWithTransactionForList(List<T> modelList, List<? extends Model>... modelLists)
        throws BusinessException {
        try {
            return Db.tx(new IAtom() {

                @Override
                public boolean run() throws SQLException {
                    int[] saveResult = Db.batchUpdate(modelList, 100);
                    for (int result : saveResult) {
                        if (result != 1) {
                            throw new SQLException("修改对象失败", "fail", 2118);
                        }
                    }
                    for (List<? extends Model> list : modelLists) {
                        int[] saveModelsResult = Db.batchUpdate(list, 100);
                        for (int result : saveModelsResult) {
                            if (result != 1) {
                                throw new SQLException("修改附加对象失败", "fail", 2122);
                            }
                        }
                    }
                    return true;
                }

            });
        } catch (ActiveRecordException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("数据更新失败", e);
            }
            throw new DatabaseException(e);
        }
    }

    @Override
    public T findById(Object... primaryKey) throws BusinessException {
        if (primaryKey.length != this.primaryKey.length) {
            throw new IllegalAgumentException(10106, new IllegalArgumentException());
        }
        SqlPara sql = accumulate(getSelectBody(), getPrimaryWhereQuery(primaryKey));
        try {
            return (T)tableModel.dao().findFirst(sql);
        } catch (ActiveRecordException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("数据查询失败，可能原因：主键为空或主键长度与表格主键长度不一致。", e);
            }
            throw new DatabaseException(e);
        }
    }

    public List<T> findForAny(Object... args) throws BusinessException {
        SqlPara sql = accumulate(getSelectBody(), getWhere(args));
        try {
            return (List<T>)tableModel.find(sql);
        } catch (ActiveRecordException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("数据查询失败，可能原因：列数或数据格式不正确。", e);
            }
            throw new DatabaseException(e);
        }
    }

    @Override
    public T find(T condition) throws BusinessException {
        SqlPara sql = accumulate(getSelectBody(), getWhereQuery(condition, null, null));
        condition.remove(Condition.CONDITION_BUILDER);
        try {
            return (T)tableModel.findFirst(sql);
        } catch (ActiveRecordException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("数据查询失败，可能原因：列不存在。", e);
            }
            throw new DatabaseException(e);
        }
    }

    @Override
    public List<T> findByIdAsGroup(Long[] idSet) throws BusinessException {
        if (idSet.length == 0) {
            return new ArrayList<T>();
        }
        if (this.primaryKey.length != 1) {
            throw new IllegalAgumentException(10106, new IllegalArgumentException());
        }

        SqlPara sql = accumulate(getSelectBody(), getPrimaryWhereQuery(idSet));
        try {
            return (List<T>)tableModel.find(sql);
        } catch (ActiveRecordException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("数据查询失败", e);
            }
            throw new DatabaseException(e);
        }
    }

    @Override
    public List<T> findByIdsAsGroup(Long[]... idSets) throws BusinessException {
        if (this.primaryKey.length != idSets.length) {
            throw new IllegalAgumentException(10106, new IllegalArgumentException());
        }

        List<SqlPara> sqlParaList = new ArrayList<SqlPara>();
        for (Long[] idSet : idSets) {
            sqlParaList.add(getPrimaryWhereQuery(idSet));
        }
        SqlPara sql = accumulate(getSelectBody(), sqlParaList);
        try {
            return (List<T>)tableModel.find(sql);
        } catch (ActiveRecordException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("数据查询失败", e);
            }
            throw new DatabaseException(e);
        }
    }

    @Override
    public List<T> findList() throws BusinessException {
        SqlPara sql = accumulate(getSelectBody());
        try {
            return (List<T>)tableModel.find(sql);
        } catch (ActiveRecordException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("数据查询失败", e);
            }
            throw new DatabaseException(e);
        }
    }

    @Override
    public List<T> findList(T condition) throws BusinessException {
        SqlPara sql = accumulate(getSelectBody(), getWhereQuery(condition, null, null));
        condition.remove(Condition.CONDITION_BUILDER);
        try {
            return (List<T>)tableModel.find(sql);
        } catch (ActiveRecordException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("数据查询失败", e);
            }
            throw new DatabaseException(e);
        }
    }

    @Override
    public Page<T> findListInPage(int pageNumber, int pageSize) throws BusinessException {
        SqlPara sql = accumulate(getSelectBody());
        try {
            return (Page<T>)tableModel.paginate(pageNumber, pageSize, sql);
        } catch (ActiveRecordException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("数据查询失败", e);
            }
            throw new DatabaseException(e);
        }
    }

    @Override
    public Page<T> findListInPageWithKeywords(int pageNumber, int pageSize, T condition) throws BusinessException {
        SqlPara sql = accumulate(getSelectBody(), getWhereQuery(condition, null, null));
        condition.remove(Condition.CONDITION_BUILDER);
        try {
            return (Page<T>)tableModel.paginate(pageNumber, pageSize, sql);
        } catch (ActiveRecordException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("数据查询失败", e);
            }
            throw new DatabaseException(e);
        }
    }

    @Override
    public Page<T> findListInPage(int pageNumber, int pageSize, String sortBy, String sortOrder)
        throws BusinessException {
        SqlPara sql = accumulate(getSelectBody());
        sql.setSql(sql.getSql() + getSortBy(sortBy) + getSortOrder(sortOrder));
        try {

            return (Page<T>)tableModel.paginate(pageNumber, pageSize, sql);
        } catch (ActiveRecordException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("数据查询失败", e);
            }
            throw new DatabaseException(e);
        }
    }

    @Override
    public Page<T> findListInPageWithKeywords(int pageNumber, int pageSize, T condition, String sortBy,
        String sortOrder) throws BusinessException {

        SqlPara sql = accumulate(getSelectBody(), getWhereQuery(condition, sortBy, sortOrder));
        condition.remove(Condition.CONDITION_BUILDER);
        try {
            return (Page<T>)tableModel.paginate(pageNumber, pageSize, sql);
        } catch (ActiveRecordException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("数据查询失败", e);
            }
            throw new DatabaseException(e);
        }
    }

    /**
     * 接近于sql语句直接查询，可适配多数情况
     * 
     * @param body 查询体
     * @param where 查询条件
     * @return 查询结果
     * @throws BusinessExceptionv 不符合规范的sql语句及条件进行操作时将抛出
     * @version 2.0
     * @author DoubleCome
     */
    public List<T> find(String body, String where) throws BusinessException {
        if (body.equals("")) {
            body = getSelectBody().getSql();
        }
        StringBuilder sql = buildSql(body, where);
        try {
            return (List<T>)tableModel.find(sql.toString());
        } catch (ActiveRecordException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("数据查询失败", e);
            }
            throw new DatabaseException(e);
        }
    }

    /**
     * 接近于sql语句直接查询，可适配多数情况
     * 
     * @param body 查询体
     * @param where 查询条件
     * @return 查询结果
     * @throws BusinessException 不符合规范的sql语句及条件进行操作时将抛出
     * @version 2.0
     * @author DoubleCome
     */
    public List<T> findList(String body, String where) throws BusinessException {
        StringBuilder sql = buildSql(SQLOperation.SELECT, body, SQLOperation.FROM, this.queryTableName, where);
        try {
            return (List<T>)tableModel.find(sql.toString());
        } catch (ActiveRecordException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("数据查询失败", e);
            }
            throw new DatabaseException(e);
        }
    }

    /**
     * 接近于sql语句直接查询，可适配多数情况
     * 
     * @param body 查询体
     * @param where 查询条件
     * @return 查询结果
     * @throws BusinessException 不符合规范的sql语句及条件进行操作时将抛出
     * @version 2.0
     * @author DoubleCome
     */
    public Page<T> findPage(int pageNumber, int pageSize, String body, String where) throws BusinessException {
        StringBuilder sql = buildSql(SQLOperation.SELECT, body, SQLOperation.FROM, this.queryTableName, where);
        SqlPara sqlPara = new SqlPara();
        sqlPara.setSql(sql.toString());
        try {
            return (Page<T>)tableModel.paginate(pageNumber, pageSize, sqlPara);
        } catch (ActiveRecordException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("数据查询失败", e);
            }
            throw new DatabaseException(e);
        }
    }

    @Override
    public int execute(String key, Map<?, ?> data) throws BusinessException {
        SqlPara sqlPara = Db.getSqlPara(key, data);
        return Db.update(sqlPara);
    }

    @Override
    public T find(String key, Map<?, ?> data) throws BusinessException {
        SqlPara sqlPara = Db.getSqlPara(key, data);
        try {
            return (T)tableModel.dao().findFirst(sqlPara);
        } catch (ActiveRecordException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("数据查询失败", e);
            }
            throw new DatabaseException(e);
        }
    }

    @Override
    public List<T> findList(String key, Map<?, ?> data) throws BusinessException {
        SqlPara sqlPara = Db.getSqlPara(key, data);
        try {
            return (List<T>)tableModel.dao().find(sqlPara);
        } catch (ActiveRecordException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("数据查询失败", e);
            }
            throw new DatabaseException(e);
        }
    }

    public SqlPara getDeleteBody() {
        SqlPara sql = new SqlPara();
        sql.setSql(buildSql(SQLOperation.DELETE, SQLOperation.FROM, this.queryTableName).toString());
        return sql;
    }

    public SqlPara getUpdateBody(T model) {
        SqlPara sql = new SqlPara();
        StringBuilder sqlBuilder = buildSql(SQLOperation.UPDATE, this.queryTableName, SQLOperation.SET);
        Set<Entry<String, Object>> entrySet = model._getAttrsEntrySet();
        for (Entry<String, Object> entry : entrySet) {
            sql.addPara(entry.getValue());
            sqlBuilder.append(" ").append(this.keyMap.get(entry.getKey())).append(",");
        }
        sql.setSql(sqlBuilder.toString().substring(0, sqlBuilder.toString().lastIndexOf(",")));
        return sql;
    }

    public SqlPara getSelectBody() {
        SqlPara sql = new SqlPara();
        sql.setSql(
            buildSql(SQLOperation.SELECT, this.queryBody, getPrimaryKeyQuery(), SQLOperation.FROM, this.queryTableName)
                .toString());
        return sql;
    }

    public String getSortBy(String sortOrder) throws BusinessException {
        if (!isFieldExist(sortOrder)) {
            throw new IllegalAgumentException(10102, new IllegalArgumentException());
        }
        return buildSql(SQLOperation.ORDER, SQLOperation.BY, "`" + sortOrder + "`").toString();
    }

    public String getSortOrder(String sortOrder) throws BusinessException {
        if (SQLOperation.DESC.toString().equals(sortOrder.toUpperCase())) {
            return SQLOperation.DESC.toString();
        }
        if (SQLOperation.ASC.toString().equals(sortOrder.toUpperCase())) {
            return SQLOperation.ASC.toString();
        }
        throw new IllegalAgumentException(10098, new IllegalArgumentException());
    }

    public SqlPara getPrimaryWhereQuery(String key, Object[] primaryKeys) {
        SqlPara sql = new SqlPara();
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append(" ").append(SQLOperation.AND).append(" ").append("`" + key + "`").append(" ")
            .append(SQLOperation.IN).append(" ").append(getWhereIn(primaryKeys.length));
        for (Object primaryKey : primaryKeys) {
            sql.addPara(primaryKey);
        }
        sql.setSql(sqlBuilder.toString());
        return sql;
    }

    public SqlPara getPrimaryWhereQuery(Object[] primaryKeys) {
        SqlPara sql = new SqlPara();
        StringBuilder sqlBuilder = new StringBuilder();
        for (String keyPrimary : this.primaryKey) {
            sqlBuilder.append(" ").append(SQLOperation.AND).append(" ").append("`" + keyPrimary + "`").append(" ")
                .append(SQLOperation.IN).append(" ").append(getWhereIn(primaryKeys.length));
            for (Object primaryKey : primaryKeys) {
                sql.addPara(primaryKey);
            }
        }
        sql.setSql(sqlBuilder.toString());
        return sql;
    }

    public SqlPara getPrimaryWhereQuery(Object primaryKey) {
        SqlPara sql = new SqlPara();
        StringBuilder sqlBuilder = new StringBuilder();
        for (String keyPrimary : this.primaryKey) {
            sql.addPara(primaryKey);
            sqlBuilder.append(" ").append(SQLOperation.AND).append(" ").append(this.keyMap.get(keyPrimary));
        }
        sql.setSql(sqlBuilder.toString());
        return sql;
    }

    public String getPrimaryKeyQuery() {
        return this.queryPrimaryKey;
    }

    public SqlPara getWhere(Object value) {
        SqlPara sql = new SqlPara();
        StringBuilder whereQuery = new StringBuilder();
        for (String key : this.keySet) {
            whereQuery.append(" ").append(SQLOperation.AND).append(" ").append(this.keyMap.get(key));
            if (value.getClass().isArray()) {
                for (Object para : (Object[])value) {
                    sql.addPara(para);
                }
                continue;
            }
            sql.addPara(value);
        }
        sql.setSql(whereQuery.toString());
        return sql;
    }

    public SqlPara getWhere(Object[] values) {
        SqlPara sql = new SqlPara();
        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append(getWhereIn(values.length));
        StringBuilder whereQuery = new StringBuilder();

        for (String key : this.primaryKey) {
            whereQuery.append(buildSql("AND `" + key + "`", SQLOperation.IN, sqlQuery));
            for (Object value : values) {
                sql.addPara(value);
            }
        }
        sql.setSql(whereQuery.toString());
        return sql;
    }

    public SqlPara getWhereQuery(String columnName, Object value, boolean isAppendWhere) throws BusinessException {
        if (!isFieldExist(columnName)) {
            throw new IllegalAgumentException(10094, new IllegalArgumentException());
        }
        SqlPara sql = new SqlPara();
        sql.addPara(value);
        sql.setSql(this.keyMap.get(columnName));
        return sql;
    }

    public SqlPara getWhereQuery(String columnName, Object[] values, boolean isAppendWhere) throws BusinessException {
        if (!isFieldExist(columnName)) {
            throw new IllegalAgumentException(10094, new IllegalArgumentException());
        }
        SqlPara sql = new SqlPara();
        for (Object value : values) {
            sql.addPara(value);
        }
        sql.setSql(buildSql("`" + columnName + "`", SQLOperation.IN, getWhereIn(values.length)).toString());
        return sql;
    }

    private boolean isCover(T condition, String key) {
        if (!isAttachCondition(condition)) {
            return false;
        }

        List<Condition> attachList = getCondition(condition);
        if (attachList.size() == 0) {
            return false;
        }

        return attachList.stream().filter(e -> key.equals(e.getBuilder().getFieldName()) && e.getIsCover()).findFirst()
            .isPresent();

    }

    private SqlPara getWhereQuery(T condition, String sortBy, String sortOrder) {
        SqlPara sql = new SqlPara();
        StringBuilder sqlBuilder = new StringBuilder();
        StringBuilder sortBuilder = new StringBuilder();

        Set<Entry<String, Object>> entrySet = condition._getAttrsEntrySet();
        for (Entry<String, Object> entry : entrySet) {
            if (this.keySet.contains(entry.getKey()) && !isCover(condition, entry.getKey())) {
                if (entry.getValue().getClass().isArray()) {
                    sqlBuilder.append(buildSql(SQLOperation.AND, "`" + entry.getKey() + "`", SQLOperation.IN,
                        getWhereIn(((Object[])entry.getValue()).length)));
                    for (Object subValues : (Object[])entry.getValue()) {
                        sql.addPara(subValues);
                    }
                    continue;
                }
                sqlBuilder.append(" ").append(SQLOperation.AND).append(" ").append("`" + entry.getKey() + "`")
                    .append(" = ?");
                sql.addPara(entry.getValue());
            }
        }

        if (!isAttachCondition(condition)) {
            if (sortBy != null && sortOrder != null) {
                sql.setSql(sqlBuilder.toString() + getSortBy(sortBy) + " " + getSortOrder(sortOrder));
            } else {
                sql.setSql(sqlBuilder.toString());
            }
            return sql;
        }

        List<Condition> attachList = getCondition(condition);
        if (attachList.size() > 0) {
            attachList.get(0).getBuilder().term(Operator.AND);
        }

        attachList.stream().forEach(attach -> {

            String key = (String)attach.getBuilder().getFieldName();
            Object value = attach.getBuilder().getValue() == null ? condition.get(key) : attach.getBuilder().getValue();

            if (value.getClass().isArray()) {
                sqlBuilder.append(buildSql(attach.getBuilder().getTerm(), "`" + key + "`", SQLOperation.IN,
                    getWhereIn(((Object[])value).length)));
                for (Object subValues : (Object[])value) {
                    sql.addPara(subValues);
                }
                return;
            }

            sqlBuilder.append(" ").append(attach.getBuilder().getTerm()).append(" ").append("`" + key + "`").append(" ")
                .append(caseType(attach.getBuilder().getEquation()));
            sql.addPara(value);

        });

        attachList.sort(new Comparator<Condition>() {
            @Override
            public int compare(Condition o1, Condition o2) {
                return o1.getBuilder().getSortLevel() - o2.getBuilder().getSortLevel();
            }
        });

        attachList.stream().filter(e -> e.getBuilder().getSortLevel() != 0).forEach(attach -> {
            sortBuilder.append("`" + attach.getBuilder().getFieldName() + "`").append(" ")
                .append(attach.getBuilder().getSort()).append(", ");
        });

        if (!"".equals(sortBuilder.toString())) {
            sqlBuilder.append(buildSql(SQLOperation.ORDER, SQLOperation.BY,
                sortBuilder.toString().substring(0, sortBuilder.toString().lastIndexOf(","))));
            if (sortBy != null && sortOrder != null) {
                sqlBuilder.append(" ,").append("`" + sortBy + "`").append(" ").append(sortOrder);
            }
        } else {
            if (sortBy != null && sortOrder != null) {
                sqlBuilder.append(" ").append(getSortBy(sortBy)).append(" ").append(getSortOrder(sortOrder));
            }
        }

        sql.setSql(sqlBuilder.toString());
        return sql;
    }

    private boolean isFieldExist(String columnName) {
        return queryBody.toString().contains(columnName) || queryPrimaryKey.contains(columnName);
    }

    public boolean isAttachCondition(Model<T> model) {
        return model.get(Condition.CONDITION_BUILDER) != null;
    }

    public List<Condition> getCondition(Model<T> model, String key) {
        if (isAttachCondition(model)) {
            List<Condition> conditionList = (List<Condition>)model.get(Condition.CONDITION_BUILDER);
            return conditionList.stream().filter(e -> key.equals(e.getBuilder().getFieldName()))
                .collect(Collectors.toList());
        }
        List<Condition> emptyList = new ArrayList<Condition>();
        return emptyList;
    }

    public List<Condition> getCondition(Model<T> model) {
        return (List<Condition>)model.get(Condition.CONDITION_BUILDER);
    }

    private StringBuilder buildSql(Object... sqls) {
        StringBuilder sql = new StringBuilder();
        for (Object s : sqls) {
            if (s == null || s.toString().equals("")) {
                continue;
            }
            sql.append(" ").append(s.toString());
        }
        return sql;
    }

    public T getCondition() {
        try {
            return (T)this.tableModel.getClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("对象初始化失败", e);
            }
            throw new RuntimeException(e);
        }
    }

    public static void setEachAlterQuantity(int setEachAlterQuantity) {
        if (setEachAlterQuantity <= 0) {
            throw new RuntimeException("Illegal eachAlterQuantity.");
        }
        eachAlterQuantity = setEachAlterQuantity;
    }

    private static final Pattern pattern = Pattern.compile("[A-Z]([a-z\\d]+)?");

    private static String camel2Underline(String line) {
        line = line.replaceAll("class ", "");
        if (line == null || "".equals(line)) {
            return "";
        }
        if (line.contains(".")) {
            line = line.split("\\.")[line.split("\\.").length - 1];
        }
        line = String.valueOf(line.charAt(0)).toUpperCase().concat(line.substring(1));
        StringBuffer sb = new StringBuffer();
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            String word = matcher.group();
            sb.append(word.toLowerCase());
            sb.append(matcher.end() == line.length() ? "" : "_");
        }
        return sb.toString();
    }

    private static <T> String getUnderLineClassName(Class<T> modelClass) {
        if (modelClass == null) {
            throw new NullPointerException("can not get null class underLineName.");
        }
        String modelClassName = modelClass.getName();
        modelClassName = camel2Underline(modelClassName);
        return modelClassName;
    }

    private String readTxtFileAll(String fileP) {
        try {

            String filePath = String.valueOf(Thread.currentThread().getContextClassLoader().getResource(""));
            filePath = filePath.replaceAll("file:/", "");
            filePath = filePath.replaceAll("%20", " ");
            filePath = filePath.trim() + fileP.trim();
            if (filePath.indexOf(":") != 1) {
                filePath = File.separator + filePath;
            }
            String encoding = "utf-8";
            File file = new File(filePath);
            if (file.isFile() && file.exists()) {
                InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);
                BufferedReader bufferedReader = new BufferedReader(read);
                StringBuffer sb = new StringBuffer();
                String lineTxt = null;
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    sb.append(lineTxt);
                }
                read.close();
                return sb.toString();
            } else {
                System.out.println("找不到指定的文件,查看此路径是否正确:" + filePath);
            }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
        }
        return "";
    }

    private String getWhereIn(int length) {
        StringBuilder value = new StringBuilder();
        value.append("(");
        for (int i = 0; i < length; i++) {
            value.append("?,");
        }
        String returnString = value.toString();
        return returnString.substring(0, returnString.length() - 1) + ")";
    }

    private String getPrimaryValue(Object[] loopObject, int markLocation, String append) {
        StringBuilder value = new StringBuilder();
        int index = 0;
        for (Object loop : loopObject) {
            if (index != markLocation) {
                value.append(append).append("`" + loop + "`");
            } else {
                value.append("`" + loop + "`");
            }
        }
        return value.toString();
    }

    public String caseType(Equation type) {
        switch (type) {
            case EQUAL:
                return " = ?";
            case LEFT_LIKE:
                return "LIKE CONCAT('%', ?)";
            case RIGHT_LIKE:
                return "LIKE CONCAT(?, '%')";
            case ALL_LIKE:
                return "LIKE CONCAT('%', ?, '%')";
            case LESS_THAN:
                return " <= ?";
            case MORE_THAN:
                return " >= ?";
            case LESS:
                return " < ?";
            case MORE:
                return " > ?";
            default:
                return "= ?";
        }
    }

    public void addPara(SqlPara sql1, SqlPara sql2) {
        for (Object param : sql2.getPara()) {
            sql1.addPara(param);
        }
    }

    public SqlPara accumulate(SqlPara body, List<SqlPara> sqlParas) {
        SqlPara[] paraArray = new SqlPara[sqlParas.size()];
        return accumulate(body, sqlParas.toArray(paraArray));
    }

    public SqlPara accumulate(SqlPara body, SqlPara... sqlParas) {
        StringBuilder sqlBuilder = new StringBuilder(body.getSql());
        where(sqlBuilder);
        Optional<String> systemId = Optional.ofNullable(RpcContext.getContext().getAttachment(SYSTEM_ID));
        if (systemId.isPresent() && hasSystemId) {
            sqlBuilder.append(buildSql(SQLOperation.AND, "`" + SYSTEM_ID + "`", "= ?"));
            body.addPara(systemId.get());
        }
        for (SqlPara para : sqlParas) {
            addPara(body, para);
            sqlBuilder.append(para.getSql());
        }
        body.setSql(sqlBuilder.toString());
        return body;
    }

    public void where(StringBuilder sqlBuilder) {
        sqlBuilder.append(" ").append(SQLOperation.WHERE).append(" 1");
    }

    private synchronized List<Record> findColumns(String tableName) {
        try {
            return Db.find(
                "SELECT COLUMN_NAME AS columnName, COLUMN_KEY AS primaryKey FROM INFORMATION_SCHEMA.COLUMNS a, (SELECT database() AS TABLE_SCHEMA)b WHERE a.table_name = ? and a.TABLE_SCHEMA = b.TABLE_SCHEMA;",
                tableName);
        } catch (Exception e) {
            // 使用h2数据库
            String truncateSql = "";
            if (StrKit.notBlank(readTxtFileAll("../../../schema.sql"))) {
                truncateSql = readTxtFileAll("../../../schema.sql");
            } else {
                truncateSql = readTxtFileAll("schema.sql");
            }
            String[] tables = truncateSql.split("DROP TABLE IF EXISTS");
            if (tables.length == 0) {
                throw new RuntimeException("scan schema.sql fail,can not initialization table.");
            }
            Set<String> primaryKeySet = new HashSet<String>();
            Set<String> columnSet = new HashSet<String>();
            for (String table : tables) {
                if (!table.contains("\";CREATE TABLE") || table.length() < 2) {
                    continue;
                }
                if (table.substring(2, table.indexOf("\";CREATE TABLE")).equals(tableName)) {
                    String[] contents = table.split("PRIMARY KEY");
                    for (String content : contents) {
                        if (content.contains("(\"") && content.contains("\")")) {
                            primaryKeySet
                                .add(content.trim().substring(content.indexOf("(\"") + 1, content.indexOf("\")") - 1));
                        } else {
                            String[] columns = content.substring(content.indexOf("(")).split(",");
                            for (String column : columns) {
                                if (!column.contains("\"")) {
                                    continue;
                                }
                                String columnName =
                                    column.substring(column.indexOf("\"") + 1, column.lastIndexOf("\""));
                                columnSet.add(columnName);
                            }
                        }
                    }
                }
            }
            if (columnSet.size() > 0) {
                for (String key : columnSet) {
                    this.queryBody.append("`").append(key).append("`").append(", ");
                    this.keyMap.put(key, "`" + key + "` = ?");
                }
                for (String columnName : primaryKeySet) {
                    this.keyMap.put(columnName, "`" + columnName + "` = ?");
                }
                columnSet.addAll(primaryKeySet);
                this.keySet = columnSet;
            }
            this.primaryKey = primaryKeySet.toArray(new String[primaryKeySet.size()]);
            return Db.find("SELECT COLUMN_NAME AS columnName FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = ?",
                tableName);
        }
    }

}

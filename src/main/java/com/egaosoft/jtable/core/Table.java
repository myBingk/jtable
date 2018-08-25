package com.egaosoft.jtable.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.rpc.RpcContext;
import com.egaosoft.jtable.config.JudgmentType;
import com.egaosoft.jtable.config.SQLOperation;
import com.egaosoft.jtable.exception.BusinessException;
import com.egaosoft.jtable.exception.DatabaseException;
import com.egaosoft.jtable.exception.IllegalAgumentException;
import com.egaosoft.jtable.service.Service;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.ActiveRecordException;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class Table<T extends Model> implements Service<T> {

	private static final Logger LOGGER = LoggerFactory.getLogger(Table.class); 
	
	private Set<String> keySet = new HashSet<String>();
	private StringBuilder queryBody = new StringBuilder();
	private String queryPrimaryKey = "";
	private String[] primaryKey;
	private String queryTableName = "";
	private T tableModel;
	
	// 特殊业务需求，多系统下区别systemId。
	private static final String SYSTEM_ID = "systemId";
	private boolean hasSystemId = false;
	
	// 每次同时操作数据的最大值
	private static int eachAlterQuantity = 100;
	private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");

	@SuppressWarnings("unused")
	private Table() {
	}

	public Table(Class<T> modelClass) {
		String tableName = getUnderLineClassName(modelClass);
		List<Record> columns = findColumns(tableName);
		if (columns == null || columns.size() == 0) {
			throw new RuntimeException("Columns initialization failed. Possible reason: The table does not exist.");
		}
		Set<String> primaryKeySet = new HashSet<String>();
		if(this.keySet.size() == 0){
			for (Record column : columns) {
				String columnName = column.getStr("columnName");
				this.keySet.add(columnName);
				if (column.get("primaryKey") != null && column.getStr("primaryKey").equals("PRI")) {
					primaryKeySet.add(columnName);
					continue;
				}
				this.queryBody.append("`").append(columnName).append("`").append(", ");
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
		if (this.queryBody.toString().contains(SYSTEM_ID)) {
			this.hasSystemId = true;
		}
		
		try {
			this.tableModel = modelClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			if(LOGGER.isErrorEnabled()){
				LOGGER.error("表格对象model实例初始化失败", e);
			}
			throw new RuntimeException("primaryKeys initialization failed.", e);
		}
	}

	public static <T extends Model> Table<T> newInstance(Class<T> modelClass) {
		if (TableManager.hasTable(modelClass)) {
			return (Table<T>) TableManager.get(modelClass);
		}
		Table<T> table = new Table<T>(modelClass);
		TableManager.set(modelClass, table);
		return table;
	}

	@Override
	public boolean save(T model) throws BusinessException {
		try{
			return model.save();
		}catch(ActiveRecordException e){
			if(LOGGER.isErrorEnabled()){
				LOGGER.error("数据插入失败", e);
			}
			throw new DatabaseException(e);
		}
	}

	@Override
	public boolean saveForAny(Object... args) throws BusinessException {
		try{
			String values = getValue(args, 0, ",");
			StringBuilder sql = buildSql(SQLOperation.INSERT, SQLOperation.INTO, this.queryTableName, SQLOperation.VALUES, "(", values, ")");
			return Db.update(sql.toString()) == 1;
		}catch(ActiveRecordException e){
			if(LOGGER.isErrorEnabled()){
				LOGGER.error("数据插入失败", e);
			}
			throw new DatabaseException(e);
		}
	}

	@Override
	public boolean save(T model, Object... noRepetition) throws BusinessException {
		if (findForAny(noRepetition).size() > 0) {
			throw new DatabaseException(1098);
		}
		try{
			return model.save();
		}catch(ActiveRecordException e){
			if(LOGGER.isErrorEnabled()){
				LOGGER.error("数据插入失败", e);
			}
			throw new DatabaseException(e);
		}
	}

	@Override
	public boolean save(T model, T noRepetition) throws BusinessException {
		if (findList(noRepetition).size() > 0) {
			throw new DatabaseException(1098);
		}
		try{
			return model.save();
		}catch(ActiveRecordException e){
			if(LOGGER.isErrorEnabled()){
				LOGGER.error("数据插入失败", e);
			}
			throw new DatabaseException(e);
		}
	}

	@Override
	public boolean save(T model, T... models) throws BusinessException {
		try{
			return Db.tx(new IAtom() {
	
				@Override
				public boolean run() throws SQLException {
					if (!model.save()) {
						throw new SQLException("添加对象失败", "fail", 2102);
					}
					for (T model2 : models) {
						if (!model2.save()) {
							throw new SQLException("添加附加对象失败", "fail", 2106);
						}
					}
					return true;
				}
	
			});
		}catch(ActiveRecordException e){
			if(LOGGER.isErrorEnabled()){
				LOGGER.error("数据插入失败", e);
			}
			throw new DatabaseException(e);
		}
	}

	@Override
	public boolean save(T model, List<T>... models) throws BusinessException {
		try{
			return Db.tx(new IAtom() {
	
				@Override
				public boolean run() throws SQLException {
					if (!model.save()) {
						throw new SQLException("添加对象失败", "fail", 2102);
					}
					for (List<T> model2 : models) {
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
		}catch(ActiveRecordException e){
			if(LOGGER.isErrorEnabled()){
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
		try{
			return saveAsGroup(saveList);
		}catch(ActiveRecordException e){
			if(LOGGER.isErrorEnabled()){
				LOGGER.error("数据插入失败", e);
			}
			throw new DatabaseException(e);
		}
	}

	@Override
	public boolean saveAsGroup(List<T> modelList, Object... noRepetition) throws BusinessException {
		if (findForAny(noRepetition).size() != 0) {
			throw new DatabaseException(1098);
		}
		try{
			int[] saveResult = Db.batchSave(modelList, eachAlterQuantity);
			for (int result : saveResult) {
				if (result != 1) {
					throw new DatabaseException(2102);
				}
			}
			return true;
		}catch(ActiveRecordException e){
			if(LOGGER.isErrorEnabled()){
				LOGGER.error("数据插入失败", e);
			}
			throw new DatabaseException(e);
		}		
	}

	@Override
	public boolean saveAsGroup(List<T> modelList, T noRepetition) throws BusinessException {
		if (findList(noRepetition).size() != 0) {
			throw new DatabaseException(1098);
		}
		try{
			int[] saveResult = Db.batchSave(modelList, eachAlterQuantity);
			for (int result : saveResult) {
				if (result != 1) {
					throw new DatabaseException(2102);
				}
			}
			return true;
		}catch(ActiveRecordException e){
			if(LOGGER.isErrorEnabled()){
				LOGGER.error("数据插入失败", e);
			}
			throw new DatabaseException(e);
		}
	}

	@Override
	public boolean saveAsGroup(List<T> modelList) throws BusinessException {
		try{
			int[] saveResult = Db.batchSave(modelList, eachAlterQuantity);
			for (int result : saveResult) {
				if (result != 1) {
					throw new DatabaseException(2102);
				}
			}
			return true;
		}catch(ActiveRecordException e){
			if(LOGGER.isErrorEnabled()){
				LOGGER.error("数据插入失败", e);
			}
			throw new DatabaseException(e);
		}
	}

	@Override
	public boolean saveAsGroup(List<T> modelList, List<T>... modelLists) throws BusinessException {
		try{
			return Db.tx(new IAtom() {

				@Override
				public boolean run() throws SQLException {
					int[] saveResult = Db.batchSave(modelList, 100);
					for (int result : saveResult) {
						if (result != 1) {
							throw new SQLException("添加对象失败", "fail", 2102);
						}
					}
					for (List<T> list : modelLists) {
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
		}catch(ActiveRecordException e){
			if(LOGGER.isErrorEnabled()){
				LOGGER.error("数据插入失败", e);
			}
			throw new DatabaseException(e);
		}
	}

	@Override
	public boolean saveAsGroup(List<T> modelList, T... modelLists) throws BusinessException {
		try{
			return Db.tx(new IAtom() {
	
				@Override
				public boolean run() throws SQLException {
					int[] saveResult = Db.batchSave(modelList, 100);
					for (int result : saveResult) {
						if (result != 1) {
							throw new SQLException("添加对象失败", "fail", 2102);
						}
					}
					for (T model : modelLists) {
						boolean saveResult2 = model.save();
						if (!saveResult2) {
							throw new SQLException("添加附加对象失败", "fail", 2102);
						}
					}
					return true;
				}
	
			});
		}catch(ActiveRecordException e){
			if(LOGGER.isErrorEnabled()){
				LOGGER.error("数据插入失败", e);
			}
			throw new DatabaseException(e);
		}
	}

	@Override
	public boolean deleteForAny(T conditions) throws BusinessException {
		StringBuilder sql = buildSql(getDeleteBody(), getWhereQuery(conditions, SQLOperation.AND));
		try{
			return Db.update(sql.toString()) > 0;
		}catch(ActiveRecordException e){
			if(LOGGER.isErrorEnabled()){
				LOGGER.error("数据删除失败", e);
			}
			throw new DatabaseException(e);
		}	
	}

	@Override
	public boolean delete(T model) throws BusinessException {
		try{
			return model.delete();
		}catch(ActiveRecordException e){
			if(LOGGER.isErrorEnabled()){
				LOGGER.error("数据删除失败", e);
			}
			throw new DatabaseException(e);
		}
	}

	@Override
	public boolean delete(T model, T... models) throws BusinessException {
		try{
			return Db.tx(new IAtom() {
	
				@Override
				public boolean run() throws SQLException {
					boolean saveResult = model.delete();
					if (!saveResult) {
						throw new SQLException("删除对象失败", "fail", 2110);
					}
					for (T model : models) {
						boolean saveResult2 = model.delete();
						if (!saveResult2) {
							throw new SQLException("删除附加对象失败", "fail", 2114);
						}
					}
					return true;
				}
	
			});
		}catch(ActiveRecordException e){
			if(LOGGER.isErrorEnabled()){
				LOGGER.error("数据删除失败", e);
			}
			throw new DatabaseException(e);
		}	
	}

	@Override
	public boolean delete(T model, List<T>... modelLists) throws BusinessException {
		try{
			return Db.tx(new IAtom() {
	
				@Override
				public boolean run() throws SQLException {
					boolean saveResult = model.delete();
					if (!saveResult) {
						throw new SQLException("删除对象失败", "fail", 2110);
					}
					for (List<T> model : modelLists) {
						boolean saveResult2;
						try {
							saveResult2 = deleteAsGroup(model);
							if (!saveResult2) {
								throw new SQLException("删除附加对象失败", "fail", 2114);
							}
						} catch (BusinessException e) {
							if(LOGGER.isErrorEnabled()){
								LOGGER.error("数据删除失败", e);
							}
							throw new SQLException("删除附加对象失败", "fail", 2114);
						}
					}
					return true;
				}
	
			});
		}catch(ActiveRecordException e){
			if(LOGGER.isErrorEnabled()){
				LOGGER.error("数据删除失败", e);
			}
			throw new DatabaseException(e);
		}	
	}

	@Override
	public boolean delete(Object... primaryKey) throws BusinessException {
		StringBuilder sql = buildSql(getDeleteBody(), getPrimaryWhereQuery(primaryKey));
		try{
			return Db.update(sql.toString()) == 1;
		}catch(ActiveRecordException e){
			if(LOGGER.isErrorEnabled()){
				LOGGER.error("数据删除失败", e);
			}
			throw new DatabaseException(e);
		}	
	}

	@Override
	public boolean deleteAsGroup(List<T> modelList) throws BusinessException {
		StringBuilder whereSql = new StringBuilder();
		for (String primaryKey : this.primaryKey) {
			if (whereSql.toString().equals("")) {
				whereSql.append(getWhereQuery(modelList.stream().map(e -> e.get(primaryKey)).toArray(), SQLOperation.OR));
			} else {
				whereSql.append(SQLOperation.AND).append(
						getWhereQuery(modelList.stream().map(e -> e.get(primaryKey)).toArray(), SQLOperation.OR));
			}
		}
		StringBuilder sql = buildSql(getDeleteBody(), whereSql);
		try{
			return Db.update(sql.toString()) > 0;
		}catch(ActiveRecordException e){
			if(LOGGER.isErrorEnabled()){
				LOGGER.error("数据删除失败", e);
			}
			throw new DatabaseException(e);
		}
	}

	@Override
	public boolean deleteAsGroup(List<T> modelList, T... models) throws BusinessException {
		try{
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
						if(LOGGER.isErrorEnabled()){
							LOGGER.error("数据删除失败", e);
						}
						throw new SQLException("删除对象失败", "fail", 2110);
					}
					for (T model : models) {
						boolean saveResult2 = model.delete();
						if (!saveResult2) {
							throw new SQLException("删除附加对象失败", "fail", 2114);
						}
					}
					return true;
				}
	
			});
		}catch(ActiveRecordException e){
			if(LOGGER.isErrorEnabled()){
				LOGGER.error("数据删除失败", e);
			}
			throw new DatabaseException(e);
		}	
	}

	@Override
	public boolean deleteAsGroup(List<T> modelList, List<T>... modelLists) throws BusinessException {
		try{
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
						if(LOGGER.isErrorEnabled()){
							LOGGER.error("数据删除失败", e);
						}
						throw new SQLException("删除对象失败", "fail", 2110);
					}
					
					for (List<T> model : modelLists) {
						boolean saveResult2;
						try {
							saveResult2 = deleteAsGroup(model);
							if (!saveResult2) {
								throw new SQLException("删除附加对象失败", "fail", 2114);
							}
						} catch (BusinessException e) {
							if(LOGGER.isErrorEnabled()){
								LOGGER.error("数据删除失败", e);
							}
							throw new SQLException("删除附加对象失败", "fail", 2114);
						}
						
					}
					return true;
				}
	
			});
		}catch(ActiveRecordException e){
			if(LOGGER.isErrorEnabled()){
				LOGGER.error("数据删除失败", e);
			}
			throw new DatabaseException(e);
		}	
	}
	
	@Override
	public boolean deleteAsGroup(Object[]... primaryKeySet) throws BusinessException {
		StringBuilder whereSql = new StringBuilder();
		for (Object[] keySet : primaryKeySet) {
			if (keySet.length == 0) {
				return false;
			}
			if (whereSql.toString().equals("")) {
				whereSql.append(getWhereQuery(keySet, ","));
			} else {
				whereSql.append(" ").append(SQLOperation.OR).append(getWhereQuery(keySet, ","));
			}
		}
		StringBuilder sql = buildSql(getDeleteBody(), whereSql);
		try{
			return Db.update(sql.toString()) > 0;
		}catch(ActiveRecordException e){
			if(LOGGER.isErrorEnabled()){
				LOGGER.error("数据删除失败", e);
			}
			throw new DatabaseException(e);
		}
	}

	@Override
	@Deprecated
	public boolean deleteAll() throws BusinessException {
		StringBuilder sql = buildSql(getDeleteBody());
		try{
			return Db.update(sql.toString()) > 0;
		}catch(ActiveRecordException e){
			if(LOGGER.isErrorEnabled()){
				LOGGER.error("表格删除失败", e);
			}
			throw new DatabaseException(e);
		}
	}

	@Override
	public boolean update(T model) throws BusinessException {
		try{
			return model.update();
		}catch(ActiveRecordException e){
			if(LOGGER.isErrorEnabled()){
				LOGGER.error("数据更新失败", e);
			}
			throw new DatabaseException(e);
		}
	}

	@Override
	public boolean update(T model, T conditation) throws BusinessException {
		StringBuilder sql = buildSql(getUpdateBody(model), getWhereQuery(conditation, SQLOperation.AND));
		try{
			return Db.update(sql.toString()) == 1;
		}catch(ActiveRecordException e){
			if(LOGGER.isErrorEnabled()){
				LOGGER.error("数据更新失败", e);
			}
			throw new DatabaseException(e);
		}	
	}

	@Override
	public boolean update(T model, T... models) throws BusinessException {
		try{
			return Db.tx(new IAtom() {
	
				@Override
				public boolean run() throws SQLException {
					boolean saveResult = model.update();
					if (!saveResult) {
						throw new SQLException("修改对象失败", "fail", 2118);
					}
					for (T model : models) {
						boolean saveModelsResult = model.update();
						if (!saveModelsResult) {
							throw new SQLException("修改附加对象失败", "fail", 2122);
						}
					}
					return true;
				}
	
			});
		}catch(ActiveRecordException e){
			if(LOGGER.isErrorEnabled()){
				LOGGER.error("数据更新失败", e);
			}
			throw new DatabaseException(e);
		}	
	}

	@Override
	public boolean update(T model, List<T>... models) throws BusinessException {
		try{
			return Db.tx(new IAtom() {
	
				@Override
				public boolean run() throws SQLException {
					boolean saveResult = model.update();
					if (!saveResult) {
						throw new SQLException("修改对象失败", "fail", 2118);
					}
					for (List<T> list : models) {
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
		}catch(ActiveRecordException e){
			if(LOGGER.isErrorEnabled()){
				LOGGER.error("数据更新失败", e);
			}
			throw new DatabaseException(e);
		}		
	}

	@Override
	public boolean updateAsGroup(List<T> modelList) throws BusinessException {
		try{
			int[] saveModelsResult = Db.batchUpdate(modelList, eachAlterQuantity);
			for (int result : saveModelsResult) {
				if (result != 1) {
					throw new DatabaseException(2118);
				}
			}
			return true;
		}catch(ActiveRecordException e){
			if(LOGGER.isErrorEnabled()){
				LOGGER.error("数据更新失败", e);
			}
			throw new DatabaseException(e);
		}	
	}

	@Override
	public boolean updateAsGroup(List<T> modelList, T conditation) throws BusinessException {
		StringBuilder sqls = new StringBuilder();
		for (T model : modelList) {
			StringBuilder sql = buildSql(getUpdateBody(model), getWhereQuery(conditation, SQLOperation.AND));
			sqls.append(sql).append(";");
		}
		try{
			return Db.update(sqls.toString()) == modelList.size();
		}catch(ActiveRecordException e){
			if(LOGGER.isErrorEnabled()){
				LOGGER.error("数据更新失败", e);
			}
			throw new DatabaseException(e);
		}
	}

	@Override
	public boolean updateAsGroup(List<T> modelList, T... models) throws BusinessException {
		try{
			return Db.tx(new IAtom() {
	
				@Override
				public boolean run() throws SQLException {
					int[] saveResult = Db.batchUpdate(modelList, 100);
					for (int result : saveResult) {
						if (result != 1) {
							throw new SQLException("修改对象失败", "fail", 2118);
						}
					}
					for (T model : models) {
						boolean saveModelsResult = model.update();
						if (!saveModelsResult) {
							throw new SQLException("修改附加对象失败", "fail", 2122);
						}
					}
					return true;
				}
	
			});
		}catch(ActiveRecordException e){
			if(LOGGER.isErrorEnabled()){
				LOGGER.error("数据更新失败", e);
			}
			throw new DatabaseException(e);
		}	
	}

	@Override
	public boolean updateAsGroup(List<T> modelList, List<T>... modelLists) throws BusinessException {
		try{
			return Db.tx(new IAtom() {
	
				@Override
				public boolean run() throws SQLException  {
					int[] saveResult = Db.batchUpdate(modelList, 100);
					for (int result : saveResult) {
						if (result != 1) {
							throw new SQLException("修改对象失败", "fail", 2118);
						}
					}
					for (List<T> list : modelLists) {
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
		}catch(ActiveRecordException e){
			if(LOGGER.isErrorEnabled()){
				LOGGER.error("数据更新失败", e);
			}
			throw new DatabaseException(e);
		}
	}

	@Override
	public T findById(Object... primaryKey) throws BusinessException {
		if (primaryKey.length != this.primaryKey.length) {
			throw new IllegalAgumentException(10106);
		}
		StringBuilder sql = buildSql(getSelectBody(), getPrimaryWhereQuery(primaryKey));
		try{
			return (T) tableModel.dao().findFirst(sql.toString());
		}catch(ActiveRecordException e){
			if(LOGGER.isErrorEnabled()){
				LOGGER.error("数据查询失败，可能原因：主键为空或主键长度与表格主键长度不一致。", e);
			}
			throw new DatabaseException(e);
		}	
	}

	public List<T> findForAny(Object... args) throws BusinessException {
		StringBuilder sql = buildSql(getSelectBody(), getWhereQuery(args, SQLOperation.OR));
		Optional<String> systemId = Optional.ofNullable(RpcContext.getContext().getAttachment(SYSTEM_ID));
		if (systemId.isPresent() && hasSystemId) {
			sql.append(buildSql(SQLOperation.AND, "`" + SYSTEM_ID + "`", "=", changeQueryValue(systemId.get())));
		}
		try{
			return (List<T>) tableModel.find(sql.toString());
		}catch(ActiveRecordException e){
			if(LOGGER.isErrorEnabled()){
				LOGGER.error("数据查询失败，可能原因：列数或数据格式不正确。", e);
			}
			throw new DatabaseException(e);
		}	
	}

	@Override
	public T find(T condition) throws BusinessException {
		StringBuilder sql = buildSql(getSelectBody(), getWhereQuery(condition, SQLOperation.AND));
		try{
			return (T) tableModel.findFirst(sql.toString());
		}catch(ActiveRecordException e){
			if(LOGGER.isErrorEnabled()){
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
			throw new IllegalAgumentException(10106);
		}
		StringBuilder sql = buildSql(getSelectBody(), getWhereQuery(idSet, SQLOperation.AND));
		Optional<String> systemId = Optional.ofNullable(RpcContext.getContext().getAttachment(SYSTEM_ID));
		if (systemId.isPresent() && hasSystemId) {
			sql.append(buildSql(SQLOperation.AND, "`" + SYSTEM_ID + "`", "=", changeQueryValue(systemId.get())));
		}
		try{
			return (List<T>) tableModel.find(sql.toString());
		}catch(ActiveRecordException e){
			if(LOGGER.isErrorEnabled()){
				LOGGER.error("数据查询失败", e);
			}
			throw new DatabaseException(e);
		}
	}

	@Override
	public List<T> findByIdsAsGroup(Long[]... idSets) throws BusinessException {
		if (this.primaryKey.length != idSets.length) {
			throw new IllegalAgumentException(10106);
		}
		StringBuilder whereSql = new StringBuilder();
		for (Long[] idSet : idSets) {
			if (idSet.length == 0) {
				return new ArrayList<T>();
			}
			if (whereSql.toString().equals("")) {
				whereSql.append(getWhereQuery(idSet, ","));
			} else {
				whereSql.append(" ").append(SQLOperation.AND).append(getWhereQuery(idSet, ","));
			}
		}
		StringBuilder sql = buildSql(getSelectBody(), whereSql);
		Optional<String> systemId = Optional.ofNullable(RpcContext.getContext().getAttachment(SYSTEM_ID));
		if (systemId.isPresent() && hasSystemId) {
			sql.append(buildSql(SQLOperation.AND, "`" + SYSTEM_ID + "`", "=", changeQueryValue(systemId.get())));
		}
		try{
			return (List<T>) tableModel.find(sql.toString());
		}catch(ActiveRecordException e){
			if(LOGGER.isErrorEnabled()){
				LOGGER.error("数据查询失败", e);
			}
			throw new DatabaseException(e);
		}	
	}

	@Override
	public List<T> findList() throws BusinessException {
		StringBuilder sql = buildSql(getSelectBody());
		try{
			Optional<String> systemId = Optional.ofNullable(RpcContext.getContext().getAttachment(SYSTEM_ID));
			if (systemId.isPresent() && hasSystemId) {
				sql.append(buildSql(SQLOperation.WHERE, "`" + SYSTEM_ID + "`", "=", changeQueryValue(systemId.get())));
			}
			return (List<T>) tableModel.find(sql.toString());
		}catch(ActiveRecordException e){
			if(LOGGER.isErrorEnabled()){
				LOGGER.error("数据查询失败", e);
			}
			throw new DatabaseException(e);
		}	
	}

	@Override
	public List<T> findList(T condition) throws BusinessException {
		StringBuilder sql = buildSql(getSelectBody(), getWhereQuery(condition, SQLOperation.AND));
		try{
			return (List<T>) tableModel.find(sql.toString());
		}catch(ActiveRecordException e){
			if(LOGGER.isErrorEnabled()){
				LOGGER.error("数据查询失败", e);
			}
			throw new DatabaseException(e);
		}
	}

	@Override
	public Page<T> findListInPage(int pageNumber, int pageSize) throws BusinessException {
		try{
			StringBuilder sql = buildSql(getSelectBody());
			Optional<String> systemId = Optional.ofNullable(RpcContext.getContext().getAttachment(SYSTEM_ID));
			if (systemId.isPresent() && hasSystemId) {
				sql.append(buildSql(SQLOperation.AND, "`" + SYSTEM_ID + "`", "=", changeQueryValue(systemId.get())));
			}
			SqlPara sqlPara = new SqlPara();
			sqlPara.setSql(sql.toString());
			return (Page<T>) tableModel.paginate(pageNumber, pageSize, sqlPara);
		}catch(ActiveRecordException e){
			if(LOGGER.isErrorEnabled()){
				LOGGER.error("数据查询失败", e);
			}
			throw new DatabaseException(e);
		}
	}

	@Override
	public Page<T> findListInPageWithKeywords(int pageNumber, int pageSize, T condition) throws BusinessException {
		StringBuilder sql = buildSql(getSelectBody(), getWhereQuery(condition, SQLOperation.AND));
		SqlPara sqlPara = new SqlPara();
		sqlPara.setSql(sql.toString());
		try{
			return (Page<T>) tableModel.paginate(pageNumber, pageSize, sqlPara);
		}catch(ActiveRecordException e){
			if(LOGGER.isErrorEnabled()){
				LOGGER.error("数据查询失败", e);
			}
			throw new DatabaseException(e);
		}
	}

	@Override
	public Page<T> findListInPage(int pageNumber, int pageSize, String sortBy, String sortOrder) throws BusinessException {
		try{
			StringBuilder sql = buildSql(getSelectBody(), getSortBy(sortBy), getSortOrder(sortOrder));
			Optional<String> systemId = Optional.ofNullable(RpcContext.getContext().getAttachment(SYSTEM_ID));
			if (systemId.isPresent() && hasSystemId) {
				sql = buildSql(getSelectBody(), buildSql(SQLOperation.WHERE, "`" + SYSTEM_ID + "`", "=", changeQueryValue(systemId.get())), getSortBy(sortBy), getSortOrder(sortOrder));
			}
			SqlPara sqlPara = new SqlPara();
			sqlPara.setSql(sql.toString());
			return (Page<T>) tableModel.paginate(pageNumber, pageSize, sqlPara);
		}catch(ActiveRecordException e){
			if(LOGGER.isErrorEnabled()){
				LOGGER.error("数据查询失败", e);
			}
			throw new DatabaseException(e);
		}
	}

	@Override
	public Page<T> findListInPageWithKeywords(int pageNumber, int pageSize, T condition, String sortBy, String sortOrder)
			throws BusinessException {
		StringBuilder sql = buildSql(getSelectBody(), getWhereQuery(condition, SQLOperation.AND), getSortBy(sortBy), getSortOrder(sortOrder));
		SqlPara sqlPara = new SqlPara();
		sqlPara.setSql(sql.toString());
		try{
			return (Page<T>) tableModel.paginate(pageNumber, pageSize, sqlPara);
		}catch(ActiveRecordException e){
			if(LOGGER.isErrorEnabled()){
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
			body = getSelectBody();
		}
		StringBuilder sql = buildSql(body, where);
		try{
			return (List<T>) tableModel.find(sql.toString());
		}catch(ActiveRecordException e){
			if(LOGGER.isErrorEnabled()){
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
		try{
			return (List<T>) tableModel.find(sql.toString());
		}catch(ActiveRecordException e){
			if(LOGGER.isErrorEnabled()){
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
		try{
			return (Page<T>) tableModel.paginate(pageNumber, pageSize, sqlPara);
		}catch(ActiveRecordException e){
			if(LOGGER.isErrorEnabled()){
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
		try{
			return (T) tableModel.dao().findFirst(sqlPara);
		}catch(ActiveRecordException e){
			if(LOGGER.isErrorEnabled()){
				LOGGER.error("数据查询失败", e);
			}
			throw new DatabaseException(e);
		}	
	}

	@Override
	public List<T> findList(String key, Map<?, ?> data) throws BusinessException {
		SqlPara sqlPara = Db.getSqlPara(key, data);
		try{
			return (List<T>) tableModel.dao().find(sqlPara);
		}catch(ActiveRecordException e){
			if(LOGGER.isErrorEnabled()){
				LOGGER.error("数据查询失败", e);
			}
			throw new DatabaseException(e);
		}	
	}
	
	public String getDeleteBody() {
		return buildSql(SQLOperation.DELETE, SQLOperation.FROM, this.queryTableName).toString();
	}

	public String getUpdateBody(T model) {
		StringBuilder sql = buildSql(SQLOperation.UPDATE, this.queryTableName, SQLOperation.SET);
		Map<String, Object> map = bean2Map(model);
		for (String key : map.keySet()) {
			sql.append(buildSql(key, "=", changeQueryValue(model.get(key)), ","));
		}
		return sql.toString().substring(0, sql.toString().lastIndexOf(","));
	}

	public String getSelectBody() {
		return buildSql(SQLOperation.SELECT, this.queryBody, getPrimaryKeyQuery(), SQLOperation.FROM, this.queryTableName).toString();
	}

	public String getSortBy(String sortOrder) throws BusinessException {
		if (!this.keySet.contains(sortOrder)) {
			throw new IllegalAgumentException(10102);
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
		throw new IllegalAgumentException(10098);
	}
	
	public static String getSortByKey() {
		return SORT_BY;
	}
	
	public static String getSortOrderKey() {
		return SORT_ORDER;
	}

	public String getPrimaryWhereQuery(Object[] primaryKey) {
		StringBuilder primaryKeyQuery = new StringBuilder();
		int primaryKeyLength = primaryKey.length;
		for (int i = 0; i < primaryKeyLength; i++) {
			if (i != 0) {
				primaryKeyQuery.append("AND").append("`" + this.primaryKey[i] + "`").append(" = ").append(changeQueryValue(primaryKey[0]));
			} else {
				primaryKeyQuery.append("`" + this.primaryKey[i] + "`").append(" = ").append(changeQueryValue(primaryKey[0]));
			}
		}
		return primaryKeyQuery.length() == 0 ? "" : buildSql(SQLOperation.WHERE, primaryKeyQuery).toString();
	}

	public String getPrimaryKeyQuery() {
		return this.queryPrimaryKey;
	}

	public Object changeQueryValue(Object object) {
		if (object == null) {
			return null;
		}
		if (object instanceof String) {
			return "'" + object + "'";
		}
		if (object instanceof Date) {
			
	        Instant instant = ((Date)object).toInstant();
	        ZoneId zoneId = ZoneId.systemDefault();
	        LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();
	        
			return "'" + localDateTime.format(DTF) + "'";
		}
		if(object.getClass().isArray()){
			for(Object obj : (Object[]) object){
				changeQueryValue(obj);
			}
		}
		return object;
	}

	public String getWhereQuery(Object value, String SplicingConditions) {
		StringBuilder whereQuery = new StringBuilder();
		value = changeQueryValue(value);
		for (String key : this.keySet) {
			if (whereQuery.toString().equals("")) {
				whereQuery.append(buildSql("`" + key + "`", "=", value));
			} else {
				whereQuery.append(buildSql(SplicingConditions, "`" + key + "`", "=", value));
			}
		}
		return whereQuery.length() == 0 ? "" : buildSql(SQLOperation.WHERE, whereQuery).toString();
	}

	public String getWhereQuery(Object[] values, Object SplicingConditions) {
		return getWhere(values, SplicingConditions, true);
	}

	public String getWhereQuery(Object[] values, String SplicingConditions) {
		return getWhere(values, SplicingConditions, true);
	}

	public String getWhere(Object[] values, Object SplicingConditions, boolean isAppendWhere) {
		StringBuilder sqlQuery = new StringBuilder();
		sqlQuery.append("(");
		sqlQuery.append(getValue(values, 0, ","));
		sqlQuery.append(")");
		StringBuilder whereQuery = new StringBuilder();
		for (String key : this.primaryKey) {
			if (whereQuery.toString().equals("")) {
				whereQuery.append(buildSql("`" + key + "`", SQLOperation.IN, sqlQuery));
			} else {
				whereQuery.append(buildSql(SplicingConditions, "`" + key + "`", SQLOperation.IN, sqlQuery));
			}
		}
		return whereQuery.length() == 0 ? "" : buildSql(isAppendWhere ? SQLOperation.WHERE : "", whereQuery).toString();
	}

	public String getWhereQuery(String columnName, Object value, boolean isAppendWhere) throws BusinessException {
		if (!queryBody.toString().contains(columnName)) {
			throw new IllegalAgumentException(10094);
		}
		StringBuilder whereQuery = new StringBuilder();
		whereQuery.append(buildSql("`" + columnName + "`", "=", changeQueryValue(value)));
		return whereQuery.length() == 0 ? "" : buildSql(isAppendWhere ? SQLOperation.WHERE : "", whereQuery).toString();
	}

	public String getWhereQuery(String columnName, Object[] values, boolean isAppendWhere) throws BusinessException {
		if (!queryBody.toString().contains(columnName)) {
			throw new IllegalAgumentException(10094);
		}
		StringBuilder sqlQuery = new StringBuilder();
		sqlQuery.append("(");
		sqlQuery.append(getValue(values, 0, ","));
		sqlQuery.append(")");
		StringBuilder whereQuery = new StringBuilder();
		whereQuery.append(buildSql("`" + columnName + "`", SQLOperation.IN, sqlQuery));
		return whereQuery.length() == 0 ? "" : buildSql(isAppendWhere ? SQLOperation.WHERE : "", whereQuery).toString();
	}

	public String getWhereQuery(T condition, Object SplicingConditions) throws BusinessException {
		T myCondition = setConditionAndJudgmentType(condition, SplicingConditions);
		return getSqlQuery(myCondition);
	}
	
	private String getSqlQuery(T condition){
		Map<String, Object> beanMap = bean2Map(condition);
		StringBuilder sqlQuery = new StringBuilder();
		StringBuilder sortQuery = new StringBuilder();
		for(Entry<String, Object> entry : beanMap.entrySet()){
			String value = entry.getValue().toString();
			int index = value.indexOf(entry.getKey());
			if(index != -1){
				if(sqlQuery.length() == 0){
					sqlQuery.append(value.substring(index - 1));
				}else{
					sqlQuery.append(entry.getValue());
				}
			}
			if(entry.getKey().equals(getSortByKey())){
				sortQuery.append(entry.getValue());
			}
		}
		String returnString = sortQuery.length() == 0 ? "" : sortQuery.toString();
		return sqlQuery.length() == 0 ? returnString : buildSql(SQLOperation.WHERE, sqlQuery, sortQuery).toString();
	}
	
	// 数组条件键开头字符
	private static final String ARRAY_KEY = "Array";
	
	// 排序条件的键
	private static final String SORT_BY = "sortBy";
	
	// 升序降序的键
	private static final String SORT_ORDER = "sortOrder";

	public T setConditionAndJudgmentType(Model<T> condition, Object SplicingConditions) throws BusinessException{
		
		SplicingConditions = SplicingConditions == null ? SQLOperation.AND : SplicingConditions;
				
		String judgmentType = com.egaosoft.jtable.config.JudgmentType.getJudgmentType();
		
		T myCondition = this.getCondition();
		Map<String, Object> beanMap = bean2Map(condition);
		
		for(Entry<String, Object> entry : beanMap.entrySet()){
			
			if(entry.getKey().startsWith(judgmentType)){
				String otherKey = entry.getKey().replace(judgmentType, "");
				Object otherValue = condition.get(otherKey);
				if(otherValue == null){
					continue;
				}
				
				otherValue = changeQueryValue(otherValue);
				JudgmentType type = condition.get(entry.getKey());
				switch(type){
					case LEFT_LIKE:
						myCondition.put(otherKey, buildSql(SplicingConditions, otherKey, SQLOperation.LIKE, SQLOperation.CONCAT, "('%', ", otherValue, ")"));
						break;
					case RIGHT_LIKE:
						myCondition.put(otherKey, buildSql(SplicingConditions, otherKey, SQLOperation.LIKE, SQLOperation.CONCAT, "(", otherValue, ", '%')"));
						break;
					case ALL_LIKE:
						myCondition.put(otherKey, buildSql(SplicingConditions, otherKey, SQLOperation.LIKE, SQLOperation.CONCAT, "('%', ", otherValue, ", '%')"));
						break;
					case EQUAL:
						myCondition.put(otherKey, buildSql(SplicingConditions, otherKey, "=", otherValue));
						break;
					case LESS_THAN:
						myCondition.put(otherKey, buildSql(SplicingConditions, otherKey, "<=", otherValue));
						break;
					case MORE_THAN:
						myCondition.put(otherKey, buildSql(SplicingConditions, otherKey, ">=", otherValue));
						break;
					case LENGTH:
						myCondition.put(otherKey, buildSql(SplicingConditions, SQLOperation.LENGTH, "(", otherKey, ")", "=", otherValue));
						break;
					default:
						break;	
				}
				myCondition.put(entry.getKey(), "true");
				continue;
			}
			
			if(entry.getValue() == null){
				continue;
			}
			
			if(entry.getKey().equals(getSortOrderKey())){
				continue;
			}
			
			if(entry.getKey().equals(getSortByKey())){
				String sortOrder = "ASC";
				if(condition.get(getSortOrderKey()) != null){
					sortOrder = condition.get(getSortOrderKey());
				}
				
				myCondition.put(entry.getKey(),
						buildSql(getSortBy(entry.getValue().toString()), sortOrder));
				continue;
			}
			
			Object value = this.changeQueryValue(entry.getValue());
			if(entry.getValue().getClass().isArray() && entry.getKey().endsWith(ARRAY_KEY)){
				String realKey = entry.getKey().replace(ARRAY_KEY, "");
				myCondition.put(realKey, buildSql(SplicingConditions, getWhereQuery(realKey, (Object[]) value, false)));
				continue;
			}
			
			if("true".equals(myCondition.get(judgmentType + entry.getKey()))){
				continue;
			}else{
				if(this.keySet.contains(entry.getKey())){
					myCondition.put(entry.getKey(), buildSql(SplicingConditions, "`" + entry.getKey() + "`", "=", value));
				}
			}
			
		}
		
		return myCondition;
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
			return (T) this.tableModel.getClass().newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			if(LOGGER.isErrorEnabled()){
				LOGGER.error("对象初始化失败", e);
			}
			throw new RuntimeException(e);
		}
	}

	private static Map<String, Object> bean2Map(Model model) {
		Map<String, Object> fieldMap = new HashMap<String, Object>();
		if (model != null) {
			Set<Entry<String, Object>> keySet = model._getAttrsEntrySet();
			for (Entry<String, Object> key : keySet) {
				fieldMap.put(key.getKey(), key.getValue());
			}
		}
		return fieldMap;
	}

	public static void setEachAlterQuantity(int setEachAlterQuantity) {
		if (setEachAlterQuantity <= 0) {
			throw new RuntimeException("Illegal eachAlterQuantity.");
		}
		eachAlterQuantity = setEachAlterQuantity;
	}

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
		Pattern pattern = Pattern.compile("[A-Z]([a-z\\d]+)?");
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

	private String getValue(Object[] loopObject, int markLocation, String append) {
		StringBuilder value = new StringBuilder();
		int index = 0;
		for (Object loop : loopObject) {
			loop = changeQueryValue(loop);
			if (index != markLocation) {
				value.append(append).append(loop);
			} else {
				value.append(loop);
			}
			index++;
		}
		return value.toString();
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

	private synchronized List<Record> findColumns(String tableName) {
		try {
			return Db
					.find("SELECT COLUMN_NAME AS columnName, COLUMN_KEY AS primaryKey FROM INFORMATION_SCHEMA.COLUMNS a, (SELECT database() AS TABLE_SCHEMA)b WHERE a.table_name = ? and a.TABLE_SCHEMA = b.TABLE_SCHEMA;",
							tableName);
		} catch (Exception e) {
			// 使用h2数据库
			String truncateSql = "";
			if(StrKit.notBlank(readTxtFileAll("../../../schema.sql"))){
				truncateSql = readTxtFileAll("../../../schema.sql");
	        }else{
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
							primaryKeySet.add(content.trim().substring(content.indexOf("(\"") + 1, content.indexOf("\")") - 1));
						}else{
							String[] columns = content.substring(content.indexOf("(")).split(",");
							for(String column : columns){
								if(!column.contains("\"")){
									continue;
								}
								columnSet.add(column.substring(column.indexOf("\"") + 1, column.lastIndexOf("\"")));
							}
						}
					}
				}
			}
			if(columnSet.size() > 0){
				for(String key : columnSet){
					this.queryBody.append("`").append(key).append("`").append(", ");
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

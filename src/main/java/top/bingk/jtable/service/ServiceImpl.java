package top.bingk.jtable.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.rpc.RpcContext;
import top.bingk.jtable.core.Table;
import top.bingk.jtable.dubbo.filter.ServiceFilter;
import top.bingk.jtable.transaction.FlowActuator;
import top.bingk.jtable.transaction.FlowKit;
import top.bingk.jtable.transaction.TransactionFlow;
import top.bingk.jtable.transaction.TransactionNode;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

/**
 * 基础实现类
 * 
 * @author DoubleCome
 * @since 2.0
 * @date 2018年7月2日 下午5:23:14
 */
@SuppressWarnings("rawtypes")
public abstract class ServiceImpl<T extends Model> implements Service<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceImpl.class);

    private static final String SYSTEM_ID_KEY = "systemId";

    @Override
    public boolean save(T model) throws BusinessException {
        if (isAssignmentAble()) {
            model = assignment(0, model);
        }
        boolean result = getTable().save(model);
        executeNode();
        return result;
    }

    @Override
    public boolean saveForAny(Object... args) throws BusinessException {
        if (isAssignmentAble()) {
            args = assignment(0, args);
        }
        boolean result = getTable().saveForAny(args);
        executeNode();
        return result;
    }

    @Override
    public boolean save(T model, Object... noRepetition) throws BusinessException {
        if (isAssignmentAble()) {
            model = assignment(0, model);
            noRepetition = assignment(1, noRepetition);
        }
        boolean result = getTable().save(model, noRepetition);
        executeNode();
        return result;
    }

    @Override
    public boolean save(T model, T noRepetitionMap) throws BusinessException {
        if (isAssignmentAble()) {
            model = assignment(0, model);
            noRepetitionMap = assignment(1, noRepetitionMap);
        }
        boolean result = getTable().save(model, noRepetitionMap);
        executeNode();
        return result;
    }

    @Override
    public boolean saveWithTransaction(T model, Model... models) throws BusinessException {
        if (isAssignmentAble()) {
            model = assignment(0, model);
            models = assignment(1, models);
        }
        boolean result = getTable().saveWithTransaction(model, models);
        executeNode();
        return result;
    }

    @Override
    public boolean saveWithTransactionForList(T model, @SuppressWarnings("unchecked") List<? extends Model>... models)
        throws BusinessException {
        if (isAssignmentAble()) {
            model = assignment(0, model);
            models = assignment(1, models);
        }
        boolean result = getTable().saveWithTransactionForList(model, models);
        executeNode();
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean saveAsGroupForAny(List<Object>... args) throws BusinessException {
        if (isAssignmentAble()) {
            args = assignment(0, args);
        }
        boolean result = getTable().saveAsGroupForAny(args);
        executeNode();
        return result;
    }

    @Override
    public boolean saveAsGroup(List<T> modelList, Object... noRepetition) throws BusinessException {
        if (isAssignmentAble()) {
            modelList = assignment(0, modelList);
            noRepetition = assignment(1, noRepetition);
        }
        boolean result = getTable().saveAsGroup(modelList, noRepetition);
        executeNode();
        return result;
    }

    @Override
    public boolean saveAsGroup(List<T> modelList, T noRepetitionMap) throws BusinessException {
        if (isAssignmentAble()) {
            modelList = assignment(0, modelList);
            noRepetitionMap = assignment(1, noRepetitionMap);
        }
        boolean result = getTable().saveAsGroup(modelList, noRepetitionMap);
        executeNode();
        return result;
    }

    @Override
    public boolean saveAsGroup(List<T> modelList) throws BusinessException {
        if (isAssignmentAble()) {
            modelList = assignment(0, modelList);
        }
        boolean result = getTable().saveAsGroup(modelList);
        executeNode();
        return result;
    }

    @Override
    public boolean saveAsGroupWithTransactionForList(List<T> modelList,
        @SuppressWarnings("unchecked") List<? extends Model>... modelLists) throws BusinessException {
        if (isAssignmentAble()) {
            modelLists = assignment(0, modelLists);
            modelLists = assignment(1, modelLists);
        }
        boolean result = getTable().saveAsGroupWithTransactionForList(modelList, modelLists);
        executeNode();
        return result;
    }

    @Override
    public boolean saveAsGroupWithTransaction(List<T> modelList, Model... models) throws BusinessException {
        if (isAssignmentAble()) {
            modelList = assignment(0, modelList);
            models = assignment(1, models);
        }
        boolean result = getTable().saveAsGroupWithTransaction(modelList, models);
        executeNode();
        return result;
    }

    @Override
    public boolean deleteForAny(T conditions) throws BusinessException {
        if (isAssignmentAble()) {
            conditions = assignment(0, conditions);
        }
        boolean result = getTable().deleteForAny(conditions);
        executeNode();
        return result;
    }

    @Override
    public boolean delete(T model) throws BusinessException {
        if (isAssignmentAble()) {
            model = assignment(0, model);
        }
        boolean result = getTable().delete(model);
        executeNode();
        return result;
    }

    @Override
    public boolean deleteWithTransaction(T model, Model... models) throws BusinessException {
        if (isAssignmentAble()) {
            model = assignment(0, model);
            models = assignment(1, models);
        }
        boolean result = getTable().delete(model, models);
        executeNode();
        return result;
    }

    @Override
    public boolean deleteWithTransactionForList(T model,
        @SuppressWarnings("unchecked") List<? extends Model>... modelLists) throws BusinessException {
        if (isAssignmentAble()) {
            model = assignment(0, model);
            modelLists = assignment(1, modelLists);
        }
        boolean result = getTable().deleteWithTransactionForList(model, modelLists);
        executeNode();
        return result;
    }

    @Override
    public boolean delete(Object... primaryKey) throws BusinessException {
        if (isAssignmentAble()) {
            primaryKey = assignment(0, primaryKey);
        }
        boolean result = getTable().delete(primaryKey);
        executeNode();
        return result;
    }

    @Override
    public boolean deleteAsGroup(List<T> modelList) throws BusinessException {
        if (isAssignmentAble()) {
            modelList = assignment(0, modelList);
        }
        boolean result = getTable().deleteAsGroup(modelList);
        executeNode();
        return result;
    }

    @Override
    public boolean deleteAsGroupWithTransaction(List<T> modelList, Model... models) throws BusinessException {
        if (isAssignmentAble()) {
            modelList = assignment(0, modelList);
            models = assignment(1, models);
        }
        boolean result = getTable().deleteAsGroupWithTransaction(modelList, models);
        executeNode();
        return result;
    }

    @Override
    public boolean deleteAsGroupWithTransactionForList(List<T> modelList,
        @SuppressWarnings("unchecked") List<? extends Model>... modelLists) throws BusinessException {
        if (isAssignmentAble()) {
            modelList = assignment(0, modelList);
            modelLists = assignment(1, modelLists);
        }
        boolean result = getTable().deleteAsGroupWithTransactionForList(modelList, modelLists);
        executeNode();
        return result;
    }

    @Override
    public boolean deleteAsGroup(Object[]... primaryKeySet) throws BusinessException {
        if (isAssignmentAble()) {
            primaryKeySet = assignment(0, primaryKeySet);
        }
        boolean result = getTable().deleteAsGroup(primaryKeySet);
        executeNode();
        return result;
    }

    @Override
    @Deprecated
    public boolean deleteAll() throws BusinessException {
        boolean result = getTable().deleteAll();
        executeNode();
        return result;
    }

    @Override
    public boolean update(T model) throws BusinessException {
        if (isAssignmentAble()) {
            model = assignment(0, model);
        }
        boolean result = getTable().update(model);
        executeNode();
        return result;
    }

    @Override
    public boolean update(T model, T conditationMap) throws BusinessException {
        if (isAssignmentAble()) {
            model = assignment(0, model);
            conditationMap = assignment(1, conditationMap);
        }
        boolean result = getTable().update(model, conditationMap);
        executeNode();
        return result;
    }

    @Override
    public boolean updateWithTransaction(T model, Model... models) throws BusinessException {
        if (isAssignmentAble()) {
            model = assignment(0, model);
            models = assignment(1, models);
        }
        boolean result = getTable().updateWithTransaction(model, models);
        executeNode();
        return result;
    }

    @Override
    public boolean updateWithTransactionForList(T model, @SuppressWarnings("unchecked") List<? extends Model>... models)
        throws BusinessException {
        if (isAssignmentAble()) {
            model = assignment(0, model);
            models = assignment(1, models);
        }
        boolean result = getTable().updateWithTransactionForList(model, models);
        executeNode();
        return result;
    }

    @Override
    public boolean updateAsGroup(List<T> modelList) throws BusinessException {
        if (isAssignmentAble()) {
            modelList = assignment(0, modelList);
        }
        boolean result = getTable().updateAsGroup(modelList);
        executeNode();
        return result;
    }

    @Override
    public boolean updateAsGroup(List<T> modelList, T conditationMap) throws BusinessException {
        if (isAssignmentAble()) {
            modelList = assignment(0, modelList);
            conditationMap = assignment(1, conditationMap);
        }
        boolean result = getTable().updateAsGroup(modelList, conditationMap);
        executeNode();
        return result;
    }

    @Override
    public boolean updateAsGroupWithTransaction(List<T> modelList, Model... models) throws BusinessException {
        if (isAssignmentAble()) {
            modelList = assignment(0, modelList);
            models = assignment(1, models);
        }
        boolean result = getTable().updateAsGroupWithTransaction(modelList, models);
        executeNode();
        return result;
    }

    @Override
    public boolean updateAsGroupWithTransactionForList(List<T> modelList,
        @SuppressWarnings("unchecked") List<? extends Model>... modelLists) throws BusinessException {
        if (isAssignmentAble()) {
            modelList = assignment(0, modelList);
            modelLists = assignment(1, modelLists);
        }
        boolean result = getTable().updateAsGroupWithTransactionForList(modelList, modelLists);
        executeNode();
        return result;
    }

    @Override
    public T findById(Object... primaryKey) throws BusinessException {
        T result = getTable().findById(primaryKey);
        return result;
    }

    @Override
    public T find(T condition) throws BusinessException {
        T result = getTable().find(condition);
        return result;
    }

    @Override
    public List<T> findByIdAsGroup(Long[] IdSet) throws BusinessException {
        List<T> result = getTable().findByIdAsGroup(IdSet);
        return result;
    }

    @Override
    public List<T> findByIdsAsGroup(Long[]... IdSets) throws BusinessException {
        List<T> result = getTable().findByIdsAsGroup(IdSets);
        return result;
    }

    @Override
    public List<T> findList() throws BusinessException {
        List<T> result = getTable().findList();
        return result;
    }

    @Override
    public List<T> findList(T condition) throws BusinessException {
        List<T> result = getTable().findList(condition);
        return result;
    }

    @Override
    public Page<T> findListInPage(int pageNumber, int pageSize) throws BusinessException {
        Page<T> result = getTable().findListInPage(pageNumber, pageSize);
        return result;
    }

    @Override
    public Page<T> findListInPageWithKeywords(int pageNumber, int pageSize, T condition) throws BusinessException {
        Page<T> result = getTable().findListInPageWithKeywords(pageNumber, pageSize, condition);
        return result;
    }

    @Override
    public Page<T> findListInPage(int pageNumber, int pageSize, String sortBy, String sortOrder)
        throws BusinessException {
        Page<T> result = getTable().findListInPage(pageNumber, pageSize, sortBy, sortOrder);
        return result;
    }

    @Override
    public Page<T> findListInPageWithKeywords(int pageNumber, int pageSize, T condition, String sortBy,
        String sortOrder) throws BusinessException {
        Page<T> result = getTable().findListInPageWithKeywords(pageNumber, pageSize, condition, sortBy, sortOrder);
        return result;
    }

    @Override
    public int execute(String key, Map<?, ?> data) throws BusinessException {
        int result = getTable().execute(key, data);
        return result;
    }

    @Override
    public T find(String key, Map<?, ?> data) throws BusinessException {
        T result = getTable().find(key, data);
        return result;
    }

    @Override
    public List<T> findList(String key, Map<?, ?> data) throws BusinessException {
        List<T> result = getTable().findList(key, data);
        return result;
    }

    public boolean isAssignmentAble() {

        TransactionFlow flow = getFlow();

        if (flow != null) {
            int start = flow.getIndex();
            int end = flow.getFlowNodes().size();

            if (start <= end) {
                return true;
            }
        }
        return false;
    }

    private void keepParam() {
        TransactionFlow flow = getFlow();

        if (null == flow) {
            return;
        }
        int start = flow.getIndex();
        int end = flow.getFlowNodes().size();

        for (int index = start; index < end; index++) {
            String key = FlowActuator.getNodeKey() + index;
            RpcContext.getContext().setAttachment(key, RpcContext.getContext().getAttachment(key));
        }
    }

    private boolean isHead() {
        String isHead = RpcContext.getContext().getAttachment(FlowActuator.getIsHead());
        if (isHead != null && "true".equals(isHead)) {
            return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    private T assignment(int argsIndex, T arg) {

        if (isHead()) {
            return arg;
        }

        TransactionFlow flow = getFlow();
        int indexNow = flow.getIndex() - 1;

        Object attachArgs = RpcContext.getContext().getAttachment(FlowActuator.getNodeKey() + indexNow);
        if (attachArgs != null) {
            T rarg = (T)((List<?>)FlowActuator.deserializeToNode(String.valueOf(attachArgs))).toArray()[argsIndex];

            String systemId = RpcContext.getContext().getAttachment(SYSTEM_ID_KEY);
            if (systemId != null) {
                boolean hasSystemId = ServiceFilter.hasSystemIdField(rarg);
                if (hasSystemId) {
                    rarg.put(SYSTEM_ID_KEY, systemId);
                }
            }

            return rarg;
        }

        return arg;
    }

    @SuppressWarnings("unchecked")
    private <A> A[] assignment(int argsIndex, A... args) {

        if (isHead()) {
            return args;
        }

        TransactionFlow flow = getFlow();
        int indexNow = flow.getIndex() - 1;

        String systemId = RpcContext.getContext().getAttachment(SYSTEM_ID_KEY);
        if (systemId != null) {
            for (A arg : args) {
                if (arg == null) {
                    continue;
                }
                if (arg.getClass().isArray()) {
                    for (Object subArg : (Object[])arg) {
                        if (subArg == null) {
                            continue;
                        }
                        if (subArg instanceof Model) {
                            if (ServiceFilter.hasSystemIdField(arg)) {
                                ((Model)subArg).put("systemId", systemId);
                            }
                        }
                    }
                }
                if (arg instanceof List) {
                    for (Object subArg : (List)arg) {
                        if (subArg == null) {
                            continue;
                        }
                        if (subArg instanceof Model) {
                            if (ServiceFilter.hasSystemIdField(subArg)) {
                                ((Model)subArg).put("systemId", systemId);
                            }
                        }
                    }
                }
                if (arg instanceof Model) {
                    if (ServiceFilter.hasSystemIdField(arg)) {
                        ((Model)arg).put("systemId", systemId);
                    }
                }
            }
        }

        Object attachArgs = RpcContext.getContext().getAttachment(FlowActuator.getNodeKey() + indexNow);
        if (attachArgs != null) {
            return (A[])((List<?>)FlowActuator.deserializeToNode(String.valueOf(attachArgs))).toArray()[argsIndex];
        }

        return args;
    }

    @SuppressWarnings("unchecked")
    private List<T> assignment(int argsIndex, List<T> arg) {

        if (isHead()) {
            return arg;
        }

        TransactionFlow flow = getFlow();
        int indexNow = flow.getIndex() - 1;

        Object attachArgs = RpcContext.getContext().getAttachment(FlowActuator.getNodeKey() + indexNow);
        if (attachArgs != null) {
            List<T> argsList =
                (List<T>)((List<?>)FlowActuator.deserializeToNode(String.valueOf(attachArgs))).toArray()[argsIndex];
            String systemId = RpcContext.getContext().getAttachment(SYSTEM_ID_KEY);
            if (systemId != null) {
                for (T t : argsList) {
                    boolean hasSystemId = ServiceFilter.hasSystemIdField(t);
                    if (hasSystemId) {
                        t.put(SYSTEM_ID_KEY, systemId);
                    }
                }
            }
            return argsList;
        }

        return arg;
    }

    private void executeNode() throws BusinessException {

        TransactionFlow flow = getFlow();
        keepParam();

        if (flow == null) {
            return;
        }

        String flowName = flow.getName();
        if (flow.getIndex() == 0) {
            LOGGER.info("流程[" + flowName + "]开始执行。");
        }

        if (flow.isFinish()) {
            LOGGER.info("流程[" + flowName + "]执行结束,结束时间:" + System.currentTimeMillis());
            return;
        }

        TransactionNode node = flow.getFlowNodes().get(flow.getIndex());

        if (isHead()) {
            RpcContext.getContext().setAttachment(FlowActuator.getIsHead(), "false");
        }

        String method = node.getClassName() + "." + node.getMethodName();
        LOGGER.info(
            "流程[" + flowName + "]-节点[" + flow.getIndex() + "]，方法[" + method + "]执行时间:" + System.currentTimeMillis());

        flow.nextNode();
        RpcContext.getContext().setAttachment(SYSTEM_ID_KEY, RpcContext.getContext().getAttachment(SYSTEM_ID_KEY));
        RpcContext.getContext().setAttachment(FlowActuator.getFlowKey(), FlowActuator.serializeToString(flow));

        FlowKit.executeTransactionFlow(node, true);
    }

    private TransactionFlow getFlow() {

        Optional<String> flowString =
            Optional.ofNullable(RpcContext.getContext().getAttachment(FlowActuator.getFlowKey()));
        if (flowString.isPresent()) {
            TransactionFlow flow = (TransactionFlow)FlowActuator.deserializeToNode(flowString.get());
            return flow;
        }
        return null;
    }

    public abstract Table<T> getTable();

}

package com.egaosoft.jtable.service;

import java.util.List;
import java.util.Map;

<<<<<<< Updated upstream
=======
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
>>>>>>> Stashed changes
import com.alibaba.dubbo.rpc.RpcContext;
import com.egaosoft.jtable.core.Table;
import com.egaosoft.jtable.exception.BusinessException;
import com.egaosoft.jtable.transaction.FlowActuator;
<<<<<<< Updated upstream
import com.egaosoft.transaction.TransactionFlow;
=======
import com.egaosoft.jtable.transaction.FlowKit;
import com.egaosoft.jtable.transaction.TransactionNode;
>>>>>>> Stashed changes
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

<<<<<<< Updated upstream
	@Override
	public boolean save(T model) throws BusinessException {
		boolean result = getTable().save(model);
		FlowActuator.excute((TransactionFlow)FlowActuator.deserializeToFlow(RpcContext.getContext().getAttachment("flow")));
		return result;
	}

	@Override
	public boolean saveForAny(Object... args) throws BusinessException {
		boolean result = getTable().saveForAny(args);
		FlowActuator.excute((TransactionFlow)FlowActuator.deserializeToFlow(RpcContext.getContext().getAttachment("flow")));
		return result;
	}

	@Override
	public boolean save(T model, Object... noRepetition) throws BusinessException {
		boolean result = getTable().save(model, noRepetition);
		FlowActuator.excute((TransactionFlow)FlowActuator.deserializeToFlow(RpcContext.getContext().getAttachment("flow")));
		return result;
	}

	@Override
	public boolean save(T model, T noRepetitionMap) throws BusinessException {
		boolean result = getTable().save(model, noRepetitionMap);
		FlowActuator.excute((TransactionFlow)FlowActuator.deserializeToFlow(RpcContext.getContext().getAttachment("flow")));
		return result;
	}

	@Override
	public boolean save(T model, @SuppressWarnings("unchecked") T... models) throws BusinessException {
		boolean result = getTable().save(model, models);
		FlowActuator.excute((TransactionFlow)FlowActuator.deserializeToFlow(RpcContext.getContext().getAttachment("flow")));
		return result;
	}

	@Override
	public boolean save(T model, @SuppressWarnings("unchecked") List<T>... models) throws BusinessException {
		boolean result = getTable().save(model, models);
		FlowActuator.excute((TransactionFlow)FlowActuator.deserializeToFlow(RpcContext.getContext().getAttachment("flow")));
		return result;
	}

	@Override
	public boolean saveAsGroupForAny(@SuppressWarnings("unchecked") List<Object>... args) throws BusinessException {
		boolean result = getTable().saveAsGroupForAny(args);
		FlowActuator.excute((TransactionFlow)FlowActuator.deserializeToFlow(RpcContext.getContext().getAttachment("flow")));
		return result;
	}

	@Override
	public boolean saveAsGroup(List<T> modelList, Object... noRepetition) throws BusinessException {
		boolean result = getTable().saveAsGroup(modelList, noRepetition);
		FlowActuator.excute((TransactionFlow)FlowActuator.deserializeToFlow(RpcContext.getContext().getAttachment("flow")));
		return result;
	}

	@Override
	public boolean saveAsGroup(List<T> modelList, T noRepetitionMap) throws BusinessException {
		boolean result = getTable().saveAsGroup(modelList, noRepetitionMap);
		FlowActuator.excute((TransactionFlow)FlowActuator.deserializeToFlow(RpcContext.getContext().getAttachment("flow")));
		return result;
	}

	@Override
	public boolean saveAsGroup(List<T> modelList) throws BusinessException {
		boolean result = getTable().saveAsGroup(modelList);
		FlowActuator.excute((TransactionFlow)FlowActuator.deserializeToFlow(RpcContext.getContext().getAttachment("flow")));
		return result;
	}

	@Override
	public boolean saveAsGroup(List<T> modelList, @SuppressWarnings("unchecked") List<T>... modelLists) throws BusinessException {
		boolean result = getTable().saveAsGroup(modelList, modelLists);
		FlowActuator.excute((TransactionFlow)FlowActuator.deserializeToFlow(RpcContext.getContext().getAttachment("flow")));
		return result;
	}

	@Override
	public boolean saveAsGroup(List<T> modelList, @SuppressWarnings("unchecked") T... modelLists) throws BusinessException {
		boolean result = getTable().saveAsGroup(modelList, modelLists);
		FlowActuator.excute((TransactionFlow)FlowActuator.deserializeToFlow(RpcContext.getContext().getAttachment("flow")));
		return result;
	}

	@Override
	public boolean deleteForAny(T conditions) throws BusinessException {
		boolean result = getTable().deleteForAny(conditions);
		FlowActuator.excute((TransactionFlow)FlowActuator.deserializeToFlow(RpcContext.getContext().getAttachment("flow")));
		return result;
	}

	@Override
	public boolean delete(T model) throws BusinessException {
		boolean result = getTable().delete(model);
		FlowActuator.excute((TransactionFlow)FlowActuator.deserializeToFlow(RpcContext.getContext().getAttachment("flow")));
		return result;
	}

	@Override
	public boolean delete(T model, @SuppressWarnings("unchecked") T... models) throws BusinessException {
		boolean result = getTable().delete(model, models);
		FlowActuator.excute((TransactionFlow)FlowActuator.deserializeToFlow(RpcContext.getContext().getAttachment("flow")));
		return result;
	}

	@Override
	public boolean delete(T model, @SuppressWarnings("unchecked") List<T>... modelLists) throws BusinessException {
		boolean result = getTable().delete(model, modelLists);
		FlowActuator.excute((TransactionFlow)FlowActuator.deserializeToFlow(RpcContext.getContext().getAttachment("flow")));
		return result;
	}

	@Override
	public boolean delete(Object... primaryKey) throws BusinessException {
		boolean result = getTable().delete(primaryKey);
		FlowActuator.excute((TransactionFlow)FlowActuator.deserializeToFlow(RpcContext.getContext().getAttachment("flow")));
		return result;
	}

	@Override
	public boolean deleteAsGroup(List<T> modelList) throws BusinessException {
		boolean result = getTable().deleteAsGroup(modelList);
		FlowActuator.excute((TransactionFlow)FlowActuator.deserializeToFlow(RpcContext.getContext().getAttachment("flow")));
		return result;
	}

	@Override
	public boolean deleteAsGroup(List<T> modelList, @SuppressWarnings("unchecked") T... models) throws BusinessException {
		boolean result = getTable().deleteAsGroup(modelList, models);
		FlowActuator.excute((TransactionFlow)FlowActuator.deserializeToFlow(RpcContext.getContext().getAttachment("flow")));
		return result;
	}

	@Override
	public boolean deleteAsGroup(List<T> modelList, @SuppressWarnings("unchecked") List<T>... modelLists) throws BusinessException {
		boolean result = getTable().deleteAsGroup(modelList, modelLists);
		FlowActuator.excute((TransactionFlow)FlowActuator.deserializeToFlow(RpcContext.getContext().getAttachment("flow")));
		return result;
	}
	
	@Override
	public boolean deleteAsGroup(Object[]... primaryKeySet) throws BusinessException {
		boolean result = getTable().deleteAsGroup(primaryKeySet);
		FlowActuator.excute((TransactionFlow)FlowActuator.deserializeToFlow(RpcContext.getContext().getAttachment("flow")));
		return result;
	}

	@Override
	@Deprecated
	public boolean deleteAll() throws BusinessException {
		boolean result = getTable().deleteAll();
		FlowActuator.excute((TransactionFlow)FlowActuator.deserializeToFlow(RpcContext.getContext().getAttachment("flow")));
		return result;
	}

	@Override
	public boolean update(T model) throws BusinessException {
		boolean result = getTable().update(model);
		FlowActuator.excute((TransactionFlow)FlowActuator.deserializeToFlow(RpcContext.getContext().getAttachment("flow")));
		return result;
	}

	@Override
	public boolean update(T model, T conditationMap) throws BusinessException {
		boolean result = getTable().update(model, conditationMap);
		FlowActuator.excute((TransactionFlow)FlowActuator.deserializeToFlow(RpcContext.getContext().getAttachment("flow")));
		return result;
	}

	@Override
	public boolean update(T model, @SuppressWarnings("unchecked") T... models) throws BusinessException {
		boolean result = getTable().update(model, models);
		FlowActuator.excute((TransactionFlow)FlowActuator.deserializeToFlow(RpcContext.getContext().getAttachment("flow")));
		return result;
	}

	@Override
	public boolean update(T model, @SuppressWarnings("unchecked") List<T>... models) throws BusinessException {
		boolean result = getTable().update(model, models);
		FlowActuator.excute((TransactionFlow)FlowActuator.deserializeToFlow(RpcContext.getContext().getAttachment("flow")));
		return result;
	}

	@Override
	public boolean updateAsGroup(List<T> modelList) throws BusinessException {
		boolean result = getTable().updateAsGroup(modelList);
		FlowActuator.excute((TransactionFlow)FlowActuator.deserializeToFlow(RpcContext.getContext().getAttachment("flow")));
		return result;
	}

	@Override
	public boolean updateAsGroup(List<T> modelList, T conditationMap) throws BusinessException {
		boolean result = getTable().updateAsGroup(modelList, conditationMap);
		FlowActuator.excute((TransactionFlow)FlowActuator.deserializeToFlow(RpcContext.getContext().getAttachment("flow")));
		return result;
	}

	@Override
	public boolean updateAsGroup(List<T> modelList, @SuppressWarnings("unchecked") T... models) throws BusinessException {
		boolean result = getTable().updateAsGroup(modelList, models);
		FlowActuator.excute((TransactionFlow)FlowActuator.deserializeToFlow(RpcContext.getContext().getAttachment("flow")));
		return result;
	}

	@Override
	public boolean updateAsGroup(List<T> modelList, @SuppressWarnings("unchecked") List<T>... modelLists) throws BusinessException {
		boolean result = getTable().updateAsGroup(modelList, modelLists);
		FlowActuator.excute((TransactionFlow)FlowActuator.deserializeToFlow(RpcContext.getContext().getAttachment("flow")));
		return result;
	}

	@Override
	public T findById(Object... primaryKey) throws BusinessException {
		T result = getTable().findById(primaryKey);
		FlowActuator.excute((TransactionFlow)FlowActuator.deserializeToFlow(RpcContext.getContext().getAttachment("flow")));
		return result;
	}

	@Override
	public T find(T condition) throws BusinessException {
		T result = getTable().find(condition);
		FlowActuator.excute((TransactionFlow)FlowActuator.deserializeToFlow(RpcContext.getContext().getAttachment("flow")));
		return result;
	}

	@Override
	public List<T> findByIdAsGroup(Long[] IdSet) throws BusinessException {
		List<T> result = getTable().findByIdAsGroup(IdSet);
		FlowActuator.excute((TransactionFlow)FlowActuator.deserializeToFlow(RpcContext.getContext().getAttachment("flow")));
		return result;
	}
	
	@Override
	public List<T> findByIdsAsGroup(Long[]... IdSets) throws BusinessException {
		List<T> result = getTable().findByIdsAsGroup(IdSets);
		FlowActuator.excute((TransactionFlow)FlowActuator.deserializeToFlow(RpcContext.getContext().getAttachment("flow")));
		return result;
	}

	@Override
	public List<T> findList() throws BusinessException {
		List<T> result = getTable().findList();
		FlowActuator.excute((TransactionFlow)FlowActuator.deserializeToFlow(RpcContext.getContext().getAttachment("flow")));
		return result;
	}

	@Override
	public List<T> findList(T condition) throws BusinessException {
		List<T> result = getTable().findList(condition);
		FlowActuator.excute((TransactionFlow)FlowActuator.deserializeToFlow(RpcContext.getContext().getAttachment("flow")));
		return result;
	}

	@Override
	public Page<T> findListInPage(int pageNumber, int pageSize) throws BusinessException {
		Page<T> result = getTable().findListInPage(pageNumber, pageSize);
		FlowActuator.excute((TransactionFlow)FlowActuator.deserializeToFlow(RpcContext.getContext().getAttachment("flow")));
		return result;
	}

	@Override
	public Page<T> findListInPageWithKeywords(int pageNumber, int pageSize, T condition) throws BusinessException {
		Page<T> result = getTable().findListInPageWithKeywords(pageNumber, pageSize, condition);
		FlowActuator.excute((TransactionFlow)FlowActuator.deserializeToFlow(RpcContext.getContext().getAttachment("flow")));
		return result;
	}

	@Override
	public Page<T> findListInPage(int pageNumber, int pageSize, String sortBy, String sortOrder) throws BusinessException {
		Page<T> result = getTable().findListInPage(pageNumber, pageSize, sortBy, sortOrder);
		FlowActuator.excute((TransactionFlow)FlowActuator.deserializeToFlow(RpcContext.getContext().getAttachment("flow")));
		return result;
	}

	@Override
	public Page<T> findListInPageWithKeywords(int pageNumber, int pageSize, T condition, String sortBy, String sortOrder)
			throws BusinessException {
		Page<T> result = getTable().findListInPageWithKeywords(pageNumber, pageSize, condition, sortBy, sortOrder);
		FlowActuator.excute((TransactionFlow)FlowActuator.deserializeToFlow(RpcContext.getContext().getAttachment("flow")));
		return result;
	}
	
	@Override
	public int execute(String key, Map<?, ?> data) throws BusinessException {
		int result = getTable().execute(key, data);
		FlowActuator.excute((TransactionFlow)FlowActuator.deserializeToFlow(RpcContext.getContext().getAttachment("flow")));
		return result;
	}
	
	@Override
	public T find(String key, Map<?, ?> data) throws BusinessException {
		T result = getTable().find(key, data);
		FlowActuator.excute((TransactionFlow)FlowActuator.deserializeToFlow(RpcContext.getContext().getAttachment("flow")));
		return result;
	}
	
	@Override
	public List<T> findList(String key, Map<?, ?> data) throws BusinessException {
		List<T> result = getTable().findList(key, data);
		FlowActuator.excute((TransactionFlow)FlowActuator.deserializeToFlow(RpcContext.getContext().getAttachment("flow")));
		return result;
	}
	
	public abstract Table<T> getTable();
=======
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceImpl.class);

    @Override
    public boolean save(T model) throws BusinessException {
        boolean result = getTable().save(model);
        executeNode();
        return result;
    }

    @Override
    public boolean saveForAny(Object... args) throws BusinessException {
        boolean result = getTable().saveForAny(args);
        executeNode();
        return result;
    }

    @Override
    public boolean save(T model, Object... noRepetition) throws BusinessException {
        boolean result = getTable().save(model, noRepetition);
        executeNode();
        return result;
    }

    @Override
    public boolean save(T model, T noRepetitionMap) throws BusinessException {
        boolean result = getTable().save(model, noRepetitionMap);
        executeNode();
        return result;
    }

    @Override
    public boolean saveWithTransaction(T model, Model... models) throws BusinessException {
        boolean result = getTable().saveWithTransaction(model, models);
        executeNode();
        return result;
    }

    @Override
    public boolean saveWithTransactionForList(T model, @SuppressWarnings("unchecked") List<? extends Model>... models)
        throws BusinessException {
        boolean result = getTable().saveWithTransactionForList(model, models);
        executeNode();
        return result;
    }

    @Override
    public boolean saveAsGroupForAny(@SuppressWarnings("unchecked") List<Object>... args) throws BusinessException {
        boolean result = getTable().saveAsGroupForAny(args);
        executeNode();
        return result;
    }

    @Override
    public boolean saveAsGroup(List<T> modelList, Object... noRepetition) throws BusinessException {
        boolean result = getTable().saveAsGroup(modelList, noRepetition);
        executeNode();
        return result;
    }

    @Override
    public boolean saveAsGroup(List<T> modelList, T noRepetitionMap) throws BusinessException {
        boolean result = getTable().saveAsGroup(modelList, noRepetitionMap);
        executeNode();
        return result;
    }

    @Override
    public boolean saveAsGroup(List<T> modelList) throws BusinessException {
        boolean result = getTable().saveAsGroup(modelList);
        executeNode();
        return result;
    }

    @Override
    public boolean saveAsGroupWithTransactionForList(List<T> modelList,
        @SuppressWarnings("unchecked") List<? extends Model>... modelLists) throws BusinessException {
        boolean result = getTable().saveAsGroupWithTransactionForList(modelList, modelLists);
        executeNode();
        return result;
    }

    @Override
    public boolean deleteForAny(T conditions) throws BusinessException {
        boolean result = getTable().deleteForAny(conditions);
        executeNode();
        return result;
    }

    @Override
    public boolean delete(T model) throws BusinessException {
        boolean result = getTable().delete(model);
        executeNode();
        return result;
    }

    @Override
    public boolean deleteWithTransaction(T model, Model... models) throws BusinessException {
        boolean result = getTable().delete(model, models);
        executeNode();
        return result;
    }

    @Override
    public boolean deleteWithTransactionForList(T model,
        @SuppressWarnings("unchecked") List<? extends Model>... modelLists) throws BusinessException {
        boolean result = getTable().deleteWithTransactionForList(model, modelLists);
        executeNode();
        return result;
    }

    @Override
    public boolean delete(Object... primaryKey) throws BusinessException {
        boolean result = getTable().delete(primaryKey);
        executeNode();
        return result;
    }

    @Override
    public boolean deleteAsGroup(List<T> modelList) throws BusinessException {
        boolean result = getTable().deleteAsGroup(modelList);
        executeNode();
        return result;
    }

    @Override
    public boolean deleteAsGroupWithTransaction(List<T> modelList, Model... models) throws BusinessException {
        boolean result = getTable().deleteAsGroupWithTransaction(modelList, models);
        executeNode();
        return result;
    }

    @Override
    public boolean deleteAsGroupWithTransactionForList(List<T> modelList,
        @SuppressWarnings("unchecked") List<? extends Model>... modelLists) throws BusinessException {
        boolean result = getTable().deleteAsGroupWithTransactionForList(modelList, modelLists);
        executeNode();
        return result;
    }

    @Override
    public boolean deleteAsGroup(Object[]... primaryKeySet) throws BusinessException {
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
        boolean result = getTable().update(model);
        executeNode();
        return result;
    }

    @Override
    public boolean update(T model, T conditationMap) throws BusinessException {
        boolean result = getTable().update(model, conditationMap);
        executeNode();
        return result;
    }

    @Override
    public boolean updateWithTransaction(T model, Model... models) throws BusinessException {
        boolean result = getTable().updateWithTransaction(model, models);
        executeNode();
        return result;
    }

    @Override
    public boolean updateWithTransactionForList(T model, @SuppressWarnings("unchecked") List<? extends Model>... models)
        throws BusinessException {
        boolean result = getTable().updateWithTransactionForList(model, models);
        executeNode();
        return result;
    }

    @Override
    public boolean updateAsGroup(List<T> modelList) throws BusinessException {
        boolean result = getTable().updateAsGroup(modelList);
        executeNode();
        return result;
    }

    @Override
    public boolean updateAsGroup(List<T> modelList, T conditationMap) throws BusinessException {
        boolean result = getTable().updateAsGroup(modelList, conditationMap);
        executeNode();
        return result;
    }

    @Override
    public boolean updateAsGroupWithTransaction(List<T> modelList, Model... models) throws BusinessException {
        boolean result = getTable().updateAsGroupWithTransaction(modelList, models);
        executeNode();
        return result;
    }

    @Override
    public boolean updateAsGroupWithTransactionForList(List<T> modelList,
        @SuppressWarnings("unchecked") List<? extends Model>... modelLists) throws BusinessException {
        boolean result = getTable().updateAsGroupWithTransactionForList(modelList, modelLists);
        executeNode();
        return result;
    }

    @Override
    public T findById(Object... primaryKey) throws BusinessException {
        T result = getTable().findById(primaryKey);
        executeNode();
        return result;
    }

    @Override
    public T find(T condition) throws BusinessException {
        T result = getTable().find(condition);
        executeNode();
        return result;
    }

    @Override
    public List<T> findByIdAsGroup(Long[] IdSet) throws BusinessException {
        List<T> result = getTable().findByIdAsGroup(IdSet);
        executeNode();
        return result;
    }

    @Override
    public List<T> findByIdsAsGroup(Long[]... IdSets) throws BusinessException {
        List<T> result = getTable().findByIdsAsGroup(IdSets);
        executeNode();
        return result;
    }

    @Override
    public List<T> findList() throws BusinessException {
        List<T> result = getTable().findList();
        executeNode();
        return result;
    }

    @Override
    public List<T> findList(T condition) throws BusinessException {
        List<T> result = getTable().findList(condition);
        executeNode();
        return result;
    }

    @Override
    public Page<T> findListInPage(int pageNumber, int pageSize) throws BusinessException {
        Page<T> result = getTable().findListInPage(pageNumber, pageSize);
        executeNode();
        return result;
    }

    @Override
    public Page<T> findListInPageWithKeywords(int pageNumber, int pageSize, T condition) throws BusinessException {
        Page<T> result = getTable().findListInPageWithKeywords(pageNumber, pageSize, condition);
        executeNode();
        return result;
    }

    @Override
    public Page<T> findListInPage(int pageNumber, int pageSize, String sortBy, String sortOrder)
        throws BusinessException {
        Page<T> result = getTable().findListInPage(pageNumber, pageSize, sortBy, sortOrder);
        executeNode();
        return result;
    }

    @Override
    public Page<T> findListInPageWithKeywords(int pageNumber, int pageSize, T condition, String sortBy,
        String sortOrder) throws BusinessException {
        Page<T> result = getTable().findListInPageWithKeywords(pageNumber, pageSize, condition, sortBy, sortOrder);
        executeNode();
        return result;
    }

    @Override
    public int execute(String key, Map<?, ?> data) throws BusinessException {
        int result = getTable().execute(key, data);
        executeNode();
        return result;
    }

    @Override
    public T find(String key, Map<?, ?> data) throws BusinessException {
        T result = getTable().find(key, data);
        executeNode();
        return result;
    }

    @Override
    public List<T> findList(String key, Map<?, ?> data) throws BusinessException {
        List<T> result = getTable().findList(key, data);
        executeNode();
        return result;
    }

    private void executeNode() {
        String index = RpcContext.getContext().getAttachment(FlowActuator.getNodeIndex());
        if (index == null) {
            return;
        }
        String endIndex = RpcContext.getContext().getAttachment(FlowActuator.getNodeIndexMax());
        if (endIndex == null) {
            return;
        }
        if (index.compareTo(endIndex) == 0 || index.compareTo(endIndex) == 1) {
            return;
        }

        int next = Integer.parseInt(index) + 1;
        int end = Integer.parseInt(endIndex);

        for (int i = next; i < end; i++) {
            String nodeKey = FlowActuator.getNodeKey() + i;
            RpcContext.getContext().setAttachment(nodeKey, RpcContext.getContext().getAttachment(nodeKey));
        }

        String flowName = RpcContext.getContext().getAttachment(FlowActuator.getFlowName());

        TransactionNode node = (TransactionNode)FlowActuator
            .deserializeToNode(RpcContext.getContext().getAttachment(FlowActuator.getNodeKey() + index));

        if ((next - 1) == 0) {
            LOGGER.info("流程[" + flowName + "]开始执行。");
        }
        if (next > end) {
            LOGGER.info("流程[" + flowName + "]执行结束,结束时间:" + System.currentTimeMillis());
            return;
        }
        String method = node.getClassName() + "." + node.getMethodName();
        LOGGER.info("流程[" + flowName + "]-节点[" + index + "]，方法[" + method + "]执行时间:" + System.currentTimeMillis());

        FlowKit.executeTransactionFlow(node);
    }

    public abstract Table<T> getTable();
>>>>>>> Stashed changes

}

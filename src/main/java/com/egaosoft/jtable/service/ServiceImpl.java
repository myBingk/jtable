package com.egaosoft.jtable.service;

import java.util.List;
import java.util.Map;

import com.alibaba.dubbo.rpc.RpcContext;
import com.egaosoft.jtable.core.Table;
import com.egaosoft.jtable.exception.BusinessException;
import com.egaosoft.jtable.transaction.FlowActuator;
import com.egaosoft.transaction.TransactionFlow;
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

}

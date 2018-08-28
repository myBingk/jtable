package com.egaosoft.jtable.dubbo;

import java.util.List;
import java.util.Map;

import com.egaosoft.jtable.exception.BusinessException;
import com.egaosoft.jtable.service.Service;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

/**
 * dubbo本地伪装，用于调用服务前的拦截操作
 * 
 * @author DoubleCome
 * @date 2018年7月9日 下午1:26:48
 */
@SuppressWarnings("rawtypes")
public class ServiceStub<T extends Model> implements Service<T> {

    protected Service<T> service;

    @Override
    public boolean save(T model) throws BusinessException {
        return service.save(model);
    }

    @Override
    public boolean saveForAny(Object... args) throws BusinessException {
        return service.saveForAny(args);
    }

    @Override
    public boolean save(T model, Object... noRepetition) throws BusinessException {
        return service.save(model, noRepetition);
    }

    @Override
    public boolean save(T model, T noRepetitionMap) throws BusinessException {
        return service.save(model, noRepetitionMap);
    }

    @Override
    public boolean saveWithTransaction(T model, Model... models) throws BusinessException {
        return service.saveWithTransaction(model, models);
    }

    @Override
    public boolean saveWithTransactionForList(T model, @SuppressWarnings("unchecked") List<? extends Model>... models)
        throws BusinessException {
        return service.saveWithTransactionForList(model, models);
    }

    @Override
    public boolean saveAsGroupForAny(@SuppressWarnings("unchecked") List<Object>... args) throws BusinessException {
        return service.saveAsGroupForAny(args);
    }

    @Override
    public boolean saveAsGroup(List<T> modelList, Object... noRepetition) throws BusinessException {
        return service.saveAsGroup(modelList, noRepetition);
    }

    @Override
    public boolean saveAsGroup(List<T> modelList, T noRepetitionMap) throws BusinessException {
        return service.saveAsGroup(modelList, noRepetitionMap);
    }

    @Override
    public boolean saveAsGroup(List<T> modelList) throws BusinessException {
        return service.saveAsGroup(modelList);
    }

    @Override
    public boolean saveAsGroupWithTransactionForList(List<T> modelList,
        @SuppressWarnings("unchecked") List<? extends Model>... modelLists) throws BusinessException {
        return service.saveAsGroupWithTransactionForList(modelList, modelLists);
    }

    @Override
    public boolean saveAsGroupWithTransaction(List<T> modelList, Model... modelLists) throws BusinessException {
        return service.saveAsGroupWithTransaction(modelList, modelLists);
    }

    @Override
    public boolean deleteForAny(T conditions) throws BusinessException {
        return service.deleteForAny(conditions);
    }

    @Override
    public boolean delete(T model) throws BusinessException {
        return service.delete(model);
    }

    @Override
    public boolean deleteWithTransaction(T model, Model... models) throws BusinessException {
        return service.deleteWithTransaction(model, models);
    }

    @Override
    public boolean deleteWithTransactionForList(T model,
        @SuppressWarnings("unchecked") List<? extends Model>... modelLists) throws BusinessException {
        return service.deleteWithTransactionForList(model, modelLists);
    }

    @Override
    public boolean delete(Object... primaryKey) throws BusinessException {
        return service.delete(primaryKey);
    }

    @Override
    public boolean deleteAsGroup(List<T> modelList) throws BusinessException {
        return service.deleteAsGroup(modelList);
    }

    @Override
    public boolean deleteAsGroupWithTransaction(List<T> modelList, Model... models) throws BusinessException {
        return service.deleteAsGroupWithTransaction(modelList, models);
    }

    @Override
    public boolean deleteAsGroupWithTransactionForList(List<T> modelList,
        @SuppressWarnings("unchecked") List<? extends Model>... modelLists) throws BusinessException {
        return service.deleteAsGroupWithTransactionForList(modelList, modelLists);
    }

    @Override
    public boolean deleteAsGroup(Object[]... primaryKeySet) throws BusinessException {
        return service.deleteAsGroup(primaryKeySet);
    }

    @Override
    @Deprecated
    public boolean deleteAll() throws BusinessException {
        return service.deleteAll();
    }

    @Override
    public boolean update(T model) throws BusinessException {
        return service.update(model);
    }

    @Override
    public boolean update(T model, T conditationMap) throws BusinessException {
        return service.update(model, conditationMap);
    }

    @Override
    public boolean updateWithTransaction(T model, Model... models) throws BusinessException {
        return service.updateWithTransaction(model, models);
    }

    @Override
    public boolean
        updateWithTransactionForList(T model, @SuppressWarnings("unchecked") List<? extends Model>... models)
            throws BusinessException {
        return service.updateWithTransactionForList(model, models);
    }

    @Override
    public boolean updateAsGroup(List<T> modelList) throws BusinessException {
        return service.updateAsGroup(modelList);
    }

    @Override
    public boolean updateAsGroup(List<T> modelList, T conditationMap) throws BusinessException {
        return service.updateAsGroup(modelList, conditationMap);
    }

    @Override
    public boolean updateAsGroupWithTransaction(List<T> modelList, Model... models) throws BusinessException {
        return service.updateAsGroupWithTransaction(modelList, models);
    }

    @Override
    public boolean updateAsGroupWithTransactionForList(List<T> modelList,
        @SuppressWarnings("unchecked") List<? extends Model>... modelLists) throws BusinessException {
        return service.updateAsGroupWithTransactionForList(modelList, modelLists);
    }

    @Override
    public T findById(Object... primaryKey) throws BusinessException {
        return service.findById(primaryKey);
    }

    @Override
    public T find(T condition) throws BusinessException {
        return service.find(condition);
    }

    @Override
    public List<T> findByIdAsGroup(Long[] IdSet) throws BusinessException {
        return service.findByIdAsGroup(IdSet);
    }

    @Override
    public List<T> findByIdsAsGroup(Long[]... IdSets) throws BusinessException {
        return service.findByIdsAsGroup(IdSets);
    }

    @Override
    public List<T> findList() throws BusinessException {
        return service.findList();
    }

    @Override
    public List<T> findList(T condition) throws BusinessException {
        return service.findList(condition);
    }

    @Override
    public Page<T> findListInPage(int pageNumber, int pageSize) throws BusinessException {
        return service.findListInPage(pageNumber, pageSize);
    }

    @Override
    public Page<T> findListInPageWithKeywords(int pageNumber, int pageSize, T condition) throws BusinessException {
        return service.findListInPageWithKeywords(pageNumber, pageSize, condition);
    }

    @Override
    public Page<T> findListInPage(int pageNumber, int pageSize, String sortBy, String sortOrder)
        throws BusinessException {
        return service.findListInPage(pageNumber, pageSize, sortBy, sortOrder);
    }

    @Override
    public Page<T>
        findListInPageWithKeywords(int pageNumber, int pageSize, T condition, String sortBy, String sortOrder)
            throws BusinessException {
        return service.findListInPageWithKeywords(pageNumber, pageSize, condition, sortBy, sortOrder);
    }

    @Override
    public int execute(String key, Map<?, ?> data) throws BusinessException {
        return service.execute(key, data);
    }

    @Override
    public T find(String key, Map<?, ?> data) throws BusinessException {
        return service.find(key, data);
    }

    @Override
    public List<T> findList(String key, Map<?, ?> data) throws BusinessException {
        return service.findList(key, data);
    }

}

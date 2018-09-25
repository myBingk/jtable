package com.egaosoft.jtable.dubbo;

import java.util.List;
import java.util.Map;

import com.alibaba.dubbo.rpc.RpcException;
import com.egaosoft.jtable.service.DubboMockException;
import com.egaosoft.jtable.service.Service;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

/**
 * dubbo服务异常处理
 * 
 * @author DoubleCome
 * @date 2018年7月9日 下午1:26:30
 */
@SuppressWarnings("rawtypes")
public class ServiceMock<T extends Model> implements Service<T> {

    @Override
    public boolean save(T model) throws DubboMockException {
        throw new DubboMockException(2000, new RpcException("网络异常"));
    }

    @Override
    public boolean saveForAny(Object... args) throws DubboMockException {
        throw new DubboMockException(2000, new RpcException("网络异常"));
    }

    @Override
    public boolean save(T model, Object... noRepetition) throws DubboMockException {
        throw new DubboMockException(2000, new RpcException("网络异常"));
    }

    @Override
    public boolean save(T model, T noRepetitionMap) throws DubboMockException {
        throw new DubboMockException(2000, new RpcException("网络异常"));
    }

    @Override
    public boolean saveWithTransaction(T model, Model... models) throws DubboMockException {
        throw new DubboMockException(2000, new RpcException("网络异常"));
    }

    @Override
    public boolean saveWithTransactionForList(T model, @SuppressWarnings("unchecked") List<? extends Model>... models)
        throws DubboMockException {
        throw new DubboMockException(2000, new RpcException("网络异常"));
    }

    @Override
    public boolean saveAsGroupForAny(@SuppressWarnings("unchecked") List<Object>... args) throws DubboMockException {
        throw new DubboMockException(2000, new RpcException("网络异常"));
    }

    @Override
    public boolean saveAsGroup(List<T> modelList, Object... noRepetition) throws DubboMockException {
        throw new DubboMockException(2000, new RpcException("网络异常"));
    }

    @Override
    public boolean saveAsGroup(List<T> modelList, T noRepetitionMap) throws DubboMockException {
        throw new DubboMockException(2000, new RpcException("网络异常"));
    }

    @Override
    public boolean saveAsGroup(List<T> modelList) throws DubboMockException {
        throw new DubboMockException(2000, new RpcException("网络异常"));
    }

    @Override
    public boolean saveAsGroupWithTransactionForList(List<T> modelList,
        @SuppressWarnings("unchecked") List<? extends Model>... modelLists) throws DubboMockException {
        throw new DubboMockException(2000, new RpcException("网络异常"));
    }

    @Override
    public boolean saveAsGroupWithTransaction(List<T> modelList, Model... modelLists) throws DubboMockException {
        throw new DubboMockException(2000, new RpcException("网络异常"));
    }

    @Override
    public boolean deleteForAny(T conditions) throws DubboMockException {
        throw new DubboMockException(2000, new RpcException("网络异常"));
    }

    @Override
    public boolean delete(T model) throws DubboMockException {
        throw new DubboMockException(2000, new RpcException("网络异常"));
    }

    @Override
    public boolean deleteWithTransaction(T model, Model... models) throws DubboMockException {
        throw new DubboMockException(2000, new RpcException("网络异常"));
    }

    @Override
    public boolean deleteWithTransactionForList(T model,
        @SuppressWarnings("unchecked") List<? extends Model>... modelLists) throws DubboMockException {
        throw new DubboMockException(2000, new RpcException("网络异常"));
    }

    @Override
    public boolean delete(Object... primaryKey) throws DubboMockException {
        throw new DubboMockException(2000, new RpcException("网络异常"));
    }

    @Override
    public boolean deleteAsGroup(List<T> modelList) throws DubboMockException {
        throw new DubboMockException(2000, new RpcException("网络异常"));
    }

    @Override
    public boolean deleteAsGroupWithTransaction(List<T> modelList, Model... models) throws DubboMockException {
        throw new DubboMockException(2000, new RpcException("网络异常"));
    }

    @Override
    public boolean deleteAsGroupWithTransactionForList(List<T> modelList,
        @SuppressWarnings("unchecked") List<? extends Model>... modelLists) throws DubboMockException {
        throw new DubboMockException(2000, new RpcException("网络异常"));
    }

    @Override
    public boolean deleteAsGroup(Object[]... primaryKeySet) throws DubboMockException {
        throw new DubboMockException(2000, new RpcException("网络异常"));
    }

    @Override
    public boolean deleteAll() throws DubboMockException {
        throw new DubboMockException(2000, new RpcException("网络异常"));
    }

    @Override
    public boolean update(T model) throws DubboMockException {
        throw new DubboMockException(2000, new RpcException("网络异常"));
    }

    @Override
    public boolean update(T model, T conditationMap) throws DubboMockException {
        throw new DubboMockException(2000, new RpcException("网络异常"));
    }

    @Override
    public boolean updateWithTransaction(T model, Model... models) throws DubboMockException {
        throw new DubboMockException(2000, new RpcException("网络异常"));
    }

    @Override
    public boolean
        updateWithTransactionForList(T model, @SuppressWarnings("unchecked") List<? extends Model>... models)
            throws DubboMockException {
        throw new DubboMockException(2000, new RpcException("网络异常"));
    }

    @Override
    public boolean updateAsGroup(List<T> modelList) throws DubboMockException {
        throw new DubboMockException(2000, new RpcException("网络异常"));
    }

    @Override
    public boolean updateAsGroup(List<T> modelList, T conditationMap) throws DubboMockException {
        throw new DubboMockException(2000, new RpcException("网络异常"));
    }

    @Override
    public boolean updateAsGroupWithTransaction(List<T> modelList, Model... models) throws DubboMockException {
        throw new DubboMockException(2000, new RpcException("网络异常"));
    }

    @Override
    public boolean updateAsGroupWithTransactionForList(List<T> modelList,
        @SuppressWarnings("unchecked") List<? extends Model>... modelLists) throws DubboMockException {
        throw new DubboMockException(2000, new RpcException("网络异常"));
    }

    @Override
    public T findById(Object... primaryKey) throws DubboMockException {
        throw new DubboMockException(2000, new RpcException("网络异常"));
    }

    @Override
    public T find(T condition) throws DubboMockException {
        throw new DubboMockException(2000, new RpcException("网络异常"));
    }

    @Override
    public List<T> findByIdAsGroup(Long[] idSet) throws DubboMockException {
        throw new DubboMockException(2000, new RpcException("网络异常"));
    }

    @Override
    public List<T> findByIdsAsGroup(Long[]... idSets) throws DubboMockException {
        throw new DubboMockException(2000, new RpcException("网络异常"));
    }

    @Override
    public List<T> findList() throws DubboMockException {
        throw new DubboMockException(2000, new RpcException("网络异常"));
    }

    @Override
    public List<T> findList(T condition) throws DubboMockException {
        throw new DubboMockException(2000, new RpcException("网络异常"));
    }

    @Override
    public Page<T> findListInPage(int pageNumber, int pageSize) throws DubboMockException {
        throw new DubboMockException(2000, new RpcException("网络异常"));
    }

    @Override
    public Page<T> findListInPageWithKeywords(int pageNumber, int pageSize, T condition) throws DubboMockException {
        throw new DubboMockException(2000, new RpcException("网络异常"));
    }

    @Override
    public Page<T> findListInPage(int pageNumber, int pageSize, String sortBy, String sortOrder)
        throws DubboMockException {
        throw new DubboMockException(2000, new RpcException("网络异常"));
    }

    @Override
    public Page<T>
        findListInPageWithKeywords(int pageNumber, int pageSize, T condition, String sortBy, String sortOrder)
            throws DubboMockException {
        throw new DubboMockException(2000, new RpcException("网络异常"));
    }

    @Override
    public int execute(String key, Map<?, ?> data) throws DubboMockException {
        throw new DubboMockException(2000, new RpcException("网络异常"));
    }

    @Override
    public T find(String key, Map<?, ?> data) throws DubboMockException {
        throw new DubboMockException(2000, new RpcException("网络异常"));
    }

    @Override
    public List<T> findList(String key, Map<?, ?> data) throws DubboMockException {
        throw new DubboMockException(2000, new RpcException("网络异常"));
    }

}

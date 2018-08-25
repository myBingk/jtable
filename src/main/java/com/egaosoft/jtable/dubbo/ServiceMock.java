package com.egaosoft.jtable.dubbo;

import java.util.List;
import java.util.Map;

import com.egaosoft.jtable.exception.DubboMockException;
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
		throw new DubboMockException(2000);
	}

	@Override
	public boolean saveForAny(Object... args) throws DubboMockException {
		throw new DubboMockException(2000);
	}

	@Override
	public boolean save(T model, Object... noRepetition) throws DubboMockException {
		throw new DubboMockException(2000);
	}

	@Override
	public boolean save(T model, T noRepetitionMap) throws DubboMockException {
		throw new DubboMockException(2000);
	}

	@Override
	public boolean save(T model, @SuppressWarnings("unchecked") T... models) throws DubboMockException {
		throw new DubboMockException(2000);
	}

	@Override
	public boolean save(T model, @SuppressWarnings("unchecked") List<T>... models) throws DubboMockException {
		throw new DubboMockException(2000);
	}

	@Override
	public boolean saveAsGroupForAny(@SuppressWarnings("unchecked") List<Object>... args) throws DubboMockException {
		throw new DubboMockException(2000);
	}

	@Override
	public boolean saveAsGroup(List<T> modelList, Object... noRepetition) throws DubboMockException {
		throw new DubboMockException(2000);
	}

	@Override
	public boolean saveAsGroup(List<T> modelList, T noRepetitionMap) throws DubboMockException {
		throw new DubboMockException(2000);
	}

	@Override
	public boolean saveAsGroup(List<T> modelList) throws DubboMockException {
		throw new DubboMockException(2000);
	}

	@Override
	public boolean saveAsGroup(List<T> modelList, @SuppressWarnings("unchecked") List<T>... modelLists) throws DubboMockException {
		throw new DubboMockException(2000);
	}

	@Override
	public boolean saveAsGroup(List<T> modelList, @SuppressWarnings("unchecked") T... modelLists) throws DubboMockException {
		throw new DubboMockException(2000);
	}

	@Override
	public boolean deleteForAny(T conditions) throws DubboMockException {
		throw new DubboMockException(2000);
	}

	@Override
	public boolean delete(T model) throws DubboMockException {
		throw new DubboMockException(2000);
	}

	@Override
	public boolean delete(T model, @SuppressWarnings("unchecked") T... models) throws DubboMockException {
		throw new DubboMockException(2000);
	}

	@Override
	public boolean delete(T model, @SuppressWarnings("unchecked") List<T>... modelLists) throws DubboMockException {
		throw new DubboMockException(2000);
	}

	@Override
	public boolean delete(Object... primaryKey) throws DubboMockException {
		throw new DubboMockException(2000);
	}

	@Override
	public boolean deleteAsGroup(List<T> modelList) throws DubboMockException {
		throw new DubboMockException(2000);
	}

	@Override
	public boolean deleteAsGroup(List<T> modelList, @SuppressWarnings("unchecked") T... models) throws DubboMockException {
		throw new DubboMockException(2000);
	}

	@Override
	public boolean deleteAsGroup(List<T> modelList, @SuppressWarnings("unchecked") List<T>... modelLists) throws DubboMockException {
		throw new DubboMockException(2000);
	}

	@Override
	public boolean deleteAsGroup(Object[]... primaryKeySet) throws DubboMockException {
		throw new DubboMockException(2000);
	}

	@Override
	public boolean deleteAll() throws DubboMockException {
		throw new DubboMockException(2000);
	}

	@Override
	public boolean update(T model) throws DubboMockException {
		throw new DubboMockException(2000);
	}

	@Override
	public boolean update(T model, T conditationMap) throws DubboMockException {
		throw new DubboMockException(2000);
	}

	@Override
	public boolean update(T model, @SuppressWarnings("unchecked") T... models) throws DubboMockException {
		throw new DubboMockException(2000);
	}

	@Override
	public boolean update(T model, @SuppressWarnings("unchecked") List<T>... models) throws DubboMockException {
		throw new DubboMockException(2000);
	}

	@Override
	public boolean updateAsGroup(List<T> modelList) throws DubboMockException {
		throw new DubboMockException(2000);
	}

	@Override
	public boolean updateAsGroup(List<T> modelList, T conditationMap) throws DubboMockException {
		throw new DubboMockException(2000);
	}

	@Override
	public boolean updateAsGroup(List<T> modelList, @SuppressWarnings("unchecked") T... models) throws DubboMockException {
		throw new DubboMockException(2000);
	}

	@Override
	public boolean updateAsGroup(List<T> modelList, @SuppressWarnings("unchecked") List<T>... modelLists) throws DubboMockException {
		throw new DubboMockException(2000);
	}

	@Override
	public T findById(Object... primaryKey) throws DubboMockException {
		throw new DubboMockException(2000);
	}

	@Override
	public T find(T condition) throws DubboMockException {
		throw new DubboMockException(2000);
	}

	@Override
	public List<T> findByIdAsGroup(Long[] idSet) throws DubboMockException {
		throw new DubboMockException(2000);
	}

	@Override
	public List<T> findByIdsAsGroup(Long[]... idSets) throws DubboMockException {
		throw new DubboMockException(2000);
	}

	@Override
	public List<T> findList() throws DubboMockException {
		throw new DubboMockException(2000);
	}

	@Override
	public List<T> findList(T condition) throws DubboMockException {
		throw new DubboMockException(2000);
	}

	@Override
	public Page<T> findListInPage(int pageNumber, int pageSize) throws DubboMockException {
		throw new DubboMockException(2000);
	}

	@Override
	public Page<T> findListInPageWithKeywords(int pageNumber, int pageSize, T condition) throws DubboMockException {
		throw new DubboMockException(2000);
	}

	@Override
	public Page<T> findListInPage(int pageNumber, int pageSize, String sortBy, String sortOrder) throws DubboMockException {
		throw new DubboMockException(2000);
	}

	@Override
	public Page<T> findListInPageWithKeywords(int pageNumber, int pageSize, T condition, String sortBy, String sortOrder)
			throws DubboMockException {
		throw new DubboMockException(2000);
	}

	@Override
	public int execute(String key, Map<?, ?> data) throws DubboMockException {
		throw new DubboMockException(2000);
	}

	@Override
	public T find(String key, Map<?, ?> data) throws DubboMockException {
		throw new DubboMockException(2000);
	}

	@Override
	public List<T> findList(String key, Map<?, ?> data) throws DubboMockException {
		throw new DubboMockException(2000);
	}

}

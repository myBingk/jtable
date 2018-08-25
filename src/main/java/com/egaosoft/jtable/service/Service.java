package com.egaosoft.jtable.service;

import java.util.List;
import java.util.Map;

import com.egaosoft.jtable.exception.BusinessException;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

@SuppressWarnings({"rawtypes", "unchecked"})
public interface Service <T extends Model> {

	/**
	 * 插入一条数据，数据为model对象的属性值
	 * 
	 * @param model 插入对象
	 * @return 插入结果
	 * @throws BusinessException 当主键重复、组合主键重复、类型列数不匹配，非空值为空且无默认值时抛出。
	 * @author DoubleCome
	 * @since 1.0
	 */
	public boolean save(T model) throws BusinessException;
	
	/**
	 * 插入一条数据，插入值依照数据表列的设计顺序，请控制每个位置上的类型和列数。
	 * 
	 * @param args Object 插入值
	 * @return 插入结果
	 * @throws BusinessException 当主键重复、组合主键、类型不匹配、列数不匹配、非空值为空且无默认值时抛出。
	 * @author DoubleCome
	 * @since 2.0
	 */
	public boolean saveForAny(Object... args) throws BusinessException;
	
	/**
	 * 插入数据时判断数据表中除主键外是否存在相同值，存在时，不再重复插入，且抛出异常(throw new BusinessException)。
	 * 
	 * @param model 需要插入的对象
	 * @param noRepetition 不想重复的值, 全列匹配,即全表唯一。
	 * @return 插入结果
	 * @throws BusinessException 当传入参数非法时抛出
	 * @author DoubleCome 
	 * @since 2.0 
	 */
	public boolean save(T model, Object... noRepetition) throws BusinessException;
	
	/**
	 * 插入一条数据，并根据map内的键匹配表格对应列表，且值为不想重复的值数组。
	 * 
	 * @param model 需要插入的对象
	 * @param noRepetitionMap 键为列名，值为不想重复的数组
	 * @return 插入结果
	 * @throws BusinessException 当传入参数非法时抛出
	 * @author DoubleCome
	 * @since 2.0
	 */
	public boolean save(T model, T noRepetitionMap) throws BusinessException;
	
	/**
	 * 插入一条数据及想一起插入且在同一个事务内的其它对象，参数models将于model
	 * 放入同一块事务区，任意一个对象插入失败，本次操作将回滚事务。
	 * 
	 * @param model 需要插入的主对象
	 * @param models 其它需要插入的数据
	 * @return 所有对象的插入结果，任意结果失败，将返回false
	 * @throws BusinessException 当参数不合法、数据库插入异常时抛出。
	 * @author DoubleCome
	 * @since 2.0
	 */
	public boolean save(T model, T... models) throws BusinessException;
	
	/**
	 * 插入一条数据及希望一同插入且同事事务的多个list
	 * 
	 * @param model 需要插入的主对象
	 * @param models 需要同事插入的多个对象
	 * @return 所有对象的插入结果，任意结果失败，将返回false
	 * @throws BusinessException 当参数不合法、数据库插入异常时抛出。
	 * @author DoubleCome
	 * @since 2.0
	 */
	public boolean save(T model, List<T>... models) throws BusinessException;
	
	/**
	 * 插入多条数据，插入值按照数据表列的设计顺序，请控制每个位置上的类型和列数。
	 * List<Object>中的Object并不是一个完整的插入对象，只是一列的多条数据，并行数列。
	 * 
	 * @param args
	 * @return 插入结果
	 * @throws BusinessException 当主键重复、组合主键、类型不匹配、列数不匹配、非空值为空且无默认值时抛出。
	 * @author DoubleCome
	 * @since 2.0
	 */
	public boolean saveAsGroupForAny(List<Object>... args) throws BusinessException;
	
	/**
	 * 插入多条数据，插入数据时判断数据表中除主键外是否存在相同值，存在时，
	 * 不再重复插入，且抛出异常(throw new BusinessException)。
	 * 
	 * @param model 需要插入的对象
	 * @param noRepetition 不想重复的值, 全列匹配,即全表唯一。
	 * @return 插入结果
	 * @throws BusinessException 当传入参数非法时抛出
	 * @author DoubleCome 
	 * @since 2.0 
	 */
	public boolean saveAsGroup(List<T> modelList, Object... noRepetition) throws BusinessException;
	
	/**
	 * 插入多条数据，并根据map内的键匹配表格对应列表，且值为不想重复的值数组。
	 * 
	 * @param model 需要插入的对象
	 * @param noRepetitionMap 键为列名，值为不想重复的数组
	 * @return 插入结果
	 * @throws BusinessException 当传入参数非法时抛出
	 * @author DoubleCome
	 * @since 2.0
	 */
	public boolean saveAsGroup(List<T> modelList, T noRepetitionMap) throws BusinessException;
	
	/**
	 * 插入多条对象，数据为modelList中的属性值。
	 * 
	 * @param modelList 需要插入的对象
	 * @return 插入结果
	 * @throws BusinessException 当参数不合法、数据库操作异常时抛出。
	 * @author DoubleCome
	 * @since 1.0
	 */
	public boolean saveAsGroup(List<T> modelList) throws BusinessException;
	
	/**
	 * 插入多条数据及希望在同一块事务区的其它对象一同插入
	 * @param modelList 插入的主数据
	 * @param modelLists 希望一同插入的其它数据
	 * @return 插入结果，任一对象插入失败，返回false
	 * @throws BusinessException 当参数不合法、数据库操作异常时抛出。
	 * @author DoubleCome
	 * @since 2.0
	 */
	public boolean saveAsGroup(List<T> modelList, List<T>... modelLists) throws BusinessException;
	
	/**
	 * 插入多条数据及希望在同一块事务区的其它对象一同插入
	 * @param modelList 插入的主数据
	 * @param modelLists 希望一同插入的其它数据
	 * @return 插入结果，任一对象插入失败，返回false
	 * @throws BusinessException 当参数不合法、数据库操作异常时抛出。
	 * @author DoubleCome
	 * @since 2.0
	 */
	public boolean saveAsGroup(List<T> modelList, T... modelLists) throws BusinessException;

	/**
	 * 删除列为map的键且值为map的值对应列
	 * 
	 * @param conditions T 条件
	 * @return 删除结果
	 * @throws BusinessException 当传入参数非法时抛出
	 * @author DoubleCome
	 * @since 2.0
	 */
	public boolean deleteForAny(T conditions) throws BusinessException;
	
	/**
	 * 删除一条数据，条件为model对象的primaryKey字段
	 * 
	 * @param model 需要删除的对象
	 * @return 删除结果
	 * @throws BusinessException 当参数不合法、数据库操作异常时抛出
	 * @author DoubleCome
	 * @since 1.0
	 */
	public boolean delete(T model) throws BusinessException;
	
	/**
	 * 删除一条数据，删除需要同时删除的数据
	 * 
	 * @param model 需要删除的对象
	 * @param models 同时需要删除的对象
	 * @return 删除结果
	 * @throws BusinessException 当参数不合法、数据库操作异常时抛出
	 * @author DoubleCome
	 * @since 1.0
	 */
	public boolean delete(T model, T... models) throws BusinessException;
	
	/**
	 * 删除一条数据，删除需要同时删除的数据
	 * 
	 * @param model 需要删除的对象
	 * @param models 同时需要删除的对象
	 * @return 删除结果
	 * @throws BusinessException 当参数不合法、数据库操作异常时抛出
	 * @author DoubleCome
	 * @since 1.0
	 */
	public boolean delete(T model, List<T>... modelLists) throws BusinessException;
	
	/**
	 * 删除一条数据
	 * 
	 * @param primaryKey 需要删除对象的主键
	 * @return 删除结果
	 * @throws BusinessException 当参数不合法、数据库操作异常时抛出
	 * @author DoubleCome
	 * @since 2.0
	 */
	public boolean delete(Object... primaryKey) throws BusinessException;
	
	/**
	 * 删除多条数据
	 * 
	 * @param modelList 需要删除的对象，条件为model中的primaryKey属性值
	 * @return 删除结果
	 * @throws BusinessException 当参数不合法、数据库操作异常时抛出
	 * @author DoubleCome
	 * @since 2.0
	 */
	public boolean deleteAsGroup(List<T> modelList) throws BusinessException;
	
	/**
	 * 删除多条数据,删除需要同时删除的数据
	 * 
	 * @param modelList 需要删除的对象，条件为model中的primaryKey属性值
	 * @param models 需要同时删除的数据
	 * @return 删除结果
	 * @throws BusinessException 当参数不合法、数据库操作异常时抛出
	 * @author DoubleCome
	 * @since 2.0
	 */
	public boolean deleteAsGroup(List<T> modelList, T... models) throws BusinessException;
	
	/**
	 * 删除多条数据,删除需要同时删除的数据
	 * 
	 * @param modelList 需要删除的对象，条件为model中的primaryKey属性值
	 * @param modelLists 需要同时删除数据
	 * @return 删除结果
	 * @throws BusinessException 当参数不合法、数据库操作异常时抛出
	 * @author DoubleCome
	 * @since 2.0
	 */
	public boolean deleteAsGroup(List<T> modelList, List<T>... modelLists) throws BusinessException;
	
	/**
	 * 删除多条数据
	 * 
	 * @param primaryKeySet 主键数组值
	 * @return 删除结果
	 * @throws BusinessException 当参数不合法、数据库操作异常时抛出
	 * @author DoubleCome
	 * @since 2.0
	 */
	public boolean deleteAsGroup(Object[]... primaryKeySet) throws BusinessException;
	
	/**
	 * 无条件删除全表数据
	 * 
	 * @return 
	 * @throws BusinessException 数据库异常
	 * @author DoubleCome
	 * @since 2.0
	 */
	public boolean deleteAll() throws BusinessException;
	
	/**
	 * 修改一条数据，条件为model内与数据表对应的primaryKey字段
	 * 
	 * @param model 需要修改的对象
	 * @return 修改结果
	 * @throws BusinessException 当参数非法、数据库操作异常时抛出
	 * @author DoubleCome
	 * @since 1.0
	 */
	public boolean update(T model) throws BusinessException;
	
	/**
	 * 修改一条数据
	 * 
	 * @param model 需要修改的对象
	 * @param conditationMap 条件
	 * @return 修改结果
	 * @throws BusinessException 当参数非法、数据库操作异常时抛出
	 * @author DoubleCome
	 * @since 2.0
	 */
	public boolean update(T model, T conditationMap) throws BusinessException;
	
	/**
	 * 修改一条数据及需要同时修改的多个对象
	 * 
	 * @param model 需要修改的主对象
	 * @param models 需要同时修改的其它对象
	 * @return 修改结果
	 * @throws BusinessException 当参数非法、数据库操作异常时抛出
	 * @author DoubleCome
	 * @since 2.0
	 */
	public boolean update(T model, T... models) throws BusinessException;
	
	/**
	 * 修改一条数据及需要同时修改的多个对象
	 * 
	 * @param model 需要修改的主对象
	 * @param models 需要同时修改的其它对象
	 * @return 修改结果
	 * @throws BusinessException 当参数非法、数据库操作异常时抛出
	 * @author DoubleCome
	 * @since 2.0
	 */
	public boolean update(T model, List<T>... models) throws BusinessException;
	
	/**
	 * 修改多条数据
	 * 
	 * @param modelList 条件为model内与数据表对应的primaryKey字段
	 * @return 修改结果
	 * @throws BusinessException 当参数非法、数据库操作异常时抛出
	 * @author DoubleCome
	 * @since 1.0
	 */
	public boolean updateAsGroup(List<T> modelList) throws BusinessException;
	
	/**
	 * 修改多条数据
	 * 
	 * @param modelList 值为modelList的数据值，条件为conditationMap
	 * @param conditationMap 修改条件
	 * @return 修改结果
	 * @throws BusinessException 当参数非法、数据库操作异常时抛出
	 * @author DoubleCome
	 * @since 2.0
	 */
	public boolean updateAsGroup(List<T> modelList, T conditationMap) throws BusinessException;
	
	/**
	 * 修改多条数据
	 * 
	 * @param modelList 需要修改的主数据
	 * @param models 需要同时修改的其它数据
	 * @return 修改结果，任一结果修改失败，事务回滚，返回false
	 * @throws BusinessException 当参数非法，数据库操作异常时抛出
	 * @author DoubleCome
	 * @since 2.0
	 */
	public boolean updateAsGroup(List<T> modelList, T... models) throws BusinessException;
	
	/**
	 * 修改多条数据
	 * 
	 * @param modelList 需要修改的主数据
	 * @param models 需要同时修改的其它数据
	 * @return 修改结果，任一结果修改失败，事务回滚，返回false
	 * @throws BusinessException 当参数非法，数据库操作异常时抛出
	 * @author DoubleCome
	 * @since 2.0
	 */
	public boolean updateAsGroup(List<T> modelList, List<T>... modelLists) throws BusinessException;
	
	/**
	 * 查询一条数据
	 * 
	 * @param primaryKey 主键条件
	 * @return 单个model
	 * @throws BusinessException 当参数非法、数据库查询异常时抛出
	 * @author DoubleCome
	 * @since 2.0
	 */
	public T findById(Object... primaryKey) throws BusinessException;
	
	/**
	 * 查询一条数据
	 * 
	 * @param condition 条件
	 * @return 单个model
	 * @throws BusinessException 当参数非法、数据库查询异常时抛出
	 * @author DoubleCome
	 * @since 2.0
	 */
	public T find(T condition) throws BusinessException;
	
	/**
	 * 查询primaryKey数据对应的数据列
	 * 
	 * @param idSet 主键，当主键长度为1时有效
	 * @return 多条数据对象
	 * @throws BusinessException 当主键长度不为1时抛出，数据查询、参数非法时抛出
	 * @author DoubleCome
	 * @since 1.0
	 */
	public List<T> findByIdAsGroup(Long[] idSet) throws BusinessException;
	
	/**
	 * 用于在多主键情况的下的多组主键值的查询，横向排列，即每个id数组的相对应位置的值为希望查询的值的条件
	 * 
	 * @param idSets 多组主键值
	 * @return 多条数据对象
	 * @throws BusinessException 当参数非法，数据库操作失败时抛出
	 * @author DoubleCome
	 * @since 2.0
	 */
	public List<T> findByIdsAsGroup(Long[]... idSets) throws BusinessException;
	
	/**
	 * 查询所有列
	 * 
	 * @return 所有列
	 * @throws BusinessException 当数据查询异常时抛出
	 * @author Administrator
	 * @since 1.0
	 */
	public List<T> findList() throws BusinessException;
	
	/**
	 * 根据condition属性值查询数据列
	 * 
	 * @param condition 条件对象
	 * @return 特定条件下的数据列
	 * @throws BusinessException 当数据查询异常时抛出
	 * @author DoubleCome
	 * @since 2.0
	 */
	public List<T> findList(T condition) throws BusinessException;
	
	/**
	 * 查询分页信息
	 * 
	 * @param pageNumber 页码
	 * @param pageSize 每页条数
	 * @return 含com.jfinal.plugin.activerecord.Page分页信息的数据列
	 * @throws BusinessException 当数据查询异常，参数非法时抛出
	 * @author DoubleCome
	 * @since 1.0
	 */
	public Page<T> findListInPage(int pageNumber, int pageSize) throws BusinessException;
	
	/**
	 * 根据条件查询数据列，含分页信息
	 * 
	 * @param pageNumber 页码
	 * @param pageSize 每页条数
	 * @param condition 条件
	 * @return 含com.jfinal.plugin.activerecord.Page分页信息的数据列
	 * @throws BusinessException 当数据查询异常，参数非法时抛出
	 * @author DoubleCome
	 * @since 1.0
	 */
	public Page<T> findListInPageWithKeywords(int pageNumber, int pageSize, T condition) throws BusinessException;
	
	/**
	 * 查询数据列，含分页信息和排序条件
	 * 
	 * @param pageNumber 页码
	 * @param pageSize 每页条数
	 * @param sortBy 排序列名
	 * @param sortOrder 排序规则，含升序(asc)和降序(desc)
	 * @return  含分页信息的数据列
	 * @throws BusinessException 当数据查询异常，参数非法时抛出
	 * @author DoubleCome
	 * @since 1.0
	 */
	public Page<T> findListInPage(int pageNumber, int pageSize, String sortBy, String sortOrder) throws BusinessException;
	
	/**
	 * 根据条件查询数据列，含分页信息和排序条件
	 * 
	 * @param pageNumber 页码
	 * @param pageSize 每页条数
	 * @param sortBy 排序列名
	 * @param sortOrder 排序规则，含升序(asc)和降序(desc)
	 * @param condition 查询条件
	 * @return 含分页信息的数据列
	 * @throws BusinessException 当数据查询异常，参数非法时抛出
	 * @author DoubleCome
	 * @since 1.0
	 */
	public Page<T> findListInPageWithKeywords(int pageNumber, int pageSize, T condition, String sortBy, String sortOrder) throws BusinessException;
	
	/**
	 * 执行文件下定义好的sql语句
	 * 
	 * @param key 文件路径
	 * @param data 参数
	 * @return 执行结果
	 * @throws BusinessException 当数据库执行异常时抛出
	 * @author DoubleCome
	 * @since 2.0
	 */
	public int execute(String key, Map<?, ?> data) throws BusinessException;
	
	/**
	 * 执行文件下定义好的sql语句
	 * 
	 * @param key 文件路径
	 * @param data 参数
	 * @return 执行结果
	 * @throws BusinessException 当数据库执行异常时抛出
	 * @author DoubleCome
	 * @since 2.0
	 */
	public T find(String key, Map<?, ?> data) throws BusinessException;
	
	/**
	 * 执行文件下定义好的sql语句
	 * 
	 * @param key 文件路径
	 * @param data 参数
	 * @return 执行结果
	 * @throws BusinessException 当数据库执行异常时抛出
	 * @author DoubleCome
	 * @since 2.0
	 */
	public List<T> findList(String key, Map<?, ?> data) throws BusinessException;
	
}

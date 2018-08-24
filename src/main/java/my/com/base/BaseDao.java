package my.com.base;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import my.com.base.impl.MyBatisTemplate;

/**
 * 继承此类注意：泛型为Serializable接口实现Bean,bean中使用@Id声明主键(不支持联合主键，只有一个属性是主键)，
 * 使用@NotColumn声明不持久化的属性 子类class上使用@RegisterDto注册要持久化的Bean 继承的子类不得覆盖此类方法
 *
 * @param <T>
 */

public interface BaseDao<T> {
	/**
	 * 插入数据
	 * 
	 * @param obj
	 */
	@InsertProvider(type = MyBatisTemplate.class, method = "insert")
	@Options(useGeneratedKeys = false)
	void insert(T obj);

	@InsertProvider(type = MyBatisTemplate.class, method = "insertId")
	@Options(useGeneratedKeys = true,keyProperty="id",keyColumn="id")
	void insertId(T obj);
	/**
	 * 批量插入
	 */
	@InsertProvider(type = MyBatisTemplate.class, method = "insertBatch")
	@Options(useGeneratedKeys = false)
	void insertBatch(List<T> objList);

	/**
	 * 根据主键更新数据中不为空的属
	 * 
	 * @param obj
	 * @return
	 */
	@UpdateProvider(type = MyBatisTemplate.class, method = "updateNotNullById")
	int updateNotNullById(T obj);

	/**
	 * 根据主键更新数据的全部属
	 * 
	 * @param obj
	 * @return
	 */
	@UpdateProvider(type = MyBatisTemplate.class, method = "updateById")
	int updateById(T obj);

	/**
	 * 根据from条件,更新为to条件
	 * 
	 * @param obj
	 * @return
	 */
	@UpdateProvider(type = MyBatisTemplate.class, method = "updateToFrom")
	int updateToFrom(@Param("to") T to, @Param("from") T from);

	/**
	 * 根据主键删除数据
	 * 
	 * @param id
	 * @return
	 */
	@DeleteProvider(type = MyBatisTemplate.class, method = "deleteById")
	int deleteById(String id);

	/**
	 * 根据对象中不为空的条件删除数据
	 * 
	 * @param id
	 * @return
	 */
	@DeleteProvider(type = MyBatisTemplate.class, method = "deleteByObject")
	int deleteByObject(T obj);

	/**
	 * 根据参数中不为空的条件删除数据,key对应dto中的属性
	 * 
	 * @param id
	 * @return
	 */
	@DeleteProvider(type = MyBatisTemplate.class, method = "deleteByParamNotEmpty")
	int deleteByParamNotEmpty(Map<String, Object> param);

	/**
	 * 根据参数中条件删除数据,key对应dto中的属性
	 * 
	 * @param id
	 * @return
	 */
	@DeleteProvider(type = MyBatisTemplate.class, method = "deleteByParam")
	int deleteByParam(Map<String, Object> param);

	/**
	 * 根据主键查询数据
	 * 
	 * @param id
	 * @return
	 */
	@SelectProvider(type = MyBatisTemplate.class, method = "queryById")
	T queryById(String id);

	/**
	 * 根据对象中不为空的属性查询列表
	 * 
	 * @param id
	 * @return
	 */
	@SelectProvider(type = MyBatisTemplate.class, method = "queryByObject")
	List<T> queryByObject(T obj);

	/**
	 * 根据参数中不为空的属性查询列表，key对应dto中的属性
	 * 
	 * @param id
	 * @return
	 */
	@SelectProvider(type = MyBatisTemplate.class, method = "queryByParamNotEmpty")
	List<T> queryByParamNotEmpty(Map<String, Object> params);

	/**
	 * 根据参数中不为空的属性查询列表，key对应dto中的属性-用IN过滤
	 * 
	 * @param params
	 * @return
	 */
	@SelectProvider(type = MyBatisTemplate.class, method = "queryByParamNotEmptyIN")
	List<T> queryByParamNotEmptyIN(Map<String, Object> params);

	/**
	 * 根据参数查询列表，key对应dto中的属性
	 * 
	 * @param id
	 * @return
	 */
	@SelectProvider(type = MyBatisTemplate.class, method = "queryByParam")
	List<T> queryByParam(Map<String, Object> params);

	/**
	 * 根据对象中不为空的属性查询总数
	 * 
	 * @param id
	 * @return
	 */
	@SelectProvider(type = MyBatisTemplate.class, method = "queryByObjectCount")
	Integer queryByObjectCount(T obj);

	/**
	 * 根据参数中不为空的属性查询总数，key对应dto中的属性
	 * 
	 * @param id
	 * @return
	 */
	@SelectProvider(type = MyBatisTemplate.class, method = "queryByParamNotEmptyCount")
	Integer queryByParamNotEmptyCount(Map<String, Object> params);

	/**
	 * 根据参数查询总数，key对应dto中的属性
	 * 
	 * @param id
	 * @return
	 */
	@SelectProvider(type = MyBatisTemplate.class, method = "queryByParamCount")
	Integer queryByParamCount(Map<String, Object> params);

	/**
	 * 根据参数中不为空的属性查询总数，key对应dto中的属性
	 * 
	 * @param id
	 * @return
	 */
	@SelectProvider(type = MyBatisTemplate.class, method = "queryPageByParamNotEmpty")
	List<T> queryPageByParamNotEmpty(Map<String, Object> params);

	/**
	 * 根据参数查询总数，key对应dto中的属性
	 * 
	 * @param id
	 * @return
	 */
	@SelectProvider(type = MyBatisTemplate.class, method = "queryPageByParam")
	List<T> queryPageByParam(Map<String, Object> params);

}

package my.com.base.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.lang.StringUtils;

import my.com.annotation.NotColumn;
import my.com.base.BaseDao;
import my.com.base.IBaseService;
import my.com.common.PageInfo;
import my.com.common.RemotePage;
import my.com.util.JsonUtil;
import my.com.util.MYBATIS_SPECIAL_STRING;

public abstract class BaseService<T> implements IBaseService<T> {

	@Override
	public List<T> queryByParamArray(Object ... params) {
		// TODO; move this logic to utils class
		Map<String, Object> mapParam = new HashMap<>();
		for (int keyIndex = 0; keyIndex <= params.length - 2;) {
			int valueIndex = keyIndex + 1;
			if (valueIndex <= params.length - 1) {
				mapParam.put(params[keyIndex].toString(), params[valueIndex]);
			}
			keyIndex = valueIndex + 1;
		};
		return queryByParam(mapParam);
	}

	protected Log	loggerBase	= LogFactory.getLog(getClass());

	public abstract BaseDao<T> getDao();

	@Override
	public T insert(T obj) {
		getDao().insert(obj);
		return obj;
	}

	@Override
	public void insertBatch(List<T> objList) {
		getDao().insertBatch(objList);
	}

	@Override
	public int updateNotNullById(T obj) {
		return getDao().updateNotNullById(obj);
	}

	@Override
	public int updateById(T obj) {
		return getDao().updateById(obj);
	}

	/**
	 * to 会生成set后面的sql from会生成where后面的sql
	 */
	@Override
	public int updateToFrom(T to, T from) {
		return getDao().updateToFrom(to, from);
	}

	@Override
	public int deleteById(Object id) {
		return getDao().deleteById(id);
	}

	@Override
	public int deleteByObject(T obj) {
		return getDao().deleteByObject(obj);
	}

	@Override
	public int deleteByParamNotEmpty(Map<String, Object> param) {
		return getDao().deleteByParamNotEmpty(param);
	}

	@Override
	public int deleteByParam(Map<String, Object> param) {
		return getDao().deleteByParam(param);
	}

	@Override
	public T queryById(Object id) {
		return getDao().queryById(id);
	}

	@Override
	public List<T> queryByObject(T obj) {
		return getDao().queryByObject(obj);
	}

	@Override
	public List<T> queryByParamNotEmpty(Map<String, Object> params) {
		return getDao().queryByParamNotEmpty(params);
	}

	@Override
	public List<T> queryByParamNotEmptyIN(Map<String, Object> params) {
		return getDao().queryByParamNotEmptyIN(params);
	}

	@Override
	public List<T> queryByParam(Map<String, Object> params) {
		return getDao().queryByParam(params);
	}

	@Override
	public Integer queryByObjectCount(T obj) {
		return getDao().queryByObjectCount(obj);
	}

	@Override
	public Integer queryByParamNotEmptyCount(Map<String, Object> params) {
		return getDao().queryByParamNotEmptyCount(params);
	}

	@Override
	public Integer queryByParamCount(Map<String, Object> params) {
		return getDao().queryByParamCount(params);
	}

	/**
	 * 这个已经实现,表一定要有id
	 */
	@Override
	public RemotePage<T> queryPageByObject(T obj, PageInfo info) {
		Map<String, Object> params = getValuesByObject(obj, info);
		return queryPageByParamNotEmpty(params, info);
	}

	@Override
	public RemotePage<T> queryPageByParamNotEmpty(Map<String, Object> params, PageInfo info) {
		Integer totalCount = queryByParamNotEmptyCount(params);
		info.setRecordCount(totalCount);
		setLimit(params, info);
		return new RemotePage<T>(getDao().queryPageByParamNotEmpty(params), info);
	}

	@Override
	public RemotePage<T> queryPageByParam(Map<String, Object> params, PageInfo info) {
		info.setRecordCount(queryByParamCount(params));
		setLimit(params, info);
		return new RemotePage<T>(getDao().queryPageByParam(params), info);
	}

	public void setLimit(final Map<String, Object> params, final PageInfo info) {
		Integer begin = info.getStartRow();
		params.put(MYBATIS_SPECIAL_STRING.LIMIT.name(), begin + "," + info.getPageSize());
	}

	protected Map<String, Object> getValuesByObject(T obj, PageInfo info) {
		Field[] fields = obj.getClass().getDeclaredFields();
		Map<String, Object> map = new HashMap<String, Object>();
		for (Field f : fields) {
			Object notColumn = f.getAnnotation(NotColumn.class);
			if (Modifier.isStatic(f.getModifiers()) || Modifier.isFinal(f.getModifiers()) || notColumn != null)
				continue;
			Object value = null;
			try {
				f.setAccessible(true);
				value = f.get(obj);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			if (value != null && (!(value instanceof String) || (value instanceof String && StringUtils.isNotBlank((String) value))))
				map.put(f.getName(), value);
		}

		map.put(MYBATIS_SPECIAL_STRING.ORDER_BY.name(), info.getOrderByFiled());

		return map;
	}

	@Override
	public T queryUniqueByObject(T obj) {
		List<T> list = queryByObject(obj);
		if (list == null || list.isEmpty()) {
			return null;
		} else if (list.size() > 1) {
			StringBuffer sb = new StringBuffer();
			sb.append("调用类：").append(this.getClass().getName()).append("\n");
			sb.append("参数类型：").append(obj.getClass().getName()).append("\n");
			sb.append("参数：").append(JsonUtil.java2json(obj)).append("\n\n");

			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			new Exception().printStackTrace(pw);
			pw.flush();
			pw.close();
			String stack = sw.toString();
			loggerBase.error(list.size() - 1 + " 条查询数据被丢弃,这可能不是你的本意.\n\n "//
					+ sb//
					+ stack);
		}
		return list.get(0);
	}

	@Override
	public T queryUniqueByParams(Map<String, Object> params) {
		List<T> list = queryByParam(params);
		if (list == null || list.isEmpty()) {
			return null;
		} else if (list.size() > 1) {
			StringBuffer sb = new StringBuffer();
			sb.append("调用类：").append(this.getClass().getName()).append("\n");
			
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			new Exception().printStackTrace(pw);
			pw.flush();
			pw.close();
			String stack = sw.toString();
			loggerBase.error(list.size() - 1 + " 条查询数据被丢弃,这可能不是你的本意\n\n,"+sb+"查询参数: " + params + "\n\n" + stack);
		}
		return list.get(0);
	}

	protected Map<String, Object> getValuesByParamObject(Object obj) {
		Field[] fields = obj.getClass().getDeclaredFields();
		Map<String, Object> map = new HashMap<String, Object>();
		for (Field f : fields) {
			Object notColumn = f.getAnnotation(NotColumn.class);
			if (Modifier.isStatic(f.getModifiers()) || Modifier.isFinal(f.getModifiers()) || notColumn != null)
				continue;
			Object value = null;
			try {
				f.setAccessible(true);
				value = f.get(obj);
				if (value != null && value instanceof String && StringUtils.isBlank(value.toString())) {
					value = null;
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			if (value != null)
				map.put(f.getName(), value);
		}
		return map;
	}
	
	/**
	 * 复制类（给相同字段赋值）
	 * 
	 * @param obj
	 * @param objDto
	 * @return
	 */
	public Object clone(Object obj, Object objDto) {
		Class<?> clazz = obj.getClass();
		Class<?> dtoClazz = objDto.getClass();
		Field[] fields = clazz.getDeclaredFields();

		Method[] methods = clazz.getDeclaredMethods();
		Method[] methodDtos = dtoClazz.getDeclaredMethods();

		for (Field f : fields) {
			invoke1(methods, methodDtos, f.getName(), obj, objDto);
		}
		return objDto;
	}
	
	private void invoke1(Method[] methods, Method[] methodDtos, String name, Object obj, Object objDto) {
		String upperName = Character.toUpperCase(name.charAt(0)) + name.substring(1);
		String setterName = "set" + upperName;
		String getterName = "get" + upperName;
		Method method = null;
		Method methodDto = null;
		method = getMethodByName(methods, getterName);
		methodDto = getMethodByName(methodDtos, setterName);
		if (method != null && methodDto != null) {
			try {
				methodDto.invoke(objDto, method.invoke(obj, null));
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private Method getMethodByName(Method[] methods, String methodName) {
		for (Method m : methods) {
			if (m.getName().equals(methodName)) {
				return m;
			}
		}
		return null;
	}
}

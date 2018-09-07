package my.com.base.impl;

import static my.com.Util.DtoUtil.SPACE_TABLE_NAME;
import static my.com.Util.DtoUtil.caculationColumnList;
import static my.com.Util.DtoUtil.id;
import static my.com.Util.DtoUtil.queryColumn;
import static my.com.Util.DtoUtil.returnInsertColumnsDefine;
import static my.com.Util.DtoUtil.returnInsertColumnsName;
import static my.com.Util.DtoUtil.returnInsertColumnsValue;
import static my.com.Util.DtoUtil.returnUpdateSet;
import static my.com.Util.DtoUtil.returnUpdateSetFull;
import static my.com.Util.DtoUtil.returnUpdateSetToFrom;
import static my.com.Util.DtoUtil.tableName;
import static my.com.Util.DtoUtil.whereColumn;
import static my.com.Util.DtoUtil.whereColumnNotEmpty;
import static my.com.Util.DtoUtil.whereColumnNotEmptyIN;
import static my.com.Util.DtoUtil.whereColumnNotNull;
import static my.com.Util.DtoUtil.whereColumnNotNullToFrom;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.jdbc.SQL;

import my.com.Util.MYBATIS_SPECIAL_STRING;

public class SqlFactory<T extends Serializable> {

	/**
	 * 插入
	 * 
	 * @param Object
	 * @return
	 */
	public String insert(T obj) {
		SQL sql = new SQL();
		sql.INSERT_INTO(tableName(obj));
		caculationColumnList(obj);
		sql.VALUES(returnInsertColumnsName(obj), returnInsertColumnsDefine(obj));
		return sql.toString();
	}

	/**
	 * 批量插入
	 * 
	 * @param map
	 * @return
	 */
	public String insertBatch(Map<String, List<T>> map) {
		List<T> objList = map.get("list");
		SQL sql = new SQL();
		sql.INSERT_INTO(tableName(objList.get(0)));
		caculationColumnList(objList.get(0));
		Map<String, String> columnAndValue = new HashMap<String, String>();
		columnAndValue = returnInsertColumnsValue(objList);
		sql.VALUES(columnAndValue.get("columnName"), columnAndValue.get("columnValue"));
		return sql.toString();
	}

	/**
	 * 根据Id更新全表
	 * 
	 * @param Object
	 * @return
	 */
	public String updateById(T obj) {
		String idname = id(obj);
		SQL sql = new SQL();
		sql.UPDATE(tableName(obj));
		caculationColumnList(obj);
		sql.SET(returnUpdateSetFull(obj));
		sql.WHERE(idname.replaceAll("([A-Z])", "_$1").toLowerCase() + "=#{" + idname + "}");
		return sql.toString();
	}

	/**
	 * 根据Id更新对象中非null字段
	 * 
	 * @param Object
	 * @return
	 */
	public String updateNotNullById(T obj) {
		String idname = id(obj);
		SQL sql = new SQL();
		sql.UPDATE(tableName(obj));
		caculationColumnList(obj);
		sql.SET(returnUpdateSet(obj));
		sql.WHERE(idname.replaceAll("([A-Z])", "_$1").toLowerCase() + "=#{" + idname + "}");
		return sql.toString();
	}

	/**
	 * 根据from对象更新to对象
	 * 
	 * @param Object
	 * @return
	 */
	public String updateToFrom(Map<String, Serializable> para) {
		Serializable to = para.get("to");
		Serializable from = para.get("from");
		SQL sql = new SQL();
		sql.UPDATE(tableName(to));
		caculationColumnList(to);
		sql.SET(returnUpdateSetToFrom(to));
		sql.WHERE(whereColumnNotNullToFrom(from));
		return sql.toString();
	}

	/**
	 * 根据ID删除
	 * 
	 * @param Object
	 * @return
	 */
	public String deleteById(T obj) {
		String idname = id(obj);
		SQL sql = new SQL();
		sql.DELETE_FROM(tableName(obj));
		sql.WHERE(idname.replaceAll("([A-Z])", "_$1").toLowerCase() + "=#{" + idname + "}");
		return sql.toString();
	}

	/**
	 * 根据Object对象条件删除
	 * 
	 * @param Object
	 * @return
	 */
	public String deleteByObject(T obj) {
		caculationColumnList(obj);
		SQL sql = new SQL();
		sql.DELETE_FROM(tableName(obj));
		sql.WHERE(whereColumnNotNull(obj));
		return sql.toString();
	}

	/**
	 * 根据Map非空条件删除
	 * 
	 * @param Object
	 * @return
	 */
	public String deleteByParamNotEmpty(Map<String, Object> param) {
		removeEmpty(param);
		SQL sql = new SQL();
		Serializable obj = (Serializable) param.get(SPACE_TABLE_NAME);
		sql.DELETE_FROM(tableName(obj));
		param.remove(SPACE_TABLE_NAME);
		sql.WHERE(whereColumnNotEmpty(param));
		return sql.toString();
	}

	/**
	 * 根据Map条件删除
	 * 
	 * @param Object
	 * @return
	 */
	public String deleteByParam(Map<String, Object> param) {
		SQL sql = new SQL();
		Serializable obj = (Serializable) param.get(SPACE_TABLE_NAME);
		sql.DELETE_FROM(tableName(obj));
		param.remove(SPACE_TABLE_NAME);
		sql.WHERE(whereColumn(param));
		return sql.toString();
	}

	public String queryById(T obj) {
		String idname = id(obj);
		caculationColumnList(obj);
		SQL sql = new SQL();
		sql.SELECT(queryColumn(obj));
		sql.FROM(tableName(obj) + " " + obj.getClass().getSimpleName());
		sql.WHERE(idname.replaceAll("([A-Z])", "_$1").toLowerCase() + "=#{" + idname + "}");
		return sql.toString();
	}

	public String queryByObject(T obj) {
		caculationColumnList(obj);
		SQL sql = new SQL();
		sql.SELECT(queryColumn(obj));
		sql.FROM(tableName(obj) + " " + obj.getClass().getSimpleName());
		if (!"".equals(whereColumnNotNull(obj))) {
			sql.WHERE(whereColumnNotNull(obj));
		}
		return sql.toString();
	}

	public String queryByParamNotEmpty(Map<String, Object> param) {
		removeEmpty(param);
		Serializable obj = (Serializable) param.get(SPACE_TABLE_NAME);
		param.remove(SPACE_TABLE_NAME);
		Object orderBy = param.get(MYBATIS_SPECIAL_STRING.ORDER_BY.name());
		param.remove(MYBATIS_SPECIAL_STRING.ORDER_BY.name());
		caculationColumnList(obj);
		SQL sql = new SQL();
		sql.SELECT(queryColumn(obj));
		sql.FROM(tableName(obj) + " " + obj.getClass().getSimpleName());
		if (!param.isEmpty())
			sql.WHERE(whereColumnNotEmpty(param));
		if (null != orderBy)
			sql.ORDER_BY(orderBy.toString());
		return sql.toString();
	}

	public String queryByParamNotEmptyIN(Map<String, Object> param) {
		removeEmpty(param);
		Serializable obj = (Serializable) param.get(SPACE_TABLE_NAME);
		param.remove(SPACE_TABLE_NAME);
		Object orderBy = param.get(MYBATIS_SPECIAL_STRING.ORDER_BY.name());
		param.remove(MYBATIS_SPECIAL_STRING.ORDER_BY.name());
		caculationColumnList(obj);
		SQL sql = new SQL();
		sql.SELECT(queryColumn(obj));
		sql.FROM(tableName(obj) + " " + obj.getClass().getSimpleName());
		if (!param.isEmpty())
			sql.WHERE(whereColumnNotEmptyIN(param));
		if (null != orderBy)
			sql.ORDER_BY(orderBy.toString());
		return sql.toString();
	}

	public String queryByParam(Map<String, Object> param) {
		Serializable obj = (Serializable) param.get(SPACE_TABLE_NAME);
		Object orderBy = param.get(MYBATIS_SPECIAL_STRING.ORDER_BY.name());
		param.remove(SPACE_TABLE_NAME);
		param.remove(MYBATIS_SPECIAL_STRING.ORDER_BY.name());
		caculationColumnList(obj);
		SQL sql = new SQL();
		sql.SELECT(queryColumn(obj));
		sql.FROM(tableName(obj) + " " + obj.getClass().getSimpleName());
		if (!param.isEmpty())
			sql.WHERE(whereColumn(param));
		if (null != orderBy)
			sql.ORDER_BY(orderBy.toString());
		return sql.toString();
	}

	public String queryByObjectCount(T obj) {
		caculationColumnList(obj);
		SQL sql = new SQL();
		sql.SELECT(" count(*) total ");
		sql.FROM(tableName(obj) + " " + obj.getClass().getSimpleName());
		sql.WHERE(whereColumnNotNull(obj));
		return sql.toString();
	}

	public String queryByParamNotEmptyCount(Map<String, Object> param) {
		removeEmpty(param);
		Serializable obj = (Serializable) param.remove(SPACE_TABLE_NAME);
		caculationColumnList(obj);
		SQL sql = new SQL();
		sql.SELECT(" count(*) total ");
		sql.FROM(tableName(obj) + " " + obj.getClass().getSimpleName());
		Object orderBy = param.remove(MYBATIS_SPECIAL_STRING.ORDER_BY.name());
		if (!param.isEmpty())
			sql.WHERE(whereColumn(param));
		param.put(MYBATIS_SPECIAL_STRING.ORDER_BY.name(), orderBy);
		return sql.toString();
	}

	public String queryByParamCount(Map<String, Object> param) {
		Serializable obj = (Serializable) param.get(SPACE_TABLE_NAME);
		param.remove(SPACE_TABLE_NAME);
		caculationColumnList(obj);
		SQL sql = new SQL();
		sql.SELECT(" count(*) total ");
		sql.FROM(tableName(obj) + " " + obj.getClass().getSimpleName());
		Object orderBy = param.remove(MYBATIS_SPECIAL_STRING.ORDER_BY.name());
		if (!param.isEmpty())
			sql.WHERE(whereColumn(param));
		param.put(MYBATIS_SPECIAL_STRING.ORDER_BY.name(), orderBy);
		return sql.toString();
	}

	public String queryPageByParamNotEmpty(Map<String, Object> param) {
		removeEmpty(param);
		Serializable obj = (Serializable) param.remove(SPACE_TABLE_NAME);
		String limit = addlimit(param);
		Object orderBy = param.remove(MYBATIS_SPECIAL_STRING.ORDER_BY.name());
		caculationColumnList(obj);
		SQL sql = new SQL();
		sql.SELECT(queryColumn(obj));
		sql.FROM(tableName(obj) + " " + obj.getClass().getSimpleName());
		if (!param.isEmpty())
			sql.WHERE(whereColumnNotEmpty(param));
		if (orderBy != null) {
			sql.ORDER_BY(orderBy.toString());
		}
		return sql.toString() + limit;
	}

	public String queryPageByParam(Map<String, Object> param) {
		Serializable obj = (Serializable) param.remove(SPACE_TABLE_NAME);
		String limit = addlimit(param);
		Object orderBy = param.remove(MYBATIS_SPECIAL_STRING.ORDER_BY.name());
		caculationColumnList(obj);
		SQL sql = new SQL();
		sql.SELECT(queryColumn(obj));
		sql.FROM(tableName(obj) + " " + obj.getClass().getSimpleName());
		if (!param.isEmpty())
			sql.WHERE(whereColumn(param));
		if (orderBy != null) {
			sql.ORDER_BY(orderBy.toString());
		}
		return sql.toString() + limit;
	}

	public String addlimit(Map<String, Object> param) {
		String subsql = " " + MYBATIS_SPECIAL_STRING.LIMIT.name() + " ";
		Object obj = param.remove(MYBATIS_SPECIAL_STRING.LIMIT.name());
		if (null == obj) {
			subsql = subsql + "0,10";
		} else {
			subsql = subsql + obj;
		}

		return subsql;
	}

	private void removeEmpty(Map<String, Object> params) {
		Iterator<String> iterator = params.keySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			if (key == null)
				params.remove(key);
			if (params.get(key) == null) {
				params.remove(key);
				iterator = params.keySet().iterator();
			}
		}
	}
}

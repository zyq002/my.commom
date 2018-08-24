package my.com.Util;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import my.com.annotation.Id;
import my.com.annotation.NotColumn;	

public final class DtoUtil {
	private final static Map<String, Class<?>> FIELD_TYPE_MAP = new HashMap<String, Class<?>>();

	public final static void setBaseType() {
		if (!FIELD_TYPE_MAP.isEmpty()) {
			return;
		}
		FIELD_TYPE_MAP.put("Double", Double.class);
		FIELD_TYPE_MAP.put("Short", Short.class);
		FIELD_TYPE_MAP.put("Long", Long.class);
		FIELD_TYPE_MAP.put("Float", Float.class);
		FIELD_TYPE_MAP.put("Integer", Integer.class);
		FIELD_TYPE_MAP.put("Byte", Byte.class);
		FIELD_TYPE_MAP.put("String", String.class);
		FIELD_TYPE_MAP.put("Character", Character.class);
		FIELD_TYPE_MAP.put("sDate", Date.class);
		FIELD_TYPE_MAP.put("Boolean", Boolean.class);
		FIELD_TYPE_MAP.put("Date", java.util.Date.class);
		FIELD_TYPE_MAP.put("BigDecimal", BigDecimal.class);
	}

	public static final String SPACE_TABLE_NAME = "SPACE_TABLE_NAME";

	/**
	 * 用于存放POJO的列信息
	 */
	private static Map<Class<? extends Serializable>, Map<String, String>> columnMap = new HashMap<Class<? extends Serializable>, Map<String, String>>();

	/**
	 * 获取POJO对应的表名 需要POJO中的属性定义@Table(name)
	 * 
	 * @return
	 */
	public static String tableName(Serializable obj) {
		String objClassName = obj.getClass().getSimpleName();
		return objClassName.replaceAll("([A-Z])", "_$1").replaceFirst("_", "").toLowerCase();
	}

	/**
	 * 获取POJO中的主键字段名 需要定义@Id
	 * 
	 * @return
	 */
	public static String id(Serializable obj) {
		for (Field field : obj.getClass().getDeclaredFields()) {
			if (null != field.getAnnotation(Id.class))
				return field.getName();
		}
		if (obj.getClass().equals(Long.class) || obj.getClass().equals(long.class) || obj.getClass().equals(int.class)
				|| obj.getClass().equals(Integer.class)) {
			return "id";
		}
		return null;
	}

	private static boolean isNull(Serializable obj, String fieldname) {
		try {
			Field field = obj.getClass().getDeclaredField(fieldname);
			return isNull(obj, field);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

		return false;
	}

	private static boolean isNull(Serializable obj, Field field) {
		try {
			field.setAccessible(true);
			return field.get(obj) == null;
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * 用于计算类定义 需要POJO中的属性定义@Column(name)
	 */
	public static void caculationColumnList(Serializable obj) {
		setBaseType();
		Class<? extends Serializable> className = obj.getClass();
		if (columnMap.containsKey(className))
			return;

		Field[] fields = className.getDeclaredFields();
		Map<String, String> fieldMap = new HashMap<String, String>();
		for (Field field : fields) {
			NotColumn notColumn = field.getAnnotation(NotColumn.class);
			boolean isStatic = Modifier.isStatic(field.getModifiers());
			boolean isFinal = Modifier.isFinal(field.getModifiers());
			boolean isPrimitive = field.getType().isPrimitive() || FIELD_TYPE_MAP.containsValue(field.getType());
			if (null != notColumn || isStatic || isFinal || !isPrimitive) {
				continue;
			}
			String fieldName = field.getName();
			 String column = fieldName.replaceAll("([A-Z])",
			 "_$1").toLowerCase();
			 fieldMap.put(fieldName, column);
			//fieldMap.put(fieldName, fieldName);
		}

		columnMap.put(className, fieldMap);
		Class<?> parentClass = className.getSuperclass();
		if (null != parentClass && !parentClass.getSimpleName().equals("Serializable")) {
			fields = className.getSuperclass().getDeclaredFields();
			for (Field field : fields) {
				NotColumn notColumn = field.getAnnotation(NotColumn.class);
				boolean isStatic = Modifier.isStatic(field.getModifiers());
				boolean isFinal = Modifier.isFinal(field.getModifiers());
				boolean isPrimitive = field.getType().isPrimitive() || FIELD_TYPE_MAP.containsValue(field.getType());
				if (null != notColumn || isStatic || isFinal || !isPrimitive) {
					continue;
				}
				String fieldName = field.getName();
				// String column = fieldName.replaceAll("([A-Z])",
				// "_$1").toLowerCase();
				// fieldMap.put(fieldName, column);
				fieldMap.put(fieldName, fieldName);
			}
		}
	}

	/**
	 * Where条件信息
	 * 
	 * 
	 */
	public class WhereColumn {
		public String name;

		public boolean isString;

		public WhereColumn(String name, boolean isString) {
			this.name = name;
			this.isString = isString;
		}
	}

	/**
	 * 用于获取Insert的字段累加
	 * 
	 * @return
	 */
	public static String returnInsertColumnsName(Serializable obj) {
		StringBuilder sb = new StringBuilder();

		Map<String, String> fieldMap = columnMap.get(obj.getClass());
		Iterator<String> iterator = fieldMap.keySet().iterator();
		int i = 0;
		while (iterator.hasNext()) {
			String fieldname = iterator.next();
			if (isNull(obj, fieldname) && !fieldname.contains("createTime") && !fieldname.contains("updateTime"))
				continue;
			if (i++ != 0) {
				sb.append(',');
			}
			sb.append(fieldMap.get(fieldname));
		}
		return sb.toString();
	}

	/**
	 * 用于获取Insert的字段映射累加
	 * 
	 * @return
	 */
	public static String returnInsertColumnsDefine(Serializable obj) {
		StringBuilder sb = new StringBuilder();

		Map<String, String> fieldMap = columnMap.get(obj.getClass());
		Iterator<String> iterator = fieldMap.keySet().iterator();
		int i = 0;
		while (iterator.hasNext()) {
			String fieldname = iterator.next();
			boolean isTime = fieldname.equalsIgnoreCase("createTime") || fieldname.equalsIgnoreCase("updateTime");
			if ((!isTime) && isNull(obj, fieldname))
				continue;
			if (i++ != 0) {
				sb.append(',');
			}
			if (isTime) {
				sb.append("NOW()");
			} else {
				sb.append("#{").append(fieldname).append('}');
			}
		}
		return sb.toString();
	}

	/**
	 * 以map的形式返回列名及对应的value
	 * 
	 * @param objList
	 * @return
	 */
	public static Map<String, String> returnInsertColumnsValue(List<?> objList) {
		StringBuilder columnName = new StringBuilder();// 存放列名
		StringBuilder columnValue = new StringBuilder(); // 存放value
		Map<String, String> columnAndValue = new HashMap<String, String>(); // 存放列名和对应的value
		int columnValueFlag = 0; // value从第二次开始需组装),(
		int columnNameFlag = 0; // 列名只取一次
		for (Object obj : objList) {
			Field[] fields = obj.getClass().getDeclaredFields();
			Map<String, String> fieldMap = columnMap.get(obj.getClass());
			if (columnValueFlag > 0) {
				columnValue.append("),(");
			}
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				if (field.getModifiers() > Modifier.PRIVATE) {
					continue;
				}
				if (columnNameFlag == 0) {
					if (fieldMap.get(field.getName()) != null) {
						columnName.append(fieldMap.get(field.getName()));
					}
				}
				if (fieldMap.get(field.getName()) == null) {
					continue;
				}
				field.setAccessible(true);

				try {
					if (field.getGenericType().toString().equals("class java.util.Date")) {

						if (field.get(obj) == null) {
							if (field.getName().equals("createTime") || field.getName().equals("updateTime")) {
								columnValue.append("NOW()");
							} else {
								columnValue.append(field.get(obj));
							}
						} else {
							java.util.Date date = (java.util.Date) field.get(obj);
							columnValue.append("'");
							columnValue.append(DateUtil.format(date, "yyyy-MM-dd HH:mm:ss"));
							columnValue.append("'");
						}
					} else if (field.get(obj) != null
							&& field.getGenericType().toString().equals("class java.lang.String")) {
						columnValue.append("'");
						columnValue.append(field.get(obj));
						columnValue.append("'");
					} else {
						columnValue.append(field.get(obj));
					}
					columnValue.append(",");
					if (columnNameFlag == 0) {
						columnName.append(",");
					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
			columnNameFlag++;
			columnValueFlag++;
			if (columnValue.toString().endsWith(",")) {
				columnValue.deleteCharAt(columnValue.length() - 1);
			}
			if (columnName.toString().endsWith(",")) {
				columnName.deleteCharAt(columnName.length() - 1);
			}
		}

		columnAndValue.put("columnName", columnName.toString());
		columnAndValue.put("columnValue", columnValue.toString());
		return columnAndValue;
	}

	/**
	 * 用于获取Update Set的字段累加
	 * 
	 * @return
	 */
	public static String returnUpdateSetFull(Serializable obj) {
		StringBuilder sb = new StringBuilder();

		Map<String, String> fieldMap = columnMap.get(obj.getClass());
		int i = 0;
		for (Map.Entry<String, String> column : fieldMap.entrySet()) {
			boolean isUpdateTime = column.getKey().equalsIgnoreCase("updateTime");
			boolean isCreateTime = column.getKey().equalsIgnoreCase("createTime");
			if (i++ != 0)
				sb.append(',');
			if (isUpdateTime) {
				sb.append("updateTime=NOW()");
			} else if (isCreateTime && isNull(obj, column.getKey())) {
				sb.append("createTime=NOW()");
			} else {
				sb.append(column.getValue()).append("=#{").append(column.getKey()).append('}');
			}

		}
		return sb.toString();
	}

	/**
	 * 用于获取Update Set的字段累加
	 * 
	 * @return
	 */
	public static String returnUpdateSet(Serializable obj) {
		StringBuilder sb = new StringBuilder();

		Map<String, String> fieldMap = columnMap.get(obj.getClass());
		int i = 0;
		for (Map.Entry<String, String> column : fieldMap.entrySet()) {
			String key = column.getKey();
			boolean isUpdateTime = key.equalsIgnoreCase("updateTime");
			if (isNull(obj, key) && !isUpdateTime)
				continue;

			if (i++ != 0)
				sb.append(',');
			if (isUpdateTime) {
				sb.append("updateTime=NOW()");
			} else {
				sb.append(column.getValue()).append("=#{").append(column.getKey()).append('}');
			}
		}
		return sb.toString();
	}

	public static String returnUpdateSetToFrom(Serializable obj) {
		StringBuilder sb = new StringBuilder();

		Map<String, String> fieldMap = columnMap.get(obj.getClass());
		int i = 0;
		for (Map.Entry<String, String> column : fieldMap.entrySet()) {
			String key = column.getKey();
			boolean isUpdateTime = key.equalsIgnoreCase("updateTime");
			if (isNull(obj, key) && !isUpdateTime)
				continue;

			if (i++ != 0)
				sb.append(',');
			if (isUpdateTime) {
				sb.append("updateTime=NOW()");
			} else {
				sb.append(column.getValue()).append("=#{to.").append(column.getKey()).append('}');
			}
		}
		return sb.toString();
	}

	/**
	 * 用于获取select、delete的条件组装
	 * 
	 * @return
	 */
	public static String whereColumnNotNull(Serializable obj) {
		StringBuilder sb = new StringBuilder();
		Map<String, String> fieldMap = columnMap.get(obj.getClass());
		int i = 0;
		for (Map.Entry<String, String> column : fieldMap.entrySet()) {
			if (isNull(obj, column.getKey()))
				continue;
			if (i++ != 0)
				sb.append(" AND ");
			sb.append(column.getValue()).append("=#{").append(column.getKey() + "}");
		}
		return sb.toString();
	}

	/**
	 * 用于update组装条件
	 * 
	 * @param obj
	 * @return
	 */
	public static String whereColumnNotNullToFrom(Serializable obj) {
		StringBuilder sb = new StringBuilder();
		Map<String, String> fieldMap = columnMap.get(obj.getClass());
		int i = 0;
		for (Map.Entry<String, String> column : fieldMap.entrySet()) {
			if (isNull(obj, column.getKey()))
				continue;
			if (i++ != 0)
				sb.append(" AND ");
			sb.append(column.getValue()).append("=#{from.").append(column.getKey() + "}");
		}
		return sb.toString();
	}

	/**
	 * 用于获取select、delete的条件组装
	 * 
	 * @return
	 */
	public static String whereColumn(Map<String, Object> param) {
		StringBuilder sb = new StringBuilder();
		int i = 0;
		for (Map.Entry<String, Object> column : param.entrySet()) {
			if (i++ != 0)
				sb.append(" AND ");
			if (!MYBATIS_SPECIAL_STRING.list().contains(column.getKey().toUpperCase())) {
				// sb.append(column.getKey().replaceAll("([A-Z])",
				// "_$1").toLowerCase()).append("=#{").append(column.getKey() +
				// "}");
				sb.append(column.getKey().toLowerCase()).append("=#{").append(column.getKey() + "}");
			} else if (MYBATIS_SPECIAL_STRING.LIKE.name().equalsIgnoreCase(column.getKey())) {
				sb.append(column.getValue());
			} else if (MYBATIS_SPECIAL_STRING.COLUMNS.name().equalsIgnoreCase(column.getKey())) {
				sb.append(column.getValue());
			}
		}
		return sb.toString();
	}

	/**
	 * 用于获取select、delete的条件组装
	 * 
	 * @return
	 */
	public static String whereColumnNotEmpty(Map<String, Object> param) {
		StringBuilder sb = new StringBuilder();
		int i = 0;
		for (Map.Entry<String, Object> column : param.entrySet()) {
			if (column.getValue() == null)
				continue;
			if (i++ != 0)
				sb.append(" AND ");
			if (!MYBATIS_SPECIAL_STRING.list().contains(column.getKey().toUpperCase())) {
				// sb.append(column.getKey().replaceAll("([A-Z])",
				// "_$1").toLowerCase()).append("=#{").append(column.getKey() +
				// "}");
				sb.append(column.getKey().toLowerCase()).append("=#{").append(column.getKey() + "}");
			} else if (MYBATIS_SPECIAL_STRING.LIKE.name().equalsIgnoreCase(column.getKey())) {
				sb.append(column.getValue());
			} else if (MYBATIS_SPECIAL_STRING.COLUMNS.name().equalsIgnoreCase(column.getKey())) {
				sb.append(column.getValue());
			}
		}
		return sb.toString();
	}

	/**
	 * 用于获取select、delete的条件组装
	 * 
	 * @return
	 */
	public static String whereColumnNotEmptyIN(Map<String, Object> param) {
		StringBuilder sb = new StringBuilder();
		int i = 0;
		for (Map.Entry<String, Object> column : param.entrySet()) {
			if (column.getValue() == null)
				continue;
			if (i++ != 0)
				sb.append(" AND ");
			if (!MYBATIS_SPECIAL_STRING.list().contains(column.getKey().toUpperCase())) {
				// sb.append(column.getKey().replaceAll("([A-Z])",
				// "_$1").toLowerCase()).append(" in
				// (").append(column.getValue() + ")");
				sb.append(column.getKey().toLowerCase()).append(" in (").append(column.getValue() + ")");
			} else if (MYBATIS_SPECIAL_STRING.LIKE.name().equalsIgnoreCase(column.getKey())) {
				sb.append(column.getValue());
			} else if (MYBATIS_SPECIAL_STRING.COLUMNS.name().equalsIgnoreCase(column.getKey())) {
				sb.append(column.getValue());
			}
		}
		return sb.toString();
	}

	public static String queryColumn(Serializable obj) {
		StringBuilder sb = new StringBuilder();

		Map<String, String> fieldMap = columnMap.get(obj.getClass());
		int i = 0;
		for (Map.Entry<String, String> column : fieldMap.entrySet()) {
			if (i++ != 0)
				sb.append(',');
			sb.append(column.getValue()).append(" as ").append(column.getKey());
		}

		System.out.println(sb.toString());
		return sb.toString();
	}

	/*
	 * public Integer getId(Serializable obj) { return 0; }
	 */

	/**
	 * 打印类字段信息
	 */
	public static String objString(Serializable obj) {
		Field[] fields = obj.getClass().getDeclaredFields();
		StringBuilder sb = new StringBuilder();
		sb.append('[');
		for (Field f : fields) {
			if (Modifier.isStatic(f.getModifiers()) || Modifier.isFinal(f.getModifiers()))
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
			if (value != null)
				sb.append(f.getName()).append('=').append(value).append(',');
		}
		sb.append(']');

		return sb.toString();
	}
}

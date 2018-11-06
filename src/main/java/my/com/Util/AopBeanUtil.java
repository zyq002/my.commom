package my.com.Util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import my.com.annotation.SetValue;

@Component
public class AopBeanUtil implements ApplicationContextAware {

	private ApplicationContext context;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.context = applicationContext;
	}

	private Method getClassAllMethod(Class clazz, String methodName, Class<?>... parameterTypes)
			throws SecurityException {
		Method retMethod = null;
		try {
			retMethod = clazz.getMethod(methodName, parameterTypes);
		} catch (NoSuchMethodException ex) {
			try {
				retMethod = clazz.getSuperclass().getMethod(methodName, parameterTypes);
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return retMethod;

	}
	/*
	 * private Method getClassAllMethod(Class clazz, String methodName) throws
	 * SecurityException, NoSuchMethodException { Method retMethod = null; Method[]
	 * methods = clazz.getMethods(); for (Method method : methods) { if
	 * (methodName.equals(method.getName())) { retMethod = method; } } return
	 * retMethod;
	 * 
	 * }
	 */

	public void setFieIdValueForCollection(Collection collection) {
		if (CollectionUtils.isEmpty(collection)) {
			return;
		}
		// 获取到集中元素的class对象
		Class<?> clazz = collection.iterator().next().getClass();
		// 获取里面的字段
		Field[] fields = clazz.getDeclaredFields();
		// 遍历处理带有指定注解的字段
		for (Field field : fields) {
			// 获取字段的注解如不包含SetValue着continue
			SetValue setValue = field.getAnnotation(SetValue.class);
			if (setValue == null) {
				continue;
			}
			// 设置访问权限
			field.setAccessible(true);
			// 获取需要用的bean
			Object bean = this.context.getBean(setValue.beanClass());
			try {
				// 获取要调用的方法 参数1：方法名称,2:

				/*
				 * Method method = setValue.beanClass().getMethod(setValue.method(),
				 * clazz.getDeclaredField(setValue.paramField()).getType());
				 */
				Method method = setValue.beanClass().getMethod(setValue.method(), setValue.paramtype());

				Field paramField = clazz.getDeclaredField(setValue.paramField());
				paramField.setAccessible(true);
				//
				Field targetField = null;
				for (Object obj : collection) {
					Object paramValue = paramField.get(obj);
					if (paramValue == null) {
						continue;
					}
					Object value = method.invoke(bean, paramValue);// 执行被方法
					if (value != null) {
						if (targetField == null) {
							//
							targetField = value.getClass().getDeclaredField(setValue.targetField());
							targetField.setAccessible(true);
						}
						value = targetField.get(value);
					}
					// 设置值
					field.set(obj, value);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}

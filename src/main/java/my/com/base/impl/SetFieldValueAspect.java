package my.com.base.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class SetFieldValueAspect {

	@Autowired
	private AopBeanUtil aopBeanUtil;

	@Around("@annotation(my.com.annotation.NeedSetValue)")
	public Object doSetFieldValue(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		Object ret = proceedingJoinPoint.proceed();
		if (ret instanceof Collection) {
			this.aopBeanUtil.setFieIdValueForCollection((Collection) ret);
		} else {
			List<Object> list = new ArrayList<>();
			list.add(ret);
			this.aopBeanUtil.setFieIdValueForCollection(list);
		}
		return ret;
	}

}

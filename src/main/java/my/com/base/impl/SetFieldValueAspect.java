package my.com.base.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import my.com.Util.AopBeanUtil;

@Component
@Async
public class SetFieldValueAspect {

	@Autowired
	private AopBeanUtil aopBeanUtil;

	@Around("")
	public Object doSetFieldValue(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		Object ret = proceedingJoinPoint.proceed();
		if (ret instanceof Collection) {
			this.aopBeanUtil.setFieIdValueForCollection((Collection) ret);
		} else {
			List<Object> list = new ArrayList<>();
			this.aopBeanUtil.setFieIdValueForCollection(list);
		}
		return ret;
	}

}

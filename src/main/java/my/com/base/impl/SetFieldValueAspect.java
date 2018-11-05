package my.com.base.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import my.com.Util.AopBeanUtil;

@Component
@Aspect
public class SetFieldValueAspect {

	public SetFieldValueAspect() {
		System.out.println("SetFieldValueAspect------------------------->");
	}

	@Autowired
	private AopBeanUtil aopBeanUtil;

	@Around("@annotation(my.com.annotation.NeedSetValue)")
	public Object doSetFieldValue(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		Object ret = proceedingJoinPoint.proceed();
		System.out.println("doSetFieldValue---------------------------->"+ret);
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

package com.stackroute.keepnote.aspectj;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/* Annotate this class with @Aspect and @Component */

@Aspect
@Component
public class LoggingAspect {
	/*
	 * Write loggers for each of the methods of Category controller, any particular
	 * method will have all the four aspectJ annotation
	 * (@Before, @After, @AfterReturning, @AfterThrowing).
	 */
	private static final Log log = LogFactory.getLog(LoggingAspect.class);

	@Before("execution(* com.stackroute.keepnote.controller.CategoryController.*(..))")
	public void logBefore(JoinPoint joinPoint) {
		log.info("before called.... " + joinPoint.getSignature().getName());
	}

	@After("execution (* com.stackroute.keepnote.controller.CategoryController.*(..))")
	public void logAfter(JoinPoint joinPoint) {
		log.info("after called.... " + joinPoint.getSignature().getName());
	}

	@AfterReturning("execution (* com.stackroute.keepnote.controller.CategoryController.*(..))")
	public void logAfterReturning(JoinPoint joinPoint) {
		log.info("after returning called.... " + joinPoint.getSignature().getName());
	}

	@AfterThrowing("execution (* com.stackroute.keepnote.controller.CategoryController.*(..))")
	public void logAfterThrowing(JoinPoint joinPoint) {
		log.info("after throwing called.... " + joinPoint.getSignature().getName());
	}

}

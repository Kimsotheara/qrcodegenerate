package com.qrcodegenerate.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.SecureRandom;
import java.util.Iterator;

@Slf4j
@Aspect
@Order(1) @Component
public class AopReqLogger {
	@Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping)" +
			"|| @annotation(org.springframework.web.bind.annotation.PostMapping)" + 
			"|| @annotation(org.springframework.web.bind.annotation.PutMapping)" +
			"|| @annotation(org.springframework.web.bind.annotation.DeleteMapping)" +
			"|| @annotation(org.springframework.web.bind.annotation.RequestMapping)")
	public void pointcut() {}
	@Before("pointcut()")
	public void requestLogger() {
		MDC.put("LOG_ID", getRandomStr(0,20));
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		log.info("[{}] request start", request.getRequestURI());
		logParams(request);
	}
	public static String getRandomStr(int type, int len) {
		final String STR_NUMBER = "0123456789";
		final String STR_ALPHABET_UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		final String STR_ALPHABET_LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
		String randomString = "";
		switch(type) {
			case 0:
				randomString = STR_ALPHABET_UPPERCASE + STR_ALPHABET_LOWERCASE + STR_NUMBER;
				break;
			case 1:
				randomString = STR_ALPHABET_UPPERCASE + STR_ALPHABET_LOWERCASE;
				break;
			case 2:
				randomString = STR_NUMBER;
				break;
			case 3:
				randomString = STR_ALPHABET_UPPERCASE;
				break;
			case 4:
				randomString = STR_ALPHABET_LOWERCASE;
				break;
			default:
				break;
		}
		String result = "";
		int ranStrLen = randomString.length();
		if (ranStrLen > 0 && len > 0) {
			SecureRandom ran = new SecureRandom();
			for (int i=0; i<len; i++) {
				result += randomString.charAt(ran.nextInt(ranStrLen));
			}
		}
		return result;
	}
	@Around("pointcut()")
	public Object requestProcess(ProceedingJoinPoint pjp) throws Throwable {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		long start = System.currentTimeMillis();
		try {
			return pjp.proceed(pjp.getArgs());
		} finally {
			long end = System.currentTimeMillis();
			log.info("[{}] request process time: {} ms", request.getRequestURI(), (end-start));
		}
	}
	@After("pointcut()")
	public void resultLogger() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
		log.info("[{}] result status: {}", request.getRequestURI(), response.getStatus());
	}
	private void logParams(HttpServletRequest request) {
		String requestUri = request.getRequestURI();
		Iterator<String> keys = request.getParameterMap().keySet().iterator();
		while (keys.hasNext()) {
			String key = keys.next();
			String[] values = request.getParameterMap().get(key);
			log.debug("[{}] request param: {} = {}", requestUri, key, values);
		}
	}
}

package com.inrice.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Http工具类
 */
public class HttpContextUtils {

	public static HttpServletRequest getHttpServletRequest() {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		if(requestAttributes == null){
			return null;
		}

		return ((ServletRequestAttributes) requestAttributes).getRequest();
	}

	public static Map<String, String> getParameterMap(HttpServletRequest request) {
		Enumeration<String> parameters = request.getParameterNames();

		Map<String, String> params = new HashMap<>();
		while (parameters.hasMoreElements()) {
			String parameter = parameters.nextElement();
			String value = request.getParameter(parameter);
			if (StringUtils.isNotBlank(value)) {
				params.put(parameter, value);
			}
		}

		return params;
	}

	public static String getLanguage() {
		//默认语言
		String defaultLanguage = "zh-CN";
		//request
//		HttpServletRequest request = getHttpServletRequest();
//		if(request == null){
//			return defaultLanguage;
//		}
//
//		//请求语言
//		defaultLanguage = request.getHeader(HttpHeaders.ACCEPT_LANGUAGE);

		return defaultLanguage;
	}

	public static List<String> getCookieList(HttpServletRequest request) {
		List<String> cookieList = new ArrayList<>();

		Cookie[] cookies = request.getCookies();
		if (cookies == null || cookies.length == 0) {
			return cookieList;
		}

		for (Cookie cookie : cookies) {
			cookieList.add(cookie.getName() + "=" + cookie.getValue());
		}

		return cookieList;
	}

	public static HttpServletResponse getResponse(){
		HttpServletResponse resp = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getResponse();
		return resp;
	}


}

package com.board.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class LoginCheckInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(
			HttpServletRequest  request, 
			HttpServletResponse response, 
			Object handler)
			throws Exception {
		
		// 1. session 에서 회원정보 조회
		HttpSession session  = request.getSession();
		
		String requestUrl    = request.getRequestURL().toString();
		System.out.println("경로:" + requestUrl + " " +  session.getAttribute("login"));
		// UserVo error 이유: obj타입은 모든 클래스가 부모클래스 -> 넣기 위해 타입변환 필요 session앞에
		// UserVo 대신 obj (세션에 로그인이 있나 없나가 중요) = testing 노필요
		
		// 요청한 주소정보 확인
		
		// /login page는 check에서 제외
		// Inteface 설정하는 곳에서 해당경로를 제외할 때 if() 필요x -> 로그인 제외할 것들을 넣음
		Object   obj         = session.getAttribute("login"); // get은 무조건 obj타입, 
		 if(requestUrl.contains("/login") ) {  // 
			return true;    // login check 중단
		 }
		
		// loginCheck 중지 주석
		/*
		if(obj == null) {   // 로그인 변수가 없으면 loginForm 으로
			// login 되어 있지 않다면 /loginForm 으로 이동  ** 핵심 (이걸 위해 만듦)
			response.sendRedirect("/loginForm");
			return false;  
		}
		*/
		
		return HandlerInterceptor.super.preHandle(request, response, handler);
	}
	
}


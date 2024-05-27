package com.board.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.board.interceptor.LoginCheckInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	
	// 폴더 위치 중요 : main() 함수가 있는 class(Board05Application)의 
	// 패키지(com.board) 아래에 .config 생성해서 저장 (com.board.config)
	// 용도 : Spring Legacy Project는 설정을 .xml에 저장
	//      : SpringBoot Project 는 설정을 WebMvcConfig.java에 저장
	
	@Autowired
	   private  LoginCheckInterceptor loginCheckInterceptor;
	   
	@Override
	public void addInterceptors(InterceptorRegistry registry) {

       // System.out.println("okokok");
        registry.addInterceptor( loginCheckInterceptor )
                .addPathPatterns("/**")      // http://localhost:9090/
                .addPathPatterns("/**/*")    
                .excludePathPatterns("/log*", "/css/**", "/img/**", "/js/**");
        
    }
	
}

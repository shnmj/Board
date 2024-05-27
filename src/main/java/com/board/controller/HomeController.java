package com.board.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.board.menus.domain.MenuVo;
import com.board.menus.mapper.MenuMapper;
import com.board.user.domain.UserVo;
import com.board.user.mapper.UserMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private MenuMapper menuMapper;  // Board가 메뉴가 바뀌기때문에 Board에도 추가해줘야함
	
	// http://localhost:9090
	@RequestMapping("/")
	public  String   home() {
		return "home";
	}
	
	// /login
	@RequestMapping("/loginForm")
	public ModelAndView loginForm() {
		ModelAndView  mv = new ModelAndView();
		mv.setViewName("users/login");
		return mv;
	}
	
	// login (userid=shin&passwd=1234)
	@RequestMapping("/login")
	public ModelAndView login(HttpServletRequest request) {
		
		String      userid  = request.getParameter("userid");
		String      passwd  = request.getParameter("passwd");
		
		UserVo      userVo  = userMapper.login(userid, passwd);
		MenuVo      menuVo  = menuMapper.getMenu("MENU01");
		
		String      loc     = "";
		
		if(userVo != null) { // id pw가 일치하면
			HttpSession session = request.getSession();
			session.setAttribute("login", userVo );    // req 필요
			session.setAttribute("menu", menuVo);
			session.setMaxInactiveInterval(30 * 60);   // 30분동안 유지
			loc    = "redirect:/";
		} else {  // id pw 오류
			loc    = "redirect:/loginForm";
		}
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName(loc);
		
		return mv;	
	}
	
	@RequestMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/loginForm";
		
		
		
	}
	
}

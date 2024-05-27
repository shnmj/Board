package com.board.user.controller;


/*
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
*/
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.board.user.domain.UserVo;
import com.board.user.mapper.UserMapper;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@RequestMapping("/Users")
public class UserController {
	
	@Autowired
	private UserMapper userMapper;
	
	//  /Users/List
	@RequestMapping("/List")
	public ModelAndView list() {
		// 사용자 목록 조회
		List<UserVo> userList = userMapper.getUserList();
		
		ModelAndView  mv      = new ModelAndView();
		mv.addObject( "userList",  userList  );
		mv.setViewName("users/list");  // views 폴더에서 검색
		
		return mv;
	}
	
	//  /Users/WriteForm
	@RequestMapping("/WriteForm")
	public ModelAndView  writeForm() {
		
		ModelAndView mv  = new ModelAndView();
		
		/*
		LocalDateTime   today = LocalDateTime.now();
		String          now   = today.format(
				DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSSS")); 
		DayOfWeek       wkday = today.getDayOfWeek();	
		now                  += " " + wkday; 
		mv.addObject("now", now);
		*/
		
		mv.setViewName("users/write");
		return  mv;
		
	}
	
	//  /Users/Write
	@RequestMapping("/Write")
	public  ModelAndView  write( UserVo  userVo ) {
		// 저장 
		userMapper.insertUser( userVo );		
		
		// data 가지고 이동 
		ModelAndView  mv  =  new  ModelAndView();
		mv.setViewName("redirect:/Users/List");
		return   mv;
	}
	
	
	//  http://localhost:9090/Users/View?userid=U0005
	@RequestMapping("/View")
	public ModelAndView view(UserVo userVo) {
		
		// userid=U0005  -> db 조회
		HashMap<String, Object>  map  =  userMapper.getUser(userVo);
		// System.out.println(vo);
		log.info("map : {}", map); 
		
		// map.get("userid")
		
		ModelAndView mv  =  new ModelAndView();
		mv.addObject("vo", map);
		mv.setViewName("users/view");
		return mv;
		
	}
	
	//  /Users/UpdateForm?userid=shin
	@RequestMapping("/UpdateForm")
	public ModelAndView updateForm(UserVo userVo) {
		// ID로 수정할 한 명의 data 조회
		HashMap<String, Object> map = userMapper.getUser(userVo);
		
		// Model에 담음
		ModelAndView mv = new ModelAndView();
		mv.addObject("vo", map);
		
		// 이동
		mv.setViewName("users/update");
		
		return mv;
	}
	
	//  /Users/Update
	@RequestMapping("/Update")
	public ModelAndView update(UserVo userVo) {
		
		log.info("userVo : {}", userVo);
		
		// 수정 
		userMapper.updateUser(userVo);
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("redirect:/Users/List");
		
		return mv;
	
	}
	
	
	//  /Users/Delete?menuid=U005
	@RequestMapping("/Delete")
	public ModelAndView delete(UserVo userVo) {
		
		// Delete
		userMapper.deleteUser(userVo);
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("redirect:/Users/List");
		return mv;
		
	}
	
	
}

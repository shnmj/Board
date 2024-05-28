package com.board.pds.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.board.menus.domain.MenuVo;
import com.board.menus.mapper.MenuMapper;
import com.board.pds.domain.FilesVo;
import com.board.pds.domain.PdsVo;
import com.board.pds.service.PdsService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/Pds")
public class PdsController {
	
	// app.pro 속성 가져오기
	@Value("${part4.upload-path}")
	private String uploadPath;
	
	@Autowired
	private MenuMapper menuMapper;
	
	@Autowired
	private PdsService pdsService;
	
	// param 받는 vo가 없으므로 HashMap 이용해서 param 처리
	// hashMap 으로 인자 처리 시 @RequestParam 필수
	// /Pds/List?nowpage=1&menu_id=MENU01
	@RequestMapping("/List") 
	public ModelAndView list (
			@RequestParam HashMap<String, Object> map // vo 없이도 parameter 받게끔
			) {
		
		// map : {nowpage=1, menu_id=MENU01}
		System.out.println("map:" + map);
		
		
		// 메뉴 목록
		List<MenuVo> menuList = menuMapper.getMenuList();
		
		// pdsService 생성 이유 : db 업무는 mappper 담당인데 여기서 추가적인 비즈니스로직(db상관x)
		
		// 자료실 목록 조회 : Board + Files
		List<PdsVo>  pdsList  = pdsService.getPdsList(map); 
		
		ModelAndView mv = new ModelAndView();
		mv.addObject("menuList", menuList); // 메뉴 목록
		mv.addObject("pdsList", pdsList);   // 자료실 목록 Board + Files
		mv.addObject("map", map); // vo랑 달라서 아무 객체나 가능 = map안에 어떤 리스트든
		
		// mv.addObject("nowpage", map.get("nowpage")); // vo랑 달라서 아무 객체나 가능 = map안에 어떤 리스트든
		
		mv.setViewName("pds/list");
		return mv;
	}
	
	
	// ============================================================================
	// /Pds/View?bno=1009&nowpage=1&menu_id=MENU01
	@RequestMapping("/View")
	public ModelAndView view (
			@RequestParam HashMap<String, Object> map
			) {
		
		log.info("======Pds/View======");
		log.info("map : {}", map);
		log.info("======Pds/View======");
		
		// 메뉴 목록
		List<MenuVo> menuList = menuMapper.getMenuList();
		log.info("======Pds/View======");
		log.info("menuList : {}", menuList);
		log.info("======Pds/View======");
		
		// 조회수 증가 (Board Table, hit = hit + 1)
		pdsService.setReadcountUpdate(map);
		
		// 조회할 자료실의 게시물 정보 : Board -> PdsVo
		PdsVo pdsVo = pdsService.getPds(map);
		log.info("======Pds/View======");
		log.info("pdsVo : {}", pdsVo);
		log.info("======Pds/View======");
		
		// 조회할 파일 정보 : FilesVo -> PdsVo
		// Bno에 해당되는 파일들 정보
		List<FilesVo> fileList = pdsService.getFileList(map);
		log.info("======Pds/View======");
		log.info("fileList : {}", fileList);
		log.info("======Pds/View======");
		
		// PdsVo (BoardVo + FilesVo)
		
		ModelAndView mv = new ModelAndView();
		mv.addObject("menuList", menuList);
		mv.addObject("vo", pdsVo);
		mv.addObject("fileList", fileList);
		// 단순 넘기는 용도로 존재하는것 뿐 아닌, 넘겼을 때 바뀐 값까지
		mv.addObject("map", map);  
		
		
		mv.setViewName("pds/view");
		return mv;
	}
	
	
	// ===============================================================================
	// 자료실 새 글 등록 (파일 업로드 포함)
	// Pds/WriteForm?nowpage=1&menu_id=MENU01
	@RequestMapping("/WriteForm")
	public ModelAndView writeForm(
			@RequestParam HashMap<String, Object> map
			) {
		
		List<MenuVo> menuList = menuMapper.getMenuList();
		
		ModelAndView mv = new ModelAndView();
		mv.addObject("menuList", menuList);
		mv.addObject("map", map);
		mv.setViewName("pds/write");
		return mv;
	}
	
	
	// ==============================================================================
	// /Pds/Write - 자료실 저장 = map : 글(title, writer, content, ...)
	//							+ upfile : 파일들 저장
	@RequestMapping("/Write")
	public ModelAndView write(
			@RequestParam HashMap<String, Object> map,   //  문자열 받음(일반 데이터)
			@RequestParam(value="upfile", required = false)
				// required=false 입력하지 않을 수 있음
				MultipartFile[] uploadFiles // 파일 처리 
			) {
		
		// 넘어온 정보 
		System.out.println("map:" + map);
		System.out.println("files:" + uploadFiles);
		
		// 저장
		// 1. map정보
		// 새 글 저장 -> Board Table 저장
		// 2. MultipartFile[] 정보 활용
		//  2-1. 실제 폴더에 파일저장         -> uploadPath (d:\dev\data 폴더) 
		//  2-2. 저장 된 파일정보를 db에 저장 -> Files Table 저장
		pdsService.setWrite(map, uploadFiles);
				
		ModelAndView mv = new ModelAndView();
		mv.addObject("map", map);
		String loc =  "redirect:/Pds/List"; 
			   loc += "?menu_id=" + map.get("menu_id");
			   loc += "&nowpage=" + map.get("nowpage");
		mv.setViewName(loc);
		
		return mv;
	}
	
	// ========================================================================
	// 자료실 글 삭제
	// http://localhost:9090/Pds/Delete?bno=1017&menu_ud=MENU01&nowpage=1
	@RequestMapping("/Delete")
	public ModelAndView delete(
			@RequestParam HashMap<String, Object> map) {
		
		// 삭제
		pdsService.setDelete(map);
		
		ModelAndView mv = new ModelAndView();
		
		// 재조회
		String loc = "redirect:/Pds/List?menu_id=" + map.get("menu_id")
				   + "&nowpage=" + map.get("nowpage");
		mv.addObject("map", map);
		mv.setViewName(loc);
		
		return mv;
		
	}
	
	// ===========================================================================
	// 자료실 글 수정
	// http://localhost:9090/Pds/UpdateForm?bno=1017&menu_id=MENU01&nowpage=1
	@RequestMapping("/UpdateForm")
	public ModelAndView updateForm(
			@RequestParam HashMap<String, Object> map) {
		
		// 메뉴 목록
		List<MenuVo>menuList = menuMapper.getMenuList();
		
		// 수정할 게시글 조회
		PdsVo pdsVo = pdsService.getPds(map);
		
		// 수정할 파일들 정보(fileList)
		List<FilesVo> fileList = pdsService.getFileList(map);
		
		
		ModelAndView mv = new ModelAndView();
		
		mv.addObject("menuList", menuList);
		mv.addObject("vo", pdsVo);
		mv.addObject("fileList", fileList);
		mv.addObject("map", map);
		
		mv.setViewName("pds/update");
		return mv;
		
	}
	
	// /Pds/Update
	@RequestMapping("/Update")
	public ModelAndView update(
			@RequestParam HashMap<String, Object> map,
			@RequestParam(value="upfile",  required = false) 
				MultipartFile[] uploadFiles) {
		
		System.out.println("update controller uploadFiles:" + uploadFiles);
		
		pdsService.setUpdate(map, uploadFiles);
		
		ModelAndView mv = new ModelAndView();
		String loc = "redirect:/Pds/List?menu_id=" + map.get("menu_id")
		           + "&nowpage="                   + map.get("nowpage");
		
		mv.setViewName(loc);
		return mv;

	}
	
	
	// ----- 파일 다운로드 ------
	// 이 안의 method는 파일을 return. not ModelAndView : @ResponseBody
	@GetMapping("/filedownload/{file_num}")
	@ResponseBody
	public void downloadFile(
			HttpServletResponse res,
			@PathVariable(value="file_num") Long file_num  // neme for args 에러 -> value="file_num" 추가
			) throws UnsupportedEncodingException {
		
		// 파일 조회 (Files)
		FilesVo fileInfo = pdsService.getFileInfo(file_num);
		
		// 파일 경로 (import nio)
		Path saveFilePath = Paths.get(
				uploadPath + java.io.File.separator
							+ fileInfo.getSfilename()
			);
		
		// 해당 경로에 파일이 없으면
		if(! saveFilePath.toFile().exists() ) {
			throw new RuntimeException("file not found");
		}
		
		// file header 설정
		setFileHeader(res, fileInfo);
		
		// file copy
		fileCopy(res,saveFilePath);
	}
	
	// 파일 복사
	private void fileCopy(HttpServletResponse res, Path saveFilePath) {
		FileInputStream fis = null;
		
		try {
			fis = new FileInputStream(saveFilePath.toFile() );
			FileCopyUtils.copy(fis, res.getOutputStream() );
			res.getOutputStream().flush();
		} catch (Exception e) {
			throw new RuntimeException(e);  // 사용자 정의 예외
		} finally {
			try {
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();    // ?
			}
		}
		
	}


	// 다운받을 파일의 header 정보 설정 -> 이 부분은 카피 
	 private void setFileHeader(HttpServletResponse res,
	          FilesVo fileInfo) 
	                throws UnsupportedEncodingException {
	        res.setHeader("Content-Disposition",
	              "attachment; filename=\"" +   // attachment : 첨부파일
	                 URLEncoder.encode(
	                 (String) fileInfo.getFilename(), "UTF-8") + "\";");
	        res.setHeader("Content-Transfer-Encoding", "binary");
	        res.setHeader("Content-Type", "application/download; utf-8");
	        res.setHeader("Pragma", "no-cache;");
	        res.setHeader("Expires", "-1;");
	    }
	
	
	
}

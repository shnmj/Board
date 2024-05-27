package com.board.pds.service.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.board.pds.domain.FilesVo;
import com.board.pds.domain.PdsVo;
import com.board.pds.mapper.PdsMapper;
import com.board.pds.service.PdsService;

@Service
public class PdsServiceImpl implements PdsService {
	
	// 파일이 저장될 경로 (uploadPath <- app.pro를 읽어옴)
	@Value("${part4.upload-path}")
	private String uploadPath;
	
	@Autowired
	private PdsMapper pdsMapper;
	
	@Override
	public List<PdsVo> getPdsList(HashMap<String, Object> map) {
		// map {"menu_id" : "MENU01", "nowpage" : 1}
		// db 조회 결과 return
		List<PdsVo> pdsList = pdsMapper.getPdsList(map);
		// System.out.println("pdsService pdsList:" + pdsList);
		return pdsList;
	}

	@Override
	public PdsVo getPds(HashMap<String, Object> map) {
		PdsVo  pdsVo = pdsMapper.getPds(map);
		System.out.println("pdsVo:" + pdsVo);
		return pdsVo;
	}

	@Override
	public List<FilesVo> getFileList(HashMap<String, Object> map) {
		List<FilesVo> fileList = pdsMapper.getFileList(map);
		System.out.println("fileList:" + fileList);
		return fileList;
	}
	
	// 자료실 글쓰기 저장
	@Override
	public void setWrite(HashMap<String, Object> map, MultipartFile[] uploadFiles) {
		
		// 파일 저장 + 자료실 글쓰기
		// 1. 파일 저장
		// uploadFiles[] 를 d:\dev\data
		map.put("uploadPath", uploadPath);
		System.out.println("1:" + map);
		
		// PdsFile class - 파일 처리 전담 class 생성
		// 1. 파일 저장
		// 2. 저장된 파일 정보 가져옴
		/* map 1 : {menu_id=MENU01, nowpage=1, title=aa, writer=aa, 
		 	content=,aaa, uploadPath=D:/dev/data/ */
		PdsFile.save(map, uploadFiles);
	
		// map이 중요한 역할
		System.out.println("2:" + map);
		/* map 2 : {menu_id=MENU01, nowpage=1, title=aa, writer=aa, 
	 		content=,aaa, uploadPath=D:/dev/data/, fileList= */
		
		// db 저장 -------------------------------------
		// 3. Board에 글 저장
		pdsMapper.setWrite(map);
		
		// 4. Files에 저장된 파일 정보 저장
		List<FilesVo> fileList = (List<FilesVo>) map.get("fileList");
		if(fileList.size() !=0)
			pdsMapper.setFileWrite(map);
	}

}

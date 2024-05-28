package com.board.pds.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.board.pds.domain.FilesVo;
import com.board.pds.domain.PdsVo;

// interface : 전체가 abstract method
// abstract : 함수 내용({})이 없는 함수 만들려면

public interface PdsService {

	List<PdsVo> getPdsList(HashMap<String, Object> map);

	PdsVo getPds(HashMap<String, Object> map);

	List<FilesVo> getFileList(HashMap<String, Object> map);

	void setWrite(HashMap<String, Object> map, MultipartFile[] uploadFiles);

	void setReadcountUpdate(HashMap<String, Object> map);

	FilesVo getFileInfo(Long file_num);

	void setDelete(HashMap<String, Object> map);

	void setUpdate(HashMap<String, Object> map, MultipartFile[] uploadFiles);

}

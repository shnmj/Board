package com.board.pds.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilesVo {
	private int file_num;
	private int bno;
	private String filename;
	private String fileext;
	private String sfilename;
}

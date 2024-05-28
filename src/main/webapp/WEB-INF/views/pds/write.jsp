<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="icon" type="image/png" href="/img/favicon.png" />
<link rel="stylesheet"  href="/css/common.css" />
<style>
   input:not(input[type=submit]) { width:100%; }
   input[type=submit] { width : 100px; }
   #goList  { width : 80px; }
   
   td { 
      padding:10px;
      width: 700px;
      text-align: center;
   }
   td:nth-of-type(1) {
      width : 200px;
   }
   
   td:not([colspan]):first-child {
      background: black;
      color : white;
      font-weight: bold;
   }
   
   input[readonly] {
      background: #EEE;
   }
  
   textarea  {
      height: 250px;
      width : 100%;
   }
</style>


<script src="https://code.jquery.com/jquery.min.js"></script>
<script>
	$(function() {
		let num = 1;
		$('#btnAddFile').on('click', function(e) {
			let tag = '<input type="file" name="upfile" '
				+ 'class="upfile" multiple/><br>';
			$('#tdfile').append(tag);
			num++;
		})
	} );
	
	// html input type="file" name="upload"  multiple />
	// 여러 파일 선택하여 전송 가능 + ctrl이나 shift 여러개 선택
</script>


</head>
<body>
  <main>
    
    <%@include file="/WEB-INF/include/pdsmenus.jsp" %>
  
	<h2>자료실 글 등록</h2>
	<form action="/Pds/Write" method="POST"
			 enctype="multipart/form-data"> <!-- 파일 전송(binary) -->
	<input type="hidden" name="menu_id" value="${ map.menu_id }" />
	<input type="hidden" name="nowpage" value="${ map.nowpage }" />
	<table>
	 <tr>
	   <td>제목</td>
	   <td><input type="text" name="title" /></td>
	 </tr>
	 <tr>
	   <td>작성자</td>
	   <td><input type="text" name="writer" /></td>
	 </tr>
	 <tr>
	   <td>내용</td>
	   <td><textarea name="content"></textarea></td>
	 </tr>	
	 <tr>
	   <td>파일</td>
	   <td id="tdfile">
	   	<input type="button" id="btnAddFile" value="파일추가(최대 30MB)" />
	   	<input type="file" name="upfile" class="upfile" multiple/>
	   </td>
	 </tr>	
	 <tr>
	   <td colspan="2">
	    <input type="submit" value="글 쓰기" />
	    <input type="button" value="목록" id="goList" />
	   </td>
	 </tr>
	
	</table>	
	</form>   
	
  </main>
  
  <script>
  // 목록 이동
  	const  goListEl  = document.getElementById('goList');
  	goListEl.addEventListener('click', function(e) {
  		location.href = '/Pds/List?menu_id=${map.menu_id}&nowpage=${map.nowpage}';
  	})
  
  </script>
  
  
</body>
</html>






<h2>자료실 파일 업로드&다운로드</h2>
<br>
<details>
  <summary>
    Error 해결
  </summary>
  <ul dir="auto">
  <li>NULL을 ("SPRING"."FILES"."FILENAME") 안에 삽입할 수 없습니다</li>
  <ol>
    <li>if문 + 연산자</li>
    files insert 문제로 null이 넘어와도 fileList에 저장 하지 않게 함<br><br>
    -> if(!fileName.trim().equals("") || fileName != null ) <br>
    (공백을 제거했을 때 equals가 아니거나 파일이름이 null이 아닐 때)
    
  </ol>
    
</details>

<br>
<details>
  <summary>
    05.24
  </summary>
  <ul dir="auto">
    <br>
    
    파일전송 : server <-> client
      프로토콜 file transfer protocol
         파일 전송을 위한 프로그램 별도 필요
        Filezilla, 알 ftp등의 ftp client (ftp = 채팅 같은 ..)
    
    
    http 방식 : fiel upload
                     download
    
    parameter type text : string
    client                -> server
    
    
    browser에서 사용 가능하나 웹사이트 적용 어려움
    ------------------------------------------
    http 전송방식
    GET(a tag)는 주소줄에 data를 저장해서 전송
     주소줄 용량 작음 - 파일 전송x
    
    POST는 용량 제한 없이 파일 전송 가능
     http protocol : 1.1 이상 
    
    
    binary data 전송 시 추가 속성
    - enctype="multipart/form-data"
    
    파일을 	보내는 client 소스 : html
    <form action="파일처리 controller" method="POST"
        enctype="multipart/form-data">
     <input type = "file" name="file1" />
     <input type = "file" name="file2" />
     <input type = "file" name="file3" />
    
     <input type = "file" name="files" multiple /> <!-- 여러개 동시-->
     <input type = "submit" />
    </form>
    
    업로드된 data를 서버에 저장하는 법
     : 서버 언어가 지원되야 가능
        old = asp, php, js 초기ver -> 별도 lib 도움 받아 저장
        new = java, C#은 언어차원에서 저장 기능 있음
              jsp는 lib 도움 받아 처리
    
    
    
    3. 파일 저장 위치 - 다운로드 코딩이 달라짐 (보안 고려)
      1)
      2)
    
    
    4. 저장된 파일정보를 db에 저장하는 법 (db 고려)
      1) 파일 자체를 db안에 저장 방식 - multimedia db(박물관, 영화관 ..)
         db column : blob (binary large obj) 블랍 :  실제 이진 data 저장
    
         Oracle BLOB = 4GB -> 여기에 file data(이진 data) 자체를 column에 저장
         => 문제 : insert에 value를 넣을 때 lib 도움을 받아 저장하는데,
                  values (to_BLOB(data), ...)
                  commit;
                 -> insert 시 insert가 완료될때까지 db table에 lock걸림 (다른사람이 select 못 함)
    
    	전문적인 멀티미디어 db에만 blob 사용해야함
    
      2) 일반적인 홈페이지에서는 파일은 별도 폴더에 저장
          db에는 파일 이름과 위치등 정보 저장
    
    
    5. download 
    
    6. 파일명 중복처리 방법
    
</details>
<br>
<details>
  <summary>
    05.27
  </summary>
  <ul dir="auto">
    <br>

    1. 자료실 파일 정보 저장(Files)을 위한 오라클 함수 생성 (INSERT ALL)
    
    CREATE FUNCTION GET_FILENUM
    RETURN 
        NUMBER
    AS
        NUM NUMBER;
    BEGIN
        SELECT FILENUM_SEQ.NEXTVAL
        INTO NUM
        FROM DUAL;
    
        RETURN NUM;
    END;
    /

    --> 호출될 때마다 FILENUM_SEQ 의 다음 값 반환.

    
    CREATE SEQUENCE FILENUM_SEQ;

    /*
    CREATE FUNCTION GET_FILENUM
    RETURN 
        NUMBER
    AS
        NUM1 NUMBER;
    BEGIN
        SELECT  (SELECT NVL(MAX(file_num),0)+1 FROM FILES)
         INTO   NUM1
         FROM DUAL;
     
         RETURN NUM1;
     
    END;
    /

    */



    SET SERVEROUTPUT ON
    DECLARE
        v_return NUMBER;
    BEGIN
        v_return := get_filenum(); 
        DBMS_OUTPUT.PUT_LINE('v_Return = ' || v_Return);
    END;
    /



    2. XML에서 Insert All 사용
    
    <insert id="setFileWrite">
      <foreach collection="fileList" item="file"
              index="i"
              open="INSERT ALL"
              close="SELECT * FROM DUAL"
              separator=" ">
          INTO FILES VALUES (
              GET_FILENUM(),
              (SELECT MAX(BNO) FROM BOARD),
              #{file.filename},
              #{file.fileext},
              #{file.sfilename}
          )
      </foreach>
    </insert>

    --> 파일 정보가 fileList의 리스트 형태로 제공, 각 파일마다 INTO FILES VALUES 절을 통해 삽입.
        GET_FILENUM() 함수를 사용하는 Oracle, STS 둘 다 동일. 차이점은 MyBatis에서 foreach 구문 사용.


    <기본적인 함수 문법>
      CREATE FUNCTION function_name
      RETURN 
          return_datatype
      IS | AS
         [declaration_section]
      BEGIN
         executable_section
         [EXCEPTION
         exception_section]
      END [function_name];


</details>
<br>
<details>
  <summary>
    05.28
  </summary>
  <ul dir="auto">
    <br>
    <li>Spring 공홈 JPA 참조 사이트 : https://spring.io/projects/spring-data-jpa#samples</li>

  
    
</details>
<br>
<details>
  <summary>
    05.29
  </summary>
  <ul dir="auto">
    <br>

    

</details>
<br>
<details>
  <summary>
    PrjJpa04
  </summary>
  <ul dir="auto">
    <br>

    content-type : text/html;charset=UTF-8  = mime type
    

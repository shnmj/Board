<h2>자료실 파일 업로드&다운로드</h2>
<br>
<details>
  <summary>
    개념 정리
  </summary>
  <ul dir="auto">
    <br>
    
    FrameWork

    1. Spring Regacy - Application + Web Application
       Enterprize (기업) 환경 프로젝트에 웹 이외의 적용하도록 만들어진것 



    Web MVC : .jsp용
      HomeController  ->          : db와 관계 비즈니스 로직처리
      @ReqMap("/")    ->  Service(Interface)  -> ServiceImpl(@Service{"userService"} class)
                          Repo    : db 조작
                      ->  Dao(Interface)      -> DaoImpl(@Repo{"userDao} class)
                      ->  MyBatis


    2. SpringBoot Web MVC (Web에 특화)
        Controller           Model         View
        HomeController       ->            .jsp
                                       .mustache()

        세분화
        Cont -> Service -> Repository -> DB
        Presentation



    3. Spring Rest Api 방식 (전체 기능을 함수화 : 
        Rest : 하나의 주소로 접근 Method를 다르게(Get, Post, Patch, Del)
        -> Prj를 service 단위로 처리하는 기술

    Method
    GET        /articles   :  select 실행결과  -> json 출력
    POST       /articles   :  insert           -> json
    PATCH      /articles   :  update           -> json
    Del        /articles   :  del              -> json
    
</details>
<li>Mustache 문법 참조 사이트 : https://bibi6666667.tistory.com/269</li>
<br>
<details>
  <summary>
    Error 해결
  </summary>
  <ul dir="auto">
  <li>Name for argument of type Error</li>
  <ol>
    <li>Parameter를 처리하는 BoardVo 객체 이용</li>
    insert(BoardVo vo) {}
    <li>insert(@RequestParam("title") String title,</li>
    @RequestParam("title") String content) {}
    
  </ol>
    
</details>

<br>
<details>
  <summary>
    04.17
  </summary>
  <ul dir="auto">
    <br>
    
    jpql (sql문 사용)
    queryDSL (jsql에서 좀 더 발전)

    Model 값 출력
     ${} : EL
     <c:forEach> : JSTL -> for 문 대신 처리 class
     자주쓰는 명령어를 tag로 바꾼,
    
    Controller
      model.addAttribute("name", "shin" );
      return "view"  // view.jsp에 전달
      
    view.jsp  ->
      <% =model.getAttribute("name") %>  // java 문법 꺼낼 때
      ${ name }

    -----------------------
    bean : instance static (static = 한 개만 가능)
    
    Class car { } class : car</ul>
    
    static - compile시 미리 생성

    
    Reference type
      Car myCar = new Car();  // heap (tree)
      instance : myCar

    
    Primitive type
      int num = 3;     // stack : 메모리에서 일부만 동작
      instance : num   // 
</details>
<br>
<details>
  <summary>
    04.18
  </summary>
  <ul dir="auto">
    <br>

    Spring      <->        jap        <->              h2 db
                           orm
                           java 문법(class 사용)
                           .save(boardVo) - (insert)
                           sql -> (대신) JPQL or QueryDSL
                           
    {{ }} = model.getAttribute                       

    a href & from tag = GetMapping
    reqmap 대신 rest (하나의 주소를 method를 바꿔서 다른일을 하게끔 : CRUD)

    form tag안 input type들을 request
    name 안의 변수를(name="title") Controller에 전송
    submit 누르면 action의 주소로 이동

    POST로 적으면 F12 payload에 form data (입력한 데이터가 담겨서 전송)

    ---------------------------

    Regacy = 변수로 받아짐
    Boot   = 객체로 받는 형태로 바뀜 (Dto = Vo)

    ---------------------------

                         entity가 생성&실행 ->  h2 create table 수행

                        class(vo)       class(entity)        jpa
    write.mustache  ->  articleDto  ->  Article       ->                 ->       h2
    title               title           id                 .save()               Article table
    content             content         title               (INSERT)
                                        content


    -----------------------------------

    정보 전달법
      POST 방식 From Data    : /articles/Write
      Get  방식 QueryStirng  : /articles?id=1  (데이터가 QS에 담김)
               PathVariable : /articles/1  -> REST에서 사용 하는 GET 방식

    
    /*
    자료를 하나씩 채우는 형식
    ArrayList 사이즈가 0이됨
    
    List<MenuVo> menuList = new ArrayList<>();
    if(menuList.size() == 0 ) // 자료가 없으면
    while 문 돌리면서 조회..?
    
    
    
    MenuVo  menuVo = null;
     menuVo = new MenuVo(1, '');
    if(menuVo == null) // 조회한 자료가 없으면
    
    
    한 개만 조회했으니 orElse
    */
    
    
</details>
<br>
<details>
  <summary>
    04.19
  </summary>
  <ul dir="auto">
    <br>
    <li>Spring 공홈 JPA 참조 사이트 : https://spring.io/projects/spring-data-jpa#samples</li>

    persist 영속 : entity를 .save 명령으로 insert

    hibernate에서 orm을 지원/사용
    ORM = java class db 연결

    -----------------------------------------------------

    				                                  @Entity               h2 db table
    write.mustache   ->    ArticleDto    ->    Article         ->   Article
    title                  title               id(번호자동증가)
    content                content             title 
                                               content
    
    
    
    p197 ~ 변경 수정, 삭제
    
    		                   vo		                @Entity               h2 db table
    write.mustache   ->    ArticleForm    ->    Article         ->    Article
    title                  id                   id(번호자동증가)
    content                content              title 
                           title                content
                                                getter/setter

    teg -> 이미지로 = view rendering

    -----------------------------------------------------------

    REST API

    페이지 이동 x --> view가 다함(html js, mustache) CRUD
     결과 data만 client에 내려보내는 형식
      data(json{대표}, xml, html, csv) --> server 기능만 하게끔.
         html + js => 서버 데이터 요청
         이 데이터는 json/xml으로 내려옴 
          -> 1. js(XHR: XMLHttpRequest)로 불러옴 (옛날 방식 - 개념만 알고)
             2. $.ajax() -> jquery 사용 (읽을 줄 알아야함) 
             3. axios
             4. fetch()
         

    /Board
    method
    GET    ->   @GetMapping("/Board")     -->  select
    GET    ->   @GetMapping("/Board/2")   -->  selecct where
    POST   ->   @PostMapping("/Board")    -->  insert
    
    -------------------------------------------------------------
    
    PUT    ->   @PutMapping("/Board")     -->  update (전체) where
    PATCH  ->   @PatchMapping("/Board")   -->  update (부분) where
    DELET  ->   @DeleteMapping("/Board")  -->  delete where 





    /Board/WriteForm --> 예전
    입력받으면 return해서 mus에 보내거나 jsp로 보냄
    페이지 이동을 cont가 담당
    
</details>
<br>
<details>
  <summary>
    04.26
  </summary>
  <ul dir="auto">
    <br>

    content-type : text/html;charset=UTF-8  = mime type
    -> appli proper에서 강제 encoding 설정을 통해 따로 기입 안해도 OK
  
    input type : submit event 발생 (submitter)
    
    ----------------------------------
    Rest Api
    
    모든 Api가 Rest일 때
    Restful API
    
    Application Properties
    # src/main/resources/data.sql을 기초 데이터로 사용하겠다
    spring.jpa.defer-datasource-initialization=true

    ------------------------------------

    1. Name for argument of type 에러 해결 -> 이클립스 설정으로 작동(소스 변경 안해도 ㄱㅊ)
      1) java.lang.IllegalArgumentException: Name for argument of type [java.lang.Long] not             specified, and parameter name information not available via reflection. 
           Ensure that the compiler uses the '-parameters' flag.
    
    
      // 에러 : Name for argument of type 
      // Project properties -> Java compiler -> enable/check "Project specific settings"
      //  -> enable/check "Store information about method parameters(usable via reflection)"
    
    
    2. Parameter 처리하는 BoardVo 객체 이용
       insert(BoardVo vo) {}
    
    
    3. @RequestParam("title")  Srtring title,
       @@RequestParam("title") Srtring content) {}
    
    
    =========================================================
    
    
    Spring 부품들을 class로 만듦
    이 class들이 역할에 따라 @을 갖고 있음
    @Component를 상속받아서 하위가 만들어짐
      @Cont
      @Service
      @Repository  --> 현재 interface로만 만들어져 있음 (@ x)
      @Mapper
    
    
    <Spring Legacy>   Service
    
                        비즈니스 로직          data 관련
                        db와 무관
    PT 계층(Layer)   ->  서비스 계층    ->      repository         ->   mybatis   ->  oracle
    @Controller
    Controller          service             dao(mapper.java)
                        serviceImpl         daoImpl
    
    RestController                          articleRepository
    

    -----------------------------
    
    /*
    50씩 증가하는 시퀀스 삭제
    drop sequence article_seq
    
    
    <?>
    type을 컴파일 할 때 결정
    뒤에서 못 바꿈
    
    이런 형태의 코딩을 가능하게 해야 t 사용 가능
    
    실행할때 compie이 가능하게 끔 설정
    
    lazy
    컴파일 시 결정이 아닌 실행할 때 type 결정
    */

</details>
<br>
<details>
  <summary>
    PrjJpa04
  </summary>
  <ul dir="auto">
    <br>

    content-type : text/html;charset=UTF-8  = mime type
    

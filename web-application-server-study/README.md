# web-application-server

책에 나와있는 챕터 3이후에 사용될 [web-application-server](https://github.com/slipp/web-application-server)
프로젝트는 는 이클립스와 메이븐으로 작성되었는데

최근엔 인텔리제이와 그래들로 많이 사용하기 때문에 새로 만들었습니다.


# 실행
WebServer 클래스의 main함수 실행

# 확인
http://localhost:8080 으로 접속했을때 "Hello World"가 출력되면 정상적으로 동작


아래는 기존의 README.md 파일 내용임

---

# 실습을 위한 개발 환경 세팅
* https://github.com/slipp/web-application-server 프로젝트를 자신의 계정으로 Fork한다. Github 우측 상단의 Fork 버튼을 클릭하면 자신의 계정으로 Fork된다.
* Fork한 프로젝트를 eclipse 또는 터미널에서 clone 한다.
* Fork한 프로젝트를 eclipse로 import한 후에 Maven 빌드 도구를 활용해 eclipse 프로젝트로 변환한다.(mvn eclipse:clean eclipse:eclipse)
* 빌드가 성공하면 반드시 refresh(fn + f5)를 실행해야 한다.

# 웹 서버 시작 및 테스트
* webserver.WebServer 는 사용자의 요청을 받아 RequestHandler에 작업을 위임하는 클래스이다.
* 사용자 요청에 대한 모든 처리는 RequestHandler 클래스의 run() 메서드가 담당한다.
* WebServer를 실행한 후 브라우저에서 http://localhost:8080으로 접속해 "Hello World" 메시지가 출력되는지 확인한다.

# 각 요구사항별 학습 내용 정리
* 구현 단계에서는 각 요구사항을 구현하는데 집중한다. 
* 구현을 완료한 후 구현 과정에서 새롭게 알게된 내용, 궁금한 내용을 기록한다.
* 각 요구사항을 구현하는 것이 중요한 것이 아니라 구현 과정을 통해 학습한 내용을 인식하는 것이 배움에 중요하다. 

### 요구사항 1 - http://localhost:8080/index.html로 접속시 응답
* try-catch-resource 라는 방식을 새로 알게됨 (Java 7부터 추가)
* stream의 동작 방식? 에 대해서 공부가 필요할듯 - OutputStream은 run 메소드가 끝나고나면 어떻게 처리되는가?

### 요구사항 2 - get 방식으로 회원가입
* 같은 URL에 여러 요청방식(get, post, put, delete)가 들어오면 조건문이 많아 질텐데 효과적으로 처리할 수 있는 방법은 없나?

### 요구사항 3 - post 방식으로 회원가입
* 헤더랑 body를 한번에 구분하는 방법은 없을까? (header를 다 확인하고, body를 확인하는게 아닌 바로 body에 접근할 수 있는 방법)

### 요구사항 4 - 302 status code 적용
* 명시적으로 코드를 처리해주는 방식 외에는 자동으로 처리하게끔 구현하면 편할듯

### 요구사항 5 - 로그인하기
* 헤더로 쿠키를 설정할때 도메인, 만료시간 등을 설정하는 방법에 대해 공부 
* 헤더로 쿠키를 삭제하는 방법에 대해 공부

### 요구사항 6 - 사용자 목록 출력
* html파일을 직접 수정해서 하는것보다 서버사이드에서 렌더링 하거나 ajax를 이용해 처리해주는게 훨씬 깔끔 할듯

### 요구사항 7 - CSS 지원하기
* Content-type에 css와 html 외에 처리해야하는 데이터 종류는?

### heroku 서버에 배포 후
* 

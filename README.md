# 네티 스터디 내용 정리

Gradle을 이용하여 각 장마다 서브프로젝트로 공부한 내용을 정리하였음
* Netty 버전 4.1.51.Final  
* java 버전 1.8
* [예제](https://github.com/netty/netty/tree/4.1/example/src/main/java/io/netty/example) 
* [네티 저자 코드](https://github.com/krisjey/netty.book.kor) 

## 목차

* netty-study-01-introduce
  - 네티를 이용하여 간단한 에코서버와 클라이언트 서버를 구현 
* netty-study-01-bootstrap
  - 네티로 작성한 네트워크 애플리케이션 동작 방식과 환경 구성
  - AbstractBoostStrap의 구현체인 서버용 ServerBootstrap과 클라이언트 Bootstrap을 상속하여 구현
  - 이벤트 핸들러 등록방법과 소켓 옵션 설정방법 소켓 입출력 방식 설정 방법 확인
* netty-study-03-pipeline
  - 채널파이프라인을 통해 이동하는 이벤트를 처리하는 이벤트 핸들러 구현방법과 자주 사용하는 이벤트 핸들러를 미리 구현해놓은 코덱의 활용
* netty-study-04-eventloop
  - 이벤트 루프에 대한 개념 및 퓨처패턴의 활용
* netty-study-05-bytebuffer
  - 네티 바이트 버퍼 활용 (자바 NIO 바이트버퍼 X) 
* netty-study-06-security
  - 네티 채널 보안 관련 내용 
* netty-study-07-springframework
  - 네티를 스프링 프레임워크를 이용해서 사용하는 방법과 테스트 방법과 관련된 내용 
package co.kr.hjt.server;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import co.kr.hjt.server.config.TelnetServerConfig;

public class Main {

    public static void main(String[] args) {
        AbstractApplicationContext context 
            = new AnnotationConfigApplicationContext(TelnetServerConfig.class);
        /**
         * 스프링 IoC 컨테이너를 사용한다면(예를 들어 리치클라이언트 데스크탑 환경이라든지) 
         * JVM에 종료 훅(shutdown hook)을 등록한다. 종료 훅을 등록함으로써 안정적인 종료를 
         * 보장하고 모든 리소스를 릴리즈하기 위해 싱글톤 빈의 적절한 파괴 메서드를 호출한다. 
         * 물론 여전히 이러한 파괴 콜백을 적절하게 설정하고 구현해야 한다.
         */
        context.registerShutdownHook();
        
        TelnetServer server = context.getBean(TelnetServer.class);
        server.start();

    }

}

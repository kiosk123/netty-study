package co.kr.hjt.api;

import co.kr.hjt.api.core.ApiRequestParser;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.ssl.SslContext;

public class ApiServerInitializer extends ChannelInitializer<SocketChannel> {
    private final SslContext sslCtx;

    public ApiServerInitializer(SslContext sslCtx) {
        this.sslCtx = sslCtx;
    }

    @Override
    public void initChannel(SocketChannel ch) {
        ChannelPipeline p = ch.pipeline();
        if (sslCtx != null) {
            p.addLast(sslCtx.newHandler(ch.alloc()));
        }

        //HTTP 요청처리 디코더 HTTP프로토콜-> 네트 바이트 버퍼로 변환
        p.addLast(new HttpRequestDecoder()); 
        
        /**
         * HTTP 프로토콜에서 발생하는 메시지 파편화 처리
         * HTTP 프로토콜을 구성하는 데이터가 나뉘어서 수신되었을 때 데이터를 하나로 합쳐주는 역할을 수행
         * 인자로 입력된 65536은 한꺼번에 처리가능한 데이터크기
         * 하나의 데이터가 65536을 초과 하면 TooLongFrameException 예외 발생
         */
        p.addLast(new HttpObjectAggregator(65536));  
        
        /**
         * HTTP 요청 처리 결과를 클라이언트로 전송할 때 HTTP 프로토콜로 변환해주는 인코더
         */
        p.addLast(new HttpResponseEncoder());
        // HTTP 프로토콜로 송수신되는 HTTP의 본문데이터를 Gzip 알고리즘을 사용하여 압축과 해제를 수행
        // ChannelDuplexHandler를 상속받았기 때문에 인바운드와 아웃바운드에서 모두 호출
        p.addLast(new HttpContentCompressor());
        
        //HTTP 데이터에서 토큰 발급과 같은 업무처리하는 클래스
        p.addLast(new ApiRequestParser());
    }
}
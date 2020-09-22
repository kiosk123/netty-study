package co.kr.hjt.bootstrap;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 논블로킹 입출력 모드를 지원하는 ServerBootStrap 초기화
 */
public class EchoServer {
    public static void main(String[] args) throws Exception {

        // 클라이언트 연결 수락 스레드 그룹 - 생성자 파라미터에 입력된 스레드 수가 1이므로 단일 스레드로 동작
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);

        // 연결된 클라이언트 소켓으로부터 데이터 입출력 및 이벤트를 처리를 담당하는 자식 스레드 그룹 - CPU코어수에 따른 스레드 수가 설정
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            /**
             * 논블로킹 ServerBootStrap 초기화
             */
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class) // 서버소켓 네트워크 입출력 모드를 NIO 모드로 설정

                    // 자식 채널 초기화 방법 설정
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        // ChannelInitializer는 클라이언트로부터 연결된 채널이 초기화 될때의 기본동작이 지정된 추상클래스
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline(); // 채널 파이프라인 객체 생성
                            p.addLast(new EchoServerHandler());// 채널파이프라인에 클라이언트 연결시 데이터처리할 이벤트 핸들러 등록
                        }
                    });

            // ServerBootstrap.bind를 이용하여 접속할 포트지정
            ChannelFuture f = b.bind(8888).sync();
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}

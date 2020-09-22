package co.kr.hjt.bootstrap;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * 이벤트 핸들러 예제
 */
public class EventHandlerExample {
    public static void main(String[] args) throws Exception {

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            /**
             * 논블로킹 ServerBootStrap 초기화
             */
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)

                    // 서버 소켓 채널에서 발생하는 이벤트를 수신하여 처리할 핸들러를 등록한다.
                    // LoggingHandler를 등록해서 서버소켓 채널에서 발생하는 모든이벤트를 로그로 출력한다.
                    .handler(new LoggingHandler(LogLevel.INFO))

                    // 서버로 연결된 클라이언트 소켓 채널에서 발생하는 이벤트를 수신하여 처리
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();

                            // 클라이언트 소켓 채널파이프라인에 LoggingHandler와 EchoServerHandler등록
                            p.addLast(new LoggingHandler(LogLevel.INFO));
                            p.addLast(new EchoServerHandler());
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

package co.kr.hjt.pipeline.v1;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class PipeLineExampleV1 {
    public static void main(String[] args) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            /**
             * 논블로킹 ServerBootStrap 초기화
             */
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class) // 서버소켓 네트워크 입출력 모드를 NIO 모드로 설정
                    // 연결된 클라이언트 소켓이 사용할 채널 파이프라인을 설정
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        // initChannel은 클라이언트 소켓이 생성될때 자동으로 호출
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline(); // 채널 파이프라인 객체 생성

                            // 채널파이프라인에 클라이언트 연결시 데이터처리할 이벤트 핸들러 등록 - 다음과 같이 여러개 등록가능
                            p.addLast(new ChannelInboundFirstEventHandler());
                            p.addLast(new ChannelInboundSecondEventHandler());
                        }
                    });

            ChannelFuture f = b.bind(8888).sync();
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}

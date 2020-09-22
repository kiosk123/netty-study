package co.kr.hjt.eventloop.asyncio.future;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class EchoServerWithFuture {
    public static void main(String[] args) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new EchoServerHandlerWithFuture());
                        }
                    });

            // 비동기 bind 메서드 호출 포트 바인딩이 완료되기전 ChannelFuture반환
            ChannelFuture bindFuture = b.bind(8888);

            // ChannelFuture작업이 완료될때까지 블러킹 bind()처리 왼료시 블러킹 해제
            bindFuture.sync();

            // 8888번에 포트 바인딩된 서버ㅐ널을 가져옴
            Channel serverChannel = bindFuture.channel();

            // 바인드완료된 서버 채널의 closeFuture 반환 네티는 내부에서 채널이 생성될 때 closeFuture도 같이 생성
            // closeFuture가 돌려주는 closeFuture객체는 항상 동일한 객체
            ChannelFuture closeFuture = serverChannel.closeFuture();

            // closeFuture 채널 연결이 종료될때 연결종료 이벤트를 받는다.
            // 채널이 생성될때 같이 생성되는 기본 closeFuture객체에는 아무동작도 설정되지 않았기 때문에
            // 이벤트를 받았을 때 아무 동작도 하지 않는다.
            closeFuture.sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}

package co.kr.hjt.server;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class EchoClient {

    public static void main(String[] args) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline p = ch.pipeline();
                    p.addLast(new EchoClientHandler());
                }
            });
            /**
             * 비동기 입출력 메서드인 connect 메서드는 메서드의 호출결과로 ChannelFuture를 돌려준다. ChannelFuture를 통해
             * 비동기 메서드의 처리결과를 알 수 있다. ChannelFuture의 sync 메서드는 ChannelFuture의 요청이 완료될때까지
             * 대기한다. 요청이 실패하면 예외를 던진다. connect 메서드의 처리가 완료될때까지 다음 라인으로 진행하지 않는다.
             */
            ChannelFuture f = b.connect("localhost", 8888).sync();
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }

}

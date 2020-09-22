package co.kr.hjt.bootstrap;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Netty의 BootStrap에서 소켓 옵션 설정에 관련된 예제 소켓 주요옵션 TCP_NODELAY : Nagle 알고리즘 비활성화 여부
 * : 기본값 false SO_KEEPALIVE : 운영체제에서 지정된 시간에 한번씩 keepalive 패킷을 상대방에게 전송 : 기본값
 * false SO_SNDBUF : 상대방으로 송신할 커널 송신버퍼의 크기 SO_RCVBUF : 상대방으로부터 수신할 커널 수신버퍼의 크기
 * SO_REUSEADDR : TIME_WAIT 상태의 포트를 서버소켓에 바인드 할수있게 한다 : 기본값 false SO_LINGER :
 * 소켓을 닫을 때 커널의 송신 버퍼에 전송되지 않은 데이터의 전송 대기시간을 지정한다 : 기본값 false close()메서드 호출시 커널
 * 버퍼의 데이터를 상대방으로 모두 전송하고 상대방의 ACK패킷을 기다린다. 포트상태가 TIME_WAIT로 전환되는 것을 방지하기 위해 옵션을
 * 활성화하고 타임아웃값을 0으로 설정하는 편법을 사용하기도 한다. SO_BACKLOG : 동시에 수용 가능한 소켓 연결 요청 수
 */
public class SocketOptionExample {
    public static void main(String[] args) throws Exception {

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            /**
             * 논블로킹 ServerBootStrap 초기화
             */
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    // option메소드는 서버소켓채널의 옵션을 설정한다
                    .option(ChannelOption.SO_BACKLOG, 1) // 서버가 동시에 하나의 연결요청만 수용하도록 소켓옵션 설정

                    // childOption메소드는 서버에 접속한 클라이언트 소켓 채널에 대한 옵션을 설정한다
                    // 0값을 주었기 때문에 커널 버퍼에 남은 데이터를 상대방 소켓 채널로 모두 전송후 연결을 끊는다.
                    .childOption(ChannelOption.SO_LINGER, 0).childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
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

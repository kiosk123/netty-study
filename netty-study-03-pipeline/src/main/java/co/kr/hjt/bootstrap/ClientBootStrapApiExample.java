package co.kr.hjt.bootstrap;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class ClientBootStrapApiExample {

	public static void main(String[] args) throws Exception{
		
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group) //클라이언트는 서버에 연결한 소켓 채널 하나만 가지고 있기 때문에 채널의 이벤트를 처리할 이벤트 루프도 하나다.
			 .channel(NioSocketChannel.class) //클라이언트 소켓 입출력 모드 설정
			 
			 //클라이언트 소켓 채널에서 발생하는 이벤트를 수신하여 처리
			 .handler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ChannelPipeline p = ch.pipeline();
					p.addLast(new EchoClientHandler());
				} 
			});
			/**
			 * 비동기 입출력 메서드인 connect 메서드는 메서드의 호출결과로 ChannelFuture를 돌려준다. 
			 * ChannelFuture를 통해 비동기 메서드의 처리결과를 알 수 있다.
			 * ChannelFuture의 sync 메서드는 ChannelFuture의 요청이 완료될때까지 대기한다.
			 * 요청이 실패하면 예외를 던진다.
			 * connect 메서드의 처리가 완료될때까지 다음 라인으로 진행하지 않는다.
			 */
			ChannelFuture f = b.connect("localhost", 8888).sync();
			f.channel().closeFuture().sync();
		} finally {
			group.shutdownGracefully();
		}
	}

}

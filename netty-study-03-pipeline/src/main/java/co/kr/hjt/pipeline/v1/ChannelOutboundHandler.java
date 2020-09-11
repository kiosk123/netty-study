package co.kr.hjt.pipeline.v1;

import java.net.SocketAddress;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

/**
 * 인바운드 이벤트는 소켓 채널에 발생한 이벤트 중에서 네티 사용자(프로그래머)가 요청한 동작에 해당하는 이벤트를 말하며 연결 요청, 데이터 전송, 소켓 닫기등이 해당한다.
 * @author USER
 *
 */
public class ChannelOutboundHandler extends ChannelOutboundHandlerAdapter {

	@Override
	public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise) throws Exception {
		/**
		 * 서버 소켓 채널이 클라이언트의 연결을 대기하는 IP와 포트가 설정되었을 때 발생
		 * 서버 소켓이 사용중인 SocketAddress를 통해 서버 소켓 채널이 사용하는 IP와 포트 정보를 확인할 수 있다.
		 */
	}

	@Override
	public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress,
			ChannelPromise promise) throws Exception {
		/**
		 * 클라이언트 소켓 채널이 서버에 연결되었을 때 발생한다.
		 * remoteAddress - 원격지 주소 정보
		 * localAddress - 로컬 주소 정보(원격지의 연결 생성시 로컬 SocketAddress를 입력하지 않았다면 null)
		 */
		
	}

	@Override
	public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
		/**
		 * 클라이언트 소켓 채널이 끊어 졌을 때 발생
		 */
	}

	@Override
	public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
		/**
		 * 클라이언트 소켓 채널의 연결이 닫혔을때 발생
		 */
	}

	@Override
	public void deregister(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
		
	}

	@Override
	public void read(ChannelHandlerContext ctx) throws Exception {
		
	}

	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		/**
		 * 소켓 채널에 데이터가 기록되었을 때 발생
		 */
	}

	@Override
	public void flush(ChannelHandlerContext ctx) throws Exception {
		/**
		 * 소켓 채널에 대한 flush 메서드가 호출되었을 때 발생
		 */
	}
}

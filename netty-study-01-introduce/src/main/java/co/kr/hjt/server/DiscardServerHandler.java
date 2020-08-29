package co.kr.hjt.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 클라이언트로 부터 받은 데이터를 처리
 * @author USER
 *
 */
public class DiscardServerHandler extends SimpleChannelInboundHandler<Object>{

	/**
	 * 클라이언트로 부터 데이터 수신시 호출
	 */
	@Override
	public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		//do nothing
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}

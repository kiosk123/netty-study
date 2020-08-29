package co.kr.hjt.server;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 클라이언트로 부터 받은 데이터를 처리
 * @author USER
 *
 */
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

	/**
	 * 클라이언트로 부터 데이터 수신시 호출
	 * @param ChannelHandlerContext ctx - 채널 파이프라인에 대한 이벤트처리
	 * @param Object msg - 수신된 데이터
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		String readMessage = ((ByteBuf)msg).toString(Charset.defaultCharset());
		System.out.println("수신한 문자열 [" + readMessage + "]");
		ctx.write(msg); //서버에 연결된 클라이언트 채널로 입력받은 데이터를 전송
	}

	/**
	 * channelRead 이벤트 처리가 완료된 후 자동으로 수행되는 이벤트 메서드
	 */
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush(); //채널 파이프라인에 저장된 버퍼를 전송
	}
}

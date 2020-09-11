package co.kr.hjt.pipeline.v2;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ChannelInboundSameEventHandlerV1 extends ChannelInboundHandlerAdapter {

	/**
	 * 데이터 수신시 호출 - 채널에 읽을 데이터가 있을 때 발생
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		String readMessage = ((ByteBuf)msg).toString(Charset.defaultCharset());
		System.out.println("수신한 문자열 [" + readMessage + "]");
		ctx.write(msg);
		
		/**
		 * 여러 이벤트 핸들러에서 같은 이벤트를 구현한 경우 먼저 등록된 이벤트 핸들러의 구현부에서 처리하면 
		 * 이벤트가 사라진다. 만약 두번째 핸들러에서도 같은 이벤트를 처리하고 싶으면 다음과 같이 메소드를 호출하면
		 * 채널파이프라인에서 이벤트가 사라지지 않고 발생하게된다.
		 * 다음은 채널파이프라인에서 channelRead이벤트를 다음 핸들러도 처리할 수 있게끔 발생시키는 코드다
		 */
		ctx.fireChannelRead(msg);
	}

	/**
	 * 데이터 수신이 완료되었음 - 채널의 데이터를 다 읽어서 더이상 데이터가 없을때 발생
	 */
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush(); //채널 파이프라인에 저장된 버퍼를 전송
	}
	

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}

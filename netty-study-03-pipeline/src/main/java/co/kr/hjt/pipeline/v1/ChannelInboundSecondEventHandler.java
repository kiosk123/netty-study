package co.kr.hjt.pipeline.v1;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * ChannelInbound 이벤트는 소켓 채널에 발생한 이벤트 중에서 연결 상대방이 어떤 동작을 취했을 때 발생
 * @author USER
 *
 */
public class ChannelInboundSecondEventHandler extends ChannelInboundHandlerAdapter {

	/**
	 * 데이터 수신시 호출 - 채널에 읽을 데이터가 있을 때 발생
	 */
//	@Override
//	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//		String readMessage = ((ByteBuf)msg).toString(Charset.defaultCharset());
//		System.out.println("수신한 문자열 [" + readMessage + "]");
//		ctx.write(msg); //서버에 연결된 클라이언트 채널로 입력받은 데이터를 전송
//		//ctx.writeAndFlush(msg); //데이터를 받자 마자 상대방에게 전송하게 됨
//	}

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

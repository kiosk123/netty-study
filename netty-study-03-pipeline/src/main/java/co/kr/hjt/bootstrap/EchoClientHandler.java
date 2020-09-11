package co.kr.hjt.bootstrap;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class EchoClientHandler extends ChannelInboundHandlerAdapter{

	/**
	 * 소켓채널이 최초 활성화되었을때 실행된다.
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		String sendMessage = "Hello, Netty!";
		ByteBuf messageBuffer = Unpooled.buffer();
		messageBuffer.writeBytes(sendMessage.getBytes());
		
		System.out.println("전송한 문자열 [" + sendMessage + "]");
		/**
		 * 내부적으로 데이터를 기록하는 write메서드와 채널에 기록된 데이터를 서버로 전송하는 flush메서드를 호출한다.
		 */
		ctx.writeAndFlush(messageBuffer);
	}

	/**
	 * 서버에서 수신된 데이터가 있을 때 호출된다.
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		String readMessage = ((ByteBuf)msg).toString(Charset.defaultCharset());
		System.out.println("수신한 문자열 [" + readMessage + "]");
	}

	/**
	 * 수신된 데이터를 모드 읽었을 때 호출된다.
	 * channelRead 메서드의 수행이 완료되고 나서 자동으로 호출된다.
	 */
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		/**
		 * 수신된 데이터를 모두 읽은 후 서버와 연결된 채널을 닫는다
		 * 이후 데이터 송수신 채널은 닫히게 되고 클라이언트 프로그램은 종료된다.
		 */
		ctx.close(); 
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}

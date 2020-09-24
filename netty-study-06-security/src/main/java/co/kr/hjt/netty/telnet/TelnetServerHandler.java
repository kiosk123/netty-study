package co.kr.hjt.netty.telnet;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.net.InetAddress;
import java.util.Date;
/**
 * @Sharable 네티가 제공하는 공유가능 어노테이션이다.
 * 이 어노테이션을 붙이면 채널 파이프라인에서 공유할 수 있다는 의미다.
 * 즉 다중스레드에서 스레드 경합없이 참조가 가능하다.
 * StringDecoder와 StringEncoder가 대표적으로 이 어노테이션이 할당되었다.
 * @Sharable 클래스 목록은 ChannelHandler.Sharable API를 참조하자
 */
@Sharable
public class TelnetServerHandler extends SimpleChannelInboundHandler<String> {
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// Send greeting for a new connection.
		ctx.write("환영합니다. "
					+ InetAddress.getLocalHost().getHostName() + "에 접속하셨습니다!\r\n");
		ctx.write("현재 시간은 " + new Date() + " 입니다.\r\n");
		ctx.flush();
	}

	@Override
	public void channelRead0(ChannelHandlerContext ctx, String request)
			throws Exception {
		String response;
		boolean close = false;

		if (request.isEmpty()) { //수신된 문자가 공백일 경우
			response = "명령을 입력해 주세요.\r\n";
		}
		else if ("bye".equals(request.toLowerCase())) {
			response = "좋은 하루 되세요!\r\n";
			close = true;
		}
		else {
			response = "입력하신 명령이 '" + request + "' 입니까?\r\n";
		}

		ChannelFuture future = ctx.write(response);

		if (close) { //종료 플래그일 경우 비동기로 채널을 닫는다.
			future.addListener(ChannelFutureListener.CLOSE);
		}
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}
}

package co.kr.hjt.eventloop.asyncio.future;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class EchoServerDefaultChannelListenerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 수신된 데이터를 클라이언트 소켓 버퍼에 기록하고 버퍼의 데이터를 채널로 전송하는 비동기 메소드를 호출 후 ChannelFuture를 반환
        ChannelFuture channelFuture = ctx.writeAndFlush(msg);

        // ChannelFuture에 채널을 종료하는 리스너를 등록한다. ChannelFuture객체가 완료이벤트를 수신할 때 수행한다.
        channelFuture.addListener(ChannelFutureListener.CLOSE);

        /**
         * ChannelFutureListener.CLOSE ChannelFuture 객체가 작업 완료 이벤트를 수신했을 때 ChannelFuture
         * 객체에 포함된 채널을 닫는다. 작업 성공 여부와 상관없이 수행된다.
         * 
         * ChannelFutureListener.CLOSE_ON_FAILURE ChannelFuture 객체가 완료 이벤트를 수신하고 결과가
         * 실패일때 ChannelFuture 객체에 포함된 채널을 닫는다.
         * 
         * ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE ChannelFuture 객체가 완료 이벤트를
         * 수신하고 결과가 실패일 때 채널 예외 이벤트를 발생시킨다.
         */
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}

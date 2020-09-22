package co.kr.hjt.eventloop.asyncio.future;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 클라인언트 소켓 채널에 데이터 기록이 완료되었을 때 기록 완료 메시지와
 * 기록된 메시지의 크기를 출력하고 소켓 채널을 닫는다
 *
 */
@Sharable
public class EchoServerHandlerWithFuture extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        //비동기 IO지원을 위한 ChannelFuture 비동기 IO메서드의 호출결과로 ChannelFuture 객체를 돌려받고 이것을 통해
        //작업 완료 유무를 확인할 수 있다.
        //ChannelFuture에 객체에 작업이 완료되었을 때 수행할 채널 리스너를 설정할 수 있다
        ChannelFuture channelFuture = ctx.writeAndFlush(msg);

        //네티가 수신한 msg객체는 ByteBuf객체
        final int writeMessageSize = ((ByteBuf) msg).readableBytes();

        //ChannelFutureListener 인터페이스를 구현한 클래스를 작성하여 ChannelFuture에 등록하면
        //네트가 제공하는 채널 리스너보다 복잡한 작업을 처리할 수 있다.
        channelFuture.addListener(new ChannelFutureListener() {
            //ChannelFuture에서 발생하는 작업 완료 이벤트 메서드로 사용자 정의 채널 리스너 구현에 포함
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                System.out.println("전송한 Byte : " + writeMessageSize);
                //ChannelFuture 객체에 포함된 채널을 가져와서 채널 닫기 이벤트를 발생시킨다.
                future.channel().close();
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}

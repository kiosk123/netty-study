package co.kr.hjt.pipeline.v1;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 인바운드 이벤트는 연결상대방이 어떤 동작을 취했을 때 발생한다. 채널활성화, 데이터 수신등의 이벤트가 이에 해당한다.
 * 
 * @author USER
 *
 */
public class ChannelInoundHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        /**
         * 채널이 이벤트 루프에 등록되었을 때 발생
         */
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        /**
         * 채널이 이벤트 루프에서 제거되었을 때 발생 이 이벤트 발생 이후에는 채널에서 발생한 이벤트를 처리할 수 없다
         */
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        /**
         * channelRegistered 이후에 발생 채널이 생성되고 이벤트 루프에 등록된 이후에 네트 API를 사용하여 채널 입출력을 수행할
         * 상태가 되었을을 알려준다.
         * 
         * 다음과 같은 경우에 활용가능하다 서버 애플리케이션에 연결된 클라이언트의 개수 확인 서버 애플리케이션에 연결된 클라이언트에게 최초 연결에
         * 대한 메시지를 전송할대 클라이언트 애플리케이션이 연결된 서버에 최초메시지를 전달할때 클라이언트 애플리케이션에서 서버에 연결된 상태에 대한
         * 작업이 필요할때
         */
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        /**
         * 채널이 비활성화 되었을 때 발생 이 이벤트가 발생한 이후에는 채널에 대한 입출력 작업을 수행할 수 없다.
         */
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        /**
         * 데이터가 수신되었을 때 발생
         */
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        /**
         * 채널의 데이터를 다 읽어서 더이상 데이터가 없을때 발생
         */
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

    }

}

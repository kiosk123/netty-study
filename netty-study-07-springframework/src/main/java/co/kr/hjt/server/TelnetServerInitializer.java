package co.kr.hjt.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class TelnetServerInitializer extends ChannelInitializer<SocketChannel> {
   /**
    * 새로 연결되는 클라이언트 채널들이 동일한 인코더 디코더 객체를 공유한다.
    */
   private static final StringDecoder DECODER = new StringDecoder();
   private static final StringEncoder ENCODER = new StringEncoder();

   private static final TelnetServerHandler SERVER_HANDLER = new TelnetServerHandler();

   @Override
   public void initChannel(SocketChannel ch) throws Exception {
      ChannelPipeline pipeline = ch.pipeline();

      /**
       * 네티에서 기본적으로 제공하는 기본 디코더로 구분자 기반의 패킷을 처리한다.
       */
      pipeline.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
      pipeline.addLast(DECODER);
      pipeline.addLast(ENCODER);
      pipeline.addLast(SERVER_HANDLER);
   }
}

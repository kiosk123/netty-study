package co.kr.hjt.pipeline.codec;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslContext;

public class HttpHelloWorldServerInitializer extends ChannelInitializer<SocketChannel> {

	private final SslContext sslCtx;
	
	public HttpHelloWorldServerInitializer(SslContext sslCtx) {
		this.sslCtx = sslCtx;
	}
	
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline p = ch.pipeline();
		if (sslCtx != null) {
			p.addLast(sslCtx.newHandler(ch.alloc()));
		}
		/**
		 * HttpServerCodec은 인바운드와 아웃바운드 핸들러를 모두 구현한다.
		 * HttpServerCodec의 생성자에서 HttpRequestDecoder와 HttpResponseEncoder를 모두 생성
		 */
		//핸들러 등록 순서 중요!!!!
		p.addLast(new HttpServerCodec());
		p.addLast(new HttpHelloWorldServerHandler());
	}
}

package co.kr.hjt.pipeline.codec;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

/**
 * 기본 제공 codec 종류
 * 예제 : https://github.com/netty/netty/tree/4.1/example/src/main/java/io/netty/example
 * 패키지 : io.netty.handler.codec
 * base64, bytes, compression(zlib,gzip,snappy등), http(sub: cors, multipart, websocketx등)
 * marshalling, protobuf, sctp, spdy, string, serialization(자바 객체의 네트워크 전송)
 * mqtt, haproxy, stomp
 *
 */

/**
 * HTTP 서버
 * @author USER
 *
 */
public class HttpHelloWorldServer {
	static final boolean SSL = System.getProperty("ssl") != null;
	static final int PORT = Integer.parseInt(System.getProperty("port", SSL? "8443" : "8080"));
	
	public static void main(String[] args) throws Exception {
		//SSL 구성
		final SslContext sslCtx;
		if (SSL) {
			SelfSignedCertificate ssc = new SelfSignedCertificate();
			sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
		} else {
			sslCtx = null;
		}
		
		//서버 구성
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.option(ChannelOption.SO_BACKLOG, 1024);
			b.group(bossGroup, workerGroup)
			.channel(NioServerSocketChannel.class)
			.handler(new LoggingHandler(LogLevel.INFO))
			
			//채널 파이프라인 설정
			.childHandler(new HttpHelloWorldServerInitializer(sslCtx));
			
			Channel ch = b.bind(PORT).sync().channel();
			System.err.println("Open your web browser and navigate to"
					+ (SSL? "https" : "http") + "://127.0.0.1:" + PORT +"/");
			ch.closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
}

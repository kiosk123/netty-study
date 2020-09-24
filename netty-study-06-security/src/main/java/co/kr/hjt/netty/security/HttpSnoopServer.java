package co.kr.hjt.netty.security;

import java.io.File;

import javax.net.ssl.SSLException;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;

/**
 * An HTTP server that sends back the content of the received HTTP request
 * in a pretty plaintext form.
 */
public final class HttpSnoopServer {

    /**
     * SSL/TLS 기본포트는 443 여기서는 8443으로 사용함
     */
    static final int PORT = 8443;
    public static void main(String[] args) throws Exception {
        // Configure SSL.
        SslContext sslCtx = null;
        
        try {
            // 인증서 파일 지정
            File certChainFile = new File("netty.crt");
            
            // 개인키 파일 지정
            File keyFile = new File("privatekey.pem");
            keyFile.exists();
            /**
             * 마지막 인수는 개인키 생성할 때 입력한 비밀번호
             */
            sslCtx = SslContextBuilder.forServer(certChainFile, keyFile, "123123").build();
        } catch (SSLException e) {
            e.printStackTrace();
            System.out.println("Can not create SSL context! \n Server will be stop!");
        }
      

        // Configure the server.
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class)
             .handler(new LoggingHandler(LogLevel.INFO))
             .childHandler(new HttpSnoopServerInitializer(sslCtx));

            Channel ch = b.bind(PORT).sync().channel();

            System.err.println("Open your web browser and navigate to " +
                    "https://127.0.0.1:" + PORT + '/');

            ch.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
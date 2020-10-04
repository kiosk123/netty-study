package co.kr.hjt.api;

import java.net.InetSocketAddress;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLException;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
/**
 * 하나의 어플리케이션 프로세스에서 두개의 서비스 포트를 생성
 * @author USER
 *
 */
public final class ApiServerDoublePort {
    private InetSocketAddress address;
    private int workerThreadCount;
    private int bossThreadCount;

    public void start() {
        EventLoopGroup bossGroup = new NioEventLoopGroup(bossThreadCount);
        EventLoopGroup workerGroup = new NioEventLoopGroup(workerThreadCount);
        ChannelFuture channelFuture = null;

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO)).childHandler(new ApiServerInitializer(null));

            Channel ch = b.bind(8080).sync().channel();

            channelFuture = ch.closeFuture();
            //channelFuture.sync();
            
            final SslContext sslCtx;
            SelfSignedCertificate ssc = new SelfSignedCertificate();
            sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
            
            ServerBootstrap b2 = new ServerBootstrap();
            b2.group(bossGroup, workerGroup)
              .channel(NioServerSocketChannel.class)
              .handler(new LoggingHandler(LogLevel.INFO))
              .childHandler(new ApiServerInitializer(sslCtx));
            
            Channel ch2 = b2.bind(8443).sync().channel();
            channelFuture = ch.closeFuture();
            channelFuture.sync();
        }
        catch (InterruptedException | SSLException | CertificateException e) {
            e.printStackTrace();
        }
        finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
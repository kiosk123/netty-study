package co.kr.hjt.server;

import static org.junit.Assert.assertEquals;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.base64.Base64Encoder;

import java.nio.charset.Charset;

import org.junit.Test;

public class Base64EncoderTest {
    @Test
    public void testEncoder() {
        String writeData = "안녕하세요";
        ByteBuf request = Unpooled.wrappedBuffer(writeData.getBytes());

        Base64Encoder encoder = new Base64Encoder();
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(encoder);

        //채널에 데이터를 기록한다. -> 클라이언트로 송신
        embeddedChannel.writeOutbound(request);
        
        //클라이언트로 전솔될 데이터가 제대로 인코딩 되었는지 확인한다.
        ByteBuf response = (ByteBuf) embeddedChannel.readOutbound();

        String expect = "7JWI64WV7ZWY7IS47JqU";
        assertEquals(expect, response.toString(Charset.defaultCharset()));

        embeddedChannel.finish();
    }
}

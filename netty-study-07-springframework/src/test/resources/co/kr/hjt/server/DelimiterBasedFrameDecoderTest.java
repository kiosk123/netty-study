package co.kr.hjt.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;

import java.nio.charset.Charset;

import org.junit.Test;

public class DelimiterBasedFrameDecoderTest {
    @Test
    public void testDecoder() {
        String writeData = "안녕하세요\r\n반갑습니다\r\n";
        String firstResponse = "안녕하세요\r\n";
        String secondResponse = "반갑습니다\r\n";

        //최대 8192 바이트의 데이터를 줄바꿈 문자를 기준으로 잘라서 디코딩하는 디코더 생성
        DelimiterBasedFrameDecoder decoder = new DelimiterBasedFrameDecoder(8192, 
                false, Delimiters.lineDelimiter());
        
        //이벤트 핸들러를 테스트할 수 있는 채널 구현체에 디코더 등록
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(decoder);

        ByteBuf request = Unpooled.wrappedBuffer(writeData.getBytes());
        
        //클라이언트로 부터 데이터를 수신한 것과 같음 - 채널에 데이터 기록
        boolean result = embeddedChannel.writeInbound(request);
        assertTrue(result); //수행 결과가 정상인지 확인

        ByteBuf response = null;

        //채널에 기록된 데이터를 읽음
        response = (ByteBuf) embeddedChannel.readInbound();
        assertEquals(firstResponse, response.toString(Charset.defaultCharset()));

        response = (ByteBuf) embeddedChannel.readInbound();
        assertEquals(secondResponse, response.toString(Charset.defaultCharset()));

        //피니시 호출 후 EmbeddedChannel에 어떠한 것도 기록할 수 없음
        embeddedChannel.finish();
    }
}

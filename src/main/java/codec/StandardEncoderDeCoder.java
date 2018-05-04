package codec;

import com.hantong.code.ErrorCode;
import com.hantong.message.RequestMessage;
import com.hantong.message.RuntimeMessage;
import com.hantong.result.Result;
import com.hantong.util.Json;

public class StandardEncoderDeCoder extends EncoderDecoder {
    @Override
    public RequestMessage decode(byte[] data) {
        String content = new String(data);
        return  Json.getInstance().fromString(content,RequestMessage.class);
    }

    @Override
    public byte[] encode(RequestMessage requestMessage, RuntimeMessage runtimeMessage) {
        Result result = new Result(runtimeMessage.getResult());
        String resultJson = Json.getInstance().toString(result);
        return resultJson.getBytes();
    }
}

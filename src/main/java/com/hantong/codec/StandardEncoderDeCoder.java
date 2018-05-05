package com.hantong.codec;

import com.hantong.inbound.processor.InboundProcessor;
import com.hantong.message.RequestMessage;
import com.hantong.message.RuntimeMessage;
import com.hantong.result.Result;
import com.hantong.util.Json;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

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

    @Override
    public Map<String, Map<String, String>> getMonitorData() {
        Map<String, Map<String, String>> monitor = new LinkedHashMap<>();
        Map<String, String> thisMonitor = new LinkedHashMap<>();
        thisMonitor.put("--","--");
        monitor.put("StandardEncoderDeCoder",thisMonitor);

        return monitor;
    }
}

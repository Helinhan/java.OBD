package com.hantong.codec;

import com.fasterxml.jackson.core.type.TypeReference;
import com.hantong.code.ErrorCode;
import com.hantong.exception.ErrorCodeException;
import com.hantong.interfaces.ICodec;
import com.hantong.model.ServerConfig;
import com.hantong.model.ServiceConfigField;
import com.hantong.service.Service;
import com.hantong.util.Json;

import java.util.ArrayList;
import java.util.List;

public abstract class EncoderDecoder implements ICodec {
    public static final String Codec_StandardEncoderDeCoder = "StandardEncoderDeCoder";
    public static List<ServiceConfigField> getConfigField() {
        String config = "[" +
                "    {" +
                "      \"name\": \""+Codec_StandardEncoderDeCoder+"\"," +
                "      \"title\": \"标准JSON编解码\"," +
                "      \"description\": \"标准JSON编解码,对于解析输入的标准JSON字符串格式\"" +
                "    }" +
                "  ]";

        try {
            return Json.getInstance().getObjectMapper().readValue(config, new TypeReference<List<ServiceConfigField>>() {
            });
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ErrorCode build(Service s, ServerConfig c) throws ErrorCodeException {
        if (c.getCodec().equals(EncoderDecoder.Codec_StandardEncoderDeCoder)) {
            s.setEncoderDecoder(new StandardEncoderDeCoder());
            return ErrorCode.Success;
        }

        throw new ErrorCodeException(ErrorCode.ServiceStartErr);
    }
}

package tyut.selab.a2.pipes.configure.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import tyut.selab.a2.pipes.entity.Result;
import tyut.selab.a2.pipes.exception.IllegalReturnClassTypeException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPOutputStream;


@RestControllerAdvice
@Slf4j
public class ControllerReturnValueAdvice implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return !returnType.getParameterType().isAssignableFrom(Result.class);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body instanceof String) {
            throw new IllegalReturnClassTypeException("不得返回String类型");
        }
        return  Result.success(body);
    }


    public String objectToJsonString(Object o) {
        Object o1 = com.alibaba.fastjson2.JSON.toJSON(o);
        return o1.toString();
    }

    public byte[] gzipObject(Object o) {
        //二进制结果数组
        byte[] bytes;
        //序列化使用的输出流
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream);
            //建立对象序列化输出流
            gzipOutputStream.write(objectToJsonString(o).getBytes(StandardCharsets.UTF_8));
            gzipOutputStream.flush();
            gzipOutputStream.close();
            bytes = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return bytes;
    }
}

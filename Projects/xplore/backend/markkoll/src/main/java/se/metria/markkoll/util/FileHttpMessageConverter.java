package se.metria.markkoll.util;

import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.File;
import java.io.IOException;

public class FileHttpMessageConverter extends AbstractHttpMessageConverter<File> {
    @Override
    protected boolean supports(Class<?> aClass) {
        return aClass == File.class;
    }

    @Override
    protected File readInternal(Class<? extends File> aClass, HttpInputMessage httpInputMessage) throws IOException, HttpMessageNotReadableException {
        var inputStream = httpInputMessage.getBody();

        return FileUtil.saveTempFile(inputStream.readAllBytes(), "httpMessage", null);
    }

    @Override
    protected void writeInternal(File file, HttpOutputMessage httpOutputMessage) throws IOException, HttpMessageNotWritableException {
        var data = FileUtils.readFileToByteArray(file);
        var outputStream = httpOutputMessage.getBody();
        outputStream.write(data);
    }
}

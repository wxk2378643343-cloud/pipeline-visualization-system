package tyut.selab.a2.pipe.test.util;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.stream.Stream;

public class FileUtil {
    public static Stream<File> getTestFile(String dirPathRelateToClassPath) {
        URL resource = FileUtil.class.getResource(dirPathRelateToClassPath);
        String rootPath = resource.getPath();
        File dir = new File(rootPath);
        File[] files = dir.listFiles();
        return Arrays.stream(files);
    }

    public static MultipartFile getMultipartFileFromClassPath(String filePathRelateToClassPath){
        URL resource = FileUtil.class.getResource(filePathRelateToClassPath);
        File file = new File(resource.getPath());
        return convertFileToMultipartFile(file);
    }

    public static MultipartFile convertFileToMultipartFile(File file){
        FileInputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            return new MockMultipartFile(file.getName(),file.getName(),null,fileInputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

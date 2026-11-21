package tyut.selab.a2.pipe.test.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import tyut.selab.a2.pipes.App;
import tyut.selab.a2.pipes.service.IExcelService;

import java.io.*;
import java.net.URL;
import java.util.Arrays;
import java.util.stream.Stream;

@SpringBootTest(classes = App.class)
public class ExcelServiceImpl {
    @Autowired
    private IExcelService service;

    @Test
    public void shouldCheckExcel() {
        getTestFile().map(file -> {
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
        }).forEach(mockMultipartFile -> {
            try {
                System.err.println("---------------------------"+mockMultipartFile.getOriginalFilename()+"--------------------------------");
                service.beforeExcelResolve(mockMultipartFile);
                service.resolveExcel(mockMultipartFile);
                System.err.println("测试数据 ["+mockMultipartFile.getOriginalFilename()+"] 未抛出异常");
            }catch (Exception e){
                System.err.println("在测试数据 ["+mockMultipartFile.getOriginalFilename()+"] 时，程序抛出了一个异常，请检查是否应该抛出此异常");
                e.printStackTrace();
            }finally {
                System.err.println("---------------------------"+mockMultipartFile.getOriginalFilename()+"--------------------------------");
            }
        });
    }

    public Stream<File> getTestFile() {
        URL resource = getClass().getResource("/data/excel");
        String rootPath = resource.getPath();
        File dir = new File(rootPath);
        File[] files = dir.listFiles();
        return Arrays.stream(files);
    }


}

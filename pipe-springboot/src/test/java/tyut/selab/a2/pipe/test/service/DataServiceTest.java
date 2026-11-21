package tyut.selab.a2.pipe.test.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import tyut.selab.a2.pipe.test.util.FileUtil;
import tyut.selab.a2.pipe.test.util.SequentUtil;
import tyut.selab.a2.pipes.App;
import tyut.selab.a2.pipes.service.IDataService;
import tyut.selab.a2.pipes.vo.DataPipeVo;
import tyut.selab.a2.pipes.vo.DataVo;
import tyut.selab.a2.pipes.vo.DataWellVo;
import tyut.selab.a2.pipes.vo.Node;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static tyut.selab.a2.pipes.utils.LonlatConverUtil.wgs84_To_Gcj02;

@SpringBootTest(classes = App.class)
public class DataServiceTest {
    @Autowired
    private IDataService service;

    @Test
    public void shouldSequential() {
        DataVo data = service.getAllPipeAndWellFromDB();
        List<DataPipeVo> pipes = data.getPipes();
        List<DataWellVo> wells = data.getWells();
        SequentUtil.requireSequential(pipes.stream().map(DataPipeVo::getId).collect(Collectors.toList()));
        SequentUtil.requireSequential(wells.stream().map(DataWellVo::getId).collect(Collectors.toList()));
    }

//    @Test
//    public void testImport(){
//        MultipartFile file = FileUtil.getMultipartFileFromClassPath("/data/realData/data2.xlsx");
//        service.importExcelDataToDB(file);
//    }

//    @Test
//    public void culData(){
//        Node node = service.calculateTree();
//        System.out.println("成功");
//    }

    @Test
    public void uploadData(){
        FileUtil.getTestFile("/data/tree").map(file -> {
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
                service.importExcelDataToDB(mockMultipartFile);
                System.err.println("测试数据 ["+mockMultipartFile.getOriginalFilename()+"] 未抛出异常");
            }catch (Exception e){
                System.err.println("在测试数据 ["+mockMultipartFile.getOriginalFilename()+"] 时，程序抛出了一个异常，请检查是否应该抛出此异常");
                e.printStackTrace();
            }finally {
                System.err.println("---------------------------"+mockMultipartFile.getOriginalFilename()+"--------------------------------");
            }
        });
    }

    @Test
    public void test0000(){
        double[] doubles = wgs84_To_Gcj02(Double.parseDouble("113.081"), Double.parseDouble("36.10483"));
        System.out.printf("====");
    }
}

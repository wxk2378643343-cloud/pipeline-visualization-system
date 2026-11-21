package tyut.selab.a2.pipes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tyut.selab.a2.pipes.service.impl.DataServiceImpl;
import tyut.selab.a2.pipes.vo.DataVo;
import tyut.selab.a2.pipes.vo.ExcelVo;
import tyut.selab.a2.pipes.vo.Node;


@RestController
@RequestMapping("/data")
public class DataController {

    @Autowired
    private DataServiceImpl dataService;

    @GetMapping("/get")
    public DataVo getAllData(){
        DataVo allPipeAndWellFromDB = dataService.getAllPipeAndWellFromDB();
        return allPipeAndWellFromDB;
    }

    @PostMapping("/import")
    public DataVo importData(MultipartFile file){
        DataVo dataVo = dataService.importExcelDataToDB(file);
        return dataVo;
    }

    @GetMapping("/tree")
    public Node tree(){
        Node node = dataService.calculateTree(null);
        return node;
    }

}

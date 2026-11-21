package tyut.selab.a2.pipes.service;

import org.springframework.web.multipart.MultipartFile;
import tyut.selab.a2.pipes.vo.DataVo;
import tyut.selab.a2.pipes.vo.ExcelVo;
import tyut.selab.a2.pipes.vo.Node;

public interface IDataService {
    
    public DataVo getAllPipeAndWellFromDB();

   
    public DataVo importExcelDataToDB(MultipartFile multipartFile);

    
    public Node calculateTree(DataVo dataVo);
}

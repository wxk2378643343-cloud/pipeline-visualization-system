package tyut.selab.a2.pipes.service;

import org.springframework.web.multipart.MultipartFile;
import tyut.selab.a2.pipes.vo.DataVo;
import tyut.selab.a2.pipes.vo.ExcelVo;

public interface IExcelService {

    /**
     * 预处理
     * @param file
     */
    public MultipartFile beforeExcelResolve(MultipartFile file);

    /**
     * 读取文件
     * @param file 文件
     * @return 解析之后的对象
     */
    public DataVo resolveExcel(MultipartFile file);
}

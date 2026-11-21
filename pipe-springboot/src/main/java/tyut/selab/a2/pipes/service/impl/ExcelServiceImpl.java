package tyut.selab.a2.pipes.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tyut.selab.a2.pipes.exception.ConvertException;
import tyut.selab.a2.pipes.exception.FileIOException;
import tyut.selab.a2.pipes.exception.FileTypeException;
import tyut.selab.a2.pipes.service.IConverterService;
import tyut.selab.a2.pipes.service.IExcelService;
import tyut.selab.a2.pipes.utils.ExcelUtil;
import tyut.selab.a2.pipes.vo.DataVo;
import tyut.selab.a2.pipes.vo.ExcelVo;

import java.io.*;

@Slf4j
@Service
public class ExcelServiceImpl implements IExcelService {

    @Autowired
    private IConverterService iConverterService;
    @Autowired
    private ExcelUtil excelUtil;

    public void ifFileNUllThanThrowException(MultipartFile file){
        if (file == null) throw new FileTypeException("File Is Null");
    }


    public InputStream transforMultipartFileToMarkedInputStream(MultipartFile file){
        try {
            return new BufferedInputStream(file.getInputStream());
        } catch (IOException e) {
            throw new ConvertException("Convert Error");
        }
    }


    public boolean fileStreamIsExcel(InputStream inputStream){
        try {
            excelUtil.isExcelFile(inputStream);
        } catch (IOException e) {
            throw new FileIOException("FileIo Error");
        }
        return true;
    }

    @Override
    public MultipartFile beforeExcelResolve(MultipartFile file) {
        ifFileNUllThanThrowException(file);

        InputStream inputStream = transforMultipartFileToMarkedInputStream(file);

        fileStreamIsExcel(inputStream);
        return file;
    }

    @Override
    public DataVo resolveExcel(MultipartFile multipartFile) {
        ExcelVo excelVo = excelUtil.readExcel(multipartFile);
        return new DataVo(iConverterService.excelPipeVoTransformationDataPipeVo(excelVo.getPipeVos()),
                iConverterService.excelWellVoTransformationDataWellVo(excelVo.getWellVos()));
    }

}

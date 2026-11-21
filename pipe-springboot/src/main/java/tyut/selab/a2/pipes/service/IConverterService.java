package tyut.selab.a2.pipes.service;

import tyut.selab.a2.pipes.entity.Pipe;
import tyut.selab.a2.pipes.entity.Well;
import tyut.selab.a2.pipes.vo.*;

import java.util.List;

public interface IConverterService {

    public List<DataPipeVo> pipeListTransformationDataPipeVoList(List<Pipe> pipes);

    public List<DataWellVo> WellListTransformationDataWellList(List<Well> wells);

    public List<Pipe> DataPipeVoListTransformationPipeList(List<DataPipeVo> pipes);

    public List<Well> DataWellVoListTransformationWellList(List<DataWellVo> wells);


    public List<Pipe> ExcelPipeVpTransformationPipe(List<ExcelPipeVo> excelPipeVos);

    public List<Well> ExcelWellVpTransformationWell(List<ExcelWellVo> excelWellVos);

    public List<DataPipeVo> excelPipeVoTransformationDataPipeVo(List<ExcelPipeVo> excelPipeVos);

    public List<DataWellVo> excelWellVoTransformationDataWellVo(List<ExcelWellVo> excelWellVos);


}

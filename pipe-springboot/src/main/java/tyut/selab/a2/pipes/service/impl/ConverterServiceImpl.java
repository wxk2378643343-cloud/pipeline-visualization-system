package tyut.selab.a2.pipes.service.impl;

import org.springframework.stereotype.Service;
import tyut.selab.a2.pipes.entity.Pipe;
import tyut.selab.a2.pipes.entity.Well;
import tyut.selab.a2.pipes.exception.CellTypeException;
import tyut.selab.a2.pipes.exception.ConvertException;
import tyut.selab.a2.pipes.service.IConverterService;
import tyut.selab.a2.pipes.vo.DataPipeVo;
import tyut.selab.a2.pipes.vo.DataWellVo;
import tyut.selab.a2.pipes.vo.ExcelPipeVo;
import tyut.selab.a2.pipes.vo.ExcelWellVo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ConverterServiceImpl implements IConverterService {

    public  void judgeListIsNull(List objects){
        if (objects == null)
            throw new ConvertException("List Is Null");
    }

    //做转换，防止伪泛型
    public  List<Long> listIntToListLong(List list){
        List<Long> objects = new ArrayList<>();
        list.stream().forEach(e->{
            objects.add(Long.parseLong(e.toString()));
        });
        return objects;
    }

    public  DataPipeVo pipeToDataPipeVo(Pipe pipe){
        return new DataPipeVo(pipe.getId(),pipe.getType(),listIntToListLong(pipe.getWellsId()),
                pipe.getMaterial(),pipe.getDiameter(),pipe.getDeep());
    }

    public  List<DataPipeVo> pipeListToDataPipeVoList(List<Pipe> pipes){
        List<DataPipeVo> pipeVos = new ArrayList<>();
        pipes.stream().forEach(e->{
            pipeVos.add(pipeToDataPipeVo(e));
        });
        return pipeVos;
    }

    @Override
    public List<DataPipeVo> pipeListTransformationDataPipeVoList(List<Pipe> pipes) {
        judgeListIsNull(pipes);
        return pipeListToDataPipeVoList(pipes);
    }



    public  DataWellVo wellToDataWellVo(Well well){
        return new DataWellVo(well.getId(),well.getLongitude(),well.getLatitude(),
                well.getType(),well.getModel());
    }

    public List<DataWellVo> WellToWellVo(List<Well> wells){
        List<DataWellVo> list = new ArrayList<>();
        wells.forEach(well -> {
            list.add(wellToDataWellVo(well));
        });
        return list;
    }
    @Override
    public List<DataWellVo> WellListTransformationDataWellList(List<Well> wells) {
        judgeListIsNull(wells);
        return WellToWellVo(wells);
    }

    @Override
    public List<Pipe> DataPipeVoListTransformationPipeList(List<DataPipeVo> pipes) {
        judgeListIsNull(pipes);
        List<Pipe> pipeList = new ArrayList<>();
        for (int i = 0; i < pipes.size(); i++) {
            pipeList.add(new Pipe(pipes.get(i).getId(),pipes.get(i).getType(),pipes.get(i).getWellsId(),pipes.get(i).getMaterial(),pipes.get(i).getDiameter(),
                    pipes.get(i).getDeep()));
        }
        return pipeList;
    }

    @Override
    public List<Well> DataWellVoListTransformationWellList(List<DataWellVo> wells) {
        judgeListIsNull(wells);
        List<Well> wells1 = new ArrayList<>();
        for (int i = 0; i < wells.size(); i++) {
            wells1.add(new Well(wells.get(i).getId(),wells.get(i).getLongitude(),wells.get(i).getLatitude(),wells.get(i).getType(),wells.get(i).getModel()));
        }
        return wells1;
    }

    public  Pipe excelPipeVoToPipe(ExcelPipeVo p){
        return new Pipe(null,p.getPipeType(),listIntToListLong(p.getWellsId()),null,null,p.getDeep());
    }

    public  void removeListNull(List list){
        List newList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) != null){
                newList.add(list.get(i));
            }
        }
    }

    public  List<Pipe> excelPipeVoListToPipeList(List<ExcelPipeVo> excelPipeVos) {
        List<Pipe> pipeList = new ArrayList<>();
        excelPipeVos.stream().forEach(e->{
            pipeList.add(excelPipeVoToPipe(e));
        });
        return pipeList;
    }

    @Override
    public List<Pipe> ExcelPipeVpTransformationPipe(List<ExcelPipeVo> excelPipeVos) {

        excelPipeVoListToPipeList(excelPipeVos);
        removeListNull(excelPipeVos);
        return excelPipeVoListToPipeList(excelPipeVos);
    }

    public  Well excelWellVoToWell(ExcelWellVo e){
        return new Well(null,e.getLongitude(),e.getLatitude(),e.getWellType(),e.getModel());
    }

    public  List<Well> excelWellVoListToWellList(List<ExcelWellVo> excelWellVos){
        List<Well> wells = new ArrayList<>();
        excelWellVos.stream().forEach(e->{
            wells.add(excelWellVoToWell(e));
        });
        return wells;
    }
    @Override
    public List<Well> ExcelWellVpTransformationWell(List<ExcelWellVo> excelWellVos) {
        judgeListIsNull(excelWellVos);
        removeListNull(excelWellVos);
        return excelWellVoListToWellList(excelWellVos);
    }

    @Override
    public List<DataPipeVo> excelPipeVoTransformationDataPipeVo(List<ExcelPipeVo> excelPipeVos) {
        judgeListIsNull(excelPipeVos);
        return excelPipeVoListToDataPipeVoList(excelPipeVos);
    }

    @Override
    public List<DataWellVo> excelWellVoTransformationDataWellVo(List<ExcelWellVo> excelWellVos) {
        return excelWellListToDataWellVoList(excelWellVos);
    }

    public DataPipeVo excelPipeVoToDataPipeVo(ExcelPipeVo excelPipeVo){
        return new DataPipeVo(null,excelPipeVo.getPipeType(),excelPipeVo.getWellsId(),null,null,excelPipeVo.getDeep());
    }

    public List<DataPipeVo> excelPipeVoListToDataPipeVoList(List<ExcelPipeVo> excelPipeVos){
        List<DataPipeVo> dataPipeVos = new ArrayList<>();
        excelPipeVos.stream().forEach(e->{
            dataPipeVos.add(excelPipeVoToDataPipeVo(e));
        });
        return dataPipeVos;
    }

    public DataWellVo excelWellVoToDataWellVo(ExcelWellVo excelWellVo){
        return new DataWellVo(null,excelWellVo.getLongitude(),excelWellVo.getLatitude(),excelWellVo.getWellType(),excelWellVo.getModel());
    }

    public List<DataWellVo> excelWellListToDataWellVoList(List<ExcelWellVo> excelWellVos){
        List<DataWellVo> wellVos = new ArrayList<>();
        excelWellVos.stream().forEach(e->{
            wellVos.add(excelWellVoToDataWellVo(e));
        });
        return wellVos;
    }

}

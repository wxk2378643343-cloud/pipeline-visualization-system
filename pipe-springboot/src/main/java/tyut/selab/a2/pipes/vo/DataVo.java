package tyut.selab.a2.pipes.vo;

import tyut.selab.a2.pipes.entity.Pipe;
import tyut.selab.a2.pipes.entity.Well;

import java.util.List;

@lombok.Data
public class DataVo {

    private List<DataPipeVo> pipes;

    private List<DataWellVo> wells;

    public DataVo(List<DataPipeVo> pipes, List<DataWellVo> wells) {
        this.pipes = pipes;
        this.wells = wells;
    }

    public DataVo() {
    }
}

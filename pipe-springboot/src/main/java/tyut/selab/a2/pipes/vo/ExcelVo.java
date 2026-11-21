package tyut.selab.a2.pipes.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ExcelVo {

    List<ExcelWellVo> wellVos;

    List<ExcelPipeVo> pipeVos;

}

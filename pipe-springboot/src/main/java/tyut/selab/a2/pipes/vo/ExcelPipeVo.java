package tyut.selab.a2.pipes.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ExcelPipeVo {
    //相连的井编号
    private List<Long> wellsId;
    // 管道类型
    private String pipeType;
    // 深度
    private BigDecimal deep;
}

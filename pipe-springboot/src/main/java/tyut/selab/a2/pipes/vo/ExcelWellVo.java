package tyut.selab.a2.pipes.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExcelWellVo {
    // 井类型
    private String wellType;
    // 经度
    private BigDecimal longitude;
    // 纬度
    private BigDecimal latitude;
    // 井型号
    private String model;

}

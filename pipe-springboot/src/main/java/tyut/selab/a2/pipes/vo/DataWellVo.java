package tyut.selab.a2.pipes.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DataWellVo {
    protected Long id;
    // 经度
    private BigDecimal longitude;
    // 纬度
    private BigDecimal latitude;
    // 井类型
    private String type;
    // 井型号
    private String model;

    public DataWellVo(Long id, BigDecimal longitude, BigDecimal latitude, String type, String model) {
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.type = type;
        this.model = model;
    }

    public DataWellVo() {
    }
}

package tyut.selab.a2.pipes.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class DataPipeVo {

    protected Long id;
    // 管道种类
    private String type;
    // 井口id
    private List<Long> wellsId;
    // 材质
    private String material;
    // 直径
    private BigDecimal diameter;
    // 深度
    private BigDecimal deep;

    public DataPipeVo(Long id, String type, List<Long> wellsId, String material, BigDecimal diameter, BigDecimal deep) {
        this.id = id;
        this.type = type;
        this.wellsId = wellsId;
        this.material = material;
        this.diameter = diameter;
        this.deep = deep;
    }

    public DataPipeVo() {
    }
}
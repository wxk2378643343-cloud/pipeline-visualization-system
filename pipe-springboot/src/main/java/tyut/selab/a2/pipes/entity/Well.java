package tyut.selab.a2.pipes.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("well")
public class Well {
    @TableId(type = IdType.AUTO)
    protected Long id;
    // 经度
    private BigDecimal longitude;
    // 纬度
    private BigDecimal latitude;
    // 井类型
    private String type;
    // 井型号
    private String model;

    public Well() {

    }

    public Well(Long id, BigDecimal longitude, BigDecimal latitude, String type, String model) {
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.type = type;
        this.model = model;
    }
}

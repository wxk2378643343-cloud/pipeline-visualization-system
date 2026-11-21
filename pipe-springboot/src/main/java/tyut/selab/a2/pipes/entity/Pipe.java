package tyut.selab.a2.pipes.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

@Data
@Accessors(chain = true)
@TableName(value = "pipe", autoResultMap = true)
public class Pipe {

    @TableId(type = IdType.AUTO)
    protected Long id;
    // 管道种类
    private String type;
    // 井口id
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Long> wellsId;
    // 材质
    private String material;
    // 直径
    private BigDecimal diameter;
    // 深度
    private BigDecimal deep;

    public Pipe(Long id, String type, List<Long> wellsId, String material, BigDecimal diameter, BigDecimal deep) {
        this.id = id;
        this.type = type;
        this.wellsId = wellsId;
        this.material = material;
        this.diameter = diameter;
        this.deep = deep;
    }

    public Pipe() {
    }
}

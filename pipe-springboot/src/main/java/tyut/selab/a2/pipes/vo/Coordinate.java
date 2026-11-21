package tyut.selab.a2.pipes.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 存入经度、纬度
 */
@Data
public class Coordinate {

    @JsonIgnore
    Long wellId;
    BigDecimal longitude;
    BigDecimal latitude;

    public Coordinate(Long wellId,BigDecimal longitude, BigDecimal latitude) {
        this.wellId = wellId;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Coordinate() {
    }
}

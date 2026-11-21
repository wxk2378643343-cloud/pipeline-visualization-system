package tyut.selab.a2.pipes.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tyut.selab.a2.pipes.entity.Pipe;
import tyut.selab.a2.pipes.entity.Well;

import java.util.List;

@Mapper
public interface WellMapper extends BaseMapper<Well> {

    /**
     * 清空表数据
     */
    void deleteWell();


    int insertBatch(@Param("wells") List<Well> wells);
}

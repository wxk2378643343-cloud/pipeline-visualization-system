package tyut.selab.a2.pipes.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import tyut.selab.a2.pipes.entity.Pipe;

import java.util.List;

@Mapper
public interface PipeMapper extends BaseMapper<Pipe> {

    /**
     * 清空表数据
     */
    void deletePipe();


    int insertBatch(@Param("pipes")List<Pipe> pipes);

}

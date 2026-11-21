package tyut.selab.a2.pipes.service.impl;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.mysql.cj.xdevapi.Collection;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tyut.selab.a2.pipes.entity.Pipe;
import tyut.selab.a2.pipes.mapper.PipeMapper;
import tyut.selab.a2.pipes.service.IPipeService;

import java.util.Comparator;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

@Service
public class PipeServiceImpl extends ServiceImpl<PipeMapper, Pipe> implements IPipeService {

    @Autowired
    private PipeMapper pipeMapper;


    /**
     * 获取数据库中所有的数据
     *
     * @return 用List接收.sort(Pipe::getId);
     */
    @Override
    public List<Pipe> getAllPipes() {
        return this.lambdaQuery().list();
    }

    /**
     * 将pipes上传至数据库
     *
     * @param pipes 管道列表
     * @return 是否上传成功
     */
    @Override
    public boolean importPipes(List<Pipe> pipes) {
        if (pipes.size() == 0){
            return false;
        }
        return pipeMapper.insertBatch(pipes)<=0 ? false : true;
    }

    /**
     * 删除数据库中所有的Pipe
     */
    @Override
    public void delectPipes() {
        pipeMapper.deletePipe();
    }
}

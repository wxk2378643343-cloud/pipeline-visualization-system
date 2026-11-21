package tyut.selab.a2.pipes.service;

import com.baomidou.mybatisplus.extension.service.IService;
import tyut.selab.a2.pipes.entity.Pipe;

import java.util.List;

public interface IPipeService extends IService<Pipe> {

    /**
     * 获取数据库中所有的数据
     * @return 用List接收
     */
    public List<Pipe> getAllPipes();

    /**
     * 将pipes上传至数据库
     * @param pipes 管道列表
     * @return 是否上传成功
     */
    public boolean importPipes(List<Pipe> pipes);

    /**
     *  删除数据库中所有的Pipe
     */
    public void delectPipes();
}

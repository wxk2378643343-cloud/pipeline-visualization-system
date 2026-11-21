package tyut.selab.a2.pipes.service;

import com.baomidou.mybatisplus.extension.service.IService;
import tyut.selab.a2.pipes.entity.Well;

import java.util.List;

public interface IWellService extends IService<Well> {

    /**
     * 获取数据库中所有的数据
     * @return 用List接收
     */
    public List<Well> getAllWells();

    /**
     * 将wells上传至数据库
     * @param wells 管道列表
     * @return 是否上传成功
     */
    public boolean importWells(List<Well> wells);

    /**
     * 删除数据库中所有的well
     */
    public void delectAllWellsInDB();
}

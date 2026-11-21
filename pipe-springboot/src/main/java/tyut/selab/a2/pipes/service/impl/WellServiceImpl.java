package tyut.selab.a2.pipes.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tyut.selab.a2.pipes.entity.Well;
import tyut.selab.a2.pipes.mapper.WellMapper;
import tyut.selab.a2.pipes.service.IWellService;

import java.math.BigDecimal;
import java.security.PrivateKey;
import java.util.List;

@Service
public class WellServiceImpl extends ServiceImpl<WellMapper, Well> implements IWellService {

    @Autowired
    private WellMapper wellMapper;


    @Override
    public List<Well> getAllWells() {
        List<Well> wells = wellMapper.selectList(null);
        return removePointError(wells);
    }


    @Override
    public boolean importWells(List<Well> wells) {
        if (wells.size() == 0){
            return false;
        }
        return wellMapper.insertBatch(wells) <= 0 ? false : true;
    }


    @Override
    public void delectAllWellsInDB() {
        wellMapper.deleteWell();
    }


    public List<Well> removePointError(List<Well> wells){
        wells.stream().forEach(e->{
            e.setLatitude(e.getLatitude());
            e.setLongitude(e.getLongitude());
        });
        return wells;
    }
}

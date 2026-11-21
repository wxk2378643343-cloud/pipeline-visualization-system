package tyut.selab.a2.pipes.test;

import tyut.selab.a2.pipes.entity.Pipe;
import tyut.selab.a2.pipes.entity.Well;
import tyut.selab.a2.pipes.service.impl.ConverterServiceImpl;
import tyut.selab.a2.pipes.vo.DataVo;
import tyut.selab.a2.pipes.vo.DataWellVo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class GenerateRandomDataUtil {
    private static Random random = new Random();
    private static final double LONGITUDE_MAX = 113.1413369;
    private static final double LONGITUDE_MIN = 113.0488103;
    private static final double LATITUDE_MAX = 36.19535659;
    private static final double LATITUDE_MIN = 36.13436004;
    private static final double LONGITUDE_RANGE = LONGITUDE_MAX - LONGITUDE_MIN;
    private static final double LATITUDE_RANGE = LATITUDE_MAX - LATITUDE_MIN;

    public static Well randomWell(long id) {
        Well well = new Well();
        well.setId(id);
        well.setLatitude(new BigDecimal(random.nextDouble() * LATITUDE_RANGE + LATITUDE_MIN));
        well.setLongitude(new BigDecimal(random.nextDouble() * LONGITUDE_RANGE + LONGITUDE_MIN));
        return well;
    }

    public static Pipe randomPipe(long id, long wellNumber) {
        Pipe pipe = new Pipe();
        pipe.setId(id);
        pipe.setWellsId(random.longs(7).sorted().map(l -> l % (wellNumber + 1)).boxed().collect(Collectors.toList()));
        pipe.setType("测试");
        return pipe;
    }

    public static DataVo generate(long wellNum, long pipeNum) {
        ConverterServiceImpl converterService = new ConverterServiceImpl();
        List<DataWellVo> wells = LongStream.range(1, wellNum)
                .mapToObj(GenerateRandomDataUtil::randomWell)
                .map(converterService::wellToDataWellVo)
                .collect(Collectors.toList());
        List<Pipe> pipes = LongStream.range(1, pipeNum)
                .mapToObj(l -> randomPipe(l, wellNum))
                .collect(Collectors.toList());
        return new DataVo(converterService.pipeListToDataPipeVoList(pipes), wells);
    }
}

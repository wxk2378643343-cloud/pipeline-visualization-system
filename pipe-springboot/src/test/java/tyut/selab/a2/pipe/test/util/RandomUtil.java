package tyut.selab.a2.pipe.test.util;

import tyut.selab.a2.pipes.entity.Pipe;
import tyut.selab.a2.pipes.entity.Well;

import java.math.BigDecimal;
import java.util.Random;
import java.util.stream.Collectors;

public class RandomUtil {
    private static Random random = new Random();

    public static Well randomWell() {
        byte[] bytes = new byte[12];
        random.nextBytes(bytes);
        Well well = new Well();
        well.setType(new String(bytes));
        well.setLatitude(new BigDecimal(random.nextDouble()));
        well.setLongitude(new BigDecimal(random.nextDouble()));
        well.setModel(new String(bytes));
        return well;
    }

    public static Pipe randomPipe(long id) {
        Pipe pipe = new Pipe();
        pipe.setId(id);
        pipe.setWellsId(random.longs(7).sorted().map(l -> l % 4872).boxed().collect(Collectors.toList()));
        pipe.setType("测试");
        return pipe;
    }
}

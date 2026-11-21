package tyut.selab.a2.pipe.test.util;

import tyut.selab.a2.pipe.test.exception.TestFailureException;

import java.util.ArrayList;
import java.util.List;

public class SequentUtil {
    public static void requireSequential(List<Long> longs) {
        Long last = longs.get(0);
        for (int i = 1; i < longs.size(); i++) {
            Long now = longs.get(i);
            if (now - last == 1) {
                last = now;
            } else {
                throw new TestFailureException("数据不是有序的");
            }
        }
    }
}

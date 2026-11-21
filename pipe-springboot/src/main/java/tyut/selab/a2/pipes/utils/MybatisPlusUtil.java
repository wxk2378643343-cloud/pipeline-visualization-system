package tyut.selab.a2.pipes.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;

public class MybatisPlusUtil {

     public static  <T> QueryWrapper<T> queryEqualsAllExceptNull(T entity) {
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        wrapper.allEq(ReflectUtil.convertEntityToMap(entity));
        return wrapper;
    }

    public static <T> UpdateWrapper<T> updateEqualsAllExceptNull(T entity) {
        UpdateWrapper<T> wrapper = new UpdateWrapper<>();
        wrapper.allEq(ReflectUtil.convertEntityToMap(entity));
        return wrapper;
    }
}

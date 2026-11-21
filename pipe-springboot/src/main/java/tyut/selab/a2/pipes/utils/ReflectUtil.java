package tyut.selab.a2.pipes.utils;

import tyut.selab.a2.pipes.exception.ReflectUtilException;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ReflectUtil {
    public static <T> Map<String, Object> convertEntityToMap(T entity) {
        HashMap<String, Object> map = new HashMap<>();
        Field[] fields = entity.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Object o = getFieldValue(fields[i], entity);
            if (o == null) continue;
            String sqlFieldName = StringUtil.convertJavaFieldNameToSqlFieldName(fields[i].getName());
            map.put(sqlFieldName, o);
        }
        return map;
    }

    public static Object getFieldValue(Field field, Object obj) {
        field.setAccessible(true);
        Object value;
        try {
            value = field.get(obj);
        } catch (IllegalAccessException e) {
            throw new ReflectUtilException("Attempt to get value by getFieldValue method failure", e);
        }
        return value;
    }

    public static String[] getFieldName(Object t){
        Class clazz = t.getClass();
        Field[] fields = clazz.getDeclaredFields();
        String[] strings = new String[fields.length];
        for (int i = 0;i <fields.length;i++){
            strings[i] = fields[i].getName();
        }
        return strings;
    }
}

package tyut.selab.a2.pipes.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

@Slf4j
public class StringUtil {
    public static String convertJavaFieldNameToSqlFieldName(String javaFieldName){
        StringBuilder stringBuilder = new StringBuilder();
        char[] chars = javaFieldName.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char aChar = chars[i];
            if (Character.isLowerCase(aChar)){
                stringBuilder.append(aChar);
            }else {
                stringBuilder.append('_').append(Character.toLowerCase(aChar));
            }
        }
        return stringBuilder.toString();
    }

    /**
     * 判断字符串是否都是空白符或者为null
     * @param matchedStr
     * @return 不符合返回false，符合返回true
     */
    public static boolean judgeCellType (String matchedStr){
        int cnt = 0;
        //非空判断
        if (matchedStr == null) return true;
        //匹配空白字符
        String pattern = " ";
        log.trace("被匹配的字符串[{}]",matchedStr);
        if (matchedStr.contains(pattern)) {
            cnt = matchedStr.length() - matchedStr.replaceAll(pattern,"").length();
        }
        if (cnt == matchedStr.length()){
            return true;
        }
        return false;
    }
}

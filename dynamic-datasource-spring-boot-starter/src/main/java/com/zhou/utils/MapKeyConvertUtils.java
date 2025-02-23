package com.zhou.utils;

/**
 * @author zhou22
 * @desc 中划线转驼峰
 * @Date 2025-02-19 11:36:47
 */
public class MapKeyConvertUtils {

    public static String middleLineToCamelHump(String mapKey) {
        StringBuilder result = new StringBuilder();
        boolean upperCaseFlag = false;
        for (char curChar : mapKey.toCharArray()) {
            if (curChar == '-') {
                upperCaseFlag = true;
                continue;
            }
            if (upperCaseFlag) {
                result.append(Character.toUpperCase(curChar));
                upperCaseFlag = false;
            } else {
                result.append(curChar);
            }
        }
        return result.toString();
    }

}
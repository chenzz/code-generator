package com.alibaba.ascp.util;

import com.google.common.base.CaseFormat;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author junhao
 * @date 2019/12/18
 */
public class ConvertUtils {

    private static Pattern tableNamePattern = Pattern.compile("CREATE TABLE `([a-z|A-Z|_]+)_[0-9]*` \\(");

    private static Map<String, String> mysqlTypeMap = new HashMap<String, String>();
    private static Map<String, String> oracleTypeMap = new HashMap<String, String>();

    static {
        mysqlTypeMap.put("bigint", "Long");
        mysqlTypeMap.put("int", "Integer");
        mysqlTypeMap.put("varchar", "String");
        mysqlTypeMap.put("blob", "String");
        mysqlTypeMap.put("text", "String");
        mysqlTypeMap.put("timestamp", "Date");
        // 此处的Date是java.util.Date
        mysqlTypeMap.put("datetime", "Date");
        mysqlTypeMap.put("date", "Date");
        mysqlTypeMap.put("tinyint", "Integer");


        oracleTypeMap.put("bigint", "long");
        oracleTypeMap.put("int", "int");
        oracleTypeMap.put("varchar", "String");
        oracleTypeMap.put("blob", "String");
        oracleTypeMap.put("text", "String");
        oracleTypeMap.put("tinyint", "int");
        // 时间统一转换成long类型
        oracleTypeMap.put("datetime", "long");
        oracleTypeMap.put("timestamp", "Timestamp");
        oracleTypeMap.put("date", "Date");
        // oracle的比较麻烦。。。
        oracleTypeMap.put("number", "aNumber");
        oracleTypeMap.put("varchar2", "String");
    }

    public static String convertMySqlType2JavaType(String mysqlType) {
        return mysqlTypeMap.get(mysqlType);
    }

    public static String convertOracleType2JavaType(String oracleType) {
        return oracleTypeMap.get(oracleType);
    }

    public static String replaceUnderlineToUpper(String field) {
        field = StringUtils.lowerCase(field); // 先把所有的大写字母转小写
        char[] fields = field.toCharArray();
        int count = 0;
        for (int i = 0, j = 0, length = fields.length; i < length; i++, j++) {
            if ("_".equals(CharUtils.toString(fields[i]))) { // 如果是下划线
                i++; // 向后移动
                if (fields[i] >= 97 && fields[i] <= 122) { // 只有小写字母做操作
                    fields[j] = (char)(fields[i] - 32); // 转大写
                } else {
                    fields[j] = fields[i];
                }
            } else {
                fields[j] = fields[i];
            }
            count = j + 1;
        }
        char[] result = new char[count];
        System.arraycopy(fields, 0, result, 0, count);
        return new String(result);
    }


    public static String extractTableName(String line) {
        Matcher m = tableNamePattern.matcher(line);

        String tableName = "";
        if (m.find()) {
            tableName = m.group(1);
        }
        return tableName;
    }

    public static String convertTableName2ClassName(String tableName) {
        String className = "";

        if (StringUtils.isNotEmpty(tableName)) {
            className = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, tableName);
            className += "PO";
        }

        if (StringUtils.isEmpty(className)) {
            className = "XxxPO";
        }

        return className;
    }

    public static String convertTableName2MapperXmlName(String tableName) {
        if (StringUtils.isEmpty(tableName)) {
            return tableName;
        }

        String mapperXmlName = tableName.replace("_", "-");
        mapperXmlName += "-mapper.xml";
        return mapperXmlName;
    }

    /**
     * 第一个字母转换
     *
     * @param input
     * @param mode
     * @return
     */
    public static String convertFirstChar(final String input, final CONVERT_MODE mode) {
        // 至少要一个字符以上才处理
        if (input == null || input.length() <= 1) {
            return input;
        }
        String result = null;
        switch (mode) {
            case UPPER:
                result = Character.toUpperCase(input.charAt(0)) + input.substring(1);
                break;
            case LOWER:
                result = Character.toLowerCase(input.charAt(0)) + input.substring(1);
                break;
            default:
                break;
        }
        return result;
    }

    public static enum CONVERT_MODE {
        UPPER,
        LOWER
    }

}

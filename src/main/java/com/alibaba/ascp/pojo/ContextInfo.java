package com.alibaba.ascp.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author junhao
 * @date 2019/12/20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContextInfo {

    private Map<String, String> pojoFieldNameTypeMap;
    private String author;
    private String fullQualifiedName;
    private String pojoClassName;
    private String tableName;

    // 对要生成的类的简单描述，会写入到代码注释中的
    private String pojoDescription;
    private List<String> pojoFields;
    private List<String> dbFields;



    // 用来做queryByXxx时的字段，
    // 数据库中对应的字段，按数据库写法来写，有可能带下划线之类的
    private String keyIdNameDB;

    // key描述
    private String keyIdNameDescription;

    // 对应的key类型，long、int、String什么的
    String keyIdType = "Long";
}

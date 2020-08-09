package me.chenzz.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
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
    Integer gmtCreateIndex = null;
    Integer gmtModifiedIndex = null;


    // 用来做queryByXxx时的字段，
    // 数据库中对应的字段，按数据库写法来写，有可能带下划线之类的
    private String keyIdNameDB;

    // key描述
    private String keyIdNameDescription;

    // 对应的key类型，long、int、String什么的
    String keyIdType = "Long";

    /**
     * 对gmtCreate\gmtModified进行修饰
     * @return
     */
    public List<String> getPojoFields4Insert() {

        List<String> pojoFields4Insert = new ArrayList<>(pojoFields);

        if (null != gmtCreateIndex) {
            pojoFields4Insert.set(gmtCreateIndex, "NOW()");
        }

        if (null != gmtModifiedIndex) {
            pojoFields4Insert.set(gmtModifiedIndex, "NOW()");
        }

        return pojoFields4Insert;
    }

    /**
     * 对gmtCreate\gmtModified进行修饰
     * @return
     */
    public List<String> getPojoFields4Update() {
        List<String> pojoFields4Modified = new ArrayList<>(pojoFields);

        if (null != gmtModifiedIndex) {
            pojoFields4Modified.set(gmtModifiedIndex, "NOW()");
        }

        return pojoFields4Modified;
    }
}

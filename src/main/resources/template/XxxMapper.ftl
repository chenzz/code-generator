package ${package};

import java.util.List;
import ${fullQualifiedPojoName};
import result.annotation.MyBatisMapper;

/**
 * 规范说明
 * 直接用Mapper的方式，不再写Dao和DaoImpl，注意命名成xxxMapper
 * @author ${author} 
 * @date ${date}
 */
@MyBatisMapper
public interface ${simpleClassName}Mapper {

    /**
     * 根据${keyIdName}获取${description}
     * @param ${keyIdName} ${keyIdNameDescription}
     * @return ${description}
     */
	${className} getBy${keyIdNameUpper}(${keyIdType} ${keyIdName});
    // TODO:注意，geyByXxx和listByXxx只能保留一个，如果key是唯一索引，就用上面的，记得删掉！
    /**
     * 根据${keyIdName}获取${description}列表
     * @param ${keyIdName} ${keyIdNameDescription}
     * @return ${description}列表
     */
	List<${className}> listBy${keyIdNameUpper}(${keyIdType} ${keyIdName});

    /**
     * 插入${description}
     * @param ${className?uncap_first} ${description}
     * @return 数据库表主键ID
     */
	Long insert(${className} ${className?uncap_first});

    /**
     * 查询${description}数量
     * @param query${className} 查询对象
     * @return ${description}数量
     */
    Integer count(Query${className} query${className});

    /**
     * 查询${description}列表
     * @param query${className} 查询对象
     * @return ${description}列表
     */
    List<${className}> list(Query${className} query${className});
}

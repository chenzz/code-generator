package ${package};


import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Date;

import com.alibaba.fastjson.JSON;
import ${fullQualifiedPojoName};

//import com.taobao.pandora.boot.test.junit4.PandoraBootRunner;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import com.alibaba.ascp.gusa.base.BaseApplication;

/**
 *
 * @author ${author} 
 * @date ${date}
 */
//@RunWith(PandoraBootRunner.class)
//@DelegateTo(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes = { BaseApplication.class })
public class ${simpleClassName}MapperTest {
	private static final Logger logger = LoggerFactory.getLogger(${simpleClassName}MapperTest.class);
	
    @Resource
    private ${simpleClassName}Mapper ${simpleClassName?uncap_first}Mapper;
    
    @Before
    public void setup() {
        Assert.assertNotNull(${simpleClassName?uncap_first}Mapper);
    }
    
    @Test
    public void testGetBy${keyIdNameUpper}() {
        ${className} ${className?uncap_first} = ${simpleClassName?uncap_first}Mapper
                        .getBy${keyIdNameUpper}(1L);
        if (${className?uncap_first} != null) {
            logger.info("${className?uncap_first} = " + JSON.toJSONString(${className?uncap_first}));
        } else {
            logger.error("查询对象不存在!");
        }
    }

    @Test
    public void testListBy${keyIdNameUpper}() {
        List<${className}> list = ${simpleClassName?uncap_first}Mapper.listBy${keyIdNameUpper}(1L);
        logger.info("list.size = " + list.size());
        for (${className} ${className?uncap_first} : list) {
            logger.info("${className?uncap_first} = " + JSON.toJSONString(${className?uncap_first}));
        }
    }

    @Test
    public void testInsert() {
        ${className} ${className?uncap_first} = new ${className}();
        // 自增主键的话去掉主键设置
        <#list javaFieldsList as field>
        <#assign fieldType=fieldTypeList[field_index]>
        ${className?uncap_first}.set${field?cap_first}(<#if fieldType == "Date">new Date()<#elseif fieldType == "Integer">1<#elseif fieldType == "Long">1L<#else>"junit-test"</#if>);
        </#list>
        Long id = ${simpleClassName?uncap_first}Mapper.insert(${className?uncap_first});
        logger.info("id = " + id);
    }

    @Test
    public void testCount() {
        Query${className} query${className} = new Query${className}();
        Integer count = ${simpleClassName?uncap_first}Mapper.count(query${className});
        logger.info("count = " + count);
    }

    @Test
    public void testList() {
        Query${className} query${className} = new Query${className}();
        List<${className}> list = ${simpleClassName?uncap_first}Mapper.list(query${className});
        logger.info("list.size = " + list.size());
        for (${className} ${className?uncap_first} : list) {
            logger.info("${className?uncap_first} = " + JSON.toJSONString(${className?uncap_first}));
        }
    }
}

package com.alibaba.ascp;

import com.alibaba.ascp.generator.MapperGenerator;
import com.alibaba.ascp.generator.PojoGenerator;
import com.alibaba.ascp.pojo.ContextInfo;
import freemarker.template.TemplateException;

import java.io.IOException;

/**
 * @author junhao
 * @date 2019/12/20
 */
public class BootStrap {

    public static void main(String[] args) throws IOException, TemplateException, ClassNotFoundException {
        ContextInfo contextInfo = new ContextInfo();
        contextInfo.setAuthor("junhao");
        contextInfo.setPojoDescription("商家货品服务订购");
        contextInfo.setKeyIdNameDB("user_id");
        contextInfo.setKeyIdNameDescription("商家ID");

        PojoGenerator.generatePojoAndReturnContext(contextInfo);

        MapperGenerator.generate(contextInfo);
    }
}

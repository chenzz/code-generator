package com.alibaba.ascp.generator;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.ascp.constants.CommonContants;
import com.alibaba.ascp.pojo.ContextInfo;
import com.alibaba.ascp.util.ConvertUtils;
import com.alibaba.ascp.util.DateTimeUtils;
import com.google.common.base.CaseFormat;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import static freemarker.template.Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS;

/**
 * @author liangxiao junhao
 * @date 2019-12-20
 */
public class MapperGenerator {

    // 模板目录和输出目录
    private static final String output = "src/main/java/" + CommonContants.resultPath;

    /**
     *
     * 生成的代码会有很多冗余，请根据实际情况清理冗余代码，以免增加后续维护的代价
     *
     * @throws IOException
     * @throws TemplateException
     */
    public static void generate(ContextInfo contextInfo) throws IOException, TemplateException, ClassNotFoundException {



        String date = DateTimeUtils.formatDate(System.currentTimeMillis(), "yyyy/MM/dd.");
        // DB相关的总是被命名为XxxPO这种
        String simpleClassName = StringUtils.substringBefore(contextInfo.getPojoClassName(), "PO");
        // mybatis相关总会有一个alias的存在
        String aliasName = simpleClassName;
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("keyIdName", CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, contextInfo.getKeyIdNameDB()));
        root.put("keyIdNameDescription", contextInfo.getKeyIdNameDescription());
        root.put("keyIdNameDB", contextInfo.getKeyIdNameDB());
        root.put("keyIdNameUpper", CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, contextInfo.getKeyIdNameDB()));
        root.put("keyIdType", contextInfo.getKeyIdType());
        root.put("className", contextInfo.getPojoClassName());
        root.put("simpleClassName", simpleClassName);
        root.put("aliasName", aliasName);
        root.put("lowerClassName", ConvertUtils.convertFirstChar(simpleClassName, ConvertUtils.CONVERT_MODE.LOWER));
        root.put("package", CommonContants.resultPackage);
        root.put("author", contextInfo.getAuthor());
        root.put("date", date);
        root.put("tableName", contextInfo.getTableName());
        root.put("description", contextInfo.getPojoDescription());
        root.put("mapperPackageName", CommonContants.resultPackage);
        root.put("fullQualifiedPojoName", contextInfo.getFullQualifiedName());

        Map<String, String> templateOutputFileMap = new HashMap<>();
        templateOutputFileMap.put("template/QueryXxxPO.ftl", "Query" + contextInfo.getPojoClassName() + ".java");
        templateOutputFileMap.put("template/XxxMapper.ftl", "" + simpleClassName + "Mapper.java");
        templateOutputFileMap.put("template/xxx-mapper.ftl", ConvertUtils.convertTableName2MapperXmlName(contextInfo.getTableName()));
        templateOutputFileMap.put("template/XxxMapperTest.ftl", simpleClassName + "MapperTest.java");
        // 字段名的生成
        generateFieldNames(root, contextInfo.getPojoFieldNameTypeMap(), contextInfo);
        // 数据库字段
        root.put("javaFieldsList", contextInfo.getPojoFields());
        // 数据库字段
        root.put("dbFieldsList", contextInfo.getDbFields());

        Configuration cfg = new Configuration(DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        cfg.setDirectoryForTemplateLoading(new File("src/main/resources"));
        cfg.setObjectWrapper(new DefaultObjectWrapper(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS));


        for (String templateFileName : templateOutputFileMap.keySet()) {
            Template template = cfg.getTemplate(templateFileName,"UTF-8");
            String fileName = templateOutputFileMap.get(templateFileName);
            System.out.println("generating file :" + fileName);
            Writer out = new OutputStreamWriter(new FileOutputStream(new File(output + "/" + fileName)),"UTF-8");
            template.process(root, out);
            out.flush();
            out.close();
        }
    }

    /**
     * 从提供的类中提取字段
     *
     * @param root
     */
    @SuppressWarnings("unchecked")
    private static void generateFieldNames(final Map<String, Object> root, Map<String, String> pojoFieldNameTypeMap, ContextInfo contextInfo) {
        if (MapUtils.isEmpty(pojoFieldNameTypeMap)) {
            return;
        }

        List<String> lowerFirstList = (List<String>) root.get("lowerFirstList");
        List<String> upperFirstList = (List<String>) root.get("upperFirstList");
        List<String> fieldTypeList = (List<String>) root.get("fieldTypeList");

        if (lowerFirstList == null) {
            lowerFirstList = new ArrayList<>();
            root.put("lowerFirstList", lowerFirstList);
        }

        if (upperFirstList == null) {
            upperFirstList = new ArrayList<>();
            root.put("upperFirstList", upperFirstList);
        }

        if (fieldTypeList == null) {
            fieldTypeList = new ArrayList<>();
            root.put("fieldTypeList", fieldTypeList);
        }


        for (Map.Entry<String, String> nameTypeEntry : pojoFieldNameTypeMap.entrySet()) {

            String fieldName = nameTypeEntry.getKey();
            if (CollectionUtils.isNotEmpty(contextInfo.getPojoFields()) && !contextInfo.getPojoFields().contains(StringUtils.uncapitalize(fieldName))) {
                continue;
            }
            lowerFirstList.add(fieldName);
            upperFirstList.add(ConvertUtils.convertFirstChar(fieldName, ConvertUtils.CONVERT_MODE.UPPER));
            fieldTypeList.add(nameTypeEntry.getValue());
        }
    }


}

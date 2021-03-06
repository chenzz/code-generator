package me.chenzz.generator;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.chenzz.constants.CommonConstants;
import me.chenzz.pojo.ContextInfo;
import me.chenzz.util.ConvertUtils;
import freemarker.template.*;
import org.apache.commons.io.IOUtils;

import com.google.common.collect.Lists;

import static freemarker.template.Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS;

/**
 * @author liangxiao junhao
 * @date 2019-12-20
 */
public class PojoGenerator {


    private static Pattern fieldPattern = Pattern.compile("\\s*`([a-zA-Z0-9_]+)` ([a-z]+).*?COMMENT '(.*?)'");

    public static void generatePojoAndReturnContext(ContextInfo contextInfo) throws IOException, TemplateException {

        InputStream inputStream = PojoGenerator.class.getResourceAsStream("/db.txt");
        List<String> lines = IOUtils.readLines(inputStream, "utf-8");

        String resultStr = "";
        String className = "";

        List<String> pojoFields = Lists.newLinkedList();
        List<String> dbFields = Lists.newLinkedList();

        int i = 0;

        Map<String, String> pojoFieldNameTypeMap = new LinkedHashMap<>();
        for (String line : lines) {
            if (0 == i) {
                String tableName = ConvertUtils.extractTableName(line);
                contextInfo.setTableName(tableName);

                className = ConvertUtils.convertTableName2ClassName(tableName);
            } else {
                Matcher m = fieldPattern.matcher(line);
                if (m.find()) {
                    String name = m.group(1);
                    name = ConvertUtils.replaceUnderlineToUpper(name);
                    pojoFields.add(name);
                    dbFields.add(m.group(1));
                    String javaType = ConvertUtils.convertMySqlType2JavaType(m.group(2));
                    String comment = m.group(3);

                    resultStr += CommonConstants.TAB + "/**" + "\n";
                    resultStr += CommonConstants.TAB + " * " + comment + "\n";
                    resultStr += CommonConstants.TAB + " */" + "\n";
                    resultStr += String.format("    private %s %s;", javaType, name) + "\n";
                    pojoFieldNameTypeMap.put(name, javaType);
                }
            }

            i++;
        }
        contextInfo.setPojoFieldNameTypeMap(pojoFieldNameTypeMap);

        Configuration cfg = new Configuration(DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        cfg.setDirectoryForTemplateLoading(new File("src/main/resources"));
        cfg.setObjectWrapper(new DefaultObjectWrapper(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS));

        Map<String, Object> root = new HashMap<String, Object>();

        root.put("className", className);
        root.put("result", resultStr);
        root.put("package", CommonConstants.RESULT_PACKAGE);

        Template template = cfg.getTemplate("template/XxxPO.ftl","UTF-8");
        String fileName = className + ".java";
        Writer out = new OutputStreamWriter(new FileOutputStream(new File(CommonConstants.RESULT_PATH + "/" + fileName)),"UTF-8");
        template.process(root, out);
        out.flush();
        out.close();

        contextInfo.setPojoFields(pojoFields);
        contextInfo.setDbFields(dbFields);


        for (i = 0; i < pojoFields.size(); i++) {

            if ("gmtCreate".equals(pojoFields.get(i))) {
                contextInfo.setGmtCreateIndex(i);
            }
            if ("gmtModified".equals(pojoFields.get(i))) {
                contextInfo.setGmtModifiedIndex(i);
            }
        }

        contextInfo.setFullQualifiedName(CommonConstants.RESULT_PACKAGE + "." + className);
        contextInfo.setPojoClassName(className);
    }

}

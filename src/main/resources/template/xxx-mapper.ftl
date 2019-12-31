<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${package}.${simpleClassName}Mapper">

    <!-- 定义数据库表字段和POJO类的字段映射关系 -->
    <resultMap id="${simpleClassName}Map" type="${className}">
    <#list dbFieldsList as db>
        <#assign jf = javaFieldsList[db?index]>
        <result column="${db}" property="${jf}"/><#if db_has_next></#if>
    </#list>
    </resultMap>

    <!-- 定一个语句块，方便其他地方引用 -->
    <sql id="allFields">
        <#list dbFieldsList as db>${db}<#if db_has_next>, </#if></#list>
    </sql>
    
    <!-- 根据对应key查询获得一个对象 -->
    <select id="getBy${keyIdNameUpper}" parameterType="${keyIdType?uncap_first}" resultMap="${simpleClassName}Map" >
        SELECT <include refid="allFields" />
        FROM ${tableName}
        WHERE ${keyIdNameDB} = ${r'#{'}${keyIdName}${r'}'}
    </select>

    注意！！！！！！！！！！！！！！！！！！！上面和下面的只能保留一个！！！！！！看看key是不是唯一索引啊，骚年！！！

    <!-- 根据对应key查询获得对象列表 -->
    <select id="listBy${keyIdNameUpper}" parameterType="${keyIdType?uncap_first}" resultMap="${simpleClassName}Map" >
        SELECT <include refid="allFields" />
        FROM ${tableName}
        WHERE ${keyIdNameDB} = ${r'#{'}${keyIdName}${r'}'}
    </select>

    注意！！！！！！！！！gmt_create, gmt_modified这2个字段使用NOW填充，如果是自增ID，那插入语句中去掉id主键相关的，无需插入来着!!!!!!!!!
    <!-- 自增ID插入，插入完成后自动填充相应对象的ID -->
    <insert id="insert" parameterType="${className}" useGeneratedKeys="true" keyProperty="id">
        INSERT INTO ${tableName} (<#list dbFieldsList as dbField>${dbField}<#if dbField_has_next>, </#if></#list>)
        VALUES(<#list javaFieldsList as javaField>${r'#{'}${javaField}${r'}'}<#if javaField_has_next>, </#if></#list>)
    </insert>

    注意！！！！！！！！！！！！！！！！！！！whereCondition要调整才能用啊，比如startTime这种，别不管啊！！！！！！！！！！！
    <sql id="whereCondition">
        <where>
        <#list dbFieldsList as db>
            <#assign dbField = dbFieldsList[db?index], type = fieldTypeList[db?index], javaField = javaFieldsList[db?index]>
            <#if javaField?matches('.*Time$')>
            <if test="startTime != null and startTime > 0">AND <![CDATA[ ${dbField} >= ${r'#{'}startTime${r'}'} ]]> </if>
            <if test="endTime != null and endTime > 0">AND <![CDATA[ ${dbField} < ${r'#{'}endTime${r'}'} ]]> </if>
            <#elseif javaField=="id">
            <if test="id != null and id > 0">AND id = ${r'#{'}id${r'}'}</if>
            <if test="minId != null and minId > 0">AND <![CDATA[ id >= ${r'#{'}minId${r'}'} ]]> </if>
            <if test="maxId != null and maxId > 0">AND <![CDATA[ id < ${r'#{'}maxId${r'}'} ]]> </if>
            <#elseif type=="int" || type=="long" || type=="Integer" || type=="Long">
            <if test="${javaField} != null and ${javaField} > 0">AND ${dbField} = ${r'#{'}${javaField}${r'}'}</if>
            <#else>
            <if test="${javaField} != null and ${javaField} != ''">AND ${dbField} = ${r'#{'}${javaField}${r'}'}</if>
            </#if>
        </#list>
        </where>
    </sql>

    <select id="count" parameterType="Query${className}" resultType="java.lang.Integer">
        SELECT COUNT(*) FROM ${tableName}
        <include refid="whereCondition"/>
    </select>

    <select id="list" parameterType="Query${className}" resultMap="${simpleClassName}Map">
        SELECT
        <include refid="allFields"/>
        FROM ${tableName}
        <include refid="whereCondition"/>
        <!-- orderStr不能修改或转义,所以用$,如果这样必须在代码中提前检查用户输入的orderStr -->
        <if test="orderStr != null and orderStr != ''">ORDER BY ${r'${'}orderStr${r'}'}</if>
        <if test="limit != null and limit >= 0">LIMIT ${r'#{'}limit${r'}'}</if>
        <if test="offset != null and offset >= 0">OFFSET ${r'#{'}offset${r'}'}</if>
    </select>

</mapper>
/*
 * @(#) MyBatisMapper.java 2016年04月18日
 * 
 * Copyright 2010 NetEase.com, Inc. All rights reserved.
 */
package result.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * 基础mapper注解,添加了此注解的mapper接口会被mybatis自动封装
 *      使用@interface定义一个注解类，用于注解其他东西
 *      有此注解的都会被扫描(mapperScanner)
 * @author liangxiao.ly
 * @version 2016年04月18日
 */
@Target(ElementType.TYPE)
public @interface MyBatisMapper {
}

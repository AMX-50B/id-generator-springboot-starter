package com.xi.id.generator.service;

/**
 * id 业务接口
 * @author LiuY
 */
public interface IdGenerator {

    /**
     * 生成下一个id并返回
     * 若${sequenceName}不存在 则会抛出异常 IllegalArgumentException
     * @param sequenceName 序列号名称
     * @return id值
     * @throws IllegalArgumentException 异常信息
     */
    Long nextId(String sequenceName);

    /**
     * 生成下一个id并返回
     * 使用默认
     * @return id值
     * @throws IllegalArgumentException 异常信息
     */
    Long nextId();
}

package com.xi.id.generator.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * id生成器配置属性
 * @author LiuY
 */
@Data
@ConfigurationProperties(prefix = IdGenerateProperties.ID_GENERATE_PREFIX)
public class IdGenerateProperties {

    public static final String ID_GENERATE_PREFIX = "xi.id";

    /**
     * 项目id 默认 1000
     */
    private Long projectId;

    /**
     * 序列表名称
     */
    private String tableName;

    /**
     * 默认序列
     */
    private String defaultSequence;

    /**
     * 默认序列号段
     */
    private Long defaultSequenceOffset;
    /**
     * 自动建表
     */
    private Boolean autoCreateTable;

    public IdGenerateProperties(){
        this.projectId = 1000L;
        this.tableName = "t_sequence";
        this.defaultSequence = "default";
        this.autoCreateTable = true;
        this.defaultSequenceOffset = 1L;
    }
}

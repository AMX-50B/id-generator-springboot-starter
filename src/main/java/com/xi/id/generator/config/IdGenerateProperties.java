package com.xi.id.generator.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * id生成器配置属性
 * @author LiuY
 */
@Data
@ConfigurationProperties(prefix = IdGenerateProperties.ID_GENERATE_PREFIX)
public class IdGenerateProperties {

    public static final String ID_GENERATE_PREFIX = "xi.id";

    private Long systemNo;

    private String tableName;

    private String defaultSequence;

    public IdGenerateProperties(){
        this.tableName = "t_sequence";
        this.defaultSequence = "default";
    }
}

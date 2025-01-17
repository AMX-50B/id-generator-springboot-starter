package com.xi.id.generator.annotation;

import com.xi.id.generator.config.IdGeneratorAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启id生成器
 * @author LiuY
 * @date 2024/12/27
 **/
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({IdGeneratorAutoConfiguration.class})
public @interface EnableXiIdGenerator {
}

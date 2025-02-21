package com.xi.id.generator.config;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.xi.id.generator.mapper.SequenceMapper;
import com.xi.id.generator.service.IdGenerator;
import com.xi.id.generator.service.impl.IdGeneratorImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * ID 生成器自动配置
 * @author LiuY
 * @date 2024/12/27
 **/
@Configuration
@ConditionalOnClass({SqlSessionFactory.class, SqlSessionFactoryBean.class })
@EnableConfigurationProperties(IdGenerateProperties.class)
@AutoConfigureAfter({MybatisAutoConfiguration.class, MybatisPlusAutoConfiguration.class})
@MapperScan("com.xi.id.generator.mapper")
@Slf4j
public class IdGeneratorAutoConfiguration {
    private final IdGenerateProperties idGenerateProperties;

    public IdGeneratorAutoConfiguration(IdGenerateProperties idGenerateProperties) {
        this.idGenerateProperties = idGenerateProperties;

    }

    @Bean("idGenerateSqlSessionFactory")
    @ConditionalOnMissingBean(SqlSessionFactory.class)
    public SqlSessionFactory idGenerateSqlSessionFactory(ObjectProvider<DataSource> dataSourceProvider) throws Exception {
        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
        DataSource dataSource = dataSourceProvider.getIfAvailable();
        if (dataSource == null) {
            log.error("idGenerateSqlSessionFactory must need dataSource but not found");
            throw new RuntimeException("idGenerateSqlSessionFactory must need dataSource but not found");
        }
        factory.setDataSource(dataSourceProvider.getIfAvailable());
        factory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/id/generator/SequenceMapper.xml"));
        return factory.getObject();
    }

    @Bean(name = "idGenerateTransactionManager")
    @ConditionalOnMissingBean(DataSourceTransactionManager.class)
    public DataSourceTransactionManager idGenerateTransactionManager(ObjectProvider<DataSource> dataSourceProvider) {
        DataSource dataSource = dataSourceProvider.getIfAvailable();
        if (dataSource == null) {
            log.error("idGenerateSqlSessionFactory must need dataSource but not found");
            throw new RuntimeException("idGenerateSqlSessionFactory must need dataSource but not found");
        }
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "idGenerateSqlSessionTemplate")
    @ConditionalOnMissingBean(SqlSessionTemplate.class)
    public SqlSessionTemplate primarySqlSessionTemplate(@Qualifier("idGenerateSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean(name = "idGenerator")
    public IdGenerator idService(SequenceMapper sequenceMapper) {
        return new IdGeneratorImpl(sequenceMapper,idGenerateProperties);
    }
}

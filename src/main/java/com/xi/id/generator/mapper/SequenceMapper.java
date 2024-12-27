package com.xi.id.generator.mapper;

import com.xi.id.generator.config.IdGenerateProperties;
import com.xi.id.generator.mapper.entity.SequenceEntity;
import com.xi.id.generator.mapper.provider.SequenceProvider;
import org.apache.ibatis.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 序列号持久层
 * @author LiuY
 */
@Mapper
public interface SequenceMapper {

    /**
     * 获取序列号
     * @param name
     * @return
     */
    @SelectProvider(type = SequenceProvider.class, method = "getSequence")
    SequenceEntity getSequence(@Param("name") String name);

    /**
     * 获取序列号 初始化用
     * @return
     */
    @SelectProvider(type = SelectProvider.class, method = "listSequence")
    List<SequenceEntity> listSequence();

    /**
     * 预占offset
     * @param name
     * @param nextOffset
     * @param currentOffset
     * @return
     */
    @UpdateProvider(type = UpdateProvider.class,method = "updateOffset")
    int updateOffset(@Param("name") String name, @Param("nextOffset") long nextOffset, @Param("currentOffset") long currentOffset);

}

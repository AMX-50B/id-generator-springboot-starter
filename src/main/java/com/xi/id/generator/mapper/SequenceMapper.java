package com.xi.id.generator.mapper;

import com.xi.id.generator.config.IdGenerateProperties;
import com.xi.id.generator.mapper.entity.SequenceEntity;
import com.xi.id.generator.mapper.provider.SequenceProvider;
import org.apache.ibatis.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

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
    @SelectProvider(type = SequenceProvider.class, method = "listSequence")
    List<SequenceEntity> listSequence();

    /**
     * 预占offset
     * @param name
     * @param nextOffset
     * @param currentOffset
     * @return
     */
    @UpdateProvider(type = SequenceProvider.class,method = "updateOffset")
    int updateOffset(@Param("name") String name, @Param("nextOffset") long nextOffset, @Param("currentOffset") long currentOffset);

    /**
     * 插入序列号
     * @param sequence
     * @return
     */
    @InsertProvider(type = SequenceProvider.class, method = "listSequence")
    int insertSequence(SequenceEntity sequence);

    /**
     * 检查表是否存在
     */
    @SelectProvider(type = SequenceProvider.class, method = "showTable")
    Map<String,String> checkTable();

    /**
     * 创建序列表
     */
    @SelectProvider(type = SequenceProvider.class, method = "createTable")
    void createSequenceTable();


}

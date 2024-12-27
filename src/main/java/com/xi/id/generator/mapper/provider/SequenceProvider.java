package com.xi.id.generator.mapper.provider;

import com.xi.id.generator.config.IdGenerateProperties;
import com.xi.id.generator.mapper.entity.SequenceEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author LiuY
 * @date 2024/12/27
 **/
@Component
public class SequenceProvider {

    @Autowired
    private IdGenerateProperties idGenerateProperties;

    private String getSequence(){
        return  " select id,name,startOffset,currentOffset,step,owner,createTime,updateTime" +
                " from "+idGenerateProperties.getTableName()+
                " where  name = #{name}";}

    /**
     * 获取序列号 初始化用
     */
    private String listSequence(){
        return  " select id,name,startOffset,currentOffset,step,owner,createTime,updateTime " +
                " from "+idGenerateProperties.getTableName()+
                " limit 10000";
    }

    /**
     * 预占offset
     */
    private String updateOffset(){
        return "update "+idGenerateProperties.getTableName()+
                " set currentOffset = nextOffset" +
                " where name = #{name} and currentOffset = #{currentOffset}";
    }
}

package com.xi.id.generator.mapper.provider;

import com.xi.id.generator.config.IdGenerateProperties;
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

    /**
     * 插入序列
     * @return
     */
    private String insertSequence(){
        return "insert into "+idGenerateProperties.getTableName()+
                "(id,name,startOffset,currentOffset,step,owner,createTime,updateTime) " +
                "values(#{id},#{name},#{startOffset},#{currentOffset},#{step},#{owner},#{createTime},#{updateTime})";
    }

    /**
     * 检查表是否存在
     * @return
     */
    private String showTable(){
        return "show tables like '"+idGenerateProperties.getTableName()+"'";
    }


    /**
     * 创建序列表
     * @return
     */
    private String createTable(){
        return "create table "+idGenerateProperties.getTableName()+
                "(id bigint(20) not null auto_increment," +
                "name varchar(255) not null," +
                "startOffset bigint(20) not null," +
                "currentOffset bigint(20) not null," +
                "step bigint(20) not null," +
                "owner varchar(255) ," +
                "createTime datetime not null," +
                "updateTime datetime not null," +
                "primary key(id)),unique key(name)";
    }
}

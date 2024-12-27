package com.xi.id.generator.mapper.entity;

import lombok.*;

import java.time.LocalDateTime;

/**
 * 序列号实体
 * @author LiuY
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class SequenceEntity {

    private Long id;

    private String name;

    private Long startOffset;

    private Long currentOffset;

    private Long step;

    private String owner;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}

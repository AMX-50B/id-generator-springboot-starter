package com.xi.id.generator.model;

import lombok.*;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 序列号 传输对象
 * @author LiuY
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Sequence {

    private String name;

    private Long enableOffset;

    private Long step;

    private AtomicLong offset;
}

package com.xi.id.generator.service.impl;

import com.xi.id.generator.config.IdGenerateProperties;
import com.xi.id.generator.mapper.SequenceMapper;
import com.xi.id.generator.mapper.entity.SequenceEntity;
import com.xi.id.generator.model.Sequence;
import com.xi.id.generator.service.IdGenerator;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * id业务接口 实现类
 * @author LiuY
 */
public class IdGeneratorImpl implements IdGenerator {

    private final SequenceMapper sequenceMapper;

    private final IdGenerateProperties idGenerateProperties;

    private static final Map<String, Lock>  SEQUENCE_LOCK_MAP = new ConcurrentHashMap<>();

    private static final Map<String, Sequence> OFFSET_MAP = new ConcurrentHashMap<>();

    public IdGeneratorImpl(SequenceMapper sequenceMapper,IdGenerateProperties idGenerateProperties) {
        this.sequenceMapper = sequenceMapper;
        this.idGenerateProperties = idGenerateProperties;
    }

    @Override
    public Long nextId() {
        return nextId(idGenerateProperties.getDefaultSequence());
    }

    @Override
    public Long nextId(String sequenceName) {
        Sequence sequence = OFFSET_MAP.get(sequenceName);
        Long id = null;
        if (sequence != null) {
            AtomicLong counter = sequence.getOffset();
            if (counter == null) {
                throw new IllegalArgumentException("counter -> " + sequenceName + " is not exist");
            }
            id = counter.getAndIncrement();
            if (id < sequence.getEnableOffset()) {
                return id;
            }
        }
        Lock lock = SEQUENCE_LOCK_MAP.computeIfAbsent(sequenceName,  key -> new ReentrantLock());
        boolean locked = lock.tryLock();
        if (locked) {
            try {
                Sequence currSequence = OFFSET_MAP.get(sequenceName);
                // double check
                if (currSequence == null || id == null || currSequence.getEnableOffset() < id) {
                    this.refreshOffset(sequenceName);
                }
            } finally {
                lock.unlock();
            }
            return this.nextId(sequenceName);
        }
        if (sequence == null) {
            throw new IllegalArgumentException("map sequence -> " + sequenceName + " is not exist");
        }
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            // nothing
        }
        return this.nextId(sequenceName);
    }

    /**
     * 更新偏移量
     * @param sequenceName
     */
    private void refreshOffset(String sequenceName) {
        SequenceEntity currentSequence = this.sequenceMapper.getSequence(sequenceName);
        if(currentSequence == null) {
            throw new IllegalArgumentException("db sequenceName -> " + sequenceName + " is not exist");
        }
        long nextOffset = currentSequence.getCurrentOffset() + currentSequence.getStep();
        int i = this.sequenceMapper.updateOffset(sequenceName, nextOffset, currentSequence.getCurrentOffset());
        if (i > 0) {
            Sequence offset = Sequence.builder()
                    .name(sequenceName)
                    .enableOffset(nextOffset)
                    .step(currentSequence.getStep())
                    .offset(new AtomicLong(currentSequence.getCurrentOffset()))
                    .build();
            OFFSET_MAP.put(sequenceName, offset);
            return;
        }
        this.refreshOffset(sequenceName);
    }

    @PostConstruct
    public void init() {
        List<SequenceEntity> sequences = this.sequenceMapper.listSequence();
        for (SequenceEntity sequence : sequences) {
            this.refreshOffset(sequence.getName());
        }
    }

}

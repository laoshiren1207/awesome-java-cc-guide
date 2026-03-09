package com.example.converter;

import com.example.entity.Candidate;
import com.example.pojo.candidate.CandidateSaveParam;
import com.example.pojo.candidate.CandidateUpdateParam;
import com.example.pojo.candidate.CandidateVO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

/**
 * 候选人对象转换器
 */
@Mapper(componentModel = "spring")
public interface CandidateConverter {

    /**
     * SaveParam → Entity
     */
    Candidate toEntity(CandidateSaveParam param);

    /**
     * UpdateParam → Entity（忽略 null 字段，实现增量更新）
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(CandidateUpdateParam param, @MappingTarget Candidate candidate);

    /**
     * Entity → VO
     */
    CandidateVO toVO(Candidate candidate);

    /**
     * Entity 列表 → VO 列表
     */
    List<CandidateVO> toVOList(List<Candidate> candidates);
}

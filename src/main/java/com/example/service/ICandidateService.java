package com.example.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.Candidate;
import com.example.pojo.candidate.CandidatePageParam;

/**
 * 候选人业务逻辑接口（只允许调用本实体 Mapper，禁止跨实体调用）
 */
public interface ICandidateService {

    /**
     * 新增候选人，返回自增主键
     */
    Long save(Candidate candidate);

    /**
     * 按主键更新候选人（仅更新非 null 字段）
     */
    void updateById(Candidate candidate);

    /**
     * 按主键逻辑删除
     */
    void removeById(Long id);

    /**
     * 按主键查询（已逻辑删除的不返回）
     */
    Candidate getById(Long id);

    /**
     * 分页查询
     */
    Page<Candidate> pageByParam(CandidatePageParam param);
}

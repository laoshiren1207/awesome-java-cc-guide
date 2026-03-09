package com.example.service;

import com.example.entity.CandidateShare;

/**
 * 候选人分享码业务逻辑接口（只允许调用本实体 Mapper，禁止跨实体调用）
 */
public interface ICandidateShareService {

    /**
     * 为候选人生成分享码
     */
    CandidateShare generateShareCode(Long candidateId);

    /**
     * 根据分享码查询分享记录（惰性校验有效期，仅返回有效状态）
     */
    CandidateShare getByShareCode(String shareCode);

    /**
     * 将分享码标记为已投递
     */
    void markDelivered(Long id);
}

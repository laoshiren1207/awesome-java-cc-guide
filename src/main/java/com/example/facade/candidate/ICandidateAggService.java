package com.example.facade.candidate;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.pojo.candidate.CandidateDeliverParam;
import com.example.pojo.candidate.CandidatePageParam;
import com.example.pojo.candidate.CandidateSaveParam;
import com.example.pojo.candidate.CandidateUpdateParam;
import com.example.pojo.candidate.CandidateVO;

/**
 * 候选人业务编排接口
 */
public interface ICandidateAggService {

    /**
     * 新增候选人，返回自增主键
     */
    Long save(CandidateSaveParam param);

    /**
     * 更新候选人信息
     */
    void update(Long id, CandidateUpdateParam param);

    /**
     * 逻辑删除候选人
     */
    void remove(Long id);

    /**
     * 按主键查询候选人
     */
    CandidateVO getById(Long id);

    /**
     * 分页查询候选人列表
     */
    Page<CandidateVO> page(CandidatePageParam param);

    /**
     * 为候选人生成分享码
     */
    String generateShareCode(Long candidateId);

    /**
     * 通过分享码获取候选人信息
     */
    CandidateVO getByShareCode(String shareCode);

    /**
     * 通过分享码投递简历
     */
    void deliverByShareCode(String shareCode, CandidateDeliverParam param);
}

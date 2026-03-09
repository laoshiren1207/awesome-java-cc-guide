package com.example.facade.candidate;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.exception.BusinessException;
import com.example.converter.CandidateConverter;
import com.example.entity.Candidate;
import com.example.entity.CandidateShare;
import com.example.pojo.candidate.CandidateDeliverParam;
import com.example.pojo.candidate.CandidatePageParam;
import com.example.pojo.candidate.CandidateSaveParam;
import com.example.pojo.candidate.CandidateUpdateParam;
import com.example.pojo.candidate.CandidateVO;
import com.example.service.ICandidateService;
import com.example.service.ICandidateShareService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 候选人业务编排实现
 */
@Slf4j
@Service
@AllArgsConstructor
public class CandidateAggServiceImpl implements ICandidateAggService {

    private final ICandidateService candidateService;
    private final ICandidateShareService candidateShareService;
    private final CandidateConverter candidateConverter;

    @Override
    public Long save(CandidateSaveParam param) {
        Candidate candidate = candidateConverter.toEntity(param);
        return candidateService.save(candidate);
    }

    @Override
    public void update(Long id, CandidateUpdateParam param) {
        Candidate candidate = candidateService.getById(id);
        if (candidate == null) {
            throw new BusinessException("候选人不存在");
        }
        candidateConverter.updateEntity(param, candidate);
        candidate.setId(id);
        candidateService.updateById(candidate);
    }

    @Override
    public void remove(Long id) {
        candidateService.removeById(id);
    }

    @Override
    public CandidateVO getById(Long id) {
        Candidate candidate = candidateService.getById(id);
        if (candidate == null) {
            throw new BusinessException("候选人不存在");
        }
        return candidateConverter.toVO(candidate);
    }

    @Override
    public Page<CandidateVO> page(CandidatePageParam param) {
        Page<Candidate> entityPage = candidateService.pageByParam(param);
        Page<CandidateVO> voPage = new Page<>(entityPage.getCurrent(), entityPage.getSize(), entityPage.getTotal());
        voPage.setRecords(candidateConverter.toVOList(entityPage.getRecords()));
        return voPage;
    }

    @Override
    public String generateShareCode(Long candidateId) {
        Candidate candidate = candidateService.getById(candidateId);
        if (candidate == null) {
            throw new BusinessException("候选人不存在");
        }
        CandidateShare share = candidateShareService.generateShareCode(candidateId);
        return share.getShareCode();
    }

    @Override
    public CandidateVO getByShareCode(String shareCode) {
        CandidateShare share = candidateShareService.getByShareCode(shareCode);
        Candidate candidate = candidateService.getById(share.getCandidateId());
        if (candidate == null) {
            throw new BusinessException("候选人不存在");
        }
        return candidateConverter.toVO(candidate);
    }

    @Override
    public void deliverByShareCode(String shareCode, CandidateDeliverParam param) {
        CandidateShare share = candidateShareService.getByShareCode(shareCode);
        Candidate candidate = candidateService.getById(share.getCandidateId());
        if (candidate == null) {
            throw new BusinessException("候选人不存在");
        }
        if (param.getPositionId() != null) {
            candidate.setPositionId(param.getPositionId());
        }
        if (param.getResumeUrl() != null) {
            candidate.setResumeUrl(param.getResumeUrl());
        }
        candidate.setApplyTime(LocalDateTime.now());
        candidateService.updateById(candidate);
        candidateShareService.markDelivered(share.getId());
        log.info("候选人[{}]通过分享码[{}]投递简历成功", candidate.getName(), shareCode);
    }
}

package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.exception.BusinessException;
import com.example.entity.Candidate;
import com.example.mapper.CandidateMapper;
import com.example.pojo.candidate.CandidatePageParam;
import com.example.service.ICandidateService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * 候选人业务逻辑实现
 */
@Slf4j
@Service
@AllArgsConstructor
public class CandidateServiceImpl implements ICandidateService {

    private final CandidateMapper candidateMapper;

    @Override
    public Long save(Candidate candidate) {
        candidateMapper.insert(candidate);
        return candidate.getId();
    }

    @Override
    public void updateById(Candidate candidate) {
        int rows = candidateMapper.updateById(candidate);
        if (rows == 0) {
            throw new BusinessException("候选人不存在或已被删除");
        }
    }

    @Override
    public void removeById(Long id) {
        int rows = candidateMapper.deleteById(id);
        if (rows == 0) {
            throw new BusinessException("候选人不存在或已被删除");
        }
    }

    @Override
    public Candidate getById(Long id) {
        return candidateMapper.selectById(id);
    }

    @Override
    public Page<Candidate> pageByParam(CandidatePageParam param) {
        LambdaQueryWrapper<Candidate> wrapper = new LambdaQueryWrapper<Candidate>()
                .like(StringUtils.isNotBlank(param.getName()), Candidate::getName, param.getName())
                .eq(StringUtils.isNotBlank(param.getChannel()), Candidate::getChannel, param.getChannel())
                .eq(param.getPositionId() != null, Candidate::getPositionId, param.getPositionId())
                .orderByDesc(Candidate::getCreateTime);

        return candidateMapper.selectPage(new Page<>(param.getPageNum(), param.getPageSize()), wrapper);
    }
}

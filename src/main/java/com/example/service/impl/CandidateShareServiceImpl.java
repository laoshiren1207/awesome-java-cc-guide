package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.exception.BusinessException;
import com.example.entity.CandidateShare;
import com.example.mapper.CandidateShareMapper;
import com.example.enums.candidate.ShareStateEnum;
import com.example.service.ICandidateShareService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 候选人分享码业务逻辑实现
 */
@Slf4j
@Service
@AllArgsConstructor
public class CandidateShareServiceImpl
        extends ServiceImpl<CandidateShareMapper, CandidateShare>
        implements ICandidateShareService {

    private static final String CODE_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int CODE_LENGTH = 6;
    private static final SecureRandom RANDOM = new SecureRandom();

    private final CandidateShareMapper candidateShareMapper;

    @Override
    public CandidateShare generateShareCode(Long candidateId) {
        // 查找该候选人是否已有有效的分享码
        LambdaQueryWrapper<CandidateShare> existWrapper = new LambdaQueryWrapper<CandidateShare>()
                .eq(CandidateShare::getCandidateId, candidateId)
                .eq(CandidateShare::getShareState, ShareStateEnum.ACTIVE.getCode())
                .last(" limit 1");
        CandidateShare existing = this.getOne(existWrapper);
        if (existing != null) {
            // 惰性过期检查
            if (existing.getExpireTime() != null && existing.getExpireTime().isBefore(LocalDateTime.now())) {
                existing.setShareState(ShareStateEnum.EXPIRED.getCode());
                existing.setStateTime(LocalDateTime.now());
                this.updateById(existing);
            } else {
                return existing;
            }
        }

        // 生成新的分享码
        CandidateShare share = new CandidateShare();
        share.setCandidateId(candidateId);
        share.setExpireTime(LocalDate.now().atStartOfDay().plusDays(8).minusSeconds(1L));
        share.setShareState(ShareStateEnum.ACTIVE.getCode());
        share.setStateTime(LocalDateTime.now());

        String code = generateUniqueCode();
        share.setShareCode(code);
        save(share);
        return share;
    }

    /**
     * 生成6位随机英文数字码，在有效期内唯一
     */
    private String generateUniqueCode() {
        for (int i = 0; i < 10; i++) {
            String code = randomCode();
            LambdaQueryWrapper<CandidateShare> wrapper = new LambdaQueryWrapper<CandidateShare>()
                    .eq(CandidateShare::getShareCode, code)
                    .eq(CandidateShare::getShareState, ShareStateEnum.ACTIVE.getCode());
            if (this.count(wrapper) == 0) {
                return code;
            }
        }
        throw new BusinessException("分享码生成失败，请重试");
    }

    private String randomCode() {
        StringBuilder sb = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            sb.append(CODE_CHARS.charAt(RANDOM.nextInt(CODE_CHARS.length())));
        }
        return sb.toString();
    }

    @Override
    public CandidateShare getByShareCode(String shareCode) {
        LambdaQueryWrapper<CandidateShare> wrapper = new LambdaQueryWrapper<CandidateShare>()
                .eq(CandidateShare::getShareCode, shareCode)
                .eq(CandidateShare::getShareState, ShareStateEnum.ACTIVE.getCode())
                .last("limit 1");
        CandidateShare share = this.getOne(wrapper);
        if (share == null) {
            throw new BusinessException("分享码无效");
        }
        // 惰性过期检查
        if (share.getExpireTime() != null && share.getExpireTime().isBefore(LocalDateTime.now())) {
            share.setShareState(ShareStateEnum.EXPIRED.getCode());
            share.setStateTime(LocalDateTime.now());
            this.updateById(share);
            throw new BusinessException("分享码已过期");
        }
        return share;
    }

    @Override
    public void markDelivered(Long id) {
        CandidateShare share = this.getById(id);
        if (share == null) {
            throw new BusinessException("分享记录不存在");
        }
        share.setShareState(ShareStateEnum.DELIVERED.getCode());
        share.setStateTime(LocalDateTime.now());
        this.updateById(share);
    }
}

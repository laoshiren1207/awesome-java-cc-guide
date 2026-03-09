package com.example.controller.candidate;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.facade.candidate.ICandidateAggService;
import com.example.pojo.R;
import com.example.pojo.candidate.CandidateDeliverParam;
import com.example.pojo.candidate.CandidatePageParam;
import com.example.pojo.candidate.CandidateSaveParam;
import com.example.pojo.candidate.CandidateUpdateParam;
import com.example.pojo.candidate.CandidateVO;
import com.example.utils.ValidUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 候选人管理接口
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/candidate")
public class CandidateController {

    private final ICandidateAggService candidateAggService;

    /**
     * 新增候选人
     */
    @PostMapping
    public R<Long> save(@RequestBody CandidateSaveParam param) {
        ValidUtil.validate(param);
        Long id = candidateAggService.save(param);
        return R.ok(id);
    }

    /**
     * 更新候选人信息
     */
    @PutMapping("/{id}")
    public R<Void> update(@PathVariable Long id, @RequestBody CandidateUpdateParam param) {
        candidateAggService.update(id, param);
        return R.ok(null);
    }

    /**
     * 逻辑删除候选人
     */
    @DeleteMapping("/{id}")
    public R<Void> remove(@PathVariable Long id) {
        candidateAggService.remove(id);
        return R.ok(null);
    }

    /**
     * 按主键查询候选人
     */
    @GetMapping("/{id}")
    public R<CandidateVO> getById(@PathVariable Long id) {
        CandidateVO vo = candidateAggService.getById(id);
        return R.ok(vo);
    }

    /**
     * 分页查询候选人列表
     */
    @GetMapping("/page")
    public R<Page<CandidateVO>> page(CandidatePageParam param) {
        ValidUtil.validate(param);
        Page<CandidateVO> page = candidateAggService.page(param);
        return R.ok(page);
    }

    /**
     * 为候选人生成分享码
     */
    @PostMapping("/{id}/share")
    public R<String> generateShareCode(@PathVariable Long id) {
        String shareCode = candidateAggService.generateShareCode(id);
        return R.ok(shareCode);
    }

    /**
     * 通过分享码获取候选人信息
     */
    @GetMapping("/share/{code}")
    public R<CandidateVO> getByShareCode(@PathVariable String code) {
        CandidateVO vo = candidateAggService.getByShareCode(code);
        return R.ok(vo);
    }

    /**
     * 通过分享码投递简历
     */
    @PostMapping("/share/{code}/deliver")
    public R<Void> deliverByShareCode(@PathVariable String code, @RequestBody CandidateDeliverParam param) {
        candidateAggService.deliverByShareCode(code, param);
        return R.ok(null);
    }
}

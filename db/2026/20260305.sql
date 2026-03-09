CREATE TABLE `candidate`
(
    `id`          bigint       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name`        varchar(100) NOT NULL COMMENT '候选人姓名',
    `phone`       varchar(20)  NOT NULL COMMENT '手机号',
    `resume_url`  varchar(500)          DEFAULT NULL COMMENT '简历URL',
    `apply_time`  datetime     NOT NULL COMMENT '投递时间',
    `channel`     varchar(50)  NOT NULL COMMENT '候选人APP渠道（如：BOSS、智联、猎聘、拉勾等）',
    `position_id` bigint       NOT NULL COMMENT '申请职位ID',
    `create_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `deleted`     tinyint      NOT NULL DEFAULT '0' COMMENT '逻辑删除（0:未删除 1:已删除）',
    PRIMARY KEY (`id`),
    KEY `idx_position_id` (`position_id`),
    KEY `idx_channel` (`channel`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='候选人信息表';

-- 候选人分享码表
CREATE TABLE IF NOT EXISTS `candidate_share` (
                                                 `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
                                                 `candidate_id` BIGINT NOT NULL COMMENT '候选人ID',
                                                 `share_code` VARCHAR(6) NOT NULL COMMENT '分享码（6位随机字母数字）',
    `expire_time` DATETIME DEFAULT NULL COMMENT '过期时间',
    `share_state` INT NOT NULL DEFAULT 10 COMMENT '分享状态：10-有效，20-已过期，30-已投递',
    `state_time` DATETIME DEFAULT NULL COMMENT '状态变更时间',
    `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
    `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_share_code_state` (`share_code`, `share_state`),
    KEY `idx_candidate_id` (`candidate_id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='候选人分享码';

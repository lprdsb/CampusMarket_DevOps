-- phpMyAdmin SQL Dump
-- version 5.2.2
-- https://www.phpmyadmin.net/
--
-- 主机： mysql
-- 生成日期： 2025-08-26 01:24:40
-- 服务器版本： 8.4.5
-- PHP 版本： 8.2.28

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- 数据库： `market`
--

-- --------------------------------------------------------

--
-- 表的结构 `category`
--

CREATE TABLE `category` (
                            `id` int NOT NULL COMMENT '商品类别ID',
                            `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '类别名',
                            `is_use` tinyint(1) DEFAULT NULL COMMENT '是否启用'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='商品类别表' ROW_FORMAT=DYNAMIC;

-- --------------------------------------------------------

--
-- 表的结构 `chatter`
--

CREATE TABLE `chatter` (
                           `id` int NOT NULL COMMENT '主键ID',
                           `sender_id` int NOT NULL COMMENT '发送者ID',
                           `receiver_id` int NOT NULL COMMENT '接收者ID',
                           `content` text COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '消息内容',
                           `is_read` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否已读（0:未读 1:已读）',
                           `create_time` datetime NOT NULL COMMENT '发送时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='互动消息表';

-- --------------------------------------------------------

--
-- 表的结构 `complaint`
--

CREATE TABLE `complaint` (
                             `id` int NOT NULL,
                             `complainant_id` int NOT NULL,
                             `target_id` int DEFAULT NULL,
                             `order_id` int DEFAULT NULL,
                             `content` varchar(500) COLLATE utf8mb4_general_ci NOT NULL,
                             `status` varchar(20) COLLATE utf8mb4_general_ci DEFAULT 'pending',
                             `create_time` datetime DEFAULT NULL,
                             `handle_time` datetime DEFAULT NULL,
                             `product_id` int DEFAULT NULL COMMENT 'product_id'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- 表的结构 `evaluations`
--

CREATE TABLE `evaluations` (
                               `id` int UNSIGNED NOT NULL COMMENT '主键',
                               `parent_id` int DEFAULT NULL COMMENT '父级评论ID',
                               `commenter_id` int DEFAULT NULL COMMENT '评论者ID',
                               `replier_id` int DEFAULT NULL COMMENT '回复者ID',
                               `content_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '内容类型',
                               `content_id` int DEFAULT NULL COMMENT '内容ID',
                               `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '评论内容',
                               `upvote_list` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin COMMENT '点赞列表，以","分割',
                               `create_time` datetime DEFAULT NULL COMMENT '评论时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- --------------------------------------------------------

--
-- 表的结构 `interaction`
--

CREATE TABLE `interaction` (
                               `id` int NOT NULL COMMENT '互动行为主键ID',
                               `user_id` int DEFAULT NULL COMMENT '用户ID',
                               `product_id` int DEFAULT NULL COMMENT '商品ID',
                               `type` int DEFAULT NULL COMMENT '行为类型（1：收藏；2：浏览；3： 想要）',
                               `create_time` datetime DEFAULT NULL COMMENT '行为互动时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='互动行为信息表' ROW_FORMAT=DYNAMIC;

-- --------------------------------------------------------

--
-- 表的结构 `message`
--

CREATE TABLE `message` (
                           `id` int NOT NULL COMMENT '消息表主键ID',
                           `user_id` int DEFAULT NULL COMMENT '接收者用户ID',
                           `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '消息体',
                           `is_read` tinyint(1) DEFAULT NULL COMMENT '是否已经阅读',
                           `create_time` datetime DEFAULT NULL COMMENT '发送时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='消息表' ROW_FORMAT=DYNAMIC;

-- --------------------------------------------------------

--
-- 表的结构 `operation_log`
--

CREATE TABLE `operation_log` (
                                 `id` int NOT NULL COMMENT '行为日志主键ID',
                                 `user_id` int DEFAULT NULL COMMENT '用户ID',
                                 `detail` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '描述',
                                 `create_time` datetime DEFAULT NULL COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- --------------------------------------------------------

--
-- 表的结构 `orders`
--

CREATE TABLE `orders` (
                          `id` int NOT NULL COMMENT '订单主键',
                          `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '订单号',
                          `detail` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注',
                          `user_id` int DEFAULT NULL COMMENT '用户ID',
                          `product_id` int DEFAULT NULL COMMENT '商品ID',
                          `buy_price` decimal(10,2) DEFAULT NULL COMMENT '购买时的价格',
                          `buy_number` int DEFAULT NULL COMMENT '购买数量',
                          `trade_status` tinyint(1) DEFAULT NULL COMMENT '交易状态',
                          `trade_time` datetime DEFAULT NULL COMMENT '交易时间',
                          `refund_status` tinyint(1) DEFAULT NULL COMMENT '退款状态',
                          `refund_time` datetime DEFAULT NULL COMMENT '退款时间',
                          `is_refund_confirm` tinyint(1) DEFAULT NULL COMMENT '退款是否已经确认(卖家进行的确认)',
                          `create_time` datetime DEFAULT NULL COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='订单信息表' ROW_FORMAT=DYNAMIC;

-- --------------------------------------------------------

--
-- 表的结构 `product`
--

CREATE TABLE `product` (
                           `id` int NOT NULL COMMENT '商品的ID，主键自增',
                           `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '商品名',
                           `detail` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin COMMENT '商品的简介',
                           `cover_list` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin COMMENT '商品封面的列表',
                           `old_level` int DEFAULT NULL COMMENT '新旧程度',
                           `category_id` int DEFAULT NULL COMMENT '所属商品分类的ID',
                           `user_id` int DEFAULT NULL COMMENT '发布者用户ID',
                           `inventory` int DEFAULT NULL COMMENT '库存',
                           `price` decimal(10,2) DEFAULT NULL COMMENT '价格',
                           `is_bargain` tinyint(1) DEFAULT NULL COMMENT '是否支持砍价',
                           `create_time` datetime DEFAULT NULL COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='商品信息表' ROW_FORMAT=DYNAMIC;

-- --------------------------------------------------------

--
-- 表的结构 `star`
--

CREATE TABLE `star` (
                        `id` int NOT NULL COMMENT '关注行为主键ID',
                        `user1_id` int DEFAULT NULL COMMENT '关注者ID',
                        `user2_id` int DEFAULT NULL COMMENT '被关注者ID',
                        `create_time` datetime DEFAULT NULL COMMENT '行为互动时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='关注行为信息表' ROW_FORMAT=DYNAMIC;

-- --------------------------------------------------------

--
-- 表的结构 `user`
--

CREATE TABLE `user` (
                        `id` int NOT NULL COMMENT '用户编号',
                        `user_account` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户账号',
                        `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户昵称',
                        `user_pwd` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户密码',
                        `user_avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户头像',
                        `user_email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户邮箱',
                        `user_role` int DEFAULT NULL COMMENT '用户角色',
                        `is_login` tinyint(1) DEFAULT NULL COMMENT '可登录状态(0：可用，1：不可用)',
                        `is_word` tinyint(1) DEFAULT NULL COMMENT '禁言状态(0：可用，1：不可用)',
                        `last_login_time` datetime DEFAULT NULL COMMENT '上一次登录时间',
                        `create_time` datetime DEFAULT NULL COMMENT '用户注册时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;

--
-- 转储表的索引
--

--
-- 表的索引 `category`
--
ALTER TABLE `category`
    ADD PRIMARY KEY (`id`) USING BTREE;

--
-- 表的索引 `chatter`
--
ALTER TABLE `chatter`
    ADD PRIMARY KEY (`id`),
    ADD KEY `idx_sender` (`sender_id`),
    ADD KEY `idx_receiver` (`receiver_id`),
    ADD KEY `idx_create_time` (`create_time`);

--
-- 表的索引 `complaint`
--
ALTER TABLE `complaint`
    ADD PRIMARY KEY (`id`);

--
-- 表的索引 `evaluations`
--
ALTER TABLE `evaluations`
    ADD PRIMARY KEY (`id`) USING BTREE;

--
-- 表的索引 `interaction`
--
ALTER TABLE `interaction`
    ADD PRIMARY KEY (`id`) USING BTREE,
    ADD KEY `interaction_user_FK` (`user_id`);

--
-- 表的索引 `message`
--
ALTER TABLE `message`
    ADD PRIMARY KEY (`id`) USING BTREE,
    ADD KEY `message_user_FK` (`user_id`);

--
-- 表的索引 `operation_log`
--
ALTER TABLE `operation_log`
    ADD PRIMARY KEY (`id`) USING BTREE,
    ADD KEY `operation_log_user_FK` (`user_id`);

--
-- 表的索引 `orders`
--
ALTER TABLE `orders`
    ADD PRIMARY KEY (`id`) USING BTREE,
    ADD KEY `orders_user_FK` (`user_id`);

--
-- 表的索引 `product`
--
ALTER TABLE `product`
    ADD PRIMARY KEY (`id`) USING BTREE,
    ADD KEY `product_user_FK` (`user_id`);

--
-- 表的索引 `star`
--
ALTER TABLE `star`
    ADD PRIMARY KEY (`id`) USING BTREE,
    ADD KEY `star_user_FK` (`user1_id`),
    ADD KEY `star_user_FK_1` (`user2_id`);

--
-- 表的索引 `user`
--
ALTER TABLE `user`
    ADD PRIMARY KEY (`id`) USING BTREE;

--
-- 在导出的表使用AUTO_INCREMENT
--

--
-- 使用表AUTO_INCREMENT `category`
--
ALTER TABLE `category`
    MODIFY `id` int NOT NULL AUTO_INCREMENT COMMENT '商品类别ID';

--
-- 使用表AUTO_INCREMENT `chatter`
--
ALTER TABLE `chatter`
    MODIFY `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID';

--
-- 使用表AUTO_INCREMENT `complaint`
--
ALTER TABLE `complaint`
    MODIFY `id` int NOT NULL AUTO_INCREMENT;

--
-- 使用表AUTO_INCREMENT `evaluations`
--
ALTER TABLE `evaluations`
    MODIFY `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键';

--
-- 使用表AUTO_INCREMENT `interaction`
--
ALTER TABLE `interaction`
    MODIFY `id` int NOT NULL AUTO_INCREMENT COMMENT '互动行为主键ID';

--
-- 使用表AUTO_INCREMENT `message`
--
ALTER TABLE `message`
    MODIFY `id` int NOT NULL AUTO_INCREMENT COMMENT '消息表主键ID';

--
-- 使用表AUTO_INCREMENT `operation_log`
--
ALTER TABLE `operation_log`
    MODIFY `id` int NOT NULL AUTO_INCREMENT COMMENT '行为日志主键ID';

--
-- 使用表AUTO_INCREMENT `orders`
--
ALTER TABLE `orders`
    MODIFY `id` int NOT NULL AUTO_INCREMENT COMMENT '订单主键';

--
-- 使用表AUTO_INCREMENT `product`
--
ALTER TABLE `product`
    MODIFY `id` int NOT NULL AUTO_INCREMENT COMMENT '商品的ID，主键自增';

--
-- 使用表AUTO_INCREMENT `star`
--
ALTER TABLE `star`
    MODIFY `id` int NOT NULL AUTO_INCREMENT COMMENT '关注行为主键ID';

--
-- 使用表AUTO_INCREMENT `user`
--
ALTER TABLE `user`
    MODIFY `id` int NOT NULL AUTO_INCREMENT COMMENT '用户编号';

--
-- 限制导出的表
--

--
-- 限制表 `chatter`
--
ALTER TABLE `chatter`
    ADD CONSTRAINT `chatter_user_FK` FOREIGN KEY (`sender_id`) REFERENCES `user` (`id`),
    ADD CONSTRAINT `chatter_user_FK_1` FOREIGN KEY (`receiver_id`) REFERENCES `user` (`id`);

--
-- 限制表 `interaction`
--
ALTER TABLE `interaction`
    ADD CONSTRAINT `interaction_user_FK` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);

--
-- 限制表 `message`
--
ALTER TABLE `message`
    ADD CONSTRAINT `message_user_FK` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);

--
-- 限制表 `operation_log`
--
ALTER TABLE `operation_log`
    ADD CONSTRAINT `operation_log_user_FK` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);

--
-- 限制表 `orders`
--
ALTER TABLE `orders`
    ADD CONSTRAINT `orders_user_FK` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);

--
-- 限制表 `product`
--
ALTER TABLE `product`
    ADD CONSTRAINT `product_user_FK` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);

--
-- 限制表 `star`
--
ALTER TABLE `star`
    ADD CONSTRAINT `star_user_FK` FOREIGN KEY (`user1_id`) REFERENCES `user` (`id`),
    ADD CONSTRAINT `star_user_FK_1` FOREIGN KEY (`user2_id`) REFERENCES `user` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

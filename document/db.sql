CREATE DATABASE IF NOT EXISTS eam_dev;
USE eam_dev;

CREATE TABLE IF NOT EXISTS `admin_user` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `username` VARCHAR ( 255 ) NOT NULL COMMENT '用户名',
    `password_hash` VARCHAR ( 255 ) NOT NULL COMMENT '密码',
    `nickname` VARCHAR ( 255 )  NOT NULL COMMENT '昵称',
    `avatar` VARCHAR ( 255 )  NOT NULL DEFAULT '' COMMENT '头像',
    `phone` VARCHAR ( 50 )  NOT NULL DEFAULT '' COMMENT '手机',
    `email` VARCHAR ( 255 )  NOT NULL DEFAULT '' COMMENT '邮箱',
    `job` VARCHAR ( 50 )  NOT NULL DEFAULT '' COMMENT '职工',
    `sex` VARCHAR ( 50 )  NOT NULL DEFAULT '男' COMMENT '性别',
    `status` INT NOT NULL DEFAULT 0,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY ( `id` ) USING BTREE
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4  COMMENT='用户表';


CREATE TABLE IF NOT EXISTS `admin_user_token` (
    `id` int NOT NULL AUTO_INCREMENT,
    `user_id` int NOT NULL COMMENT '用户表id',
    `token` varchar(255) NOT NULL COMMENT 'token',
    `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `status` INT NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户Token关联表';


CREATE TABLE IF NOT EXISTS `admin_role` (
    `id` int NOT NULL AUTO_INCREMENT,
    `name` varchar(255) NOT NULL COMMENT '角色名',
    `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `status` INT NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';


CREATE TABLE IF NOT EXISTS `admin_user_role` (
    `id` int NOT NULL AUTO_INCREMENT,
    `user_id` int NOT NULL COMMENT '用户表ID',
    `role_id` int NOT NULL COMMENT '角色表ID',
    `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `status` INT NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';


CREATE TABLE IF NOT EXISTS `admin_permissions` (
    `id` int NOT NULL AUTO_INCREMENT,
    `name` varchar(255)  NOT NULL COMMENT '权限名称',
    `tag` varchar(255) NOT NULL DEFAULT '' COMMENT '类别标记',
    `code` varchar(100) NOT NULL DEFAULT '' COMMENT '权限标记',
    `created_at` timestamp(5) NOT NULL DEFAULT CURRENT_TIMESTAMP(5) COMMENT '创建时间',
    `updated_at` datetime(5) DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(5) COMMENT '更新时间',
    `status` INT NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=107 DEFAULT CHARSET=utf8mb4 COMMENT='权限表';


CREATE TABLE IF NOT EXISTS `admin_role_permission` (
    `id` int NOT NULL AUTO_INCREMENT,
    `role_id` int NOT NULL COMMENT '角色表id',
    `permission_id` int NOT NULL COMMENT '权限表id',
    `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `status` INT NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4  COMMENT='角色权限关系表';


CREATE TABLE IF NOT EXISTS `admin_department` (
    `id` int NOT NULL AUTO_INCREMENT ,
    `parent_id` int NOT NULL DEFAULT 0 COMMENT '父级部门',
    `principal_person_id` int NOT NULL DEFAULT 0 COMMENT '负责人id',
    `name` varchar(100) NOT NULL COMMENT '部门名',
    `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `status` INT NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门表';


CREATE TABLE IF NOT EXISTS `admin_user_department` (
    `id` int NOT NULL AUTO_INCREMENT,
    `user_id` int NOT NULL COMMENT '用户表id',
    `department_id` int NOT NULL COMMENT '部门表id',
    `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `status` INT NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户部门关联表';


CREATE TABLE IF NOT EXISTS `todo_record` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `user_id` int(11) NOT NULL COMMENT '代办创建人id',
    `solve_id` int(11) NOT NULL COMMENT '解决人id',
    `title` varchar(100) NOT NULL COMMENT '代办标题',
    `description` varchar(100) NOT NULL DEFAULT '' COMMENT '代办描述',
    `priority` varchar(100) NOT NULL DEFAULT '0' COMMENT '优先级',
    `handle_description` varchar(100) NOT NULL DEFAULT '' COMMENT '完成代办描述',
    `start_at` datetime DEFAULT NULL COMMENT '开始处理时间',
    `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `tag` varchar(100) DEFAULT NULL COMMENT '标签',
    `status` int(11) NOT NULL DEFAULT '0',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='代办表';


CREATE TABLE IF NOT EXISTS `asset_category` (
    `id` int NOT NULL AUTO_INCREMENT,
    `parent_id` int NOT NULL DEFAULT 0 COMMENT '父级分类id',
    `name` varchar(100)  NOT NULL DEFAULT '' COMMENT '分类名称',
    `description` varchar(100)  NOT NULL DEFAULT '' COMMENT '分类描述',
    `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `status` INT NOT NULL DEFAULT 0,
    `path` varchar(100) NOT NULL DEFAULT '' COMMENT '分类id路径',
    `flag` int (11) NOT NULL DEFAULT 0 COMMENT '分类标识',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4  COMMENT='资产分类表';


CREATE TABLE IF NOT EXISTS `fixed_asset` (
    `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '资产id',
    `asset_category_id` int(11) NOT NULL COMMENT '资产分类id',
    `flag` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '资产分类',
    `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '归属人',
    `lend_user_id` int(11) NOT NULL DEFAULT '0' COMMENT '借用人id',
    `device_id` int(11) NOT NULL DEFAULT '0' COMMENT '归属设备id',
    `depreciate_id` int(11) NOT NULL DEFAULT '0' COMMENT '折旧模板id',
    `buy_router_id` int(11) NOT NULL DEFAULT '0' COMMENT '购入途径id',
    `vendor_id` int(11) NOT NULL DEFAULT '0' COMMENT '供应商id',
    `name` varchar(100) NOT NULL COMMENT '资产名',
    `description` varchar(100) DEFAULT '' COMMENT '资产描述',
    `out_price` decimal(11,2) NOT NULL DEFAULT '0.00' COMMENT '折旧后价格',
    `price` decimal(11,2) NOT NULL DEFAULT '0.00' COMMENT '价格',
    `mac` varchar(100) DEFAULT '' COMMENT 'mac地址',
    `ip` varchar(100) NOT NULL DEFAULT '' COMMENT 'ip地址',
    `specification` varchar(100) NOT NULL DEFAULT '' COMMENT '规格',
    `version` varchar(100) NOT NULL DEFAULT '' COMMENT '版本',
    `issue_id` int(11) NOT NULL DEFAULT '0' COMMENT '发行版本方式id',
    `warranty_number` int(11) NOT NULL DEFAULT '0' COMMENT '授权数量',
    `start_at` datetime(5) DEFAULT NULL COMMENT '开始使用时间',
    `end_at` datetime(5) DEFAULT NULL COMMENT '结束使用时间',
    `buy_at` datetime(5) DEFAULT NULL COMMENT '购入时间',
    `expired_at` datetime(5) DEFAULT NULL COMMENT '过保时间',
    `expired_date` bigint(20) DEFAULT '0' COMMENT '过保日期',
    `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `status` int(11) NOT NULL DEFAULT '1' COMMENT '归属状态',
    `malfunction_status` int(11) NOT NULL DEFAULT '0' COMMENT '故障状态',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资产表';


CREATE TABLE IF NOT EXISTS `fixed_asset_track` (
    `id` int NOT NULL AUTO_INCREMENT,
    `fixed_asset_id` int NOT NULL COMMENT '固定资产id',
    `user_id` int(11) NOT NULL DEFAULT 0 COMMENT '归属人id',
    `lend_user_id` int(11) NOT NULL DEFAULT 0 COMMENT '借用人id',
    `device_id` int NOT NULL DEFAULT 0 COMMENT '归属设备id',
    `binding_start_at` datetime DEFAULT NULL COMMENT '归属开始时间',
    `binding_end_at` datetime DEFAULT NULL COMMENT '归属中止时间',
    `lend_description` varchar(255) NOT NULL DEFAULT '' COMMENT '借用描述',
    `lend_start_at` datetime DEFAULT NULL COMMENT '借用开始时间',
    `lend_plan_return_at` datetime DEFAULT NULL COMMENT '借用预计归还时间',
    `lend_return_at` datetime DEFAULT NULL COMMENT '借用实际归还时间',
    `lend_end_description` varchar(255) NOT NULL DEFAULT '' COMMENT '归还描述',
    `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `status` INT NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资产归属记录表';


CREATE TABLE IF NOT EXISTS `asset_affiliate` (
    `id` int NOT NULL AUTO_INCREMENT,
    `asset_id` int NOT NULL COMMENT '软件id，配件id',
    `asset_affiliate` int NOT NULL COMMENT '设备id',
    `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `status` INT NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资产归属关系表';


CREATE TABLE IF NOT EXISTS `malfunction` (
    `id` int NOT NULL AUTO_INCREMENT,
    `fixed_asset_id` int NOT NULL COMMENT '资产id',
    `solve_id` int NOT NULL DEFAULT 0 COMMENT '维修人id',
    `malfunction_description` varchar(100) NOT NULL DEFAULT '' COMMENT '故障描述',
    `repair_description` varchar(100) NOT NULL DEFAULT '' COMMENT '维修描述',
    `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `status` INT NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='故障表';


CREATE TABLE IF NOT EXISTS `consumable` (
    `id` int NOT NULL AUTO_INCREMENT,
    `description` varchar(100)  NOT NULL DEFAULT '' COMMENT '描述',
    `name` varchar(100)  NOT NULL COMMENT '耗材名称',
    `specification` varchar(100)  NOT NULL DEFAULT '' COMMENT '规格',
    `unit_price` decimal(10,2) NOT NULL COMMENT '单价',
    `category_id` int NOT NULL DEFAULT 0 COMMENT '耗材分类id',
    `total` int NOT NULL DEFAULT 0 COMMENT '耗材数量',
    `vendor_id` int NOT NULL DEFAULT 0 COMMENT '厂商id',
    `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `status` INT NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='耗材表';


CREATE TABLE IF NOT EXISTS `consumable_track` (
    `id` int NOT NULL AUTO_INCREMENT,
    `consumable_id` int NOT NULL DEFAULT 0 COMMENT '耗材id',
    `recipient_id` int NOT NULL DEFAULT 0 COMMENT '领用人id',
    `out_number` int NOT NULL DEFAULT 0 COMMENT '出库数',
    `in_number` int NOT NULL DEFAULT 0 COMMENT '入库数',
    `in_warehouse_user_id` int NOT NULL DEFAULT 0 COMMENT '入库人id',
    `out_warehouse_user_id` int NOT NULL DEFAULT 0 COMMENT '出库人id',
    `int_description` varchar(100) DEFAULT NULL DEFAULT '' COMMENT '入库描述',
    `out_description` varchar(100) DEFAULT NULL DEFAULT '' COMMENT '出库描述',
    `buy_at` datetime DEFAULT NULL COMMENT '购买时间',
    `out_warehouse_at` datetime DEFAULT NULL COMMENT '出库时间',
    `in_warehouse_at` datetime DEFAULT NULL COMMENT '入库时间',
    `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `status` INT NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='耗材记录表';


CREATE TABLE IF NOT EXISTS `asset_check` (
    `id` int NOT NULL AUTO_INCREMENT COMMENT '盘点资产项目表',
    `asset_category_id` int NOT NULL DEFAULT 0 COMMENT '资产分类id',
    `duty_id` int NOT NULL DEFAULT 0 COMMENT '负责人id',
    `total_count` int NOT NULL DEFAULT 0 COMMENT '盘点总数',
    `win_count` int NOT NULL DEFAULT 0 COMMENT '盘盈数',
    `loss_count` int NOT NULL DEFAULT 0 COMMENT '盘亏数',
    `wait_count` int NOT NULL DEFAULT 0 COMMENT '待盘数',
    `end_at` datetime DEFAULT NULL COMMENT '实际完成时间',
    `predict_at` datetime DEFAULT NULL COMMENT '预计完成时间',
    `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `status` INT NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='盘点资产项目表';


CREATE TABLE IF NOT EXISTS `asset_check_detail` (
    `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '盘点描述',
    `check_id` int(11) NOT NULL DEFAULT '0' COMMENT '盘点项目id',
    `asset_id` int(11) NOT NULL DEFAULT '0' COMMENT '资产id',
    `asset_category_id` int(11) NOT NULL DEFAULT '0' COMMENT '资产分类id',
    `createdAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updatedAt` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `status` int(11) NOT NULL DEFAULT '0' COMMENT '盘点状态',
    `asset_name` varchar(255) NOT NULL DEFAULT '' COMMENT '资产名',
    `description` varchar(255) NOT NULL DEFAULT '' COMMENT '盘点描述',
    `check_user_id` varchar(255) NOT NULL DEFAULT '' COMMENT '盘点人id',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资产盘点详情表';


CREATE TABLE IF NOT EXISTS `message` (
    `id` int NOT NULL AUTO_INCREMENT,
    `user_id` int NOT NULL COMMENT '消息接受人',
    `content` varchar(100) NOT NULL COMMENT '内容',
    `send_user_id` int NOT NULL COMMENT '消息发送人',
    `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `status` INT NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消息表';


CREATE TABLE IF NOT EXISTS `buy_router` (
    `id` int NOT NULL AUTO_INCREMENT,
    `name` varchar(100) NOT NULL COMMENT '名称',
    `description` varchar(100) NOT NULL DEFAULT '' COMMENT '描述',
    `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `status` INT NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='购入途径表';


CREATE TABLE IF NOT EXISTS `vendor` (
    `id` int NOT NULL AUTO_INCREMENT,
    `name` varchar(100) NOT NULL DEFAULT '' COMMENT '厂商名',
    `description` varchar(100) NOT NULL DEFAULT '' COMMENT '描述',
    `location` varchar(100) NOT NULL DEFAULT '' COMMENT '所在地',
    `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `status` INT NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='厂商表';


CREATE TABLE IF NOT EXISTS `vendor_user` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `vendor_id` int(11) NOT NULL COMMENT '厂商id',
    `name` varchar(100) NOT NULL DEFAULT '' COMMENT '联系人姓名',
    `phone` varchar(100) NOT NULL DEFAULT '' COMMENT '联系人电话',
    `job` varchar(100) NOT NULL DEFAULT '' COMMENT '联系人职位',
    `email` varchar(100) NOT NULL DEFAULT '' COMMENT '联系人邮箱',
    `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `status` int(11) NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='厂商联系人';


CREATE TABLE IF NOT EXISTS `issue` (
    `id` int NOT NULL AUTO_INCREMENT,
    `name` varchar(100) NOT NULL DEFAULT '' COMMENT '发行名称',
    `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `status` INT NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='发行方式表';


CREATE TABLE IF NOT EXISTS `depreciation_template` (
    `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '折旧模板表',
    `depreciation_id` int(11) NOT NULL COMMENT '折旧项目id',
    `period` int(11) NOT NULL DEFAULT '0' COMMENT '周期',
    `measure` int(11) NOT NULL DEFAULT '0' COMMENT '尺度',
    `rate` varchar(255) NOT NULL DEFAULT '0' COMMENT '比率',
    `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `status` int(11) NOT NULL DEFAULT '0',
    `templateName` varchar(255) NOT NULL DEFAULT '' COMMENT '模板名称',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='折旧模板表';


CREATE TABLE IF NOT EXISTS `depreciation` (
    `id` int NOT NULL AUTO_INCREMENT,
    `description` varchar(255) NOT NULL DEFAULT '' COMMENT '描述',
    `name` varchar(255) NOT NULL DEFAULT '' COMMENT '折旧名称',
    `asset_category_id` int NOT NULL DEFAULT 0 COMMENT '资产类别id',
    `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `status` INT NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4  COMMENT='折旧表';


CREATE TABLE  IF NOT EXISTS `asset_check_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '盘点描述',
  `check_id` int(11) NOT NULL DEFAULT '0' COMMENT '盘点项目id',
  `asset_id` int(11) NOT NULL DEFAULT '0' COMMENT '资产id',
  `asset_category_id` int(11) NOT NULL DEFAULT '0' COMMENT '资产分类id',
  `createdAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updatedAt` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '盘点状态',
  `asset_name` varchar(255) NOT NULL DEFAULT '' COMMENT '资产名',
  `description` varchar(255) NOT NULL DEFAULT '' COMMENT '盘点描述',
  `check_user_id` varchar(255) NOT NULL DEFAULT '' COMMENT '盘点人id',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4  COMMENT='盘点详情表';

CREATE TABLE IF NOT EXISTS `service_error`(
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `asset_id` int(11) NOT NULL DEFAULT '0' COMMENT '资产ID',
    `description` varchar(255) NOT NULL DEFAULT '' COMMENT '异常说明',
    `start_at` datetime DEFAULT NULL COMMENT '开始时间',
    `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `status` int(11) NOT NULL DEFAULT '0',
    `error_status` int(11) NOT NULL DEFAULT '0' COMMENT '0异常 1已恢复',
    `end_at` datetime DEFAULT NULL COMMENT '异常修复时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4  COMMENT='服务异常表';

CREATE TABLE IF NOT EXISTS `service_affiliate`(
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `asset_id` int(11) NOT NULL DEFAULT '0' COMMENT '服务ID',
    `asset_affiliate` int(11) NOT NULL DEFAULT '0' COMMENT '绑定设备ID',
    `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `status` int(11) NOT NULL DEFAULT '0',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4  COMMENT='服务绑定设备表';

CREATE TABLE IF NOT EXISTS `status` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `status` int(11) NOT NULL COMMENT '状态',
  `content` varchar(11) NOT NULL COMMENT '内容',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='状态表';


/*
 2021/6/18
 alter asset_category add path varchar(100) NOT NULL DEFAULT ''
 */

`price` decimal(11,2) NOT NULL DEFAULT '0.00' COMMENT '价格',
/**
  2021/6/28
 */
ALTER TABLE asset_check ADD  lossPrice decimal(11,2) DEFAULT NULL  COMMENT '丢失金额';
ALTER TABLE asset_check ADD  findPrice decimal(11,2) DEFAULT NULL  COMMENT '正常金额';
ALTER TABLE asset_check_detail ADD price decimal(11,2) DEFAULT NULL  COMMENT '价格';
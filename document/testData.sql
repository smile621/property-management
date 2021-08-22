
-- 分类初始值
INSERT INTO `asset_category` VALUES (1, 0, '设备', '', '2021-05-25 21:40:17', NULL, 0, '',1);
INSERT INTO `asset_category` VALUES (2, 0, '配件', '', '2021-05-25 21:41:16', NULL, 0, '',2);
INSERT INTO `asset_category` VALUES (3, 0, '软件', '', '2021-05-25 21:42:07', NULL, 0, '',3);
INSERT INTO `asset_category` VALUES (4, 0, '服务', '', '2021-05-25 21:42:35', NULL, 0, '',4);
INSERT INTO `asset_category` VALUES (5, 0, '耗材', '', '2021-05-25 21:43:27', NULL, 0, '',5);

-- 用户初始值
INSERT INTO `admin_user` VALUES (1, 'smile', '105d213ef4c3b05d6a8cd0a67093e32a', 'smile', '/2021/05/cdd4b658-7221-443f-b231-7c56564b2089.jpeg', '', '', '', '女', 0, '2021-05-26 09:35:41', '2021-06-18 10:16:37');
INSERT INTO `admin_user_role`(`id`, `user_id`, `role_id`, `created_at`, `updated_at`, `status`) VALUES (1, 1, 1, '2021-06-20 19:08:08', NULL, 0);
INSERT INTO `admin_role`(`id`, `name`, `created_at`, `updated_at`, `status`) VALUES (1, '管理员', '2021-06-20 19:07:00', '2021-06-20 19:09:33', 0);
INSERT INTO `admin_role_permission`(`id`, `role_id`, `permission_id`, `created_at`, `updated_at`, `status`) VALUES (1, 1, 1, '2021-06-20 19:07:00', '2021-06-20 19:08:33', 0);
INSERT INTO `admin_role_permission`(`id`, `role_id`, `permission_id`, `created_at`, `updated_at`, `status`) VALUES (2, 1, 2, '2021-06-20 19:07:00', '2021-06-20 19:08:33', 0);
INSERT INTO `admin_role_permission`(`id`, `role_id`, `permission_id`, `created_at`, `updated_at`, `status`) VALUES (3, 1, 3, '2021-06-20 19:07:00', '2021-06-20 19:08:33', 0);
INSERT INTO `admin_role_permission`(`id`, `role_id`, `permission_id`, `created_at`, `updated_at`, `status`) VALUES (4, 1, 4, '2021-06-20 19:07:00', '2021-06-20 19:08:33', 0);
INSERT INTO `admin_role_permission`(`id`, `role_id`, `permission_id`, `created_at`, `updated_at`, `status`) VALUES (5, 1, 5, '2021-06-20 19:07:00', '2021-06-20 19:08:33', 0);
INSERT INTO `admin_role_permission`(`id`, `role_id`, `permission_id`, `created_at`, `updated_at`, `status`) VALUES (6, 1, 6, '2021-06-20 19:07:00', '2021-06-20 19:08:33', 0);
INSERT INTO `admin_role_permission`(`id`, `role_id`, `permission_id`, `created_at`, `updated_at`, `status`) VALUES (7, 1, 7, '2021-06-20 19:07:00', '2021-06-20 19:08:33', 0);
INSERT INTO `admin_role_permission`(`id`, `role_id`, `permission_id`, `created_at`, `updated_at`, `status`) VALUES (8, 1, 8, '2021-06-20 19:07:00', '2021-06-20 19:08:33', 0);
INSERT INTO `admin_role_permission`(`id`, `role_id`, `permission_id`, `created_at`, `updated_at`, `status`) VALUES (9, 1, 9, '2021-06-20 19:07:00', '2021-06-20 19:08:33', 0);
INSERT INTO `admin_role_permission`(`id`, `role_id`, `permission_id`, `created_at`, `updated_at`, `status`) VALUES (10, 1, 10, '2021-06-20 19:07:00', '2021-06-20 19:08:33', 0);
INSERT INTO `admin_role_permission`(`id`, `role_id`, `permission_id`, `created_at`, `updated_at`, `status`) VALUES (11, 1, 11, '2021-06-20 19:07:00', '2021-06-20 19:08:33', 0);
INSERT INTO `admin_role_permission`(`id`, `role_id`, `permission_id`, `created_at`, `updated_at`, `status`) VALUES (12, 1, 12, '2021-06-20 19:07:00', '2021-06-20 19:08:33', 0);
INSERT INTO `admin_role_permission`(`id`, `role_id`, `permission_id`, `created_at`, `updated_at`, `status`) VALUES (13, 1, 13, '2021-06-20 19:07:00', '2021-06-20 19:08:33', 0);

-- 权限初始值
INSERT INTO `admin_permissions`(`id`, `name`, `tag`, `code`, `created_at`, `updated_at`, `status`) VALUES (1, '仪表盘', 'homepage', 'homepage', '2021-06-19 18:33:05.02252', '2021-06-19 18:54:58.00199', 0);
INSERT INTO `admin_permissions`(`id`, `name`, `tag`, `code`, `created_at`, `updated_at`, `status`) VALUES (2, '代办', 'todo', 'todo', '2021-06-19 18:34:05.26242', NULL, 0);
INSERT INTO `admin_permissions`(`id`, `name`, `tag`, `code`, `created_at`, `updated_at`, `status`) VALUES (3, '故障', 'malfunction', 'malfunction', '2021-06-19 18:34:35.28006', NULL, 0);
INSERT INTO `admin_permissions`(`id`, `name`, `tag`, `code`, `created_at`, `updated_at`, `status`) VALUES (4, '设备', 'device', 'device', '2021-06-19 18:35:04.93012', NULL, 0);
INSERT INTO `admin_permissions`(`id`, `name`, `tag`, `code`, `created_at`, `updated_at`, `status`) VALUES (5, '配件', 'part', 'part', '2021-06-19 18:35:20.81307', NULL, 0);
INSERT INTO `admin_permissions`(`id`, `name`, `tag`, `code`, `created_at`, `updated_at`, `status`) VALUES (6, '软件', 'software', 'software', '2021-06-19 18:45:03.36422', NULL, 0);
INSERT INTO `admin_permissions`(`id`, `name`, `tag`, `code`, `created_at`, `updated_at`, `status`) VALUES (7, '耗材', 'consumable', 'consumable', '2021-06-19 18:45:55.48833', NULL, 0);
INSERT INTO `admin_permissions`(`id`, `name`, `tag`, `code`, `created_at`, `updated_at`, `status`) VALUES (8, '服务', 'service', 'service', '2021-06-19 18:46:17.58284', NULL, 0);
INSERT INTO `admin_permissions`(`id`, `name`, `tag`, `code`, `created_at`, `updated_at`, `status`) VALUES (9, '组织', 'organization', 'organization', '2021-06-19 18:47:23.11935', NULL, 0);
INSERT INTO `admin_permissions`(`id`, `name`, `tag`, `code`, `created_at`, `updated_at`, `status`) VALUES (10, '盘点', 'check', 'check', '2021-06-19 18:47:40.98906', NULL, 0);
INSERT INTO `admin_permissions`(`id`, `name`, `tag`, `code`, `created_at`, `updated_at`, `status`) VALUES (11, '厂商', 'vendor', 'vendor', '2021-06-19 18:48:34.95591', NULL, 0);
INSERT INTO `admin_permissions`(`id`, `name`, `tag`, `code`, `created_at`, `updated_at`, `status`) VALUES (12, '购入途径', 'buyRouter', 'buyRouter', '2021-06-19 18:50:53.84552', NULL, 0);
INSERT INTO `admin_permissions`(`id`, `name`, `tag`, `code`, `created_at`, `updated_at`, `status`) VALUES (13, '折旧规则', 'depreciation', 'depreciation', '2021-06-19 18:52:53.47052', NULL, 0);

-- 发行方式初始值
INSERT INTO `issue`(`id`, `name`, `created_at`, `updated_at`, `status`) VALUES (1, '开源', '2021-05-25 21:54:53', '2021-06-08 17:17:27', 0);
INSERT INTO `issue`(`id`, `name`, `created_at`, `updated_at`, `status`) VALUES (2, '授权', '2021-05-26 09:50:01', '2021-06-08 17:17:31', 0);
INSERT INTO `issue`(`id`, `name`, `created_at`, `updated_at`, `status`) VALUES (3, '免费', '2021-05-26 09:50:07', '2021-06-08 17:17:35', 0);

-- 状态初始值
INSERT INTO `status`(`id`, `status`, `content`, `created_at`, `updated_at`) VALUES (1, 0, '正常', '2021-05-29 16:29:34', NULL);
INSERT INTO `status`(`id`, `status`, `content`, `created_at`, `updated_at`) VALUES (2, 1, '闲置', '2021-05-29 16:29:43', NULL);
INSERT INTO `status`(`id`, `status`, `content`, `created_at`, `updated_at`) VALUES (3, 2, '归属', '2021-05-29 16:29:50', NULL);
INSERT INTO `status`(`id`, `status`, `content`, `created_at`, `updated_at`) VALUES (4, 3, '借用', '2021-05-29 16:30:01', NULL);
INSERT INTO `status`(`id`, `status`, `content`, `created_at`, `updated_at`) VALUES (5, 4, '故障', '2021-05-29 16:30:08', NULL);
INSERT INTO `status`(`id`, `status`, `content`, `created_at`, `updated_at`) VALUES (6, 5, '待处理', '2021-05-29 16:30:22', NULL);
INSERT INTO `status`(`id`, `status`, `content`, `created_at`, `updated_at`) VALUES (7, 6, '处理中', '2021-05-29 16:30:30', NULL);
INSERT INTO `status`(`id`, `status`, `content`, `created_at`, `updated_at`) VALUES (8, 7, '处理完成', '2021-05-29 16:30:41', NULL);
INSERT INTO `status`(`id`, `status`, `content`, `created_at`, `updated_at`) VALUES (9, 8, '未盘点', '2021-05-29 16:30:54', NULL);
INSERT INTO `status`(`id`, `status`, `content`, `created_at`, `updated_at`) VALUES (10, 9, '盘点完成', '2021-05-29 16:31:05', NULL);
INSERT INTO `status`(`id`, `status`, `content`, `created_at`, `updated_at`) VALUES (11, 10, '丢失', '2021-05-29 16:31:11', NULL);
INSERT INTO `status`(`id`, `status`, `content`, `created_at`, `updated_at`) VALUES (12, 11, '中止', '2021-06-18 17:15:03', '2021-06-18 17:15:03');
INSERT INTO `status`(`id`, `status`, `content`, `created_at`, `updated_at`) VALUES (13, 12, '耗材入库', '2021-06-18 17:14:30', NULL);
INSERT INTO `status`(`id`, `status`, `content`, `created_at`, `updated_at`) VALUES (14, 13, '号才出库', '2021-06-18 17:14:43', NULL);
INSERT INTO `status`(`id`, `status`, `content`, `created_at`, `updated_at`) VALUES (15, 100, '删除', '2021-06-18 17:15:18', NULL);

-- 用户表
INSERT INTO waitfall.`t_user` (`id`, `avatar`,`nickname`, `account`, `password`, `unit_id` ,`enabled`, `created_by`, `updated_by`, `create_time`, `update_time`, `deleted`)
VALUES ('admin', 'https://ts1.tc.mm.bing.net/th/id/R-C.8bbf769b39bb26eefb9b6de51c23851d?rik=crTnc5i8A%2b8p7A&riu=http%3a%2f%2fpicview.iituku.com%2fcontentm%2fzhuanji%2fimg%2f202207%2f09%2fe7196ac159f7cf2b.jpg%2fnu&ehk=DYPLVpoNAXLj5qzwgR5vHf9DladFh%2b34s4UcuP3Kn6E%3d&risl=&pid=ImgRaw&r=0','admin', 'admin', '$2a$10$czjB7DcPb7bq6TP8wnfSoOl82SzioGFefaWrrk9OgX1l/nfkU8ENS', '',1, 'admin',
        'admin', '2025-07-14 15:56:14', '2025-07-14 15:56:16', 0);

-- 角色表
-- 角色表
INSERT INTO waitfall.`t_role` (`id`, `name`, `tag`, `sort`, `description`, `enabled`, `created_by`, `updated_by`, `create_time`, `update_time`, `deleted`)
VALUES
    ('1', '管理员', 'admin', 0, '管理员', 1, 'admin', 'admin',
     '2025-07-14 16:06:50', '2025-07-14 16:06:52', 0),
    ('2', '普通用户', 'user', 0, '普通用户', 1, 'admin', 'admin',
     '2025-07-14 16:07:26', '2025-07-14 16:07:29', 0);

-- 用户角色关联表
INSERT INTO waitfall.`t_user_role` (`id`, `user_id`, `role_id`) VALUES ('1', 'admin', '1');


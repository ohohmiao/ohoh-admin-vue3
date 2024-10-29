# ohoh-admin-vue3

### 介绍 

基于Spring Boot + Vue3前后端分离的开发脚手架。

- 后端技术栈：Spring Boot、MyBatis-Plus、Redis。
- 前端采用 [Geeker-Admin](https://gitee.com/HalseySpicy/Geeker-Admin) 框架：Vue3.4、TypeScript、Vite5、Pinia、Element-Plus。
- 权限框架依赖 [Sa-Token](https://sa-token.cc/) 实现。
- 系统功能齐全且极简的管理后端基础框架。
- 支持菜单和按钮级的动态权限控制。
- 支持数据权限分级授权。

### 代码仓库 

- Gitee：https://gitee.com/ohohmiao/ohoh-admin-vue3
- GitHub：https://github.com/ohohmiao/ohoh-admin-vue3

### 项目功能 

- 系统资源管理：配置系统菜单及按钮。
- 系统字典管理：维护系统字典数据。
- 组织机构管理：配置系统组织机构。
- 系统角色管理：支持授权系统资源，支持按组织机构层级授权数据范围，支持配置全局角色。
- 系统用户管理：配置系统用户。
- 岗位管理：配置岗位数据。
- 系统日志管理：查询系统登录、登出、操作、异常日志。

### 文件目录 

```text
ohoh-admin-vue3
├─ ohoh-admin-server        # 后端代码
├─ ohoh-admin-client        # 前端代码
├─ db                       # 建库脚本
└─ README.md                # README 介绍
```

### Respect！！！

- [Geeker-Admin](https://gitee.com/HalseySpicy/Geeker-Admin)：优雅的前端框架。
- [RuoYi-Vue-Plus](https://gitee.com/dromara/RuoYi-Vue-Plus)：是重写 [RuoYi-Vue](https://gitee.com/y_project/RuoYi-Vue) ，针对分布式集群与多租户场景的全方位升级。

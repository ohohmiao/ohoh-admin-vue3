# ohoh-admin-vue3

### 介绍 

基于Spring Boot + Vue3前后端分离的开发脚手架。

- 前端采用 [Geeker-Admin](https://gitee.com/HalseySpicy/Geeker-Admin) 框架。Geeker-Admin是一款优雅的开源后台管理框架，原作者使用Vue3.4、TypeScript、Vite5、Pinia、Element-Plus等目前最新技术栈开发，并推出ProTable、TreeFilter等常用组件，助力二次开发。
- 后端采用Spring Boot 2.7、MyBatis-Plus、Redis。
- 权限框架依赖 [Sa-Token](https://sa-token.cc/) 实现。
- 支持菜单和按钮级的动态权限控制。

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

- [Geeker-Admin](https://gitee.com/HalseySpicy/Geeker-Admin)
- [RuoYi-Vue-Plus](https://gitee.com/dromara/RuoYi-Vue-Plus)：是重写 [RuoYi-Vue](https://gitee.com/y_project/RuoYi-Vue) ，针对分布式集群与多租户场景的全方位升级。

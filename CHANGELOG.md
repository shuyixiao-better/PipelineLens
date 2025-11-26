# Changelog

All notable changes to the PipelineLens project will be documented in this file.

## [1.0.0] - 2024-11-26

### Added
- 🎉 首次发布 PipelineLens 插件
- 🐳 完整的 Jenkins Pipeline 文件类型支持
- 🎨 自定义 Jenkins 机器人图标，支持亮色和暗色主题
- ✨ 增强的语法高亮，11种鲜艳颜色
- 🔍 智能补全功能
  - pipeline、stage、step 等关键字
  - 环境变量补全 (env.BUILD_NUMBER, env.WORKSPACE 等)
  - 参数补全 (params.APP_NAME, params.DEPLOY_ENV 等)
- 📚 悬停文档支持
  - 方法签名显示
  - 参数说明
  - Jenkins 官方文档链接
- ⚙️ 可自定义语法高亮颜色
- 🛡️ 5层主题覆盖防护，确保图标正确显示
- 📝 GDSL 脚本支持，提供更好的代码补全
- 🔧 智能文件识别
  - Jenkinsfile
  - Jenkinsfile.*
  - *.jenkinsfile
  - jenkins
  - pipeline

### Technical Details
- 基于 IntelliJ Platform 2024.1.6
- 使用 Java 17
- Gradle 8.5 构建系统
- 支持 Groovy 语言

### Documentation
- 完整的 README.md
- 详细的项目结构说明
- 开发指南
- 使用示例

---

## Future Plans

### [1.1.0] - 计划中
- [ ] 添加 Jenkins Pipeline 代码片段
- [ ] 支持更多 Jenkins 插件的语法
- [ ] 添加 Pipeline 可视化预览
- [ ] 支持 Shared Library 补全

### [1.2.0] - 计划中
- [ ] 添加 Pipeline 语法检查
- [ ] 支持 Pipeline 重构功能
- [ ] 添加 Jenkins API 集成
- [ ] 支持远程 Jenkins 服务器连接

---

[1.0.0]: https://github.com/yourusername/PipelineLens/releases/tag/v1.0.0

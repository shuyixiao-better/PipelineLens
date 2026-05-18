# PipelineLens

![Version](https://img.shields.io/badge/Version-1.0.0-blue)
![IntelliJ Platform](https://img.shields.io/badge/IntelliJ-2024.1+-orange)
![License](https://img.shields.io/badge/License-MIT-green)

PipelineLens 是一款专为 Jenkins Pipeline 开发设计的 IntelliJ IDEA 插件，提供完整的语法支持、智能补全和可视化增强功能。

## 🚀 核心功能

### 🐳 Jenkins Pipeline 完整支持

- **自定义文件类型和图标**：Jenkins 机器人图标，5层主题覆盖防护
- **增强语法高亮**：11种鲜艳颜色的 VS Code 风格语法高亮
- **智能补全**：pipeline、stage、step 等关键字和方法
- **环境变量补全**：自动识别 env.BUILD_NUMBER、env.WORKSPACE 等
- **参数补全**：自动补全 params.APP_NAME、params.DEPLOY_ENV 等
- **文档支持**：悬停显示方法签名和参数说明
- **可自定义颜色**：支持在 IDE 设置中调整语法高亮颜色

## 🎯 适用场景

- **Jenkins 用户**：提升 Pipeline 开发效率和体验
- **DevOps 工程师**：快速编写和维护 CI/CD 流水线
- **团队协作**：统一 Pipeline 代码规范，提升代码质量

## 🎨 技术特色

- **智能文件识别**：自动识别 Jenkinsfile、Jenkinsfile.*、*.jenkinsfile 等文件
- **主题兼容**：自定义图标支持亮色和暗色主题
- **性能优化**：线程安全缓存和智能加载

## 📦 安装

### 从 JetBrains Marketplace 安装

1. 打开 IntelliJ IDEA
2. 进入 `Settings/Preferences` → `Plugins`
3. 搜索 "PipelineLens"
4. 点击 `Install` 安装

### 手动安装

1. 下载最新的 [Release](https://github.com/yourusername/PipelineLens/releases)
2. 打开 IntelliJ IDEA
3. 进入 `Settings/Preferences` → `Plugins` → `⚙️` → `Install Plugin from Disk...`
4. 选择下载的 `.zip` 文件

## 🔧 开发

### 环境要求

- JDK 17+
- IntelliJ IDEA 2024.1+
- Gradle 8.0+

### 构建项目

```bash
# 克隆项目
git clone https://github.com/yourusername/PipelineLens.git
cd PipelineLens

# 构建插件
./gradlew buildPlugin

# 运行插件
./gradlew runIde
```

### 项目结构

```
PipelineLens/
├── src/main/
│   ├── java/com/poeticcoder/jenkins/
│   │   ├── file/              # 文件类型定义
│   │   ├── highlight/         # 语法高亮
│   │   ├── icon/              # 图标提供器
│   │   ├── provider/          # 内容提供器
│   │   ├── documentation/     # 文档提供器
│   │   ├── gdsl/              # GDSL 服务
│   │   ├── model/             # 数据模型
│   │   └── util/              # 工具类
│   └── resources/
│       ├── META-INF/          # 插件配置
│       ├── icons/             # 图标资源
│       ├── descriptors/       # 语法描述符
│       └── jenkins-pipeline.gdsl  # GDSL 脚本
├── build.gradle               # Gradle 构建脚本
└── README.md                  # 项目说明
```

## 📝 使用示例

创建一个 `Jenkinsfile`：

```groovy
pipeline {
    agent any
    
    environment {
        MAVEN_OPTS = '-Xmx1024m'
    }
    
    parameters {
        string(name: 'BRANCH_NAME', defaultValue: 'main')
        booleanParam(name: 'SKIP_TESTS', defaultValue: false)
    }
    
    stages {
        stage('Build') {
            steps {
                sh 'mvn clean compile'
                echo "Build completed"
            }
        }
        
        stage('Test') {
            when {
                not { params.SKIP_TESTS }
            }
            steps {
                sh 'mvn test'
            }
        }
    }
    
    post {
        always {
            archiveArtifacts artifacts: 'target/*.jar'
        }
    }
}
```

## 🤝 贡献

欢迎贡献代码、报告问题或提出建议！

1. Fork 本项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

## 📄 许可证

本项目采用 MIT 许可证 - 详见 [LICENSE](LICENSE) 文件

## 👨‍💻 作者

**舒一笑不秃头**

- 网站: [https://www.poeticcoder.com](https://www.poeticcoder.com)
- Email: yixiaoshu88@163.com

## 🙏 致谢

感谢所有为本项目做出贡献的开发者！

---

⭐ 如果这个项目对你有帮助，请给它一个星标！

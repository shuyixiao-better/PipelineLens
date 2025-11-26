# PipelineLens 快速启动指南

## 🚀 快速开始

### 1. 打开项目

在 IntelliJ IDEA 中打开项目：

```
File -> Open -> /Users/shuyixiao/IdeaProjects/PipelineLens
```

### 2. 等待索引完成

首次打开项目时，IntelliJ IDEA 会进行索引，请等待完成。

### 3. 配置 JDK

确保项目使用 JDK 17：

```
File -> Project Structure -> Project -> SDK -> 选择 JDK 17
```

### 4. 构建项目

在终端中运行：

```bash
./gradlew buildPlugin
```

或者在 IntelliJ IDEA 中：

```
View -> Tool Windows -> Gradle -> Tasks -> intellijPlatform -> buildPlugin
```

### 5. 运行插件

在终端中运行：

```bash
./gradlew runIde
```

或者在 IntelliJ IDEA 中：

```
View -> Tool Windows -> Gradle -> Tasks -> intellijPlatform -> runIde
```

这将启动一个新的 IntelliJ IDEA 实例，其中已安装 PipelineLens 插件。

### 6. 测试插件

在新启动的 IDEA 中：

1. 创建一个新文件，命名为 `Jenkinsfile`
2. 输入以下内容：

```groovy
pipeline {
    agent any
    
    environment {
        MAVEN_OPTS = '-Xmx1024m'
        BUILD_NUMBER = "${BUILD_NUMBER}"
    }
    
    parameters {
        string(name: 'BRANCH_NAME', defaultValue: 'main')
        booleanParam(name: 'SKIP_TESTS', defaultValue: false)
    }
    
    stages {
        stage('Build') {
            steps {
                sh 'mvn clean compile'
                echo "Build completed with ${env.MAVEN_OPTS}"
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
        success {
            echo 'Pipeline succeeded!'
        }
    }
}
```

### 7. 验证功能

检查以下功能是否正常工作：

#### ✅ 文件图标
- Jenkinsfile 应该显示 Jenkins 机器人图标

#### ✅ 语法高亮
- 关键字（pipeline, stage, steps）应该有颜色
- 字符串应该有不同的颜色
- 注释应该是灰色

#### ✅ 智能补全
- 输入 `env.` 应该显示环境变量补全
- 输入 `params.` 应该显示参数补全
- 输入 `stage` 应该显示补全建议

#### ✅ 文档悬停
- 将鼠标悬停在 `sh`、`echo` 等方法上
- 应该显示方法文档

#### ✅ 颜色设置
- 打开 `Settings -> Editor -> Color Scheme -> Jenkins Pipeline`
- 应该能看到自定义颜色设置

## 🔧 常见问题

### Q1: 构建失败，提示找不到 JDK

**解决方案**：
```bash
# 设置 JAVA_HOME 环境变量
export JAVA_HOME=/path/to/jdk-17
```

### Q2: 下载依赖很慢

**解决方案**：
在 `gradle.properties` 中添加国内镜像：

```properties
systemProp.https.proxyHost=mirrors.aliyun.com
systemProp.https.proxyPort=443
```

### Q3: 运行 runIde 时内存不足

**解决方案**：
在 `gradle.properties` 中增加内存：

```properties
org.gradle.jvmargs=-Xmx4096m
```

### Q4: 插件图标不显示

**解决方案**：
1. 检查 `src/main/resources/icons/` 目录下是否有图标文件
2. 重新构建项目
3. 清理缓存：`./gradlew clean`

## 📦 打包发布

### 构建发布版本

```bash
./gradlew buildPlugin
```

生成的插件文件位于：
```
build/distributions/PipelineLens-1.0.0.zip
```

### 安装到本地 IDEA

1. 打开 IntelliJ IDEA
2. `Settings -> Plugins -> ⚙️ -> Install Plugin from Disk...`
3. 选择 `build/distributions/PipelineLens-1.0.0.zip`
4. 重启 IDEA

## 🎯 开发技巧

### 调试插件

1. 在代码中设置断点
2. 运行 `./gradlew runIde --debug-jvm`
3. 在 IntelliJ IDEA 中创建 Remote JVM Debug 配置
4. 连接到端口 5005

### 查看日志

插件日志位于：
```
build/idea-sandbox/system/log/idea.log
```

### 热重载

修改代码后，在运行的 IDEA 中：
```
Help -> Find Action -> Reload All from Disk
```

## 📚 相关资源

- [IntelliJ Platform SDK 文档](https://plugins.jetbrains.com/docs/intellij/)
- [Gradle IntelliJ Plugin](https://github.com/JetBrains/gradle-intellij-plugin)
- [Jenkins Pipeline 文档](https://www.jenkins.io/doc/book/pipeline/)

## 🤝 获取帮助

如果遇到问题：

1. 查看 [README.md](README.md)
2. 查看 [CONTRIBUTING.md](CONTRIBUTING.md)
3. 发送邮件到 yixiaoshu88@163.com
4. 访问 https://www.poeticcoder.com

---

祝你开发愉快！🎉

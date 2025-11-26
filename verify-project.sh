#!/bin/bash

echo "======================================"
echo "PipelineLens 项目验证脚本"
echo "======================================"
echo ""

PROJECT_DIR="/Users/shuyixiao/IdeaProjects/PipelineLens"

# 颜色定义
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# 检查函数
check_file() {
    if [ -f "$1" ]; then
        echo -e "${GREEN}✓${NC} $2"
        return 0
    else
        echo -e "${RED}✗${NC} $2 (缺失)"
        return 1
    fi
}

check_dir() {
    if [ -d "$1" ]; then
        echo -e "${GREEN}✓${NC} $2"
        return 0
    else
        echo -e "${RED}✗${NC} $2 (缺失)"
        return 1
    fi
}

echo "1. 检查项目配置文件..."
check_file "$PROJECT_DIR/build.gradle" "build.gradle"
check_file "$PROJECT_DIR/settings.gradle" "settings.gradle"
check_file "$PROJECT_DIR/gradle.properties" "gradle.properties"
check_file "$PROJECT_DIR/gradlew" "gradlew"
echo ""

echo "2. 检查源代码目录..."
check_dir "$PROJECT_DIR/src/main/java/com/poeticcoder/jenkins" "Java 源代码目录"
check_dir "$PROJECT_DIR/src/main/resources" "资源文件目录"
echo ""

echo "3. 检查 Java 源文件..."
JAVA_FILES=(
    "file/JenkinsFileType.java"
    "JenkinsFileTypeRegistrar.java"
    "highlight/JenkinsSyntaxHighlighter.java"
    "highlight/JenkinsSyntaxHighlighterFactory.java"
    "highlight/JenkinsColorSettingsPage.java"
    "icon/JenkinsIconProvider.java"
    "icon/JenkinsFileIconDecorator.java"
    "provider/JenkinsMapContentProvider.java"
    "documentation/JenkinsDocumentationProvider.java"
    "gdsl/JenkinsGdslService.java"
    "gdsl/JenkinsGdslMembersProvider.java"
    "model/Descriptor.java"
    "util/PsiUtils.java"
    "util/JenkinsFileDetector.java"
)

for file in "${JAVA_FILES[@]}"; do
    check_file "$PROJECT_DIR/src/main/java/com/poeticcoder/jenkins/$file" "$file"
done
echo ""

echo "4. 检查资源文件..."
check_file "$PROJECT_DIR/src/main/resources/META-INF/plugin.xml" "plugin.xml"
check_file "$PROJECT_DIR/src/main/resources/icons/jenkinsfile.svg" "jenkinsfile.svg"
check_file "$PROJECT_DIR/src/main/resources/icons/jenkinsfile@2x.svg" "jenkinsfile@2x.svg"
check_file "$PROJECT_DIR/src/main/resources/descriptors/jenkinsPipeline.xml" "jenkinsPipeline.xml"
check_file "$PROJECT_DIR/src/main/resources/jenkins-pipeline.gdsl" "jenkins-pipeline.gdsl"
echo ""

echo "5. 检查文档文件..."
check_file "$PROJECT_DIR/README.md" "README.md"
check_file "$PROJECT_DIR/LICENSE" "LICENSE"
check_file "$PROJECT_DIR/CHANGELOG.md" "CHANGELOG.md"
check_file "$PROJECT_DIR/CONTRIBUTING.md" "CONTRIBUTING.md"
check_file "$PROJECT_DIR/QUICKSTART.md" "QUICKSTART.md"
check_file "$PROJECT_DIR/MIGRATION_SUMMARY.md" "MIGRATION_SUMMARY.md"
check_file "$PROJECT_DIR/.gitignore" ".gitignore"
echo ""

echo "6. 统计信息..."
JAVA_COUNT=$(find "$PROJECT_DIR/src" -name "*.java" 2>/dev/null | wc -l | tr -d ' ')
RESOURCE_COUNT=$(find "$PROJECT_DIR/src/main/resources" -type f 2>/dev/null | wc -l | tr -d ' ')
echo -e "${YELLOW}→${NC} Java 源文件: $JAVA_COUNT"
echo -e "${YELLOW}→${NC} 资源文件: $RESOURCE_COUNT"
echo ""

echo "7. 检查 gradlew 权限..."
if [ -x "$PROJECT_DIR/gradlew" ]; then
    echo -e "${GREEN}✓${NC} gradlew 可执行"
else
    echo -e "${RED}✗${NC} gradlew 不可执行"
    echo "   运行: chmod +x $PROJECT_DIR/gradlew"
fi
echo ""

echo "======================================"
echo "验证完成！"
echo "======================================"
echo ""
echo "下一步操作:"
echo "1. cd $PROJECT_DIR"
echo "2. ./gradlew buildPlugin"
echo "3. ./gradlew runIde"
echo ""

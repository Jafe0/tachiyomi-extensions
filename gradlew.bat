@echo off

:: Gradle wrapper script for Windows

set DIR=%~dp0
set APP_BASE_NAME=%~n0
set APP_HOME=%DIR%

if exist "%APP_HOME%\gradle\wrapper\gradle-wrapper.jar" (
    set CLASSPATH=%APP_HOME%\gradle\wrapper\gradle-wrapper.jar
) else (
    echo ERROR: gradle-wrapper.jar not found
    exit /b 1
)

java -classpath "%CLASSPATH%" org.gradle.wrapper.GradleWrapperMain %*
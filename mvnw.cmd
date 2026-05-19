@echo off
setlocal

set MAVEN_VERSION=3.9.6
set MAVEN_HOME=%USERPROFILE%\.m2\wrapper\dists\apache-maven-%MAVEN_VERSION%
set MAVEN_BIN=%MAVEN_HOME%\bin\mvn.cmd

if exist "%MAVEN_BIN%" goto runMaven

echo Downloading Maven %MAVEN_VERSION%...
mkdir "%MAVEN_HOME%" 2>nul
set DOWNLOAD_URL=https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/%MAVEN_VERSION%/apache-maven-%MAVEN_VERSION%-bin.zip
set TEMP_FILE=%TEMP%\maven-%RANDOM%.zip

powershell -Command "Invoke-WebRequest -Uri '%DOWNLOAD_URL%' -OutFile '%TEMP_FILE%'"
powershell -Command "Expand-Archive -Path '%TEMP_FILE%' -DestinationPath '%MAVEN_HOME%\..' -Force"
xcopy /E /Y /Q "%MAVEN_HOME%\..\apache-maven-%MAVEN_VERSION%\*" "%MAVEN_HOME%\" >nul 2>&1
rmdir /S /Q "%MAVEN_HOME%\..\apache-maven-%MAVEN_VERSION%" 2>nul
del "%TEMP_FILE%" 2>nul

:runMaven
"%MAVEN_BIN%" %*

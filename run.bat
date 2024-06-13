@echo off

REM Check if arguments are provided
if "%~1"=="" (
    echo Usage: %~0 class args
    exit /b 1
)

REM Check if classpath.txt exists, if not, generate it using Maven
if not exist target\classpath.txt (
    mvn dependency:build-classpath -Dmdep.outputFile=target/classpath.txt
)

REM Read classpath.txt and set the CLASSPATH environment variable
setlocal enabledelayedexpansion
set CLASSPATH=
for /f "delims=" %%i in (target\classpath.txt) do set CLASSPATH=%%i;target\classes

REM Run the Java program
java -cp %CLASSPATH% %*

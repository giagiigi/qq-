@echo off
set CURRENT_DIR=%~dp0
set IMCROOT=%CURRENT_DIR%..\..
set sybase=%IMCROOT%\common\dbinterfaces
set PATH=%IMCROOT%\server\dll;%PATH%
set O2O_HOME=%CURRENT_DIR%..
set JAVA_HOME=%IMCROOT%\common\jre
set -Do2o.home="%O2O_HOME%"

"%JAVA_HOME%\bin\javaw" -Djava.io.tmpdir="%IMCROOT%\tmp" -jar "%O2O_HOME%\bin\bootstrap.jar" stop -port 8046

@echo off
set CURRENT_DIR=%~dp0

set IMCROOT=%CURRENT_DIR%..\..
set JAVA_HOME=%IMCROOT%\common\jre

"%JAVA_HOME%\bin\java.exe" -cp "%CURRENT_DIR%/../repository/imc/jars/ead_common_server.jar" -Djavax.net.ssl.trustStore="%CURRENT_DIR%/../security/truststore" -Djavax.net.ssl.trustStorePassword=iMCV300R002 com.h3c.imc.eadserver.bootstrap.CpuTimeMonitor -port 9195

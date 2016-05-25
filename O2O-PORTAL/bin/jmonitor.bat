@echo off
set CURRENT_DIR=%~dp0
set O2O_HOME=%CURRENT_DIR%..
set IMCROOT=%CURRENT_DIR%..\..
set JAVA_HOME=%IMCROOT%\deploy\jdk

"%JAVA_HOME%\bin\jconsole.exe" -J-Djavax.net.ssl.trustStore="%O2O_HOME%/security/truststore" -J-Djavax.net.ssl.trustStorePassword=iMCV300R002 localhost:9195

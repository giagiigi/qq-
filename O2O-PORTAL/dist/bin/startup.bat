@echo off
set CURRENT_DIR=%~dp0

set O2O_HOME=%CURRENT_DIR%..
set JAVA_HOME=%O2O_HOME%\..\common\jre
set JAVA_OPTS=-server -Xmx512m -Xrs -XX:PermSize=16m -XX:MaxPermSize=128m -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=../log -Do2o.home="%O2O_HOME%" -Duser.language=${LANGUAGE} -Duser.country=${COUNTRY} -Djava.io.tmpdir="%O2O_HOME%\tmp"

rem Enabling Local/Remote Monitoring and Management
set JMXRMI_PORT=9195
set JAVA_OPTS=%JAVA_OPTS% -Dcom.sun.management.jmxremote.port=%JMXRMI_PORT% -Dcom.sun.management.jmxremote.authenticate=false -Djavax.net.ssl.keyStore="%O2O_HOME%\security\keystore" -Djavax.net.ssl.keyStorePassword=iMCV300R002

"%JAVA_HOME%\bin\javaw.exe" %JAVA_OPTS% -jar "%O2O_HOME%\bin\bootstrap.jar" start -port 8046

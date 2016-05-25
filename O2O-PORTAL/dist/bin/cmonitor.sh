#!/bin/sh

IMC_CFG="/etc/iMC-Reserved/imc.conf"
[ -r "$IMC_CFG" ] && . "${IMC_CFG}"

if [ -z "$IMC_ROOT" ]; then
    echo IMC_ROOT not set yet.
    exit 1
fi

PATH=$IMCROOT/server/bin:$IMCROOT/server/dll:$PATH
JAVA_HOME=$IMCROOT/common/jre
EAD_HOME="$IMC_ROOT/emomdmagent"

export IMCROOT
export PATH

JAVA_HOME=$IMCROOT/deploy/jdk

"$JAVA_HOME/bin/java" -cp "$EAD_HOME/repository/imc/jars/ead_common_server.jar" -Djavax.net.ssl.trustStore="$EAD_HOME/security/truststore" -Djavax.net.ssl.trustStorePassword=iMCV300R002 com.h3c.imc.bootstrap.CpuTimeMonitor -port 9195

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

"$JAVA_HOME/bin/jconsole" -J-Djavax.net.ssl.trustStore="$EAD_HOME/security/truststore" -J-Djavax.net.ssl.trustStorePassword=iMCV300R002 localhost:9195

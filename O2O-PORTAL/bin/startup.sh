#!/bin/sh

IMC_CFG="/etc/iMC-Reserved/imc.conf"
[ -r "$IMC_CFG" ] && . "${IMC_CFG}"

if [ -z "$IMC_ROOT" ]; then
    echo IMC_ROOT not set yet.
    exit 1
fi

PATH=$IMCROOT/server/bin:$IMCROOT/server/dll:$PATH
JAVA_HOME=$IMCROOT/common/jre
PORTAL_HOME="$IMC_ROOT/o2oportal"

export IMCROOT
export PATH

ulimit -n 1024

"$JAVA_HOME/bin/java" -server -Xmx512m -Xrs -XX:PermSize=64m -XX:MaxPermSize=512m -Do2o.home="$PORTAL_HOME" -Duser.language=${LANGUAGE} -Duser.country=${COUNTRY} -Djava.awt.headless=true -Dcom.sun.management.jmxremote.port=9195 -Dcom.sun.management.jmxremote.authenticate=false -Djavax.net.ssl.keyStore="$PORTAL_HOME/security/keystore" -Djavax.net.ssl.keyStorePassword=iMCV300R002 -Djava.io.tmpdir="$IMC_ROOT/tmp" -jar "$PORTAL_HOME/bin/bootstrap.jar" start -port 8046 &

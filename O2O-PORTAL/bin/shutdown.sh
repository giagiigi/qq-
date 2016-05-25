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


"$JAVA_HOME/bin/java" -Do2o.home="$PORTAL_HOME" -Djava.io.tmpdir="$IMC_ROOT/tmp" -jar "$PORTAL_HOME/bin/bootstrap.jar" stop -port 8046 &

#!/bin/sh

# Process command-line
if [ "$#" -eq 0 ]; then
	echo "Usage: $0 NODEID" >&2
	echo "Where NODEID is a number from 1 to 3" >&2
	exit 1
fi
NODEID=$1

# Initialize node directory
WORKDIR=/tmp/cassandra-lab/node-$NODEID
mkdir -p $WORKDIR
cat cassandra.yaml.tmpl | sed -e s+@NODEID@+$NODEID+ > $WORKDIR/cassandra.yaml
cat log4j-server.properties.tmpl | sed -e s+@NODEID@+$NODEID+ > $WORKDIR/log4j-server.properties

JMX_PORT=7199
JMX_HOST=127.0.1.$NODEID
if [ "$NODEID" -eq 1 ]; then
	JMX="-Djava.net.preferIPv4Stack=true \
		-Dcom.sun.management.jmxremote.host=$JMX_HOST \
		-Dcom.sun.management.jmxremote.port=$JMX_PORT \
		-Dcom.sun.management.jmxremote.ssl=false \
		-Dcom.sun.management.jmxremote.authenticate=false"
fi

<<<<<<< HEAD
JAVA=/usr/lib/jvm/java-7-openjdk/bin/java
=======
JAVA=/usr/lib/jvm/java-1.7.0-openjdk-amd64/bin/java
>>>>>>> 030e71122f4002f4bff967625a9fe63fd2ddfcfa
CLASSPATH=`find apache-cassandra-2.0.1/ -name "*.jar" | paste -d: -s`
$JAVA $JMX \
	-Dlog4j.configuration=log4j-server.properties -Dlog4j.defaultInitOverride=true \
	-Dcassandra-foreground=yes -cp $WORKDIR:$CLASSPATH org.apache.cassandra.service.CassandraDaemon

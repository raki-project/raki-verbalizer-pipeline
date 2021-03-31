#!/bin/sh

export MAVEN_OPTS="-Xmx4G"

ontology="koala.owl"
axioms="koala.owl"
out="koala.owl.out"

mainClass="org.dice_research.raki.verbalizer.pipeline.ui.RAKICommandLineInterface"

nohup mvn exec:java \
	-Dexec.mainClass=$mainClass \
	-Dexec.args="-o $ontology -a $axioms -s $out" \
	> $0.log 2>&1 </dev/null &



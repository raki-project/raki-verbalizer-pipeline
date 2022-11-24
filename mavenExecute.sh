#!/bin/sh

eval "$(command conda 'shell.bash' 'hook' 2> /dev/null)"
conda activate rakiEnv

export MAVEN_OPTS="-Xmx4G"

ontology="biopax.owl"
axioms="biopax.owl"
out="biopax.json"

mainClass="org.dice_research.raki.verbalizer.pipeline.ui.RAKICommandLineInterface"

nohup mvn exec:java \
	-Dexec.mainClass=$mainClass \
	-Dexec.args="-o $ontology -a $axioms -s $out -t model" \
	> $0.log 2>&1 </dev/null &

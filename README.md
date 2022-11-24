# RAKI Verbalizer Pipeline

### Install

A [conda](https://docs.conda.io/projects/conda/en/latest/user-guide/install/index.html) installation is required.

Install:
```bash
conda create --quiet --yes --name rakiEnv python=3.9 && \
conda run --name rakiEnv pip install sentencepiece==0.1.96 subword-nmt==0.3.7 OpenNMT-py==2.3.0 && \
conda activate rakiEnv && conda install  sentencepiece
```


Compile: ```mvn -T 1C compile  -X -am```


### Run:

```bash
ontology="ontology.owl"
axioms="axioms.owl"
out="out.json"
type="model"


export MAVEN_OPTS="-Xmx4G"

eval "$(command conda 'shell.bash' 'hook' 2> /dev/null)"
conda activate rakiEnv

mainClass="org.dice_research.raki.verbalizer.pipeline.ui.RAKICommandLineInterface"

mvn exec:java \
-Dexec.mainClass=$mainClass \
-Dexec.args="-o $ontology -a $axioms -s $out -t $type
```

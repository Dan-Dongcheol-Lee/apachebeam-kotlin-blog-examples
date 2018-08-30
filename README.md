# Apache Beam kotlin blog examples

Apache Beam kotlin blog examples


### Quick Start

The project environment:
* Refer to build.gradle.kts
* run WordCount kotlin example:


    ./make.sh runLocal local apachebeam.kotlin.examples.wordcount.WordCount --inputFile=./src/main/resources/apachebeam/kotlin/examples/wordcount/simple.txt --output=./output.txt

* WordCount pipeline will run on local and produce the output file in current directory.
* WordCount pipeline can run on Google Cloud Dataflow if you have a project setup in your local. 


    ./make.sh runCloud <your-project-id> apachebeam.kotlin.examples.wordcount.WordCount --output=<gs://your-cloud-storage-bucket>/output.txt

* inputFile option is defined by default in WordCount options, so that it will run with the input file and produce output files in <gs://your-cloud-storage-bucket>


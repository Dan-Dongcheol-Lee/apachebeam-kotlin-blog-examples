package apachebeam.kotlin.examples.wordcount

import apachebeam.javaexamples.wordcount.WordCount.TOKENIZER_PATTERN
import apachebeam.kotlin.dsl.kpipe.*
import org.apache.beam.sdk.options.Default
import org.apache.beam.sdk.options.Description
import org.apache.beam.sdk.options.PipelineOptions
import org.apache.beam.sdk.options.Validation

object WordCount {

    @JvmStatic
    fun main(args: Array<String>) {

        val (pipe, options) = KPipe.from<WordCountOptions>(args)

        pipe.fromText(path = options.inputFile)
            .flatMap { it.split(Regex(TOKENIZER_PATTERN)).filter { it.isNotEmpty() }.toList() }
            .countPerElement()
            .map { "${it.key}: ${it.value}" }
            .toText(filename = options.output)

        pipe.run().waitUntilFinish()
    }

    interface WordCountOptions : PipelineOptions {

        @get:Description("Path of the file to read from")
        @get:Default.String("gs://apache-beam-samples/shakespeare/kinglear.txt")
        var inputFile: String

        @get:Description("Path of the file to write to")
        @get:Validation.Required
        var output: String
    }
}

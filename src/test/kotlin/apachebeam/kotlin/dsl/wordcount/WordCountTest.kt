package apachebeam.kotlin.dsl.wordcount

import apachebeam.javaexamples.wordcount.WordCount
import apachebeam.kotlin.dsl.kpipe.countPerElement
import apachebeam.kotlin.dsl.kpipe.flatMap
import apachebeam.kotlin.dsl.kpipe.map
import org.apache.beam.sdk.testing.PAssert
import org.apache.beam.sdk.testing.TestPipeline
import org.apache.beam.sdk.transforms.Create
import org.junit.Rule
import java.io.Serializable
import kotlin.test.Test

class WordCountTest : Serializable {

    @Rule
    @Transient
    @JvmField
    val pipeline = TestPipeline.create()

    @Test
    fun shouldProcessWordCountByGroupByKey() {

        val results = pipeline
                .apply(Create.of(
                        "apache beam in kotlin",
                        "this is kotlin",
                        "awesome kotlin",
                        ""))
                .flatMap { it.split(Regex(WordCount.TOKENIZER_PATTERN)).filter { it.isNotEmpty() }.toList() }
                .countPerElement()
                .map { "${it.key}: ${it.value}" }

        PAssert.that(results).containsInAnyOrder(
                "this: 1", "apache: 1", "beam: 1", "is: 1", "kotlin: 3", "awesome: 1", "in: 1")

        pipeline.run()
    }
}
package apachebeam.kotlin.dsl.kpipe

import org.apache.beam.sdk.Pipeline
import org.apache.beam.sdk.coders.NullableCoder
import org.apache.beam.sdk.io.TextIO
import org.apache.beam.sdk.options.PipelineOptions
import org.apache.beam.sdk.options.PipelineOptionsFactory
import org.apache.beam.sdk.transforms.Count
import org.apache.beam.sdk.transforms.FlatMapElements
import org.apache.beam.sdk.transforms.MapElements
import org.apache.beam.sdk.values.KV
import org.apache.beam.sdk.values.PCollection
import org.apache.beam.sdk.values.PDone
import org.apache.beam.sdk.values.TypeDescriptor

object KPipe {
    inline fun <reified R : PipelineOptions> from(args: Array<String>): Pair<Pipeline, R> {
        val options = PipelineOptionsFactory.fromArgs(*args)
                .withValidation()
                .`as`(R::class.java)
        return Pipeline.create(options) to options
    }
}

fun Pipeline.fromText(
        name: String? = null,
        path: String): PCollection<String> {
    return this.apply(name?: "Read from Text",
            TextIO.read().from(path))
}

fun PCollection<String>.toText(
        name: String? = null,
        filename: String
): PDone {
    return this.apply(name?: "Write to Text",
            TextIO.write().to(filename))
}


inline fun <I, reified O> PCollection<I>.map(
        name: String? = null,
        noinline transform: (I) -> O): PCollection<O> {
    val pc = this.apply(name ?: "map to ${O::class.simpleName}",
            MapElements.into(TypeDescriptor.of(O::class.java))
                    .via(transform))
    return pc.setCoder(NullableCoder.of(pc.coder))
}

inline fun <I, reified O> PCollection<I>.flatMap(
        name: String? = null,
        noinline transform: (I) -> Iterable<O>): PCollection<O> {
    val pc = this.apply(name ?: "flatMap to ${O::class.simpleName}",
            FlatMapElements.into(TypeDescriptor.of(O::class.java))
                    .via(transform))
    return pc.setCoder(NullableCoder.of(pc.coder))
}

fun <I> PCollection<I>.countPerElement(
        name: String? = null): PCollection<KV<I, Long>> {
    return this.apply(name ?: "count per element",
            Count.perElement<I>())
            .setTypeDescriptor(object : TypeDescriptor<KV<I, Long>>() {})
}

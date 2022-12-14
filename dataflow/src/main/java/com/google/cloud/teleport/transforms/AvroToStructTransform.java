package com.google.cloud.teleport.transforms;

import com.google.cloud.spanner.Struct;
import com.google.cloud.teleport.util.converter.RecordToStructConverter;
import org.apache.beam.sdk.coders.AvroCoder;
import org.apache.beam.sdk.io.AvroIO;
import org.apache.beam.sdk.options.ValueProvider;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.values.PBegin;
import org.apache.beam.sdk.values.PCollection;

public class AvroToStructTransform extends PTransform<PBegin, PCollection<Struct>> {

    private final ValueProvider<String> input;

    public AvroToStructTransform(ValueProvider<String> input) {
        this.input = input;
    }

    public final PCollection<Struct> expand(PBegin begin) {
        PCollection<Struct> structs = begin
                .apply("ReadAvroFiles", AvroIO
                        .parseGenericRecords(RecordToStructConverter::convert)
                        .withCoder(AvroCoder.of(Struct.class))
                        .from(this.input));
        return structs;
    }
}

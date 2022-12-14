package com.google.cloud.teleport.templates;

import com.google.cloud.teleport.transforms.JdbcQueryIO;
import com.google.cloud.teleport.transforms.StructToAvroTransform;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.options.*;
import org.apache.beam.sdk.options.ValueProvider;

public class JdbcToAvro {

    public interface JdbcToAvroPipelineOption extends PipelineOptions {

        @Description("SQL query to extract records")
        ValueProvider<String> getQuery();
        void setQuery(ValueProvider<String> query);

        @Description("GCS path to output. prefix must start with gs://")
        ValueProvider<String> getOutput();
        void setOutput(ValueProvider<String> output);

        @Description("DriverClass, `com.mysql.cj.jdbc.Driver` or `org.postgresql.Driver`")
        ValueProvider<String> getDriverClass();
        void setDriverClass(ValueProvider<String> fieldKey);

        @Description("Database connection URL")
        ValueProvider<String> getUrl();
        void setUrl(ValueProvider<String> url);

        @Description("Database username to access")
        ValueProvider<String> getUsername();
        void setUsername(ValueProvider<String> username);

        @Description("Database access user's password")
        ValueProvider<String> getPassword();
        void setPassword(ValueProvider<String> password);

        @Description("CyptoKeyName to decrypt password by Cloud KMS")
        ValueProvider<String> getCryptoKeyName();
        void setCryptoKeyName(ValueProvider<String> getCryptoKeyName);

        @Description("Struct field key to separate output path")
        ValueProvider<String> getFieldKey();
        void setFieldKey(ValueProvider<String> fieldKey);

        @Description("Use snappy or default codec")
        @Default.Boolean(true)
        ValueProvider<Boolean> getUseSnappy();
        void setUseSnappy(ValueProvider<Boolean> useSnappy);

        @Description("File name")
        ValueProvider<String> getFileName();
        void setFileName(ValueProvider<String> fileName);

    }

    public static void main(String[] args) {

        // Date date = new Date();
        // SimpleDateFormat FormatDate = new SimpleDateFormat("yyyyMMdd");
        // String strDate = FormatDate.format(date);
        
        JdbcToAvroPipelineOption options = PipelineOptionsFactory.fromArgs(args).as(JdbcToAvroPipelineOption.class);
        Pipeline pipeline = Pipeline.create(options);
        
        pipeline.apply("Query", JdbcQueryIO.read(
                        options.getDriverClass(),
                        options.getUrl(),
                        options.getUsername(),
                        options.getPassword(),
                        options.getQuery(),
                        options.getCryptoKeyName()))
                .apply("StoreGCSAvro", new StructToAvroTransform(
                        options.getOutput(),
                        options.getFileName(),
                        options.getFieldKey(),
                        options.getUseSnappy()));

        pipeline.run();
    }
}
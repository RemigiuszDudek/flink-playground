package prv.dudekre.flink.producer;

import org.apache.flink.api.common.ExecutionConfig;
import org.apache.flink.api.common.serialization.TypeInformationSerializationSchema;
import org.apache.flink.api.java.typeutils.GenericTypeInfo;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import prv.dudekre.flink.exception.ExceptionEvent;
import prv.dudekre.flink.util.ListSource;

import static prv.dudekre.flink.exception.ExceptionState.OPENED;

public class ExceptionEventProducer {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment
                .getExecutionEnvironment();
        env.addSource(new ListSource(new ExceptionEvent("1", OPENED)))
                .addSink(new FlinkKafkaProducer<>(
                        "localhost:9092",
                        "my-test-topic",
                        new TypeInformationSerializationSchema<>(new GenericTypeInfo<>(ExceptionEvent.class), new ExecutionConfig())));
        env.execute();
    }
}

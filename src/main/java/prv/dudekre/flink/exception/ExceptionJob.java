package prv.dudekre.flink.exception;

import org.apache.flink.api.common.ExecutionConfig;
import org.apache.flink.api.common.serialization.TypeInformationSerializationSchema;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.typeutils.GenericTypeInfo;
import org.apache.flink.runtime.state.filesystem.FsStateBackend;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import prv.dudekre.flink.util.ListSink;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import static java.util.UUID.randomUUID;
import static prv.dudekre.flink.exception.ExceptionState.CLOSED;
import static prv.dudekre.flink.exception.ExceptionState.OPENED;

public class ExceptionJob {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment
                .getExecutionEnvironment()
                .enableCheckpointing(10_000)
                .setStateBackend(new FsStateBackend("file:///d:/tmp/flink/checkpoints"));
        env.setStreamTimeCharacteristic(TimeCharacteristic.IngestionTime);
        ListSink<ExceptionHistory> exceptionReportSink = new ListSink<>();
        env.addSource(kafkaConsumer())
                .keyBy((KeySelector<ExceptionEvent, String>) ExceptionEvent::getId)
                .process(new ExceptionProcessFunction())
                .addSink(exceptionReportSink);
        env.execute();
        exceptionReportSink.elements().forEach(System.out::println);
    }

    private static FlinkKafkaConsumer<ExceptionEvent> kafkaConsumer() {
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "localhost:9092");
        properties.setProperty("zookeeper.connect", "localhost:2181");
        properties.setProperty("group.id", "test");
        return new FlinkKafkaConsumer<>("my-test-topic",
                new TypeInformationSerializationSchema<>(new GenericTypeInfo<>(ExceptionEvent.class), new ExecutionConfig()),
                properties);
    }

    private static class ExceptionEventSourceFunction implements SourceFunction<ExceptionEvent> {
        private List<String> generatedIds = new ArrayList<>();

        @Override
        public void run(SourceContext<ExceptionEvent> ctx) {
            Random random = new Random();
            for (int i = 0; i < 100; i++) {
                sleep();
                int indicator = random.nextInt(2);
                if (indicator == 1 || generatedIds.size() == 0) {
                    String id = randomUUID().toString();
                    ctx.collect(new ExceptionEvent(id, OPENED));
                    generatedIds.add(id);
                } else {
                    int idx = random.nextInt(generatedIds.size());
                    ctx.collect(new ExceptionEvent(generatedIds.get(idx), CLOSED));
                    generatedIds.remove(idx);
                }
            }
        }

        private void sleep() {
            try {
                Random r = new Random();
                Thread.sleep(r.nextInt(100));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void cancel() {

        }
    }
}

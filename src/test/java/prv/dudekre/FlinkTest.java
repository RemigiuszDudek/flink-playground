package prv.dudekre;

import org.apache.flink.api.common.ExecutionConfig;
import org.apache.flink.api.common.serialization.TypeInformationSerializationSchema;
import org.apache.flink.api.java.typeutils.GenericTypeInfo;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.junit.Test;
import prv.dudekre.flink.exception.ExceptionEvent;
import prv.dudekre.flink.util.ListSource;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static prv.dudekre.flink.exception.ExceptionState.OPENED;

public class FlinkTest {
    private StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

    @Test
    public void twoSubsequentMessagesForTheSameException() throws Exception {
        String id = UUID.randomUUID().toString();
        sendEvents(
                new ExceptionEvent("1", OPENED)
        );
        System.out.println(id);
        env.execute();
    }

    private void sendEvents(ExceptionEvent... exceptionEvents) {
        List<ExceptionEvent> events = Arrays.asList(exceptionEvents);
//        Collections.reverse(events);
        env.addSource(new ListSource(events))
                .addSink(new FlinkKafkaProducer<>(
                        "localhost:9092",
                        "my-test-topic",
                        new TypeInformationSerializationSchema<>(new GenericTypeInfo<>(ExceptionEvent.class), new ExecutionConfig())));
    }
}

package prv.dudekre.flink;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import prv.dudekre.flink.util.ListSink;

import java.util.Random;

public class StreamProcessing {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        ListSink<TransactionEvent> transactionEventSink = new ListSink<>();
        env.addSource(new TransactionEventSourceFunction())
                .addSink(transactionEventSink);
        env.execute();
        transactionEventSink.elements().forEach(System.out::println);
    }

    private static class TransactionEventSourceFunction implements SourceFunction<TransactionEvent> {
        @Override
        public void run(SourceContext<TransactionEvent> ctx) throws Exception {
            for (int i = 0; i < 100; i++) {
                Random randomId = new Random();
                ctx.collect(new TransactionEvent("id"+randomId.nextInt(10), 1));
            }
        }

        @Override
        public void cancel() {

        }
    }

    private static class TransactionEventIntegerMapFunction implements MapFunction<TransactionEvent, Integer> {
        @Override
        public Integer map(TransactionEvent transactionEvent) throws Exception {
            return transactionEvent.value;
        }
    }
}

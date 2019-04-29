package prv.dudekre.flink.exception;

import org.apache.flink.api.common.state.StateTtlConfig;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ExceptionProcessFunction extends KeyedProcessFunction<String, ExceptionEvent, ExceptionHistory> {
    private static final Logger LOG = LoggerFactory.getLogger(ExceptionProcessFunction.class);

    private transient ValueState<ExceptionHistory> exceptionHistory;

    @Override
    public void open(Configuration parameters) {
        ValueStateDescriptor<ExceptionHistory> valueStateDescriptor = new ValueStateDescriptor<>(
                "Exception report", ExceptionHistory.class
        );

        StateTtlConfig timeToLiveConfig = StateTtlConfig.newBuilder(Time.seconds(10))
                .neverReturnExpired()
                .updateTtlOnReadAndWrite()
                .useProcessingTime()
                .cleanupFullSnapshot()
                .build();

        valueStateDescriptor.enableTimeToLive(timeToLiveConfig);

        exceptionHistory = getRuntimeContext().getState(valueStateDescriptor);
    }

    @Override
    public void processElement(ExceptionEvent exceptionEvent, Context ctx, Collector<ExceptionHistory> out) throws IOException {
        LOG.info(exceptionEvent.toString());
        ExceptionHistory value = this.exceptionHistory.value();
        ExceptionHistory exceptionHistory = value == null
                ? ExceptionHistory.start(exceptionEvent)
                : value.update(exceptionEvent);

        this.exceptionHistory.update(exceptionHistory);
        out.collect(exceptionHistory);
    }

    @Override
    public void onTimer(long timestamp, OnTimerContext ctx, Collector<ExceptionHistory> out) throws Exception {
        super.onTimer(timestamp, ctx, out);
    }
}

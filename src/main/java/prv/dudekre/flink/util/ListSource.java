package prv.dudekre.flink.util;

import org.apache.flink.streaming.api.functions.source.SourceFunction;
import prv.dudekre.flink.exception.ExceptionEvent;

import java.util.List;

import static java.util.Arrays.asList;

public class ListSource implements SourceFunction<ExceptionEvent> {
    private List<ExceptionEvent> events;

    public ListSource(ExceptionEvent... exceptionEvents) {
        events = asList(exceptionEvents);
    }

    public ListSource(List<ExceptionEvent> exceptionEvents) {
        events = exceptionEvents;
    }

    @Override
    public void run(SourceContext<ExceptionEvent> ctx) {
        events.forEach(ctx::collect);
    }

    private void safeSleep() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void cancel() {
    }
}

package prv.dudekre.flink.util;

import org.apache.flink.streaming.api.functions.sink.SinkFunction;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class ListSink<T> implements SinkFunction<T> {
    private static List<Object> elements = new ArrayList<>();

    @Override
    public void invoke(T value, Context context) throws Exception {
        System.out.println(value);
        elements.add(value);
    }

    public List<T> elements() {
        return elements.stream().map(o -> (T) o).collect(toList());
    }
}

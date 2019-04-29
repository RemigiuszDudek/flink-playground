package prv.dudekre.flink.exception;

import java.io.Serializable;

public class ExceptionEvent implements Serializable {
    private final String id;
    private final ExceptionState state;

    public ExceptionEvent(String id, ExceptionState state) {
        this.id = id;
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public ExceptionState getState() {
        return state;
    }

    @Override
    public String toString() {
        return "ExceptionEvent{" +
                "id='" + id + '\'' +
                ", state=" + state +
                '}';
    }
}

package prv.dudekre.flink;

import java.io.Serializable;

public class TransactionEvent implements Serializable {
    String id;
    Integer value;

    public TransactionEvent(String id, int value) {
        this.id = id;
        this.value= value;
    }

    public String getId() {
        return id;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "TransactionEvent{" +
                "id='" + id + '\'' +
                ", value=" + value +
                '}';
    }
}

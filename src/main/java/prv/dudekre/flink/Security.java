package prv.dudekre.flink;

import org.apache.flink.api.java.tuple.Tuple3;

public class Security extends Tuple3<String, String, Long> {
    public static final int ISIN_ID_IDX = 0;
    public static final int CURRENCY_IDX = 1;
    public static final int SECURITY_VALUE_IDX = 2;

    public Security() {}

    public Security(String isinId, String currency, long securityValue) {
        this.f0 = isinId;
        this.f1 = currency;
        this.f2 = securityValue;
    }

    public String getIsinId() {
        return f0;
    }

    public String getCurrency() {
        return f1;
    }

    public long getSecurityValue() {
        return f2;
    }
}

package prv.dudekre.flink;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.tuple.Tuple4;

import static prv.dudekre.flink.Security.*;

public class CollateralValuationJob {
    public static void main(String[] args) throws Exception {
        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        DataSource<Tuple4<String, String, Long, String>> valuations = env.readCsvFile("d:/Projects/flink-playground/src/main/resources/input.csv")
                .ignoreFirstLine()
                .types(String.class, String.class, Long.class, String.class);

        valuations
                .map(new MapFunction<Tuple4<String, String, Long, String>, Security>() {
                    @Override
                    public Security map(Tuple4<String, String, Long, String> value) throws Exception {
                        String isinId = value.getField(1);
                        String currency = value.getField(3);
                        Long securityValue = value.getField(2);
                        return new Security(isinId, currency, securityValue);
                    }
                })
                .groupBy(ISIN_ID_IDX, CURRENCY_IDX)
                .sum(SECURITY_VALUE_IDX)
                .print();
    }
}

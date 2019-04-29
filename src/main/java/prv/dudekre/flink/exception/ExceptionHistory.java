package prv.dudekre.flink.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

import static prv.dudekre.flink.exception.ExceptionState.CLOSED;
import static prv.dudekre.flink.exception.ExceptionState.OPENED;

public class ExceptionHistory {
    private static Logger LOG = LoggerFactory.getLogger(ExceptionHistory.class);

    private final String exceptionId;
    private Date openDate;
    private Date closeDate;
    private ExceptionState currentState;

    private ExceptionHistory(String exceptionId, Date openDate, Date closeDate, ExceptionState currentState) {
        this.exceptionId = exceptionId;
        this.openDate = openDate;
        this.closeDate = closeDate;
        this.currentState = currentState;
    }

    static ExceptionHistory start(ExceptionEvent exceptionEvent) {
        if (exceptionEvent.getState() == OPENED) {
            return new ExceptionHistory(exceptionEvent.getId(), new Date(), null, OPENED);
        } else {
            LOG.warn("creating closed exception: {}", exceptionEvent);
            return new ExceptionHistory(exceptionEvent.getId(), new Date(), null, CLOSED);
        }
    }

    ExceptionHistory update(ExceptionEvent event) {
        if (event.getState() == OPENED) {
            LOG.warn("exception cannot be reopened: {}", this);
        }
        return markAsClosed();
    }

    private ExceptionHistory markAsClosed() {
        if (currentState == CLOSED) LOG.warn("exception already closed: {}", this);
        currentState = ExceptionState.CLOSED;
        closeDate = new Date();
        return this;
    }

    public String getExceptionId() {
        return exceptionId;
    }

    public Date getOpenDate() {
        return openDate;
    }

    public Date getCloseDate() {
        return closeDate;
    }

    public ExceptionState getCurrentState() {
        return currentState;
    }

    @Override
    public String toString() {
        return "ExceptionHistory{" +
                "exceptionId='" + exceptionId + '\'' +
                ", openDate=" + openDate +
                ", closeDate=" + closeDate +
                ", currentState=" + currentState +
                '}';
    }
}

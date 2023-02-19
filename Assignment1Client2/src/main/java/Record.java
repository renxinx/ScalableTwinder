import java.sql.Timestamp;

public class Record {
    private Timestamp beforeRequest;
    private String requestFunction;
    private int responseCode;
    private long latency;

    public Record(Timestamp beforeRequest, String requestFunction, int responseCode, long latency) {
        this.beforeRequest = beforeRequest;
        this.requestFunction = requestFunction;
        this.responseCode = responseCode;
        this.latency = latency;
    }

    public Timestamp getBeforeRequest() {
        return beforeRequest;
    }

    public void setBeforeRequest(Timestamp beforeRequest) {
        this.beforeRequest = beforeRequest;
    }

    public String getRequestFunction() {
        return requestFunction;
    }

    public void setRequestFunction(String requestFunction) {
        this.requestFunction = requestFunction;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public long getLatency() {
        return latency;
    }

    public void setLatency(long latency) {
        this.latency = latency;
    }
}

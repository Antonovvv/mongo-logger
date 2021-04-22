package com.qww.mongologger.mapreduce;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class LogAsKey implements WritableComparable<LogAsKey> {
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    private String requestURL;
    private String logTime;

    public LogAsKey() {}

    public LogAsKey(String logTime) {
        this.setLogTime(logTime);
    }

    public void set(String requestURL, String logTime) {
        this.setRequestURL(requestURL);
        this.setLogTime(logTime);
    }

    public void setRequestURL(String requestURL) {
        this.requestURL = requestURL;
    }

    public String getRequestURL() {
        return requestURL;
    }

    public void setLogTime(String logTime) {
        this.logTime = logTime;
    }

    public String getLogTime() {
        return logTime;
    }

    @Override
    public int compareTo(LogAsKey log) {
        try {
            Date time1 = sdf.parse(logTime);
            Date time2 = sdf.parse(log.logTime);
            return time1.compareTo(time2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        String reqURL = dataInput.readUTF();
        String time = dataInput.readUTF();
        this.set(reqURL, time);
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(this.requestURL);
        dataOutput.writeUTF(this.logTime);
    }

    @Override
    public String toString() {
        return "LogComparator{" +
                "logTime=" + logTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LogAsKey)) return false;
        LogAsKey logAsKey = (LogAsKey) o;
        return Objects.equals(getRequestURL(), logAsKey.getRequestURL()) &&
                Objects.equals(getLogTime(), logAsKey.getLogTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRequestURL(), getLogTime());
    }
}

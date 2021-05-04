package com.qww.mongologger.mapreduce.routetimeline;

import com.qww.mongologger.mapreduce.RouteKey;
import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

public class RouteTimelineKey extends RouteKey implements WritableComparable<RouteTimelineKey> {
    private TimelineHelper timelineHelper;
    private String logTime;
    public boolean isNeedTimelineHelper = true;

    public void set(String requestURL, String requestMethod, Date logTime) {
        this.set(requestURL, requestMethod, TimelineHelper.sdf.format(logTime));
    }

    public void set(String requestURL, String requestMethod, String logTime) {
        this.setRequestURL(requestURL);
        this.setRequestMethod(requestMethod);
        this.setLogTime(logTime);
    }

    public void setTimeLineHelper(TimelineHelper timelineHelper) {
        this.timelineHelper = timelineHelper;
        this.isNeedTimelineHelper = false;
    }

    public void setLogTime(String logTime) {
        this.logTime = logTime;
    }

    public String getLogTime() {
        return logTime;
    }

    @Override
    public int compareTo(RouteTimelineKey log) {
        try {
            return timelineHelper.getDifference(logTime, log.logTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(this.requestURL);
        dataOutput.writeUTF(this.requestMethod);
        dataOutput.writeUTF(this.logTime);
        dataOutput.writeUTF(this.timelineHelper.serialize());
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        String reqURL = dataInput.readUTF();
        String reqMethod = dataInput.readUTF();
        String time = dataInput.readUTF();
        String serial = dataInput.readUTF();
        try {
            this.setTimeLineHelper(new TimelineHelper().deserialize(serial));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.set(reqURL, reqMethod, time);
    }
}

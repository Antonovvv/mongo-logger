package com.qww.mongologger.mapreduce.routetimeline;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimelineHelper implements Writable {
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    public static int DEFAULT_INTERVAL_COUNT = 20;

    private Date startTime;
    private Date endTime;
    private int intervalCount = DEFAULT_INTERVAL_COUNT;
    private long interval;

    public TimelineHelper() {
        this(1);
    }

    /**
     * @param days 当前时间之前的天数跨度
     */
    public TimelineHelper(int days) {
        this(getDateBeforeNow(days));
    }

    /**
     * @param startTime as pattern "yyyy-MM-dd HH:mm:ss.SSS"
     */
    public TimelineHelper(String startTime) throws ParseException {
        this(sdf.parse(startTime));
    }

    public TimelineHelper(String startTime, String endTime) throws ParseException {
        this(sdf.parse(startTime), sdf.parse(endTime));
    }

    public TimelineHelper(Date startTime) {
        this(startTime, new Date());
    }

    public TimelineHelper(Date startTime, Date endTime) {
        this(startTime, endTime, DEFAULT_INTERVAL_COUNT);
    }

    public TimelineHelper(Date startTime, Date endTime, int intervalCount) {
        this.setStartTime(startTime);
        this.setEndTime(endTime);
        this.setInterval((endTime.getTime() - startTime.getTime()) / intervalCount);
    }

    public TimelineHelper(Date startTime, Date endTime, long interval) {
        this.setStartTime(startTime);
        this.setEndTime(endTime);
        this.setInterval(interval);
    }

    public void set(Date startTime, Date endTime, int intervalCount, long interval) {
        this.setStartTime(startTime);
        this.setEndTime(endTime);
        this.setIntervalCount(intervalCount);
        this.setInterval(interval);
    }

    public TimelineHelper deserialize(String serial) throws ParseException {
        String[] fields = serial.split("\\|");
        this.set(sdf.parse(fields[0]), sdf.parse(fields[1]), Integer.parseInt(fields[2]), Long.parseLong(fields[3]));
        return this;
    }

    public int getDifference(String Date1, String Date2) throws ParseException {
        return this.getDifference(sdf.parse(Date1), sdf.parse(Date2));
    }

    public int getDifference(Date Date1, Date Date2) {
        if (Date1.before(startTime) || Date1.after(endTime) || Date2.before(startTime) || Date2.after(endTime)) {
            throw new IllegalArgumentException("Invalid date");
        }
        int part1 = (int) ((Date1.getTime() - startTime.getTime()) / interval);
        int part2 = (int) ((Date2.getTime() - startTime.getTime()) / interval);
        return part1 - part2;
    }

    public String getTimeInLine(String time) throws ParseException {
        return this.getTimeInLine(sdf.parse(time));
    }

    public String getTimeInLine(Date time) {
        if (time.before(startTime) || time.after(endTime)) {
            throw new IllegalArgumentException("Invalid time");
        }
        int part = (int) ((time.getTime() - startTime.getTime()) / interval);
        long timeInLine = startTime.getTime() + part * interval;
        return sdf.format(new Date(timeInLine));
    }

    public String getIntervalTime(int intervalCnt) {
        long intervalTime = startTime.getTime() + interval * intervalCnt;
        return sdf.format(new Date(intervalTime));
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setIntervalCount(int intervalCount) {
        this.intervalCount = intervalCount;
    }

    public int getIntervalCount() {
        return intervalCount;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public long getInterval() {
        return interval;
    }

    public static Date getDateBeforeNow(int days) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, -days);
        return calendar.getTime();
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(sdf.format(this.startTime));
        dataOutput.writeUTF(sdf.format(this.endTime));
        dataOutput.writeInt(this.intervalCount);
        dataOutput.writeLong(this.interval);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        try {
            Date startTime = sdf.parse(dataInput.readUTF());
            Date endTime = sdf.parse(dataInput.readUTF());
            int intervalCount = dataInput.readInt();
            long interval = dataInput.readLong();
            this.set(startTime, endTime, intervalCount, interval);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String serialize() {
        return String.join("|",
                sdf.format(startTime),
                sdf.format(endTime),
                String.valueOf(intervalCount),
                String.valueOf(interval));
    }

    @Override
    public String toString() {
        return "TimelineHelper{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                ", intervalCount=" + intervalCount +
                ", interval=" + interval +
                '}';
    }
}

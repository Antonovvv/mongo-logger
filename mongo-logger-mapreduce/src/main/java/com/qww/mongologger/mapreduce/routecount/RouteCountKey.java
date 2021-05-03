package com.qww.mongologger.mapreduce.routecount;

import com.qww.mongologger.mapreduce.RouteKey;
import org.apache.hadoop.io.WritableComparable;
import org.springframework.lang.NonNull;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.Field;

public class RouteCountKey extends RouteKey implements WritableComparable<RouteCountKey> {
    @Override
    public int compareTo(@NonNull RouteCountKey routeCountKey) {
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field f : fields) {
            if (f.getType() != String.class) continue;
            try {
                String v1 = (String) f.get(this);
                String v2 = (String) f.get(routeCountKey);
                int comp = v1.compareTo(v2);
                if (comp != 0) return comp;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(this.requestURL);
        dataOutput.writeUTF(this.requestMethod);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        String reqURL = dataInput.readUTF();
        String reqMethod = dataInput.readUTF();
        this.set(reqURL, reqMethod);
    }
}

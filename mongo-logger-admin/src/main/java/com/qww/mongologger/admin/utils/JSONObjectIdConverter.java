package com.qww.mongologger.admin.utils;

import org.bson.json.Converter;
import org.bson.json.StrictJsonWriter;
import org.bson.types.ObjectId;

public class JSONObjectIdConverter implements Converter<ObjectId> {
    @Override
    public void convert(ObjectId value, StrictJsonWriter writer) {
        writer.writeString(value.toString());
    }
}

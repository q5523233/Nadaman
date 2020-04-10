package com.sm.nadaman.common.db;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.List;

public class StringConverter implements PropertyConverter<List<List<Short>>, String> {
    @Override
    public List<List<Short>> convertToEntityProperty(String databaseValue) {
        return new Gson().fromJson(databaseValue, new TypeToken<List<List<Short>>>() {
        }.getType());
    }

    @Override
    public String convertToDatabaseValue(List<List<Short>> entityProperty) {
        return new Gson().toJson(entityProperty);

    }

}

package com.sm.nadaman.common.bean;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
@Entity
public class UserList {

    @Id(autoincrement = true)
    Long id;

    @Convert(converter = UserConverter.class, columnType = String.class)
    List<User> users;

    @Generated(hash = 1850295718)
    public UserList(Long id, List<User> users) {
        this.id = id;
        this.users = users;
    }

    @Generated(hash = 2088493246)
    public UserList() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<User> getUsers() {
        return this.users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public static class UserConverter implements PropertyConverter<List<User>, String> {

        @Override
        public List<User> convertToEntityProperty(String databaseValue) {
            TypeToken<List<User>> typeToken = new TypeToken<List<User>>() {
            };
            return new Gson().fromJson(databaseValue, typeToken.getType());
        }

        @Override
        public String convertToDatabaseValue(List<User> entityProperty) {
            return new Gson().toJson(entityProperty);
        }
    }
}

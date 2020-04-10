package com.sm.nadaman.common.bean;

import com.sm.nadaman.common.db.StringConverter;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
@Entity
public class Ecg12Data implements Serializable {
    private static final long serialVersionUID = -1L;
    @Id
    Long id;
    @Convert(converter = StringConverter.class, columnType = String.class)
    List<List<Short>> data;
    @Generated(hash = 1124188363)
    public Ecg12Data(Long id, List<List<Short>> data) {
        this.id = id;
        this.data = data;
    }
    @Generated(hash = 1176498556)
    public Ecg12Data() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public List<List<Short>> getData() {
        return this.data;
    }
    public void setData(List<List<Short>> data) {
        this.data = data;
    }
}

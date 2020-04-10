package com.sm.nadaman.common.db;

import java.util.List;

public interface BaseOpe<T> {
    public List<T> getList();

    public long insert(T data);

    public void update(T data);

    public void delete(T data);
    
}

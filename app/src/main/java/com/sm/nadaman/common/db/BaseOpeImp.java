package com.sm.nadaman.common.db;

import org.greenrobot.greendao.AbstractDao;

import java.util.List;

public abstract class BaseOpeImp<T> implements BaseOpe<T> {
    AbstractDao dao;

    BaseOpeImp() {
        dao = createDao();
        if (dao == null) {
            throw new IllegalStateException("dao not create");
        }
    }

    protected abstract AbstractDao createDao();

    @Override
    public List<T> getList() {
        return dao.queryBuilder().list();
    }

    @Override
    public long insert(T data) {
        return dao.insert(data);
    }

    @Override
    public void update(T data) {
        dao.update(data);
    }

    @Override
    public void delete(T data) {
        dao.delete(data);
    }
}

package com.sm.nadaman.common.bean;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.DaoException;
import com.sm.nadaman.common.dao.DaoSession;
import com.sm.nadaman.common.dao.Ecg12DataDao;
import com.sm.nadaman.common.dao.HealthDao;

@Entity
public class Health implements Serializable {
    private static final long serialVersionUID = -1L;

    @Id(autoincrement = true)
    private Long Id;

    private int aveHeartRate;
    private int minHeartRate;
    private int maxHeartRate;
    private String heartStrException;
    private int exceptionLevel;

    private int measureDuration;
    //波形数据
    private String mPoint;
    private String date;
    private int type;//0 单导联 1 12导联
    @ToOne(joinProperty = "Id")
    private Ecg12Data ecg12Data;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1380668564)
    private transient HealthDao myDao;

    @Generated(hash = 1556397871)
    private transient Long ecg12Data__resolvedKey;

    public Health() {
    }

    @Generated(hash = 920296183)
    public Health(Long Id, int aveHeartRate, int minHeartRate, int maxHeartRate,
            String heartStrException, int exceptionLevel, int measureDuration,
            String mPoint, String date, int type) {
        this.Id = Id;
        this.aveHeartRate = aveHeartRate;
        this.minHeartRate = minHeartRate;
        this.maxHeartRate = maxHeartRate;
        this.heartStrException = heartStrException;
        this.exceptionLevel = exceptionLevel;
        this.measureDuration = measureDuration;
        this.mPoint = mPoint;
        this.date = date;
        this.type = type;
    }

    public Long getId() {
        return this.Id;
    }

    public void setId(Long Id) {
        this.Id = Id;
    }

    public int getAveHeartRate() {
        return this.aveHeartRate;
    }

    public void setAveHeartRate(int aveHeartRate) {
        this.aveHeartRate = aveHeartRate;
    }

    public int getMinHeartRate() {
        return this.minHeartRate;
    }

    public void setMinHeartRate(int minHeartRate) {
        this.minHeartRate = minHeartRate;
    }

    public int getMaxHeartRate() {
        return this.maxHeartRate;
    }

    public void setMaxHeartRate(int maxHeartRate) {
        this.maxHeartRate = maxHeartRate;
    }

    public String getHeartStrException() {
        return this.heartStrException;
    }

    public void setHeartStrException(String heartStrException) {
        this.heartStrException = heartStrException;
    }

    public int getMeasureDuration() {
        return this.measureDuration;
    }

    public void setMeasureDuration(int measureDuration) {
        this.measureDuration = measureDuration;
    }

    public String getMPoint() {
        return this.mPoint;
    }

    public void setMPoint(String mPoint) {
        this.mPoint = mPoint;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getExceptionLevel() {
        return this.exceptionLevel;
    }

    public void setExceptionLevel(int exceptionLevel) {
        this.exceptionLevel = exceptionLevel;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1815963801)
    public Ecg12Data getEcg12Data() {
        Long __key = this.Id;
        if (ecg12Data__resolvedKey == null
                || !ecg12Data__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            Ecg12DataDao targetDao = daoSession.getEcg12DataDao();
            Ecg12Data ecg12DataNew = targetDao.load(__key);
            synchronized (this) {
                ecg12Data = ecg12DataNew;
                ecg12Data__resolvedKey = __key;
            }
        }
        return ecg12Data;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 625919662)
    public void setEcg12Data(Ecg12Data ecg12Data) {
        synchronized (this) {
            this.ecg12Data = ecg12Data;
            Id = ecg12Data == null ? null : ecg12Data.getId();
            ecg12Data__resolvedKey = Id;
        }
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 212554855)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getHealthDao() : null;
    }
}

package com.sm.nadaman.common.db;

import com.sm.nadaman.common.bean.Health;
import com.sm.nadaman.common.bean.User;
import com.sm.nadaman.common.dao.DaoSession;
import com.sm.nadaman.common.dao.HealthDao;
import com.techne.nomnompos.app.App;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by shenmai_vea on 2018/7/11.
 */

public class HealthOpe {
    private HealthDao healthDao;

    private HealthOpe() {
        DaoSession daoSession = App.Companion.getApp().getCurrentUserDaoSession();
        if (daoSession != null)
            healthDao = daoSession.getHealthDao();
    }

    public static HealthOpe create() {
        return new HealthOpe();
    }

    /**
     * 获取所有健康列表
     *
     * @return
     */
    public List<Health> getAllHealthList() {
        return healthDao.queryBuilder()
                .orderDesc(HealthDao.Properties.Id)
                .list();
    }

    public List<Health> getEcgHealthList() {
        return healthDao.queryBuilder()
                .orderDesc(HealthDao.Properties.Id)
                .where(HealthDao.Properties.Type.eq(0))
                .list();
    }

    public List<Health> getEcg12HealthList() {
        return healthDao.queryBuilder()
                .orderDesc(HealthDao.Properties.Id)
                .where(HealthDao.Properties.Type.eq(1))
                .list();
    }

    public List<String> getAllTimeList(User user,int type) {
        List<String> date = new ArrayList<>();
        List<Health> list = healthDao.queryBuilder()
                .orderDesc(HealthDao.Properties.Id)
                .where(HealthDao.Properties.Type.eq(type))
                .list();
        ListIterator<Health> healthListIterator = list.listIterator();
        while (healthListIterator.hasNext()) {
            date.add(healthListIterator.next().getDate());
        }
        return date;
    }

    public Health getHealthById(Long id) {
        return healthDao.queryBuilder()
                .where(HealthDao.Properties.Id.eq(id))
                .unique();
    }


    public List<Health> getUserHealthListFromDate( String date,int type) {
        return healthDao.queryBuilder()
                .where(HealthDao.Properties.Date.like("%" + date + "%"),HealthDao.Properties.Type.eq(type))
                .orderDesc(HealthDao.Properties.Id)
                .list();
    }


    public long insertHealth(Health health) {
        return healthDao.insert(health);
    }

    public void updateHealth(Health health) {
        healthDao.update(health);
    }

    public void delHealth(Health health) {
        healthDao.delete(health);
    }

    public void delHealthInDay(String date) {
        Iterator<Health> iterator = healthDao.queryBuilder()
                .where(HealthDao.Properties.Date.like("%" + date + "%"))
                .list()
                .iterator();
        while (iterator.hasNext()) {
            delHealth(iterator.next());
        }
    }

}

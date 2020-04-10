package com.sm.nadaman.common.db.manager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.sm.nadaman.common.dao.DaoMaster;
import com.sm.nadaman.common.dao.FriendDao;
import com.sm.nadaman.common.dao.HealthDao;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseOpenHelper;

public class UserInfoDaoMaster extends DaoMaster {
    public UserInfoDaoMaster(SQLiteDatabase db) {
        super(db);
    }
    /**
     * Creates underlying database table using DAOs.
     */
    public static void createAllTables(Database db, boolean ifNotExists) {
        /*Ecg12DataDao.createTable(db, ifNotExists);*/
        FriendDao.createTable(db, ifNotExists);
        HealthDao.createTable(db, ifNotExists);
    }

    /**
     * Drops underlying database table using DAOs.
     */
    public static void dropAllTables(Database db, boolean ifExists) {
        /*Ecg12DataDao.dropTable(db, ifExists);*/
        FriendDao.dropTable(db, ifExists);
        HealthDao.dropTable(db, ifExists);
    }

    public static class OpenHelper extends DatabaseOpenHelper {
        public OpenHelper(Context context, String name) {
            super(context, name, SCHEMA_VERSION);
        }

        public OpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
            super(context, name, factory, SCHEMA_VERSION);
        }

        @Override
        public void onCreate(Database db) {
            Log.i("greenDAO", "Creating tables for schema version " + SCHEMA_VERSION);
            createAllTables(db, false);
        }

        @Override
        public void onUpgrade(Database db, int oldVersion, int newVersion) {
            Log.i("greenDAO", "Upgrading schema from version " + oldVersion + " to " + newVersion + " by dropping all tables");
            dropAllTables(db, true);
            onCreate(db);
        }
    }
}

package com.sm.nadaman.common.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.sm.nadaman.common.bean.UserList.UserConverter;
import java.util.List;

import com.sm.nadaman.common.bean.UserList;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "USER_LIST".
*/
public class UserListDao extends AbstractDao<UserList, Long> {

    public static final String TABLENAME = "USER_LIST";

    /**
     * Properties of entity UserList.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Users = new Property(1, String.class, "users", false, "USERS");
    }

    private final UserConverter usersConverter = new UserConverter();

    public UserListDao(DaoConfig config) {
        super(config);
    }
    
    public UserListDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"USER_LIST\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"USERS\" TEXT);"); // 1: users
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"USER_LIST\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, UserList entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        List users = entity.getUsers();
        if (users != null) {
            stmt.bindString(2, usersConverter.convertToDatabaseValue(users));
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, UserList entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        List users = entity.getUsers();
        if (users != null) {
            stmt.bindString(2, usersConverter.convertToDatabaseValue(users));
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public UserList readEntity(Cursor cursor, int offset) {
        UserList entity = new UserList( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : usersConverter.convertToEntityProperty(cursor.getString(offset + 1)) // users
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, UserList entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setUsers(cursor.isNull(offset + 1) ? null : usersConverter.convertToEntityProperty(cursor.getString(offset + 1)));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(UserList entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(UserList entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(UserList entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}

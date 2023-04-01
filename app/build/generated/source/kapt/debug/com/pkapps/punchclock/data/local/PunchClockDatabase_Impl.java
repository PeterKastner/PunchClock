package com.pkapps.punchclock.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.pkapps.punchclock.feature_time_tracking.data.local.PunchClockDatabase;
import com.pkapps.punchclock.feature_time_tracking.data.local.WorkTimeDao;

import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"unchecked", "deprecation"})
public final class PunchClockDatabase_Impl extends PunchClockDatabase {
  private volatile WorkTimeDao _workTimeDao;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `work_time` (`id` BLOB NOT NULL, `start` TEXT NOT NULL, `end` TEXT, `pause` TEXT NOT NULL, `comment` TEXT NOT NULL, PRIMARY KEY(`id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '15c8a14717291fe8d0753ac435050b7d')");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `work_time`");
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onDestructiveMigration(_db);
          }
        }
      }

      @Override
      public void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      @Override
      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      @Override
      public void onPreMigrate(SupportSQLiteDatabase _db) {
        DBUtil.dropFtsSyncTriggers(_db);
      }

      @Override
      public void onPostMigrate(SupportSQLiteDatabase _db) {
      }

      @Override
      public RoomOpenHelper.ValidationResult onValidateSchema(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsWorkTime = new HashMap<String, TableInfo.Column>(5);
        _columnsWorkTime.put("id", new TableInfo.Column("id", "BLOB", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorkTime.put("start", new TableInfo.Column("start", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorkTime.put("end", new TableInfo.Column("end", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorkTime.put("pause", new TableInfo.Column("pause", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorkTime.put("comment", new TableInfo.Column("comment", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysWorkTime = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesWorkTime = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoWorkTime = new TableInfo("work_time", _columnsWorkTime, _foreignKeysWorkTime, _indicesWorkTime);
        final TableInfo _existingWorkTime = TableInfo.read(_db, "work_time");
        if (! _infoWorkTime.equals(_existingWorkTime)) {
          return new RoomOpenHelper.ValidationResult(false, "work_time(com.pkapps.punchclock.data.local.WorkTime).\n"
                  + " Expected:\n" + _infoWorkTime + "\n"
                  + " Found:\n" + _existingWorkTime);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "15c8a14717291fe8d0753ac435050b7d", "bb7d8c5ce7cad75609afea38fc4a4fa6");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "work_time");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `work_time`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(WorkTimeDao.class, WorkTimeDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  public List<Migration> getAutoMigrations(
      @NonNull Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecsMap) {
    return Arrays.asList();
  }

  @Override
  public WorkTimeDao getWorkTimeDao() {
    if (_workTimeDao != null) {
      return _workTimeDao;
    } else {
      synchronized(this) {
        if(_workTimeDao == null) {
          _workTimeDao = new WorkTimeDao_Impl(this);
        }
        return _workTimeDao;
      }
    }
  }
}

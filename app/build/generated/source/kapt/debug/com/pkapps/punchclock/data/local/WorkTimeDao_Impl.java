package com.pkapps.punchclock.data.local;

import android.database.Cursor;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.EntityUpsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.UUIDUtil;
import androidx.sqlite.db.SupportSQLiteStatement;

import com.pkapps.punchclock.feature_time_tracking.data.local.PunchClockTypeConverters;
import com.pkapps.punchclock.feature_time_tracking.data.local.WorkTime;
import com.pkapps.punchclock.feature_time_tracking.data.local.WorkTimeDao;

import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@SuppressWarnings({"unchecked", "deprecation"})
public final class WorkTimeDao_Impl implements WorkTimeDao {
  private final RoomDatabase __db;

  private final EntityDeletionOrUpdateAdapter<WorkTime> __deletionAdapterOfWorkTime;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllWorkTimes;

  private final EntityUpsertionAdapter<WorkTime> __upsertionAdapterOfWorkTime;

  public WorkTimeDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__deletionAdapterOfWorkTime = new EntityDeletionOrUpdateAdapter<WorkTime>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `work_time` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, WorkTime value) {
        if (value.getId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindBlob(1, UUIDUtil.convertUUIDToByte(value.getId()));
        }
      }
    };
    this.__preparedStmtOfDeleteAllWorkTimes = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM work_time";
        return _query;
      }
    };
    this.__upsertionAdapterOfWorkTime = new EntityUpsertionAdapter<WorkTime>(new EntityInsertionAdapter<WorkTime>(__db) {
      @Override
      public String createQuery() {
        return "INSERT INTO `work_time` (`id`,`start`,`end`,`pause`,`comment`) VALUES (?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, WorkTime value) {
        if (value.getId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindBlob(1, UUIDUtil.convertUUIDToByte(value.getId()));
        }
        final String _tmp = PunchClockTypeConverters.INSTANCE.fromLocalDateTime(value.getStart());
        if (_tmp == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, _tmp);
        }
        final String _tmp_1 = PunchClockTypeConverters.INSTANCE.fromLocalDateTime(value.getEnd());
        if (_tmp_1 == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, _tmp_1);
        }
        final String _tmp_2 = PunchClockTypeConverters.INSTANCE.fromDuration(value.getPause());
        if (_tmp_2 == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, _tmp_2);
        }
        if (value.getComment() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getComment());
        }
      }
    }, new EntityDeletionOrUpdateAdapter<WorkTime>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE `work_time` SET `id` = ?,`start` = ?,`end` = ?,`pause` = ?,`comment` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, WorkTime value) {
        if (value.getId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindBlob(1, UUIDUtil.convertUUIDToByte(value.getId()));
        }
        final String _tmp = PunchClockTypeConverters.INSTANCE.fromLocalDateTime(value.getStart());
        if (_tmp == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, _tmp);
        }
        final String _tmp_1 = PunchClockTypeConverters.INSTANCE.fromLocalDateTime(value.getEnd());
        if (_tmp_1 == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, _tmp_1);
        }
        final String _tmp_2 = PunchClockTypeConverters.INSTANCE.fromDuration(value.getPause());
        if (_tmp_2 == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, _tmp_2);
        }
        if (value.getComment() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getComment());
        }
        if (value.getId() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindBlob(6, UUIDUtil.convertUUIDToByte(value.getId()));
        }
      }
    });
  }

  @Override
  public Object deleteWorkTime(final WorkTime workTime,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfWorkTime.handle(workTime);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object deleteAllWorkTimes(final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllWorkTimes.acquire();
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
          __preparedStmtOfDeleteAllWorkTimes.release(_stmt);
        }
      }
    }, continuation);
  }

  @Override
  public Object upsertWorkTime(final WorkTime workTime,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __upsertionAdapterOfWorkTime.upsert(workTime);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object upsertWorkTimes(final WorkTime[] workTimes,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __upsertionAdapterOfWorkTime.upsert(workTimes);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Flow<List<WorkTime>> getWorkTimes() {
    final String _sql = "SELECT * FROM work_time";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[]{"work_time"}, new Callable<List<WorkTime>>() {
      @Override
      public List<WorkTime> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStart = CursorUtil.getColumnIndexOrThrow(_cursor, "start");
          final int _cursorIndexOfEnd = CursorUtil.getColumnIndexOrThrow(_cursor, "end");
          final int _cursorIndexOfPause = CursorUtil.getColumnIndexOrThrow(_cursor, "pause");
          final int _cursorIndexOfComment = CursorUtil.getColumnIndexOrThrow(_cursor, "comment");
          final List<WorkTime> _result = new ArrayList<WorkTime>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final WorkTime _item;
            final UUID _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = UUIDUtil.convertByteToUUID(_cursor.getBlob(_cursorIndexOfId));
            }
            final LocalDateTime _tmpStart;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfStart)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfStart);
            }
            _tmpStart = PunchClockTypeConverters.INSTANCE.toLocalDateTime(_tmp);
            final LocalDateTime _tmpEnd;
            final String _tmp_1;
            if (_cursor.isNull(_cursorIndexOfEnd)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getString(_cursorIndexOfEnd);
            }
            _tmpEnd = PunchClockTypeConverters.INSTANCE.toLocalDateTime(_tmp_1);
            final Duration _tmpPause;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfPause)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfPause);
            }
            _tmpPause = PunchClockTypeConverters.INSTANCE.toDuration(_tmp_2);
            final String _tmpComment;
            if (_cursor.isNull(_cursorIndexOfComment)) {
              _tmpComment = null;
            } else {
              _tmpComment = _cursor.getString(_cursorIndexOfComment);
            }
            _item = new WorkTime(_tmpId,_tmpStart,_tmpEnd,_tmpPause,_tmpComment);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<WorkTime> getWorkTimeWithEndTimeOfNull() {
    final String _sql = "SELECT DISTINCT * FROM work_time WHERE end IS NULL";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[]{"work_time"}, new Callable<WorkTime>() {
      @Override
      public WorkTime call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStart = CursorUtil.getColumnIndexOrThrow(_cursor, "start");
          final int _cursorIndexOfEnd = CursorUtil.getColumnIndexOrThrow(_cursor, "end");
          final int _cursorIndexOfPause = CursorUtil.getColumnIndexOrThrow(_cursor, "pause");
          final int _cursorIndexOfComment = CursorUtil.getColumnIndexOrThrow(_cursor, "comment");
          final WorkTime _result;
          if(_cursor.moveToFirst()) {
            final UUID _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = UUIDUtil.convertByteToUUID(_cursor.getBlob(_cursorIndexOfId));
            }
            final LocalDateTime _tmpStart;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfStart)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfStart);
            }
            _tmpStart = PunchClockTypeConverters.INSTANCE.toLocalDateTime(_tmp);
            final LocalDateTime _tmpEnd;
            final String _tmp_1;
            if (_cursor.isNull(_cursorIndexOfEnd)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getString(_cursorIndexOfEnd);
            }
            _tmpEnd = PunchClockTypeConverters.INSTANCE.toLocalDateTime(_tmp_1);
            final Duration _tmpPause;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfPause)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfPause);
            }
            _tmpPause = PunchClockTypeConverters.INSTANCE.toDuration(_tmp_2);
            final String _tmpComment;
            if (_cursor.isNull(_cursorIndexOfComment)) {
              _tmpComment = null;
            } else {
              _tmpComment = _cursor.getString(_cursorIndexOfComment);
            }
            _result = new WorkTime(_tmpId,_tmpStart,_tmpEnd,_tmpPause,_tmpComment);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}

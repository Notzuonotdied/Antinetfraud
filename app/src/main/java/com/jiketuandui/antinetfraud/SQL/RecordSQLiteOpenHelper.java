package com.jiketuandui.antinetfraud.SQL;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Notzuonotdied on 2016/9/16.
 * 保存搜索历史
 */
public class RecordSQLiteOpenHelper extends SQLiteOpenHelper {

    private static String name = "Recoder.db";
    private static Integer version = 1;
    private static SQLiteDatabase db;

    public RecordSQLiteOpenHelper(Context context) {
        super(context, name, null, version);
    }

    public RecordSQLiteOpenHelper(Context context, String name,
                                  SQLiteDatabase.CursorFactory factory,
                                  int version) {
        super(context, name, factory, version);
    }

    public RecordSQLiteOpenHelper(Context context, String name,
                                  SQLiteDatabase.CursorFactory factory,
                                  int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "create table records(id integer primary key autoincrement,name varchar(200))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    /**
     * 插入数据
     */
    public void insertData(String tempName) {
        db = getWritableDatabase();
        db.execSQL("insert into records(name) values('" + tempName + "')");
        db.close();
    }

    /**
     * 模糊查询数据
     */
    public Cursor queryData(String tempName) {
        return getReadableDatabase().rawQuery(
                "select id as _id,name from records where name like '%" + tempName +
                        "%' order by id desc ", null);
    }

    /**
     * 检查数据库中是否已经有该条记录
     */
    public boolean hasData(String tempName) {//判断是否有下一个
        return getReadableDatabase().rawQuery(
                "select id as _id,name from records where name =?", new String[]{tempName})
                .moveToNext();
    }

    /**
     * 清空数据
     */
    public void deleteData() {
        db = getWritableDatabase();
        db.execSQL("delete from records");
        db.close();
    }

}

package id.meteor.alfamind.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by bodacious on 28/12/17.
 */

public class MySQLiteHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "alfamind.db";
    private static final int DATABASE_VERSION = 1;

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.e("DATABASE","context");
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.e("DATABASE","contextOnCreate");
        TableAddress.onCreate(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

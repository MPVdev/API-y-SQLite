package com.example.supletoriomovil;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class BDHelper extends SQLiteOpenHelper {
    public BDHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE SupleMovil (" +
                "tempe DOUBLE," +
                "feels_like DOUBLE NOT NULL," +
                "temp_min DOUBLE NOT NULL," +
                "temp_max DOUBLE NOT NULL," +
                "pressure DOUBLE NOT NULL," +
                "sea_level DOUBLE NOT NULL," +
                "grnd_level DOUBLE NOT NULL," +
                "humidity DOUBLE NOT NULL," +
                "temp_kf DOUBLE NOT NULL)");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //CAMBIE LA VERSIÓN DE LA TABLA DE LA BDD
        db.execSQL("DROP TABLE SupleMovil");
        onCreate(db);
    }
}

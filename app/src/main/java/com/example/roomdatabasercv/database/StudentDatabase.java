package com.example.roomdatabasercv.database;

import android.content.Context;

import androidx.room.AutoMigration;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.roomdatabasercv.model.Student;

@Database(entities = {Student.class}, version = 1)

public abstract class StudentDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "student.db";
    private static StudentDatabase instance;

    public static synchronized StudentDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), StudentDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    public abstract StudentDAO studentDAO();
}

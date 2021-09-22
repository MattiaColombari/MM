package com.example.mm.optionActivity.localDatabaseInteraction;

import android.app.Activity;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteException;

import androidx.room.Room;

import com.example.mm.optionActivity.Option;

import java.util.List;

import localDatabase.LocalDatabase;
import localDatabase.LocalDatabaseDao;
import localDatabase.Tables.Course;

public class GetCourse implements  Runnable{
    Context context;
    LocalDatabaseDao localDatabaseDao;
    LocalDatabase localDatabase;
    Option optionActivity;

    public GetCourse(Context context, Option optionActivity) {
        this.context = context;
        localDatabase = Room.databaseBuilder(context, LocalDatabase.class, "LocalDatabase")
                .fallbackToDestructiveMigration() /* Is needed to overwrite the old scheme of the
         *  local database, it will ERASE all the current
         *  data.
         * */
                .build();
        localDatabaseDao = localDatabase.localDatabaseDao();
        this.optionActivity = optionActivity;
    }

    @Override
    public void run() {
        List<Course> courseList;
        try{
            courseList = localDatabaseDao.getAllCourse();
            this.updateDropDown(courseList, true);
        }
        catch (SQLiteException e){
            this.updateDropDown(null, false);
        }
    }

    void updateDropDown(List<Course> courseList, boolean error) {
        //updateDropDown
        if (context instanceof Activity) {
            Activity mainActivity = (Activity)context;
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    optionActivity.updateDropDown(courseList, error);
                }
            });
        }
    }
}

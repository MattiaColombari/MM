package com.example.mm.exerciseActivity.localDatabaseInteraction;

import android.content.Context;
import android.database.sqlite.SQLiteException;
import androidx.room.Room;
import com.example.mm.exerciseActivity.Exercise;
import java.util.ArrayList;
import localDatabase.LocalDatabase;
import localDatabase.LocalDatabaseDao;
import localDatabase.Tables.StatisticUser;

public class SetStatisticUser implements Runnable {
    Context context;
    LocalDatabase localDatabase;
    LocalDatabaseDao localDatabaseDao;

    Exercise exercise;
    ArrayList<StatisticUser> statisticUserArrayList;

    public SetStatisticUser(Context context, Exercise exercise, ArrayList<StatisticUser> statisticUserArrayList){
        this.context = context;
        this.exercise = exercise;
        this.statisticUserArrayList = statisticUserArrayList;

        localDatabase = Room.databaseBuilder(context, LocalDatabase.class, "LocalDatabase")
                .fallbackToDestructiveMigration()  /* Is needed to overwrite the old scheme of the
         *  local database, it will ERASE all the current
         *  data.
         * */
                .build();
        localDatabaseDao = localDatabase.localDatabaseDao();
    }
    @Override
    public void run() {
        try{
            for(StatisticUser statisticUser: statisticUserArrayList){
                localDatabaseDao.insertStatisticUser(statisticUser);
            }
        }
        catch(SQLiteException e){
            /* I don't check for error in case some date are not saved. */
        }
    }
}

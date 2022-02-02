package com.example.mm.homeActivity.localDatabaseInteraction;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteException;
import android.widget.PopupWindow;
import androidx.room.Room;
import com.example.mm.homeActivity.optionFragment.OptionFragment;
import localDatabase.LocalDatabase;
import localDatabase.LocalDatabaseDao;
import localDatabase.Tables.UserInformation;

public class SetUserInformation implements Runnable {
    Context context;
    PopupWindow popupWindow;
    OptionFragment optionFragment;
    SetUserInfoInterface setUserInfoInterface;
    String name;
    String surname;
    String email;
    int matr;

    public SetUserInformation(Context context, SetUserInfoInterface setUserInfoInterface, PopupWindow popupWindow, OptionFragment optionFragment, String name, String surname, String email, int matr) {
        this.context = context;
        this.popupWindow = popupWindow;
        this.optionFragment = optionFragment;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.matr = matr;

        this.setUserInfoInterface = setUserInfoInterface;
    }

    @Override
    public void run() {
        try{
            setUserInfoInterface.deleteAllUserInformation();
            setUserInfoInterface.insertUserInformation(new UserInformation(name, surname, email, matr));
            this.updateStatistic("Name: " + name, "Surname: " + surname, "Email: " + email, "Matr.: " + matr);
        }
        catch(SQLiteException e){
            this.updateStatistic("Error local database,", "", "Try add/update information again.", "", "Add/Update User Information");
        }
    }

    void updateStatistic(String name, String surname, String email, String matr){
        if (context instanceof Activity) {
            Activity mainActivity = (Activity)context;
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    optionFragment.updateInfo(name, surname, email, matr);
                    popupWindow.dismiss();
                }
            });
        }
    }
    void updateStatistic(String name, String surname, String email, String matr, String button){
        if (context instanceof Activity) {
            Activity mainActivity = (Activity)context;
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    optionFragment.updateInfo(name, surname, email, matr, button);
                    popupWindow.dismiss();
                }
            });
        }
    }
}

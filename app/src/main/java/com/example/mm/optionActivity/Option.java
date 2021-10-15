package com.example.mm.optionActivity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.mm.R;
import com.example.mm.exerciseActivity.Exercise;
import com.example.mm.optionActivity.localDatabaseInteraction.GetCourse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import localDatabase.Tables.Course;

public class Option extends AppCompatActivity implements View.OnClickListener {
    // initialize variables
    TextView textView;
    TextView txtNumberSelect;
    boolean[] selectedLanguage;
    ArrayList<Integer> subList = new ArrayList<>();

            // {"Informatica", "OOP", "Basi di Dati", "Geometria", "Diritto del Lavoro", "D U M M Y"}; //DUMMY
    List<Course> courseName;

    ArrayList<String> list;
    ArrayList<Course> courses;
    ArrayList<Course> coursesToSend;

    RadioButton rdb1;
    RadioButton rdb2;
    RadioButton rdb3;

    TextView textView11; //Debug Purpose.

    Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        list = new ArrayList<>();
        courses = new ArrayList<>();
        coursesToSend = new ArrayList<>();

        // assign variable
        btnNext = findViewById(R.id.btnNext);
        btnNext.setOnClickListener(this);

        textView = findViewById(R.id.textView);
        textView.setOnClickListener(this);

        txtNumberSelect = findViewById(R.id.txtNumberSelect);

        rdb1 = findViewById(R.id.rdb1);
        rdb2 = findViewById(R.id.rdb2);
        rdb3 = findViewById(R.id.rdb3);

        textView11 = (TextView) findViewById(R.id.textView11); //Debug Purpose.
        // initialize selected language array //DUMMY

        Thread thread = new Thread(new GetCourse(getApplicationContext(), this));
        thread.start();
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == this.btnNext.getId()) {
            Intent intent = new Intent(this, Exercise.class);
            Bundle b = new Bundle();
            int diff = 1; //default difficulty, 1 easy, 2 medium, 3 hard

            if(rdb1.isChecked())
                diff = 1;
            if(rdb2.isChecked())
                diff = 2;
            if(rdb3.isChecked())
                diff = 3;

            b.putString("lista", (String) this.textView.getText());
            b.putInt("difficulty", diff);
            b.putInt("questions", Integer.parseInt(String.valueOf(txtNumberSelect.getText())));

            intent.putExtras(b);
            startActivity(intent);
        }
        if(v.getId() == this.textView.getId())
        {
            // Initialize alert dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            // set title
            builder.setTitle("Select Subject");

            // set dialog non cancelable
            builder.setCancelable(false);

            String[] stringArray = list.toArray(new String[0]);
            selectedLanguage = new boolean[list.size()];

            builder.setMultiChoiceItems(stringArray, selectedLanguage, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                    // check condition
                    if (b) {
                        // when checkbox selected
                        // Add position  in lang list
                        subList.add(i);
                        // Sort array list
                        Collections.sort(subList);
                    } else {
                        // when checkbox unselected
                        // Remove position from langList
                        subList.remove(i);
                    }
                }
            });

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Initialize string builder
                    StringBuilder stringBuilder = new StringBuilder();
                    // use for loop
                    for (int j = 0; j < subList.size(); j++) {
                        // concat array value
                        stringBuilder.append(courses.get(subList.get(j)).getName());
                        // check condition
                        if (j != subList.size() - 1) {
                            // When j value  not equal
                            // to lang list size - 1
                            // add comma
                            stringBuilder.append(", ");
                        }
                    }
                    // set text on textView
                    textView.setText(stringBuilder.toString());
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // dismiss dialog
                    dialogInterface.dismiss();
                }
            });

            builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // use for loop
                    for (int j = 0; j < selectedLanguage.length; j++) {
                        // remove all selection
                        selectedLanguage[j] = false;
                        // clear language list
                        subList.clear();
                        // clear text view value
                        textView.setText("");
                    }
                }
            });
            // show dialog
            builder.show();
        }


    }


    public void updateDropDown(List<Course> courseName, boolean chkErr) {
        //chkErr = boolean passato per verifica errore
        if(chkErr)
            this.textView.setText("errore lettura lista");
        else
        {
            this.textView.setText("va bene, cancella questo messaggio");
            //lavori normalmente
            //this.langArray = courseName.stream().toArray();

            if((courseName != null) && (!courseName.isEmpty())) {
                courses.addAll(courseName);

                /* --- Start Debug Purpose Code --- */

                StringBuilder stringBuilder = new StringBuilder();
                for(Course i : courseName){
                    stringBuilder.append("\t" + i.getName() + "\n");
                }
                textView11.setText("ChkErr = " + (chkErr ? "True" : "False") + "\nCourse Names =\n" + stringBuilder.toString());

                /* --- End Debug Purpose Code --- */

                for (Course i : courseName) {
                    list.add(i.getName());
                }
            }
            else{
                if(chkErr){
                    textView11.setText("True");
                }
                else{
                    textView11.setText("False");
                }
            }
        }
    }
}
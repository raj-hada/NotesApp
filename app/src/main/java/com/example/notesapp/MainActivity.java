package com.example.notesapp;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    Button btnCreateNote;
    FloatingActionButton fabAdd;
    RecyclerView recyclerNotes;
    DatabaseHelper databaseHelper;
    LinearLayout llrow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initVar();
        showNotes();



        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.add_notes);

                EditText edtTitle,edtContent;
                Button btnSave;

                edtTitle = dialog.findViewById(R.id.edtTitle);
                edtContent = dialog.findViewById(R.id.edtContent);
                btnSave = dialog.findViewById(R.id.btnSave);
                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String title = edtTitle.getText().toString();
                        String content = edtContent.getText().toString();
                        String time = new SimpleDateFormat("HH.mm.ss  dd.MM.yyyy").format(new Date());

                        if(!content.equals("")){

                            databaseHelper.noteDao().addNotes(new Note(title,content,time));
                            showNotes();
                            dialog.dismiss();

                        }else{
                            Toast.makeText(MainActivity.this,"Please Enter Content",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.show();

            }
        });
        btnCreateNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabAdd.performClick();         // same click as fabAdd
            }
        });


    }

    void showNotes() {
        ArrayList<Note> arrNotes =(ArrayList<Note>) databaseHelper.noteDao().getNotes();
        if(arrNotes.size() >0){
            recyclerNotes.setVisibility(View.VISIBLE);
            llrow.setVisibility(View.GONE);
            recyclerNotes.setAdapter(new RecyclerNotesAdapter(this,arrNotes,databaseHelper));
        }else{
            llrow.setVisibility(View.VISIBLE);
            recyclerNotes.setVisibility(View.GONE);
        }
    }

    private void initVar() {

        btnCreateNote = findViewById(R.id.btnCreateNote);
        fabAdd = findViewById(R.id.fabAdd);
        recyclerNotes = findViewById(R.id.recycleNotes);

        databaseHelper = DatabaseHelper.getInstance(this);
        llrow = findViewById(R.id.llrow);
        recyclerNotes.setLayoutManager(new LinearLayoutManager(this));

    }
}
package com.example.notesapp;

import androidx.room.*;

import java.util.List;

@Dao
public interface NoteDao {

    @Query("select * from note")
    List<Note> getNotes();

    @Insert
    void addNotes(Note note);

    @Delete
    void deleteNotes(Note note);
    @Update
    void updateNotes(Note note);

}

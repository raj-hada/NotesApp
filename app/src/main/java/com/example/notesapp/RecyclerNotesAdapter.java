package com.example.notesapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerNotesAdapter extends RecyclerView.Adapter<RecyclerNotesAdapter.ViewHolder> {

    Context context;
    ArrayList<Note> arrNotes;
    DatabaseHelper databaseHelper;

    public RecyclerNotesAdapter(Context context, ArrayList<Note> arrNotes,DatabaseHelper databaseHelper) {
        this.context = context;
        this.arrNotes = arrNotes;
        this.databaseHelper = databaseHelper;
    }

    @NonNull
    @Override
    public RecyclerNotesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.note_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerNotesAdapter.ViewHolder holder, int position) {
        holder.txtTitle.setText(arrNotes.get(position).getTitle());
        holder.txtContent.setText(arrNotes.get(position).getContent());
        holder.llrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateItem(position);
            }
        });
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteItem(position);
            }
        });
        holder.llrow.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {


                deleteItem(position);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrNotes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle , txtContent;
        RelativeLayout llrow;
        ImageView imgDelete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtContent = itemView.findViewById(R.id.txtContent);
            llrow = itemView.findViewById(R.id.llrow);
            imgDelete = itemView.findViewById(R.id.imgDelete);

        }
    }
    public void deleteItem(int pos){
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("Delete")
                .setMessage("Are you sure want to delete?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        databaseHelper.noteDao().deleteNotes(new Note(arrNotes.get(pos).getId(),arrNotes.get(pos).getTitle(),arrNotes.get(pos).getContent()));
                        ((MainActivity)context).showNotes();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
    }
    public void updateItem(int pos) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.add_notes);

        TextView txtTitle = dialog.findViewById(R.id.addtitle);
        txtTitle.setText("Update Notes");

        EditText edtTitle, edtContent;
        edtTitle = dialog.findViewById(R.id.edtTitle);
        edtContent = dialog.findViewById(R.id.edtContent);

        Button btnSave = dialog.findViewById(R.id.btnSave);

        String title = arrNotes.get(pos).getTitle().toString();
        String content = arrNotes.get(pos).getContent().toString();

        edtTitle.setText(title);
        edtContent.setText(content);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newTitle = edtTitle.getText().toString();
                String newContent = edtContent.getText().toString();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                    if (!newContent.trim().isEmpty()) { // Validate content
                        try {
                            // Update note in the database
                            Note noteToUpdate = arrNotes.get(pos);
                            noteToUpdate.setTitle(newTitle);
                            noteToUpdate.setContent(newContent);
                            databaseHelper.noteDao().updateNotes(noteToUpdate);

                            // Notify adapter about the change
                            arrNotes.set(pos, noteToUpdate); // Update the list item
                            notifyItemChanged(pos);

                            // Dismiss the dialog after successful update
                            dialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace(); // Handle database operation exceptions
                            Toast.makeText(context, "Failed to update note", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, "Please Enter Content", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        dialog.show();
    }


}

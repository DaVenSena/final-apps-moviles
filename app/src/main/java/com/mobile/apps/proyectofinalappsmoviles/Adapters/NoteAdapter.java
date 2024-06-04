package com.mobile.apps.proyectofinalappsmoviles.Adapters;

import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobile.apps.proyectofinalappsmoviles.Classes.Note;
import com.mobile.apps.proyectofinalappsmoviles.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {
    final private ArrayList<Note> data;

    public NoteAdapter(ArrayList<Note> data) {
        this.data = data;
    }

    public void addNote(Note noteObject) {
        data.add(noteObject);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NoteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notes, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteAdapter.ViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_noteTitle,
                txt_noteDate,
                txt_noteDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_noteDate = itemView.findViewById(R.id.txt_noteDate);
            txt_noteDescription = itemView.findViewById(R.id.txt_noteDescription);
            txt_noteTitle = itemView.findViewById(R.id.txt_noteTitleShow);
        }

        public void bind(Note noteObject) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
                txt_noteTitle.setText(noteObject.getTitle());
                txt_noteDate.setText(sdf.format(noteObject.getCreatedAt()));
                txt_noteDescription.setText(noteObject.getDesc());
            } catch (Exception e) {
                Toast.makeText(itemView.getContext(), "Error: " + e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}

package com.mobile.apps.proyectofinalappsmoviles.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobile.apps.proyectofinalappsmoviles.Classes.ListObject;
import com.mobile.apps.proyectofinalappsmoviles.Classes.Note;
import com.mobile.apps.proyectofinalappsmoviles.R;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {
    private List<Note> data;

    @NonNull
    @Override
    public NoteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_notes, parent, false);
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_noteTitle, txt_noteDate, txt_noteDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_noteTitle = itemView.findViewById(R.id.txt_noteTitle);
            txt_noteDate = itemView.findViewById(R.id.txt_noteDate);
            txt_noteDescription = itemView.findViewById(R.id.txt_noteDescription);
        }

        public void bind(Note noteObject) {
            txt_noteTitle.setText(noteObject.getTitle());
            txt_noteDate.setText(noteObject.getCreatedAt().toString());
            txt_noteDescription.setText(noteObject.getDesc());
        }
    }
}

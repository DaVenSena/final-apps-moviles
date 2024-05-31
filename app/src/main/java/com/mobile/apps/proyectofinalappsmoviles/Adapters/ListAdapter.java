package com.mobile.apps.proyectofinalappsmoviles.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobile.apps.proyectofinalappsmoviles.Classes.ListObject;
import com.mobile.apps.proyectofinalappsmoviles.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private List<ListObject> data;

    public ListAdapter(List<ListObject> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapter.ViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_listTitle, txt_quantity;
        ImageView img_list;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_listTitle = itemView.findViewById(R.id.txt_listTitle);
            txt_quantity = itemView.findViewById(R.id.txt_quantity);
            img_list = itemView.findViewById(R.id.img_list);
        }

        public void bind(ListObject listObject) {
            txt_listTitle.setText(listObject.getName());
            txt_quantity.setText(String.valueOf(listObject.getQuantity()));
            Picasso.get().load(listObject.getImageURL()).into(img_list);
        }
    }
}

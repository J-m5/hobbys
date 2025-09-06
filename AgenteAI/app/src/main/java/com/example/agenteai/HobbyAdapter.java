package com.example.agenteai;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class HobbyAdapter extends RecyclerView.Adapter<HobbyAdapter.HobbyViewHolder> {

    private List<Hobby> hobbies;
    private Context context;
    private OnHobbyClickListener onHobbyClickListener;

    public interface OnHobbyClickListener {
        void onEditClick(Hobby hobby);
        void onDeleteClick(Hobby hobby);
    }

    public HobbyAdapter(Context context, List<Hobby> hobbies) {
        this.context = context;
        this.hobbies = hobbies;
    }

    public void setOnHobbyClickListener(OnHobbyClickListener listener) {
        this.onHobbyClickListener = listener;
    }

    @NonNull
    @Override
    public HobbyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_hobby, parent, false);
        return new HobbyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HobbyViewHolder holder, int position) {
        Hobby hobby = hobbies.get(position);

        holder.textNombre.setText(hobby.getNombre());
        holder.textNivel.setText("Nivel: " + hobby.getNivelDificultad());
        holder.textFecha.setText("Registrado: " + hobby.getFechaRegistro());

        holder.btnEdit.setOnClickListener(v -> {
            if (onHobbyClickListener != null) {
                onHobbyClickListener.onEditClick(hobby);
            }
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (onHobbyClickListener != null) {
                onHobbyClickListener.onDeleteClick(hobby);
            }
        });
    }

    @Override
    public int getItemCount() {
        return hobbies.size();
    }

    public void updateHobbies(List<Hobby> newHobbies) {
        this.hobbies = newHobbies;
        notifyDataSetChanged();
    }

    static class HobbyViewHolder extends RecyclerView.ViewHolder {
        TextView textNombre, textNivel, textFecha;
        Button btnEdit, btnDelete;

        public HobbyViewHolder(@NonNull View itemView) {
            super(itemView);
            textNombre = itemView.findViewById(R.id.text_hobby_nombre);
            textNivel = itemView.findViewById(R.id.text_hobby_nivel);
            textFecha = itemView.findViewById(R.id.text_hobby_fecha);
            btnEdit = itemView.findViewById(R.id.btn_edit_hobby);
            btnDelete = itemView.findViewById(R.id.btn_delete_hobby);
        }
    }
}

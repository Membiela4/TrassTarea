package com.example.tareaut02.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tareaut02.R;
import com.example.tareaut02.model.Tarea;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TareaAdapter extends RecyclerView.Adapter<TareaAdapter.TareaViewHolder> {

    private List<Tarea> tareaList;
    private Context context;
    private Tarea tareaSeleccionada;

    public TareaAdapter(Context context, List<Tarea> tareaList) {
        this.context = context;
        this.tareaList = tareaList;
    }

    public void setTareas(List<Tarea> tareas) {
        tareaList = tareas;
        notifyDataSetChanged();
    }

    public Tarea getTarea(){
        return tareaSeleccionada;
    }

    public Integer getPosicion(){return tareaList.indexOf(tareaSeleccionada);}
    @NonNull
    @Override
    public TareaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new TareaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TareaViewHolder holder, int position) {
        if (tareaList != null && position < tareaList.size()) {
            Tarea tarea = tareaList.get(position);

            // Configurar los elementos de la vista con los datos de la tarea
            if (tarea != null) {
                holder.titleTextView.setText(tarea.getTitulo());
                holder.progressBar.setProgress(tarea.getProgreso());
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                holder.startDateTextView.setText(tarea.getFechaInicio());
                holder.descriptionTextView.setText(tarea.getDescripcion());
            }

            int diasRestantes = tarea.diasRestantes(tarea.getFechaFinal());
            if (diasRestantes < 0) {
                holder.remainingDays.setTextColor(ContextCompat.getColor(context, com.google.android.material.R.color.design_error));
                holder.remainingDays.setText(String.valueOf(diasRestantes));
            } else {
                holder.remainingDays.setTextColor(ContextCompat.getColor(context, android.R.color.black));
                holder.remainingDays.setText(String.valueOf(diasRestantes));
            }

            if (tarea.getProgreso() == 100) {
                holder.remainingDays.setText("0");
                holder.titleTextView.setPaintFlags(holder.titleTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                holder.titleTextView.setPaintFlags(holder.titleTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            }

            if (tarea.isPrioritaria()) {
                holder.titleTextView.setTypeface(null, Typeface.BOLD);
                holder.prioritariaImageView.setImageResource(R.drawable.star_filled);
            } else {
                holder.titleTextView.setTypeface(null, Typeface.NORMAL);
                holder.prioritariaImageView.setImageResource(R.drawable.star_unfilled);
            }

            // Verificar si el método onCreateContextMenu es llamado y la tarea seleccionada
            holder.itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                @Override
                public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                    Log.d("TareaAdapter", "onCreateContextMenu called for position: " + holder.getAdapterPosition());
                    MenuInflater inflater = new MenuInflater(context);
                    inflater.inflate(R.menu.menu_contextual, menu);
                    tareaSeleccionada = tareaList.get(holder.getAdapterPosition());
                    Log.d("TareaAdapter", "Selected task: " + tareaSeleccionada.getTitulo());
                }
            });
        }
    }




    @Override
    public int getItemCount() {
        return tareaList != null ? tareaList.size() : 0;
    }

    public class TareaViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView descriptionTextView;
        ProgressBar progressBar;
        TextView startDateTextView;
        ImageView prioritariaImageView;
        TextView remainingDays;

        public TareaViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            progressBar = itemView.findViewById(R.id.progressBar);
            startDateTextView = itemView.findViewById(R.id.startDateText);
            prioritariaImageView = itemView.findViewById(R.id.prioritariaImageView);
            remainingDays = itemView.findViewById(R.id.remainingDays);
        }
    }
}

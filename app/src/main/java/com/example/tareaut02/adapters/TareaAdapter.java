package com.example.tareaut02.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tareaut02.R;
import com.example.tareaut02.model.Tarea;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TareaAdapter extends RecyclerView.Adapter<TareaAdapter.TareaViewHolder> {

    private List<Tarea> tareaList;
    private Context context;

    public TareaAdapter(Context context, List<Tarea> tareaList) {
        this.context = context;
        this.tareaList = tareaList;
    }

    @Override
    public TareaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new TareaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TareaViewHolder holder, int position) {
        Tarea tarea = tareaList.get(position);

        // Configurar los elementos de la vista con los datos de la tarea
        holder.titleTextView.setText(tarea.getTitulo());
        holder.descriptionTextView.setText(tarea.getDescripcion());
        holder.progressTextView.setText("Progreso: " + tarea.getProgreso() + "%");

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        holder.startDateTextView.setText("Fecha Inicio: " + dateFormat.format(tarea.getFechaInicio()));
        holder.endDateTextView.setText("Fecha Final: " + dateFormat.format(tarea.getFechaFinal()));

        if (tarea.isPrioritaria()) {
            holder.prioritariaImageView.setVisibility(View.VISIBLE);
        } else {
            holder.prioritariaImageView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return tareaList.size();
    }

    public class TareaViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView descriptionTextView;
        TextView progressTextView;
        TextView startDateTextView;
        TextView endDateTextView;
        ImageView prioritariaImageView;

        public TareaViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            progressTextView = itemView.findViewById(R.id.progressTextView);
            startDateTextView = itemView.findViewById(R.id.startDateTextView);
            endDateTextView = itemView.findViewById(R.id.endDateTextView);
            prioritariaImageView = itemView.findViewById(R.id.prioritariaImageView);
        }
    }
}

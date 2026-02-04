package com.lemas.asistincialemas.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.lemas.asistincialemas.R;
import com.lemas.asistincialemas.model.AsistenciaResponse;

import java.util.ArrayList;
import java.util.List;

public class AsistenciaAdapter extends RecyclerView.Adapter<AsistenciaAdapter.AsistenciaViewHolder> {

    private final Context context;
    private List<AsistenciaResponse> asistencias;

    public AsistenciaAdapter(Context context) {
        this.context = context;
        this.asistencias = new ArrayList<>();
    }

    public void setAsistencias(List<AsistenciaResponse> asistencias) {
        this.asistencias = asistencias != null ? asistencias : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AsistenciaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_asistencia, parent, false);
        return new AsistenciaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AsistenciaViewHolder holder, int position) {
        AsistenciaResponse asistencia = asistencias.get(position);
        holder.bind(asistencia);
    }

    @Override
    public int getItemCount() {
        return asistencias.size();
    }

    class AsistenciaViewHolder extends RecyclerView.ViewHolder {

        private final View viewEstado;
        private final TextView tvFecha;
        private final TextView tvHora;
        private final TextView tvEstado;

        public AsistenciaViewHolder(@NonNull View itemView) {
            super(itemView);
            viewEstado = itemView.findViewById(R.id.viewEstado);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvHora = itemView.findViewById(R.id.tvHora);
            tvEstado = itemView.findViewById(R.id.tvEstado);
        }

        public void bind(AsistenciaResponse asistencia) {
            tvFecha.setText(formatFecha(asistencia.getFecha()));
            tvHora.setText(formatHora(asistencia.getHoraRegistro()));

            String estado = asistencia.getEstadoAsistencia();
            tvEstado.setText(getEstadoText(estado));

            int color = getEstadoColor(estado);
            tvEstado.setTextColor(color);
            viewEstado.setBackgroundColor(color);
        }

        private String formatFecha(String fecha) {
            if (fecha == null) return "-";
            return fecha;
        }

        private String formatHora(String hora) {
            if (hora == null || hora.isEmpty()) return "Sin registro";
            return hora;
        }

        private String getEstadoText(String estado) {
            if (estado == null) return "-";
            switch (estado) {
                case "PRESENTE":
                    return context.getString(R.string.estado_presente);
                case "ATRASO":
                    return context.getString(R.string.estado_atraso);
                case "FALTA":
                    return context.getString(R.string.estado_falta);
                default:
                    return estado;
            }
        }

        private int getEstadoColor(String estado) {
            if (estado == null) return ContextCompat.getColor(context, R.color.black);
            switch (estado) {
                case "PRESENTE":
                    return ContextCompat.getColor(context, R.color.status_presente);
                case "ATRASO":
                    return ContextCompat.getColor(context, R.color.status_atraso);
                case "FALTA":
                    return ContextCompat.getColor(context, R.color.status_falta);
                default:
                    return ContextCompat.getColor(context, R.color.black);
            }
        }
    }
}

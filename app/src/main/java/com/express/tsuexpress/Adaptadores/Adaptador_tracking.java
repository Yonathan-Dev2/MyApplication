package com.express.tsuexpress.Adaptadores;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.express.tsuexpress.R;

import java.util.ArrayList;

/**
 * Created by Yonathan on 07/11/2019.
 */

public class Adaptador_tracking extends RecyclerView.Adapter<Adaptador_tracking.ViewHolderDatos>{

    ImageView img_tipo_envi_trac;

    ArrayList<String> ListEnvio;
    ArrayList<String> ListTipo;
    ArrayList<String> ListFecha;
    ArrayList<String> ListTime;

    public Adaptador_tracking(ArrayList<String> listEnvio, ArrayList<String> listTipo, ArrayList<String> listFecha, ArrayList<String> listTime) {
        ListEnvio   = listEnvio;
        ListTipo    = listTipo;
        ListFecha   = listFecha;
        ListTime    = listTime;
    }


    @NonNull
    @Override
    public ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_envi,null,false);



        return new ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDatos holder, int position) {
        holder.asignardatos_codienvio(ListEnvio.get(position));
        holder.asignardatos_tipoenvio(ListTipo.get(position));
        holder.asignardatos_fecha(ListFecha.get(position));
        holder.asignardatos_Time(ListTime.get(position));
    }

    @Override
    public int getItemCount() {
        return ListEnvio.size();
    }

    public class ViewHolderDatos extends RecyclerView.ViewHolder {

        TextView list_codi_envi;
        TextView list_tipo_envi;
        TextView list_acti_fech;
        TextView list_acti_time;

        public ViewHolderDatos(@NonNull View itemView) {

            super(itemView);

            list_codi_envi = (TextView) itemView.findViewById(R.id.list_codi_envi);
            list_tipo_envi = (TextView) itemView.findViewById(R.id.list_tipo_envi);
            list_acti_fech = (TextView) itemView.findViewById(R.id.list_acti_fech);
            list_acti_time = (TextView) itemView.findViewById(R.id.list_acti_time);
            img_tipo_envi_trac = (ImageView) itemView.findViewById(R.id.img_tipo_envi_trac);

        }

        public void asignardatos_codienvio(String datos) {
            list_codi_envi.setText(datos);

        }

        public void asignardatos_tipoenvio(String datos) {
            list_tipo_envi.setText(datos);
            Log.i("datos", ""+datos);
            /*if (datos.equalsIgnoreCase("SOBRE")){
                img_tipo_envi_trac.setImageResource(R.drawable.sobre);
            } else{
                img_tipo_envi_trac.setImageResource(R.drawable.paquete);
            }*/
        }

        public void asignardatos_fecha(String datos) {
            list_acti_fech.setText(datos);
        }

        public void asignardatos_Time(String datos) {
            list_acti_time.setText(datos);
        }

    }
}

package com.express.tsuexpress.Fragmentos;


import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.express.tsuexpress.Adaptadores.Adaptador_tracking;
import com.express.tsuexpress.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Fragment_tracking extends Fragment {

    View vista;
    Fragment_tracking contexto;

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    SearchView sv_trac;
    TextView txt_tipo_envi, txt_acti_hora_envi;
    ScrollView sclv_codi_envi, sclv_nume_iden;
    RadioButton rdb_codi_envi, rdb_nume_iden;

    RecyclerView rv_soli_envi;
    ArrayList<String> ListEnvio;
    ArrayList<String> ListTipo;
    ArrayList<String> ListFecha;
    ArrayList<String> ListTime;

    int band = 0;

    ProgressDialog pdp = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        contexto = this;

        vista = inflater.inflate(R.layout.fragment_tracking, container, false);

        sv_trac             = (SearchView)   vista.findViewById(R.id.sv_trac);
        txt_tipo_envi       = (TextView)     vista.findViewById(R.id.txt_tipo_envi);
        txt_acti_hora_envi  = (TextView)     vista.findViewById(R.id.txt_acti_hora_envi);
        sclv_codi_envi      = (ScrollView)   vista.findViewById(R.id.sclv_codi_envi);
        sclv_nume_iden      = (ScrollView)   vista.findViewById(R.id.sclv_nume_iden);
        rv_soli_envi        = (RecyclerView) vista.findViewById(R.id.rv_soli_envi);
        rdb_codi_envi       = (RadioButton)  vista.findViewById(R.id.rdb_codi_envi);
        rdb_nume_iden       = (RadioButton)  vista.findViewById(R.id.rdb_nume_iden);

        rv_soli_envi.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));


        rdb_codi_envi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    band = 1;
                    sclv_nume_iden.setVisibility(View.GONE);
                    sclv_codi_envi.setVisibility(View.GONE);
                    sv_trac.clearFocus();
                }
            }
        });

        rdb_nume_iden.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    band = 2;
                    sclv_nume_iden.setVisibility(View.GONE);
                    sclv_codi_envi.setVisibility(View.GONE);
                    sv_trac.clearFocus();

                }
            }
        });

        sv_trac.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String codi_envi) {
                sclv_nume_iden.setVisibility(View.GONE);
                sclv_codi_envi.setVisibility(View.GONE);

                if (band==1){
                    trac_envi (codi_envi);
                    sclv_nume_iden.setVisibility(View.GONE);

                } else if(band==2) {
                    trac_iden (codi_envi);
                    sclv_codi_envi.setVisibility(View.GONE);

                } else{
                    Toast.makeText(getContext(), "Seleccione una opcion",Toast.LENGTH_SHORT).show();
                }

                sv_trac.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (newText.equalsIgnoreCase("")){
                    sv_trac.clearFocus();
                    sclv_nume_iden.setVisibility(View.GONE);
                    sclv_codi_envi.setVisibility(View.GONE);
                }

                return true;
            }
        });

        sclv_codi_envi.setVisibility(View.GONE);
        sclv_nume_iden.setVisibility(View.GONE);

        request= Volley.newRequestQueue(getContext());

        return vista;
    }

    private void trac_iden(String nume_iden) {


        String url = "https://www.tsuexpress.com/movil/listaenvios.php?nume_iden="+nume_iden;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray json = response.optJSONArray("list_envi");

                        int t = json.length();

                        try {

                            ListEnvio = new ArrayList<String>();
                            ListTipo = new ArrayList<String>();
                            ListFecha = new ArrayList<String>();
                            ListTime = new ArrayList<String>();

                            for (int i=0; i< t;i++){
                                JSONObject jsonObject =  json.getJSONObject(i);

                                ListEnvio.add(jsonObject.getString("codi_envi").trim());
                                ListTipo.add(jsonObject.getString("tipo_envi").trim());
                                ListFecha.add(jsonObject.getString("acti_fech").trim());
                                ListTime.add(jsonObject.getString("acti_time").trim());
                            }


                            Adaptador_tracking adapter = new Adaptador_tracking(ListEnvio, ListTipo, ListFecha, ListTime);
                            rv_soli_envi.setAdapter(adapter);

                            sclv_nume_iden.setVisibility(View.VISIBLE);
                            pdp.dismiss();

                        } catch (JSONException e) {

                            Toast.makeText(getContext(), "* Error el número de identificación no cuenta con solicitudes.", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                            pdp.dismiss();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(),"No se pudo conectar con sel servidor "+error.toString(), Toast.LENGTH_SHORT).show();
                        pdp.dismiss();
                    }
                });
        request.add(jsonObjectRequest);

        pdp = new ProgressDialog(getContext());
        pdp.show();
        pdp.setContentView(R.layout.progressbar_consu);
        pdp.setCancelable(false);
        pdp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }




    private void trac_envi(String codi_envi) {

        String url = "https://www.tsuexpress.com/movil/tracking.php?codi_envi="+codi_envi;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray json = response.optJSONArray("esta_tipo");

                        try {
                            JSONObject jsonObject =  json.getJSONObject(0);
                            String esta_envi = jsonObject.getString("esta_envi");
                            String tipo_envi = jsonObject.getString("tipo_envi");
                            String acti_hora = jsonObject.getString("acti_hora");

                            txt_tipo_envi.setText(tipo_envi);
                            txt_acti_hora_envi.setText(acti_hora);

                            sclv_codi_envi.setVisibility(View.VISIBLE);
                            pdp.dismiss();

                        } catch (JSONException e) {
                            Toast.makeText(getContext(), "* Error el código de envio no existe.", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                            pdp.dismiss();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(),"No se pudo conectar con sel servidor "+error.toString(), Toast.LENGTH_SHORT).show();
                        pdp.dismiss();
                    }
                });
        request.add(jsonObjectRequest);

        pdp = new ProgressDialog(getContext());
        pdp.show();
        pdp.setContentView(R.layout.progressbar_consu);
        pdp.setCancelable(false);
        pdp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }

}

package com.express.tsuexpress.Fragmentos;


import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.express.tsuexpress.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */



public class Fragment_rece_soli extends Fragment {

    View vista;
    Fragment_rece_soli contexto;
    Spinner spn_codi_envi;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    ProgressDialog pdp = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        contexto = this;

        vista = inflater.inflate(R.layout.fragment_rece_soli, container, false);

        spn_codi_envi = (Spinner) vista.findViewById(R.id.spn_codi_envi);

        spn_codi_envi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (!parent.getItemAtPosition(position).toString().equalsIgnoreCase("Seleccione")){
                    Toast.makeText(getContext(),"Seleccionado :"+parent.getItemAtPosition(position).toString(),Toast.LENGTH_SHORT).show();
                    visu_envi(parent.getItemAtPosition(position).toString());
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        request= Volley.newRequestQueue(getContext());

        carg_codi_envi();

        return  vista;
    }

    private void carg_codi_envi() {

        String url = "https://www.tsuexpress.com/movil/codigos.php?";


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray json = response.optJSONArray("codigos");


                        try {

                            ArrayList<String> codigos = new ArrayList<String>();

                            codigos.add("Seleccione");
                            for (int i=0;i<json.length();i++){
                                JSONObject jsonObject =  json.getJSONObject(i);
                                String codi_envi = jsonObject.getString("codi_envi");
                                codigos.add(codi_envi);
                            }

                            ArrayAdapter<CharSequence> adapter = new ArrayAdapter(getContext(),android.R.layout.select_dialog_item, codigos);
                            spn_codi_envi.setAdapter(adapter);

                            pdp.dismiss();

                        } catch (JSONException e) {
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
        pdp.setContentView(R.layout.progressbar);
        pdp.setCancelable(false);
        pdp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }


    private void visu_envi(String codi_envi) {


    }

}

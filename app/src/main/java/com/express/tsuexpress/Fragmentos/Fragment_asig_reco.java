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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
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

import static com.express.tsuexpress.Constants.MY_DEFAULT_TIMEOUT;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_asig_reco extends Fragment {

    View vista;
    Fragment_asig_reco contexto;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    Spinner spn_codi_envi, spn_cola;
    EditText edt_nomb_clie, edt_dire_clie, edt_tipo_envi, edt_dist_clie, edt_zona_clie;
    String  codi_envi = "", codi_usua;
    Button  btn_asig_reco;

    ProgressDialog pdp = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        contexto = this;

        vista = inflater.inflate(R.layout.fragment_asig_reco, container, false);
        spn_codi_envi = (Spinner)  vista.findViewById(R.id.spn_codi_envi);
        spn_cola      = (Spinner)  vista.findViewById(R.id.spn_cola);
        edt_nomb_clie = (EditText) vista.findViewById(R.id.edt_nomb_clie);
        edt_dire_clie = (EditText) vista.findViewById(R.id.edt_dire_clie);
        edt_tipo_envi = (EditText) vista.findViewById(R.id.edt_tipo_envi);
        btn_asig_reco = (Button)   vista.findViewById(R.id.btn_asig_reco);
        edt_dist_clie = (EditText) vista.findViewById(R.id.edt_dist_clie);
        edt_zona_clie = (EditText) vista.findViewById(R.id.edt_zona_clie);

        edt_nomb_clie.setEnabled(false);
        edt_dire_clie.setEnabled(false);
        edt_tipo_envi.setEnabled(false);
        edt_dist_clie.setEnabled(false);
        edt_zona_clie.setEnabled(false);


        spn_codi_envi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (!parent.getItemAtPosition(position).toString().equalsIgnoreCase("Seleccione")){
                    limpiar_datos();
                    codi_envi = parent.getItemAtPosition(position).toString();
                    visu_envi(codi_envi);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_asig_reco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String codi_envi = spn_codi_envi.getSelectedItem().toString();
                String codi_usua = spn_cola.getSelectedItem().toString();
                String tipo_asig = "RECOJO";
                if (!codi_envi.equalsIgnoreCase("Seleccione") && !codi_usua.equalsIgnoreCase("Seleccione")){
                    asig_reco(codi_envi, tipo_asig, codi_usua);
                } else {
                    Toast.makeText(getContext(),"*Error debe selecciona el codigo de envio y el nombre del colaborador.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        request= Volley.newRequestQueue(getContext());

        carg_codi_envi();
        carg_cola();

        return vista;
    }


    private void carg_codi_envi() {

        String url = "https://www.tsuexpress.com/movil/codigosrecojo.php?";

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

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        request.add(jsonObjectRequest);

        pdp = new ProgressDialog(getContext());
        pdp.show();
        pdp.setContentView(R.layout.progressbar_esperar);
        pdp.setCancelable(false);
        pdp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }

    private void visu_envi(String codi_envi) {

        String url = "https://www.tsuexpress.com/movil/recojolist.php?codi_envi="+codi_envi;

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray json = response.optJSONArray("codigos");

                        try {
                            JSONObject jsonObject =  json.getJSONObject(0);

                            String nomb_clie = jsonObject.getString("nomb_clie");
                            String dire_clie = jsonObject.getString("dire_clie");
                            String tipo_envi = jsonObject.getString("tipo_envi");

                            String dist_clie = jsonObject.getString("dist_clie");
                            String zona_clie = jsonObject.getString("zona_clie");

                            edt_nomb_clie.setText(nomb_clie);
                            edt_dire_clie.setText(dire_clie);
                            edt_tipo_envi.setText(tipo_envi);
                            edt_dist_clie.setText(dist_clie);
                            edt_zona_clie.setText(zona_clie);

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

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        request.add(jsonObjectRequest);

        pdp = new ProgressDialog(getContext());
        pdp.show();
        pdp.setContentView(R.layout.progressbar_esperar);
        pdp.setCancelable(false);
        pdp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }


    private void carg_cola() {

        String url = "https://www.tsuexpress.com/movil/colaborador.php?";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray json = response.optJSONArray("operador");

                        try {

                            ArrayList<String> colaboradores = new ArrayList<String>();

                            colaboradores.add("Seleccione");
                            for (int i=0;i<json.length();i++){
                                JSONObject jsonObject =  json.getJSONObject(i);
                                codi_usua = jsonObject.getString("codi_usua");

                                colaboradores.add(codi_usua);
                            }
                            ArrayAdapter<CharSequence> adapter = new ArrayAdapter(getContext(),android.R.layout.select_dialog_item, colaboradores);
                            spn_cola.setAdapter(adapter);

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

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        request.add(jsonObjectRequest);
    }


    private void asig_reco(String codi_envi, String tipo_asig, String codi_usua) {

        String url = "https://www.tsuexpress.com/movil/asignar.php?codi_envi="+codi_envi+"&tipo_asig="+tipo_asig+"&codi_usua="+codi_usua;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray json = response.optJSONArray("asignar");

                        try {

                            JSONObject jsonObject =  json.getJSONObject(0);
                            String codi_envi = jsonObject.getString("codi_envi");

                            carg_codi_envi();
                            limpiar_datos();
                            spn_codi_envi.setSelection(0);

                            pdp.dismiss();
                            Toast.makeText(getContext(),"Se asigno el codigo de envio "+codi_envi, Toast.LENGTH_SHORT).show();

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

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        request.add(jsonObjectRequest);


    }

    private void limpiar_datos() {
        edt_nomb_clie.setText("");
        edt_dire_clie.setText("");
        edt_tipo_envi.setText("");
        edt_dist_clie.setText("");
        edt_zona_clie.setText("");
        spn_cola.setSelection(0);
    }

}

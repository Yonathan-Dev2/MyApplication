package com.express.tsuexpress.Fragmentos;


import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
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

import static com.express.tsuexpress.Constants.MY_DEFAULT_TIMEOUT;


public class Fragment_oper_cola extends Fragment {

    View vista;
    Fragment_oper_cola contexto;

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    Button btn_reco_envi, btn_entr_envi;
    SearchView sv_codi_envi;
    EditText edt_nomb_clie, edt_dire_clie, edt_refe_clie, edt_celu_clie, edt_tipo_envi, edt_form_pago;
    ProgressDialog pdp = null;
    private String codi_envi_maes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        contexto = this;

        vista = inflater.inflate(R.layout.fragment_oper_cola, container, false);
        btn_entr_envi = (Button) vista.findViewById(R.id.btn_entr_envi);
        btn_reco_envi = (Button) vista.findViewById(R.id.btn_reco_envi);
        sv_codi_envi  = (SearchView) vista.findViewById(R.id.sv_codi_envi);
        edt_nomb_clie = (EditText) vista.findViewById(R.id.edt_nomb_clie);
        edt_dire_clie = (EditText) vista.findViewById(R.id.edt_dire_clie);
        edt_refe_clie = (EditText) vista.findViewById(R.id.edt_refe_clie);
        edt_celu_clie = (EditText) vista.findViewById(R.id.edt_celu_clie);
        edt_tipo_envi = (EditText) vista.findViewById(R.id.edt_tipo_envi);
        edt_form_pago = (EditText) vista.findViewById(R.id.edt_form_pago);


        sv_codi_envi.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String codi_envi) {
                operacion_colaborador(codi_envi);
                codi_envi_maes = codi_envi;
                sv_codi_envi.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.equalsIgnoreCase("")){
                    sv_codi_envi.clearFocus();
                }
                limpiar_datos();
                return true;
            }
        });

        btn_reco_envi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrar_recojo();
            }
        });

        btn_entr_envi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrar_entrega();

            }
        });

        btn_entr_envi.setVisibility(View.GONE);
        btn_reco_envi.setVisibility(View.GONE);
        edt_nomb_clie.setEnabled(false);
        edt_dire_clie.setEnabled(false);
        edt_refe_clie.setEnabled(false);
        edt_celu_clie.setEnabled(false);
        edt_tipo_envi.setEnabled(false);
        edt_form_pago.setEnabled(false);

        request= Volley.newRequestQueue(getContext());

        return vista;
    }


    private void operacion_colaborador(final String codi_envi) {

        String url = "https://www.tsuexpress.com/movil/operacionrecojo.php?codi_envi="+codi_envi;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray json = response.optJSONArray("oper_reco");

                        try {
                            JSONObject jsonObject =  json.getJSONObject(0);
                            String nomb_clie      = jsonObject.getString("nomb_clie").trim();
                            String dire_clie      = jsonObject.getString("dire_clie").trim();
                            String refe_clie      = jsonObject.getString("refe_clie").trim();
                            String celu_clie      = jsonObject.getString("celu_clie").trim();
                            String tipo_envi      = jsonObject.getString("tipo_envi").trim();
                            String form_pago      = jsonObject.getString("form_pago").trim();
                            String esta_oper_reco = jsonObject.getString("esta_oper_reco").trim();
                            String esta_oper_entr = jsonObject.getString("esta_oper_entr").trim();
                            String esta_entr      = jsonObject.getString("esta_entr").trim();




                            if (esta_oper_reco.equalsIgnoreCase("null")){
                                btn_entr_envi.setVisibility(View.GONE);
                                btn_reco_envi.setVisibility(View.VISIBLE);
                                edt_nomb_clie.setText(nomb_clie);
                                edt_dire_clie.setText(dire_clie);
                                edt_refe_clie.setText(refe_clie);
                                edt_celu_clie.setText(celu_clie);
                                edt_tipo_envi.setText(tipo_envi);
                                edt_form_pago.setText(form_pago);

                            } else if (esta_entr.equalsIgnoreCase("null") ){
                                btn_reco_envi.setVisibility(View.GONE);
                                btn_entr_envi.setVisibility(View.GONE);
                                Toast.makeText(getContext(), "* Error el envío no fue asignado para su entrega.", Toast.LENGTH_SHORT).show();
                            } else if (esta_entr.equalsIgnoreCase("SI")){
                                btn_reco_envi.setVisibility(View.GONE);
                                btn_entr_envi.setVisibility(View.VISIBLE);
                                edt_nomb_clie.setText(nomb_clie);
                                edt_dire_clie.setText(dire_clie);
                                edt_refe_clie.setText(refe_clie);
                                edt_celu_clie.setText(celu_clie);
                                edt_tipo_envi.setText(tipo_envi);
                                edt_form_pago.setText(form_pago);
                            }

                            pdp.dismiss();

                        } catch (JSONException e) {
                            Toast.makeText(getContext(), "* Error el número de envío es incorrecto.", Toast.LENGTH_SHORT).show();
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


    private void registrar_recojo() {

        String url = "https://www.tsuexpress.com/movil/registrarecojo.php?codi_envi="+codi_envi_maes;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        JSONArray json = response.optJSONArray("mensaje");
                        String mensaje = ""+json.toString().replace("[","").replace("]","").replace("'","");

                        Toast.makeText(getContext(),mensaje, Toast.LENGTH_SHORT).show();

                        limpiar_datos();

                        pdp.dismiss();

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


    private void registrar_entrega() {

        String url = "https://www.tsuexpress.com/movil/registrarentrega.php?codi_envi="+codi_envi_maes;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        JSONArray json = response.optJSONArray("mensaje");
                        String mensaje = ""+json.toString().replace("[","").replace("]","").replace("'","");

                        Toast.makeText(getContext(),mensaje, Toast.LENGTH_SHORT).show();

                        limpiar_datos();

                        pdp.dismiss();

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



    private void limpiar_datos(){
        edt_nomb_clie.setText("");
        edt_dire_clie.setText("");
        edt_refe_clie.setText("");
        edt_celu_clie.setText("");
        edt_form_pago.setText("");
        edt_tipo_envi.setText("");
        btn_reco_envi.setVisibility(View.GONE);
        btn_entr_envi.setVisibility(View.GONE);
    }

}

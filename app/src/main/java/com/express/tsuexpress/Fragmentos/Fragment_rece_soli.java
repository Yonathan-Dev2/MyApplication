package com.express.tsuexpress.Fragmentos;


import android.app.ProgressDialog;
import android.graphics.Bitmap;
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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
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
    EditText edt_nomb_clie, edt_acti_hora, edt_celu_clie, edt_tipo_envi, edt_form_pago, edt_nomb_dest, edt_dire_clie, edt_refe_clie;
    EditText edt_dire_dest, edt_refe_dest;
    ImageView img_ruta_comp;
    Button btn_acep_soli, btn_rech_soli;
    String  codi_envi = "";
    ProgressDialog pdp = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        contexto = this;

        vista = inflater.inflate(R.layout.fragment_rece_soli, container, false);

        spn_codi_envi = (Spinner)   vista.findViewById(R.id.spn_codi_envi);
        edt_nomb_clie = (EditText)  vista.findViewById(R.id.edt_nomb_clie);
        edt_acti_hora = (EditText)  vista.findViewById(R.id.edt_acti_hora);
        edt_celu_clie = (EditText)  vista.findViewById(R.id.edt_celu_clie);
        edt_tipo_envi = (EditText)  vista.findViewById(R.id.edt_tipo_envi);
        edt_form_pago = (EditText)  vista.findViewById(R.id.edt_form_pago);
        edt_nomb_dest = (EditText)  vista.findViewById(R.id.edt_nomb_dest);
        edt_dire_clie = (EditText)  vista.findViewById(R.id.edt_dire_clie);
        edt_refe_clie = (EditText)  vista.findViewById(R.id.edt_refe_clie);
        edt_dire_dest = (EditText)  vista.findViewById(R.id.edt_dire_dest);
        edt_refe_dest = (EditText)  vista.findViewById(R.id.edt_refe_dest);
        img_ruta_comp = (ImageView) vista.findViewById(R.id.img_ruta_comp);
        btn_acep_soli = (Button)    vista.findViewById(R.id.btn_acep_soli);
        btn_rech_soli = (Button)    vista.findViewById(R.id.btn_rech_soli);

        edt_acti_hora.setEnabled(false);
        edt_celu_clie.setEnabled(false);
        edt_form_pago.setEnabled(false);


        img_ruta_comp.setVisibility(View.GONE);

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


        btn_acep_soli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String esta_soli = "ACEPTADO";
                actualizar_recepcion(esta_soli, codi_envi);

            }
        });

        btn_rech_soli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String esta_soli = "RECHAZADO";
                actualizar_recepcion(esta_soli, codi_envi);
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
        pdp.setContentView(R.layout.progressbar_esperar);
        pdp.setCancelable(false);
        pdp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }


    private void visu_envi(String codi_envi) {

        String url = "https://www.tsuexpress.com/movil/recepcion.php?codi_envi="+codi_envi;

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray json = response.optJSONArray("recepcion");

                        int i = json.length();


                        try {
                            JSONObject jsonObject =  json.getJSONObject(0);

                            String nomb_clie = jsonObject.getString("nomb_clie");
                            String acti_hora = jsonObject.getString("acti_hora");
                            String celu_clie = jsonObject.getString("celu_clie");
                            String tipo_envi = jsonObject.getString("tipo_envi");
                            String form_pago = jsonObject.getString("form_pago");
                            String ruta_comp = jsonObject.getString("ruta_comp");
                            String nomb_dest = jsonObject.getString("nomb_dest");

                            String dire_clie = jsonObject.getString("dire_clie");
                            String refe_clie = jsonObject.getString("refe_clie");
                            String dire_dest = jsonObject.getString("dire_dest");
                            String refe_dest = jsonObject.getString("refe_dest");

                            edt_nomb_clie.setText(nomb_clie);
                            edt_acti_hora.setText(acti_hora);
                            edt_celu_clie.setText(celu_clie);
                            edt_tipo_envi.setText(tipo_envi);
                            edt_form_pago.setText(form_pago);
                            edt_nomb_dest.setText(nomb_dest);
                            edt_dire_clie.setText(dire_clie);
                            edt_refe_clie.setText(refe_clie);
                            edt_dire_dest.setText(dire_dest);
                            edt_refe_dest.setText(refe_dest);

                            if(form_pago.equalsIgnoreCase("DEPOSITO")){
                                cargar_imagen(ruta_comp);
                            } else {
                                pdp.dismiss();
                            }

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
        pdp.setContentView(R.layout.progressbar_esperar);
        pdp.setCancelable(false);
        pdp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }

    private void cargar_imagen(String ruta_comp) {

        ruta_comp = ruta_comp.replace(" ", "%20");

        ImageRequest imageRequest = new ImageRequest(ruta_comp, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                pdp.dismiss();
                img_ruta_comp.setVisibility(View.VISIBLE);
                img_ruta_comp.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pdp.dismiss();
                Toast.makeText(getContext(),"*Error al cargar la Imagen"+error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        request.add(imageRequest);

    }


    private void actualizar_recepcion(String esta_soli, String codi_envi) {

        String nomb_clie = edt_nomb_clie.getText().toString().trim();
        String tipo_envi = edt_tipo_envi.getText().toString().trim();
        String nomb_dest = edt_nomb_dest.getText().toString().trim();
        String dire_clie = edt_dire_clie.getText().toString().trim();
        String refe_clie = edt_refe_clie.getText().toString().trim();
        String dire_dest = edt_dire_dest.getText().toString().trim();
        String refe_dest = edt_refe_dest.getText().toString().trim();

        String url = "https://www.tsuexpress.com/movil/actualizarrecepcion.php?codi_envi="+codi_envi+"&esta_soli="+esta_soli+"&nomb_clie="+nomb_clie+"&tipo_envi="+tipo_envi+"&nomb_dest="+nomb_dest+"&dire_clie="+dire_clie+"&refe_clie="+refe_clie+"&dire_dest="+dire_dest+"&refe_dest="+refe_dest;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        JSONArray json = response.optJSONArray("mensaje");
                        String estado = ""+json.toString().replace("[","").replace("]","").replace("'","");

                        pdp.dismiss();
                        carg_codi_envi();
                        limpiar_datos();
                        Toast.makeText(getContext(),"La solicitud fue "+estado, Toast.LENGTH_SHORT).show();

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
        pdp.setContentView(R.layout.progressbar_esperar);
        pdp.setCancelable(false);
        pdp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }


    private void limpiar_datos(){
        edt_nomb_clie.setText("");
        edt_acti_hora.setText("");
        edt_celu_clie.setText("");
        edt_tipo_envi.setText("");
        edt_form_pago.setText("");
        edt_nomb_dest.setText("");
        edt_dire_clie.setText("");
        edt_refe_clie.setText("");
        edt_dire_dest.setText("");
        edt_refe_dest.setText("");
        img_ruta_comp.setVisibility(View.GONE);
    }

}

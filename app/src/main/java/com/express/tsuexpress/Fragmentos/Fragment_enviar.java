package com.express.tsuexpress.Fragmentos;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.express.tsuexpress.R;
import com.express.tsuexpress.cuadro_mensaje;
import com.express.tsuexpress.toma_foto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

import static com.express.tsuexpress.Constants.MY_DEFAULT_TIMEOUT;


public class Fragment_enviar extends Fragment {
    View vista;
    Fragment_enviar contexto;

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    RadioButton rdb_tipo_paqu, rdb_tipo_sobr, rdb_form_efec, rdb_form_depo;
    TextView    txt_tota_pago, txt_mens_pago;
    EditText    edt_nume_iden, edt_nomb_clie, edt_celu_clie, edt_dire_clie, edt_refe_clie, edt_iden_dest, edt_nomb_dest, edt_celu_dest, edt_dire_dest, edt_refe_dest;
    Button      btn_envi;
    TextInputLayout  til_nume_iden, til_nomb_clie, til_celu_clie, til_dire_clie, til_refe_clie, til_iden_dest, til_nomb_dest, til_celu_dest, til_dire_dest, til_refe_dest;
    ImageView img_depo;


    String tipo_envi="", form_pago="";
    double tota_pago=0;

    ProgressDialog pdp = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        contexto = this;

        vista = inflater.inflate(R.layout.fragment_enviar, container, false);

        rdb_tipo_paqu = (RadioButton) vista.findViewById(R.id.rdb_tipo_paqu);
        rdb_tipo_sobr = (RadioButton) vista.findViewById(R.id.rdb_tipo_sobr);
        rdb_form_efec = (RadioButton) vista.findViewById(R.id.rdb_form_efec);
        rdb_form_depo = (RadioButton) vista.findViewById(R.id.rdb_form_depo);
        txt_tota_pago = (TextView) vista.findViewById(R.id.txt_tota_paga);
        txt_mens_pago = (TextView) vista.findViewById(R.id.txt_mens_pago);
        edt_nume_iden = (EditText) vista.findViewById(R.id.edt_nume_iden);
        edt_nomb_clie = (EditText) vista.findViewById(R.id.edt_nomb_clie);
        edt_celu_clie = (EditText) vista.findViewById(R.id.edt_celu_clie);
        edt_dire_clie = (EditText) vista.findViewById(R.id.edt_dire_clie);
        edt_refe_clie = (EditText) vista.findViewById(R.id.edt_refe_clie);
        edt_iden_dest = (EditText) vista.findViewById(R.id.edt_iden_dest);
        edt_nomb_dest = (EditText) vista.findViewById(R.id.edt_nomb_dest);
        edt_celu_dest = (EditText) vista.findViewById(R.id.edt_celu_dest);
        edt_dire_dest = (EditText) vista.findViewById(R.id.edt_dire_dest);
        edt_refe_dest = (EditText) vista.findViewById(R.id.edt_refe_dest);


        til_nume_iden = (TextInputLayout)vista.findViewById(R.id.til_nume_iden);
        til_nomb_clie = (TextInputLayout)vista.findViewById(R.id.til_nomb_clie);

        til_celu_clie = (TextInputLayout)vista.findViewById(R.id.til_celu_clie);
        til_dire_clie = (TextInputLayout)vista.findViewById(R.id.til_dire_clie);
        til_refe_clie = (TextInputLayout)vista.findViewById(R.id.til_refe_clie);
        til_iden_dest = (TextInputLayout)vista.findViewById(R.id.til_iden_dest);
        til_nomb_dest = (TextInputLayout)vista.findViewById(R.id.til_nomb_dest);
        til_celu_dest = (TextInputLayout)vista.findViewById(R.id.til_celu_dest);
        til_dire_dest = (TextInputLayout)vista.findViewById(R.id.til_dire_dest);
        til_refe_dest = (TextInputLayout)vista.findViewById(R.id.til_refe_dest);

        img_depo      = (ImageView) vista.findViewById(R.id.img_depo);


        btn_envi      = (Button) vista.findViewById(R.id.btn_envi);


        request= Volley.newRequestQueue(getContext());


        img_depo.setVisibility(View.GONE);
        txt_tota_pago.setVisibility(View.GONE);
        txt_mens_pago.setVisibility(View.GONE);

        rdb_tipo_sobr.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    txt_tota_pago.setVisibility(View.VISIBLE);
                    txt_tota_pago.setText("Total a pagar S/. 8.00");
                    tipo_envi = "SOBRE";
                    tota_pago = 8;
                }
            }
        });

        rdb_tipo_paqu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    txt_tota_pago.setVisibility(View.VISIBLE);
                    txt_tota_pago.setText("Total a pagar S/. 12.00");
                    tipo_envi = "PAQUETE";
                    tota_pago = 12;
                }
            }
        });

        rdb_form_depo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    txt_mens_pago.setVisibility(View.VISIBLE);
                    txt_mens_pago.setText("Debe tomar una fotografia del comprobante para confirma el envío (clic en la imagen).");
                    form_pago = "DEPOSITO";

                    img_depo.setVisibility(View.VISIBLE);
                    btn_envi.setVisibility(View.GONE);
                }
            }
        });

        rdb_form_efec.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    txt_mens_pago.setVisibility(View.VISIBLE);
                    txt_mens_pago.setText("Entregara al transportista el monto a pagar.");
                    form_pago = "EFECTIVO";
                    btn_envi.setVisibility(View.VISIBLE);
                    img_depo.setVisibility(View.GONE);
                }
            }
        });


        btn_envi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tipo_envi.equalsIgnoreCase("")){
                    Toast.makeText(getContext(), "Seleccine el tipo de envío", Toast.LENGTH_SHORT).show();
                } else if (form_pago==""){
                    Toast.makeText(getContext(), "Seleccine la forma de pago", Toast.LENGTH_SHORT).show();

                } else  if (validarNume_iden(edt_nume_iden.getText().toString()) && validarNomb_clie(edt_nomb_clie.getText().toString()) &&
                            validarCelu_clie(edt_celu_clie.getText().toString()) && validarDire_clie(edt_dire_clie.getText().toString()) &&
                            validarRefe_clie(edt_refe_clie.getText().toString()) && validarIden_dest(edt_iden_dest.getText().toString()) &&
                            validarNomb_dest(edt_nomb_dest.getText().toString()) && validarCelu_dest(edt_celu_dest.getText().toString()) &&
                            validarDire_dest(edt_dire_dest.getText().toString()) && validarRefe_dest(edt_refe_dest.getText().toString()) ){
                            registrar_envio();
                }
            }
        });


        img_depo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tipo_envi.equalsIgnoreCase("")){
                    Toast.makeText(getContext(), "Seleccine el tipo de envío", Toast.LENGTH_SHORT).show();
                } else if (form_pago==""){
                    Toast.makeText(getContext(), "Seleccine la forma de pago", Toast.LENGTH_SHORT).show();

                } else  if (validarNume_iden(edt_nume_iden.getText().toString()) && validarNomb_clie(edt_nomb_clie.getText().toString()) &&
                            validarCelu_clie(edt_celu_clie.getText().toString()) && validarDire_clie(edt_dire_clie.getText().toString()) &&
                            validarRefe_clie(edt_refe_clie.getText().toString()) && validarIden_dest(edt_iden_dest.getText().toString()) &&
                            validarNomb_dest(edt_nomb_dest.getText().toString()) && validarCelu_dest(edt_celu_dest.getText().toString()) &&
                            validarDire_dest(edt_dire_dest.getText().toString()) && validarRefe_dest(edt_refe_dest.getText().toString()) ){

                            Intent t = new Intent(getContext(), toma_foto.class);

                            t.putExtra("nume_iden",edt_nume_iden.getText().toString());
                            t.putExtra("nomb_clie",edt_nomb_clie.getText().toString());
                            t.putExtra("celu_clie",edt_celu_clie.getText().toString());
                            t.putExtra("dire_clie",edt_dire_clie.getText().toString());
                            t.putExtra("refe_clie",edt_refe_clie.getText().toString());
                            t.putExtra("tipo_envi",tipo_envi);
                            t.putExtra("tota_pago",String.valueOf(tota_pago));
                            t.putExtra("iden_dest",edt_iden_dest.getText().toString());
                            t.putExtra("nomb_dest",edt_nomb_dest.getText().toString());
                            t.putExtra("celu_dest",edt_celu_dest.getText().toString());
                            t.putExtra("dire_dest",edt_dire_dest.getText().toString());
                            t.putExtra("refe_dest",edt_refe_dest.getText().toString());
                            t.putExtra("form_pago",form_pago);

                            startActivity(t);
                }

            }
        });


        return vista;

    }


    private void registrar_envio() {
        String nume_iden = edt_nume_iden.getText().toString();
        String nomb_clie = edt_nomb_clie.getText().toString();
        String celu_clie = edt_celu_clie.getText().toString();
        String dire_clie = edt_dire_clie.getText().toString();
        String refe_clie = edt_refe_clie.getText().toString();
        String iden_dest = edt_iden_dest.getText().toString();
        String nomb_dest = edt_nomb_dest.getText().toString();
        String celu_dest = edt_celu_dest.getText().toString();
        String dire_dest = edt_dire_dest.getText().toString();
        String refe_dest = edt_refe_dest.getText().toString();

        String url = "https://www.tsuexpress.com/movil/envio.php?nume_iden="+nume_iden+"&nomb_clie="+nomb_clie+"&celu_clie="+celu_clie
                +"&dire_clie="+dire_clie+"&refe_clie="+refe_clie+"&iden_dest="+iden_dest+"&nomb_dest="+nomb_dest+"&celu_dest="+celu_dest
                +"&dire_dest="+dire_dest+"&refe_dest="+refe_dest+"&tipo_envi="+tipo_envi+"&tota_pago="+tota_pago+"&iden_dest="+iden_dest
                +"&form_pago="+form_pago;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray json = response.optJSONArray("mensaje");


                        try {
                            JSONObject jsonObject =  json.getJSONObject(0);
                            String conf = jsonObject.getString("id");


                            new cuadro_mensaje(getContext(), "Solicitud exitosa el codigo de envio es : "+conf);

                            //Toast.makeText(getContext(), "Envio exitoso el codigo de envio es : "+conf, Toast.LENGTH_SHORT).show();

                            limpiar_registro();
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
        pdp.setContentView(R.layout.progressbar);
        pdp.setCancelable(false);
        pdp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }


    private void limpiar_registro(){
        txt_mens_pago.setText("");
        txt_tota_pago.setText("");
        edt_nomb_clie.setText("");
        edt_nume_iden.setText("");
        edt_celu_clie.setText("");
        edt_dire_clie.setText("");
        edt_refe_clie.setText("");
        edt_iden_dest.setText("");
        edt_nomb_dest.setText("");
        edt_celu_dest.setText("");
        edt_dire_dest.setText("");
        edt_refe_dest.setText("");

        rdb_form_efec.setChecked(false);
        rdb_form_depo.setChecked(false);
        rdb_tipo_sobr.setChecked(false);
        rdb_tipo_paqu.setChecked(false);

        tipo_envi="";
        form_pago="";
        tota_pago=0;
        img_depo.setVisibility(View.GONE);
        txt_tota_pago.setVisibility(View.GONE);
        txt_mens_pago.setVisibility(View.GONE);

        //edt_nume_iden.requestFocus();

    }



    private boolean validarNume_iden(String nume_iden) {
        boolean toke;

        //Patterns.PHONE.matcher(nume_iden).matches()


        if ( nume_iden.length()==8 || nume_iden.length() == 11)  {
            til_nume_iden.setErrorEnabled(false);
            toke = true;
        } else {
            til_nume_iden.setError("Identidad inválido");
            toke = false;
        }

        return toke;
    }

    private boolean validarNomb_clie(String nomb_clie){
        boolean toke;

        Pattern patron = Pattern.compile("^[a-zA-Z ]+$");
        if (!patron.matcher(nomb_clie).matches() || nomb_clie.length() < 4) {
            til_nomb_clie.setError("Nombre inválido");
            toke = false;
        } else {
            til_nomb_clie.setErrorEnabled(false);
            toke = true;
        }

        return toke;
    }

    private boolean validarCelu_clie(String celu_clie) {
        boolean toke;

        if (!Patterns.PHONE.matcher(celu_clie).matches() || celu_clie.length() != 9) {

            til_celu_clie.setError("Celular inválido");
            toke = false;
        } else {
            til_celu_clie.setErrorEnabled(false);
            toke = true;
        }

        return toke;
    }

    private boolean validarDire_clie(String dire_clie){
        boolean toke;

        if (dire_clie.length() < 10) {
            til_dire_clie.setError("Dirección corta");
            toke = false;
        } else {
            til_dire_clie.setErrorEnabled(false);
            toke = true;
        }

        return toke;
    }

    private boolean validarRefe_clie(String refe_clie){
        boolean toke;

        if (refe_clie.length() < 10) {
            til_refe_clie.setError("Refencia corta");
            toke = false;
        } else {
            til_refe_clie.setErrorEnabled(false);
            toke = true;
        }

        return toke;
    }



    private boolean validarIden_dest(String iden_dest) {
        boolean toke;
        //!Patterns.PHONE.matcher(iden_dest).matches() ||
        if (iden_dest.length() == 11 || iden_dest.length() == 8) {

            til_iden_dest.setErrorEnabled(false);
            toke = true;

        } else {
            til_iden_dest.setError("Identidad inválido");
            toke = false;

        }

        return toke;
    }

    private boolean validarNomb_dest(String nomb_dest){
        boolean toke;

        Pattern patron = Pattern.compile("^[a-zA-Z ]+$");
        if (!patron.matcher(nomb_dest).matches() || nomb_dest.length() < 4) {
            til_nomb_dest.setError("Nombre inválido");
            toke = false;
        } else {
            til_nomb_dest.setErrorEnabled(false);
            toke = true;
        }

        return toke;
    }

    private boolean validarCelu_dest(String celu_dest) {
        boolean toke;

        if (!Patterns.PHONE.matcher(celu_dest).matches() || celu_dest.length() != 9) {

            til_celu_dest.setError("Celular inválido");
            toke = false;
        } else {
            til_celu_dest.setErrorEnabled(false);
            toke = true;
        }

        return toke;
    }

    private boolean validarDire_dest(String dire_dest){
        boolean toke;

        if (dire_dest.length() < 10) {
            til_dire_dest.setError("Dirección corta");
            toke = false;
        } else {
            til_dire_dest.setErrorEnabled(false);
            toke = true;
        }

        return toke;
    }

    private boolean validarRefe_dest(String refe_dest){
        boolean toke;

        if (refe_dest.length() < 10) {
            til_refe_dest.setError("Refencia corta");
            toke = false;
        } else {
            til_refe_dest.setErrorEnabled(false);
            toke = true;
        }

        return toke;
    }



    public void onResume() {
        super.onResume();
        limpiar_registro();
    }

}

package com.express.tsuexpress.Fragmentos;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.express.tsuexpress.BuildConfig;
import com.express.tsuexpress.NavigationExpress;
import com.express.tsuexpress.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

import static android.content.Context.MODE_PRIVATE;


public class Fragment_ingresar extends Fragment {

    View vista;
    Fragment_ingresar contexto;

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    TextView txt_vers_name;
    EditText edt_codi_usua, edt_clav_usua;
    Button btn_inic_sesi;
    CheckBox chb_reco_usua;
    TextInputLayout til_codi_usua, til_clav_usua;
    String iden_usua,nomb_usua, apel_usua, esta_usua;

    private SharedPreferences preferences;

    ProgressDialog pdp = null;
    String[] modulo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        contexto = this;



        vista = inflater.inflate(R.layout.fragment_ingresar, container, false);

        txt_vers_name = (TextView) vista.findViewById(R.id.txt_vers_name);
        edt_codi_usua = (EditText) vista.findViewById(R.id.edt_codi_usua);
        edt_clav_usua = (EditText) vista.findViewById(R.id.edt_clav_usua);
        btn_inic_sesi = (Button) vista.findViewById(R.id.btn_inic_sesi);
        chb_reco_usua = (CheckBox) vista.findViewById(R.id.chb_reco_usua);
        til_codi_usua = (TextInputLayout) vista.findViewById(R.id.til_codi_usua);
        til_clav_usua = (TextInputLayout) vista.findViewById(R.id.til_clav_usua);

        txt_vers_name.setText("Versión : "+getVersionName());

        btn_inic_sesi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarCodi_usua(edt_codi_usua.getText().toString()) && validarUsua_clav(edt_clav_usua.getText().toString())){
                    ingr_navi();
                }
            }
        });

        cargarpreferencias();
        request= Volley.newRequestQueue(getContext());
        return  vista;

    }


    private void ingr_navi() {

        String url = "https://www.tsuexpress.com/movil/login.php?codi_usua="+edt_codi_usua.getText().toString()+"&clav_usua="+edt_clav_usua.getText().toString();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            JSONArray json = response.optJSONArray("modulo");
                            int i = json.length();

                            modulo = new String[i];

                            do {

                                JSONObject jsonObject =  json.getJSONObject(i-1);

                                esta_usua = jsonObject.getString("esta_usua");
                                iden_usua = jsonObject.getString("iden_usua");
                                nomb_usua = jsonObject.getString("nomb_usua");
                                apel_usua = jsonObject.getString("apel_usua");

                                modulo[i-1] = String.valueOf(jsonObject.getString("codi_prog"));

                                i--;
                            } while (i>0);


                            if (esta_usua.equalsIgnoreCase("ACT")){

                                Intent t = new Intent(getContext(), NavigationExpress.class);
                                t.putExtra("modulo", modulo);
                                startActivity(t);

                                if (chb_reco_usua.isChecked()){
                                    guardarpreferencia();
                                } else {
                                    limp_contr();
                                    guardarpreferencia();
                                }
                            } else {
                                Toast.makeText(getContext(), "*Error el estado del usuario es inactivo.", Toast.LENGTH_SHORT).show();
                            }

                            pdp.dismiss();




                        } catch (JSONException e) {
                            Toast.makeText(getContext(), "*Error el usuario o clave es incorrecto.", Toast.LENGTH_SHORT).show();
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

    private void limp_contr() {
        edt_clav_usua.setText("");
        edt_codi_usua.setText("");
        chb_reco_usua.setChecked(false);
    }

    private int getCodiVersionCode(){
        Log.i("VERSION CODE", String.valueOf(BuildConfig.VERSION_CODE));
        return BuildConfig.VERSION_CODE;
    }

    public String getVersionName(){
        Log.i("VERSION NAME", String.valueOf(BuildConfig.VERSION_NAME));
        return BuildConfig.VERSION_NAME;
    }



    private boolean validarCodi_usua(String codi_usua){
        boolean toke;

        Pattern patron = Pattern.compile("^[a-zA-Z ]+$");
        if (!patron.matcher(codi_usua).matches() || codi_usua.length() <= 3) {
            til_codi_usua.setError("   Usuario inválido");
            toke = false;
        } else {
            til_codi_usua.setErrorEnabled(false);
            toke = true;
        }

        return toke;
    }


    private boolean validarUsua_clav(String usua_clav){
        boolean toke;

        if (usua_clav.length() < 6) {
            til_clav_usua.setError("   Clave corta");
            toke = false;
        } else {
            til_clav_usua.setErrorEnabled(false);
            toke = true;
        }

        return toke;
    }


    private void guardarpreferencia() {

        SharedPreferences preferences = this.getActivity().getSharedPreferences("credenciales", MODE_PRIVATE);
        String  codi_usua = edt_codi_usua.getText().toString();
        String  usua_clav = edt_clav_usua.getText().toString();
        Boolean reco_usua = chb_reco_usua.isChecked();
        String  iden_usua = this.iden_usua;
        String  nomb_usua = this.nomb_usua;
        String  apel_usua = this.apel_usua;

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("codi_usua", codi_usua);
        editor.putString("usua_clav", usua_clav);
        editor.putBoolean("reco_usua",reco_usua);

        editor.putString("iden_usua", iden_usua);
        editor.putString("nomb_usua", nomb_usua);
        editor.putString("apel_usua", apel_usua);

        editor.commit();
    }

    private void cargarpreferencias() {
        SharedPreferences preferences = this.getActivity().getSharedPreferences("credenciales", Context.MODE_PRIVATE);

        String  codi_usua = preferences.getString("codi_usua","");
        String  usua_clav = preferences.getString("usua_clav","");
        Boolean reco_usua = preferences.getBoolean("reco_usua",false);

        edt_codi_usua.setText(codi_usua);
        edt_clav_usua.setText(usua_clav);
        chb_reco_usua.setChecked(reco_usua);
    }


}

package com.express.tsuexpress.Fragmentos;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
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

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.express.tsuexpress.BuildConfig;
import com.express.tsuexpress.R;

import java.util.regex.Pattern;


public class Fragment_ingresar extends Fragment {

    View vista;
    Fragment_ingresar contexto;

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    TextView txt_vers_name;
    EditText edt_codi_usua, edt_usua_clav;
    Button btn_inic_sesi;
    CheckBox chb_reco_usua;
    TextInputLayout til_codi_usua, til_usua_clav;

    private SharedPreferences preferences;

    ProgressDialog pdp = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        contexto = this;

        //cargarpreferencias();

        vista = inflater.inflate(R.layout.fragment_ingresar, container, false);

        txt_vers_name = (TextView) vista.findViewById(R.id.txt_vers_name);
        edt_codi_usua = (EditText) vista.findViewById(R.id.edt_codi_usua);
        edt_usua_clav = (EditText) vista.findViewById(R.id.edt_usua_clav);
        btn_inic_sesi = (Button) vista.findViewById(R.id.btn_inic_sesi);
        chb_reco_usua = (CheckBox) vista.findViewById(R.id.chb_reco_usua);
        til_codi_usua = (TextInputLayout) vista.findViewById(R.id.til_codi_usua);
        til_usua_clav = (TextInputLayout) vista.findViewById(R.id.til_usu_clav);

        txt_vers_name.setText("Versión : "+getVersionName());

        btn_inic_sesi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarCodi_usua(edt_codi_usua.getText().toString()) && validarUsua_clav(edt_usua_clav.getText().toString())){
                    Toast.makeText(getContext(), "Ingresar", Toast.LENGTH_SHORT).show();
                }

            }
        });


        request= Volley.newRequestQueue(getContext());
        return  vista;

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
            til_codi_usua.setError("Usuario inválido");
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
            til_usua_clav.setError("Clave corta");
            toke = false;
        } else {
            til_usua_clav.setErrorEnabled(false);
            toke = true;
        }

        return toke;
    }



/*

    private void guardarpreferencia() {
       SharedPreferences preferences = getSharedPreferences("credenciales", MODE_PRIVATE);
        String  codi_usua = edt_codi_usua.getText().toString();
        String  usua_clav = edt_usua_clav.getText().toString();
        Boolean reco_usua = chb_reco_usua.isChecked();

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("codi_usua", codi_usua);
        editor.putString("usua_clav", usua_clav);
        editor.putBoolean("reco_usua",reco_usua);

        editor.commit();
    }

    private void cargarpreferencias() {
        SharedPreferences preferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);

        String  codi_usua = preferences.getString("codi_usua","");
        String  usua_clav = preferences.getString("usua_clav","");
        Boolean reco_usua = preferences.getBoolean("reco_usua",false);

        edt_codi_usua.setText(codi_usua);
        edt_usua_clav.setText(usua_clav);
        chb_reco_usua.setChecked(reco_usua);
    } */



}

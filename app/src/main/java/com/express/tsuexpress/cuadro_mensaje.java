package com.express.tsuexpress;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class cuadro_mensaje  {

    public cuadro_mensaje(Context Contexto, String msj) {

        final Dialog dialogo = new Dialog(Contexto);
        dialogo.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogo.setCancelable(false);
        dialogo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogo.setContentView(R.layout.activity_cuadro_mensaje);
        TextView mensaje = (TextView)dialogo.findViewById(R.id.txt_mens);
        mensaje.setText(msj);
        final Button cerrar = (Button)dialogo.findViewById(R.id.btn_ok);

        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogo.dismiss();
            }
        });

        dialogo.show();
    }
}

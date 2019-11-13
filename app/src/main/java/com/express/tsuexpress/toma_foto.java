package com.express.tsuexpress;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class toma_foto extends AppCompatActivity {

    private final String CARPETA_RAIZ="TsuExpress/";
    private final String RUTA_IMAGEN=CARPETA_RAIZ+"misComprobantes";

    final int COD_SELECCIONA=10;
    final int COD_FOTO=20;

    Button botonCargar, btn_guar_imag, btn_cerr;
    ImageView imagen;
    String path;
    String nume_iden, nomb_clie, celu_clie, dire_clie,refe_clie,tipo_envi, tota_pago, iden_dest,nomb_dest,celu_dest,dire_dest,refe_dest,form_pago;

    String nombreImagen="";
    Bitmap bitmap;
    Context contexto;
    RequestQueue request;


    private ProgressDialog pdp = null;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toma_foto);


        contexto = this;

        request= Volley.newRequestQueue(getBaseContext());

        imagen= (ImageView) findViewById(R.id.imagemId);
        botonCargar= (Button) findViewById(R.id.btnCargarImg);
        btn_guar_imag = (Button) findViewById(R.id.btn_guar_imag);
        btn_cerr = (Button) findViewById(R.id.btn_cerr);

        final Drawable imag_anti = imagen.getDrawable();

        btn_guar_imag.setVisibility(View.VISIBLE);
        botonCargar.setVisibility(View.VISIBLE);
        btn_cerr.setVisibility(View.GONE);

        if(validaPermisos()){
            botonCargar.setEnabled(true);
        }/*else{
            botonCargar.setEnabled(true);
        }*/
        btn_cerr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Bundle envio = this.getIntent().getExtras();
        if(envio !=null){

            nume_iden   = envio.getString("nume_iden");
            nomb_clie   = envio.getString("nomb_clie");
            celu_clie   = envio.getString("celu_clie");
            dire_clie   = envio.getString("dire_clie");
            refe_clie   = envio.getString("refe_clie");
            tipo_envi   = envio.getString("tipo_envi");
            tota_pago   = envio.getString("tota_pago");
            iden_dest   = envio.getString("iden_dest");
            nomb_dest   = envio.getString("nomb_dest");
            celu_dest   = envio.getString("celu_dest");
            dire_dest   = envio.getString("dire_dest");
            refe_dest   = envio.getString("refe_dest");
            form_pago   = envio.getString("form_pago");
        }

        btn_guar_imag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardar_imagen(imag_anti);
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
        dialogo1.setTitle("Salir");
        dialogo1.setMessage("¿ Esta seguro que desea salir ?");
        dialogo1.setCancelable(false);
        dialogo1.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                finish();
            }
        });
        dialogo1.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                dialogo1.dismiss();
            }
        });
        dialogo1.show();

    }

    private boolean validaPermisos() {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            return true;
        }
        if((checkSelfPermission(CAMERA)==PackageManager.PERMISSION_GRANTED)&&
                (checkSelfPermission(WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED)){
            return true;
        }
        if((shouldShowRequestPermissionRationale(CAMERA)) ||
                (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE))){
            cargarDialogoRecomendacion();
        }else{
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA},100);
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==100){
            if(grantResults.length==2 && grantResults[0]==PackageManager.PERMISSION_GRANTED
                    && grantResults[1]== PackageManager.PERMISSION_GRANTED){
                botonCargar.setEnabled(true);
            }
            /*else{
                solicitarPermisosManual();
            }*/
        }
    }

   /* private void solicitarPermisosManual() {
        final CharSequence[] opciones={"si","no"};
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(toma_foto.this);
        alertOpciones.setTitle("¿Desea configurar los permisos de forma manual?");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("si")){
                    Intent intent=new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri=Uri.fromParts("package",getPackageName(),null);
                    intent.setData(uri);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(),"Los permisos no fueron aceptados",Toast.LENGTH_SHORT).show();
                    dialogInterface.dismiss();
                }
            }
        });
        alertOpciones.show();
    }*/

    private void cargarDialogoRecomendacion() {
        AlertDialog.Builder dialogo=new AlertDialog.Builder(toma_foto.this);
        dialogo.setTitle("Permisos Desactivados");
        dialogo.setMessage("Debe aceptar los permisos para el correcto funcionamiento de la App");

        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA},100);
                }
            }
        });
        dialogo.show();
    }

    public void onclick(View view) {
        //cargarImagen();
        tomarFotografia();
    }

    /*private void cargarImagen() {
        final CharSequence[] opciones={"Tomar Fotografía","Cargar Imagen","Cancelar"};
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(toma_foto.this);
        alertOpciones.setTitle("Seleccione una Opción");
        alertOpciones.setCancelable(false);
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Tomar Fotografía")){
                    tomarFotografia();
                }else{
                    if (opciones[i].equals("Cargar Fotografía")){
                        Intent intent=new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/");
                        startActivityForResult(intent.createChooser(intent,"Seleccione la Aplicación"),COD_SELECCIONA);
                    }else{
                        dialogInterface.dismiss();
                    }
                }
            }
        });
        alertOpciones.show();
    }*/




    private void tomarFotografia() {
        File fileImagen = new File(Environment.getExternalStorageDirectory(),RUTA_IMAGEN);
        boolean isCreada = fileImagen.exists();

        String nomb_imag = getDateTime().replace("-", "").replace(":","").replace(" ", "").trim()+nume_iden;

        if(isCreada==false){
            isCreada = fileImagen.mkdirs();
        }
        if(isCreada==true){
            nombreImagen = nomb_imag+".jpg";

            path= Environment.getExternalStorageDirectory()+File.separator+RUTA_IMAGEN+File.separator+nombreImagen;
            File imagen=new File(path);

            if (imagen.exists()){
                imagen.delete();
            }

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.N) {
                String authorities=getApplicationContext().getPackageName()+".provider";
                Uri imageUri= FileProvider.getUriForFile(this,authorities,imagen);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            } else {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imagen));
            }
            startActivityForResult(intent,COD_FOTO);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case COD_SELECCIONA:
                    Uri miPath=data.getData();
                    imagen.setImageURI(miPath);
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getBaseContext().getContentResolver(),miPath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    imagen.setImageBitmap(bitmap);

                    break;

                case COD_FOTO:
                    MediaScannerConnection.scanFile(this, new String[]{path}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("Ruta de almacenamiento","Path: "+path);
                                }
                            });
                    bitmap= BitmapFactory.decodeFile(path);
                    imagen.setImageBitmap(bitmap);
                    break;
            }
            bitmap = redimensionar_imagen(bitmap, 500, 500);
            //Reduccion de imagen a 15KB
        }
    }


    private Bitmap redimensionar_imagen(Bitmap bitmap, float  anchoNuevo, float altoNuevo) {

        int ancho = bitmap.getWidth();
        int alto = bitmap.getHeight();

        if (ancho>anchoNuevo || alto>altoNuevo){
            float escalaancho = anchoNuevo / ancho;
            float escalalto = altoNuevo / alto;

            Matrix matrix = new Matrix();
            matrix.postScale(escalaancho, escalalto);

            return Bitmap.createBitmap(bitmap, 0, 0 , ancho, alto, matrix, false);
        }
        else{
            return bitmap;
        }
    }

    private void guardar_imagen(Drawable imag_anti) {

        if (imagen.getDrawable()==imag_anti){
            Toast.makeText(getBaseContext(),"No has capturado una fotografía !!.",Toast.LENGTH_SHORT).show();
        }
        else {
                String url = "https://www.tsuexpress.com/movil/deposito.php?";

                StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>()
                        {
                            @Override
                            public void onResponse(String response) {
                                pdp.dismiss();
                                new cuadro_foto(contexto, "Solicitud exitosa el codigo de envio es : "+response.trim());
                                btn_guar_imag.setVisibility(View.GONE);
                                botonCargar.setVisibility(View.GONE);
                                btn_cerr.setVisibility(View.VISIBLE);
                            }
                        },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                pdp.dismiss();
                                Toast.makeText(getBaseContext(),"No se puede conectar con el servidor."+error,Toast.LENGTH_SHORT).show();
                            }
                        }
                ) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError
                    {
                        String imag_captura = ConvertirStringImag (bitmap);
                        Map<String, String> params = new HashMap<>();
                        params.put("nomb_imag", nombreImagen);
                        params.put("imag_depo", imag_captura);

                        params.put("nume_iden", nume_iden);
                        params.put("nomb_clie", nomb_clie);
                        params.put("celu_clie", celu_clie);
                        params.put("dire_clie", dire_clie);
                        params.put("refe_clie", refe_clie);
                        params.put("tipo_envi", tipo_envi);
                        params.put("tota_pago", tota_pago);
                        params.put("iden_dest", iden_dest);
                        params.put("nomb_dest", nomb_dest);
                        params.put("celu_dest", celu_dest);
                        params.put("dire_dest", dire_dest);
                        params.put("refe_dest", refe_dest);
                        params.put("form_pago", form_pago);

                        return params;
                    }
                };

                pdp = new ProgressDialog(this);
                pdp.show();
                pdp.setContentView(R.layout.progressbar);
                pdp.setCancelable(false);
                pdp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                request.add(postRequest);

        }
    }

    private String ConvertirStringImag(Bitmap bitmap) {
        ByteArrayOutputStream array = new ByteArrayOutputStream();
        bitmap.compress (Bitmap.CompressFormat.JPEG,100,array);
        byte [] imagenByte = array.toByteArray();
        String imagenString = Base64.encodeToString (imagenByte, Base64.DEFAULT);
        return imagenString;
    }


    private String getDateTime(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }


}

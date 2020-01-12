package com.express.tsuexpress;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.express.tsuexpress.Fragmentos.Fragment_enviar;
import com.express.tsuexpress.Fragmentos.Fragment_ingresar;
import com.express.tsuexpress.Fragmentos.Fragment_tracking;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    FloatingActionButton fab_scre_shot;
    View main;
    ImageView img_scre_shot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = (BottomNavigationView)findViewById(R.id.btn_navigationview);
        fab_scre_shot        = (FloatingActionButton)findViewById(R.id.fab_scre_shot);
        main                 =  findViewById(R.id.main);
        img_scre_shot        = (ImageView)findViewById(R.id.img_scre_shot);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Fragment_enviar()).commit();


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                switch (item.getItemId()){
                    case R.id.enviar:
                        selectedFragment = new Fragment_enviar();
                        break;
                    case R.id.ingresar:
                        selectedFragment = new Fragment_ingresar();
                        break;
                    case R.id.seguimiento:
                        selectedFragment = new Fragment_tracking();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

                return true;
            }
        });

        fab_scre_shot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                screetshot();
            }
        });

    }

    public void screetshot(){
        String filename = Environment.getExternalStorageDirectory()+"/CompartirAsesora/"+"test"+".jpg";
        Bitmap b = ScreenShot.takescreenshotOfrootView(img_scre_shot);
        img_scre_shot.setImageBitmap(b);
        main.setBackgroundColor(Color.parseColor("#999999"));

        File file = new File(filename);
        file.getParentFile().mkdir();
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            b.compress(Bitmap.CompressFormat.JPEG, 100,fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            Uri uri = Uri.fromFile(file);

            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(filename));
            shareIntent.setType("image/jpeg");
            startActivity(Intent.createChooser(shareIntent, "Compartir..."));


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


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
}

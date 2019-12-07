package com.express.tsuexpress;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.express.tsuexpress.Fragmentos.Fragment_asig_cola;
import com.express.tsuexpress.Fragmentos.Fragment_inic_home;
import com.express.tsuexpress.Fragmentos.Fragment_oper_cola;
import com.express.tsuexpress.Fragmentos.Fragment_rece_soli;

public class NavigationExpress extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Context contexto;
    Fragment fragment = null;
    View hview;
    TextView txt_nomb_usua, txt_apel_usua;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_express);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        contexto = this;


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //Agegar el contenedor en el appbar del navigationDrawer
        Fragment fragment = new Fragment_inic_home();
        getSupportFragmentManager().beginTransaction().add(R.id.contenedorFragmento,fragment).commit();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        hview = navigationView.getHeaderView(0);
        txt_nomb_usua = (TextView) hview.findViewById(R.id.txt_nomb_usua);
        txt_apel_usua = (TextView) hview.findViewById(R.id.txt_apel_usua);

        navigationView.setNavigationItemSelectedListener(this);

        cargarpreferencias();

        navigationView.getMenu().findItem(R.id.ASIG_COLA).setVisible(false);
        navigationView.getMenu().findItem(R.id.RECE_SOLI).setVisible(false);
        navigationView.getMenu().findItem(R.id.OPER_COLA).setVisible(false);
        navigationView.getMenu().findItem(R.id.RECE_RECO).setVisible(false);


        try {
            Bundle extra = getIntent().getExtras();
            String[] modulo = extra.getStringArray("modulo");

            int t = modulo.length - 1;
            while (t >= 0){
                switch (String.valueOf(modulo[t]).trim()){
                    case "ASIG_COLA":
                        navigationView.getMenu().findItem(R.id.ASIG_COLA).setVisible(true);
                        break;
                    case "RECE_SOLI":
                        navigationView.getMenu().findItem(R.id.RECE_SOLI).setVisible(true);
                        break;
                    case "OPER_COLA":
                        navigationView.getMenu().findItem(R.id.OPER_COLA).setVisible(true);
                        break;
                    case "RECE_RECO":
                        navigationView.getMenu().findItem(R.id.RECE_RECO).setVisible(true);
                        break;
                }
                t--;
            }
        }
        catch (Exception e){
            /*Intent mainIntent = new Intent(getApplicationContext(), Fragment_ingresar.class);
            mainIntent.putExtra("cerrar", "1");
            startActivity(mainIntent);
            finish(); */
        }


    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
        dialogo1.setTitle("Salir");
        dialogo1.setMessage("¿ Esta seguro que desea salir de Tsu Express?");
        dialogo1.setCancelable(false);
        dialogo1.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                finish();
                //System.exit(0);
            }
        });
        dialogo1.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                dialogo1.dismiss();
            }
        });
        dialogo1.show();
    }


    /* Menu de opciones
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_express, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    } */

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.INIC_HOME) {
            CargarFragmento(new Fragment_inic_home());
        } else if (id == R.id.ASIG_COLA) {
            CargarFragmento(new Fragment_asig_cola());
        } else if (id == R.id.RECE_SOLI ) {
            CargarFragmento(new Fragment_rece_soli());
        } else if (id == R.id.RECE_RECO ) {


        } else if (id == R.id.OPER_COLA) {
            CargarFragmento(new Fragment_oper_cola());
        } else if (id == R.id.CERR_SESI) {

            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
            dialogo1.setTitle("Salir");
            dialogo1.setMessage("¿ Esta seguro que desea salir de Tsu Express?");
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void CargarFragmento(Fragment fragmento){
        FragmentManager Manager = getSupportFragmentManager();
        Manager.beginTransaction().replace(R.id.contenedorFragmento,fragmento).commit();
    }


    private void cargarpreferencias() {
        SharedPreferences preferences = this.getSharedPreferences("credenciales", Context.MODE_PRIVATE);

        String  nomb_usua = preferences.getString("nomb_usua","");
        String  apel_usua = preferences.getString("apel_usua","");

        txt_nomb_usua.setText(nomb_usua);
        txt_apel_usua.setText(apel_usua);

    }

}
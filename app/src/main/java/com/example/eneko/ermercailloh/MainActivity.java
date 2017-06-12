package com.example.eneko.ermercailloh;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener {

    private DatabaseReference bdArticulos = FirebaseDatabase.getInstance().getReference().child("articulos");

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Usuario u1 = Usuario.getInstance();
        Fragment fragment = new pestaniaInicio();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
        navigationView.setCheckedItem(R.id.Pagina_Inicio);

        this.getWindow().getDecorView().findViewById(R.id.intro).setVisibility(View.INVISIBLE);
        if(!u1.estaLogueado){
            this.getWindow().getDecorView().findViewById(R.id.intro).setVisibility(View.INVISIBLE);
            navigationView.getMenu().findItem(R.id.Usuario).setVisible(false);
            navigationView.getMenu().findItem(R.id.subir_articulo).setVisible(false);
            navigationView.getMenu().findItem(R.id.mis_articulos).setVisible(false);
            navigationView.getMenu().findItem(R.id.mis_pujas).setVisible(false);
            navigationView.getMenu().findItem(R.id.cerrar_sesion).setVisible(false);
        }else {
            navigationView.getMenu().findItem(R.id.login).setVisible(false);
            //Recuperamos la información pasada en el intent
            Bundle bundle = this.getIntent().getExtras();
            navigationView.getMenu().findItem(R.id.Usuario).setTitle(u1.getNombre());

        }

        if (ListaProductos.getInstance().listaVacia())
            generarListaProductos();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        boolean fragmentTransaction = false;
        Fragment fragment = null;
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_buscar) {
            fragment = new pestaniaSetings();
            fragmentTransaction =true;
            Toast toast = Toast.makeText(this.getCurrentFocus().getContext(), "Introduce los datos de busqueda", Toast.LENGTH_SHORT);
            toast.show();

        }
        if(fragmentTransaction) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .commit();

            //item.setChecked(true);
            getSupportActionBar().setTitle(item.getTitle());
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawers();


        return true;

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        boolean fragmentTransaction = false;
        Fragment fragment = null;

        int id = item.getItemId();

        if (id == R.id.login) {

            fragment = new pestaniaLogin();
            Bundle b = new Bundle();
            b.putString("FRAG", "IN");

            //Añadimos la información al intent

            fragment.setArguments(b);
            fragmentTransaction =true;

        } else if (id == R.id.Usuario) {
           fragment = new DatosUsuario();
           fragmentTransaction =true;
        } else if (id == R.id.subir_articulo) {
            fragment = new subirArticulo();
            fragmentTransaction=true;

        } else if (id == R.id.mis_articulos) {
            fragment = new articulosUsuario();
            fragmentTransaction =true;
        }   else if (id == R.id.Pagina_Inicio) {
            fragment = new pestaniaInicio();
            fragmentTransaction =true;
        }   else if (id == R.id.mis_pujas) {
            fragment = new pestaniaMisPujas();
            fragmentTransaction =true;
        } else if (id == R.id.cerrar_sesion) {
            Usuario u11 = Usuario.getInstance();
            u11.cerrarSesion();

            Intent i = new Intent(MainActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }

        if(fragmentTransaction) {
           getSupportFragmentManager().beginTransaction()
                  .replace(R.id.content_frame, fragment)
                 .commit();

            item.setChecked(true);
            getSupportActionBar().setTitle(item.getTitle());
        }
     DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
       drawer.closeDrawers();


        return true;
    }

    private void generarListaProductos(){

        bdArticulos.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator it = dataSnapshot.child("articulos").getChildren().iterator();
                while(it.hasNext()){
                    DataSnapshot ds = (DataSnapshot) it.next();

                    Producto p = new Producto(
                            Integer.parseInt(dataSnapshot.getKey()),
                            dataSnapshot.child("estado").getValue().toString(),
                            Integer.parseInt(dataSnapshot.child("idPropietario").getValue().toString()),
                            dataSnapshot.child("imagenUri").getValue().toString(),
                            Integer.parseInt(dataSnapshot.child("precio").getValue().toString()),
                            dataSnapshot.child("titulo").getValue().toString()
                    );
                    ListaProductos.getInstance().addProducto(p);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}

package com.example.eneko.ermercailloh;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;


public class PaginaInformacionArticulo extends Fragment {

    private int idArticulo;
    private float pujaMasAlta;
    private int idPujadorMasAlto = 0;
    private DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference();

    public PaginaInformacionArticulo() {
        // Required empty public constructor
    }

    public static PaginaInformacionArticulo newInstance(String param1, String param2) {
        PaginaInformacionArticulo fragment = new PaginaInformacionArticulo();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle b = getArguments();
        idArticulo = b.getInt("idArticulo");
        return inflater.inflate(R.layout.fragment_pagina_informacion_articulo, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        // Toast.makeText(getActivity(), "Back Pressed", Toast.LENGTH_SHORT).show();
                        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("ErMercailloH");
                        Fragment fragment2 = new pestaniaInicio();
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.content_frame, fragment2);
                        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
                        navigationView.getMenu().findItem(R.id.Pagina_Inicio).setChecked(true);
                        fragmentTransaction.commit();
                        return true;
                    }
                }
                return false;
            }
        });
        super.onViewCreated(view, savedInstanceState);

        final LinearLayout layoutPrincipal = (LinearLayout) view.findViewById(R.id.InfoLayout);
        final String idProducto = String.valueOf(idArticulo);

        dbReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                LinearLayout layout = new LinearLayout(layoutPrincipal.getContext());
                layout.setOrientation(LinearLayout.VERTICAL);
                if(dataSnapshot.child("pujadoPor").child(idProducto).exists()) {

                    pujaMasAlta = Float.valueOf(dataSnapshot.child("pujadoPor").child(idProducto).child("0").getValue().toString());

                    Iterator it = dataSnapshot.child("pujadoPor").child(idProducto).getChildren().iterator();
                    it.next();
                    boolean encontrado = false;
                    while (it.hasNext()) {
                        DataSnapshot ds = (DataSnapshot) it.next();
                        TextView tUsuario = new TextView(getContext());
                        String key = ds.getKey();
                        tUsuario.setText("El usuario "+dataSnapshot.child("usuarios").child(key).child("email").getValue()+" ha hecho las siguientes pujas:");
                        Iterator it2 = ds.getChildren().iterator();
                        int cont = 1;
                        layout.addView(tUsuario);
                        while (it2.hasNext()) {
                            DataSnapshot ds2 = (DataSnapshot) it2.next();
                            TextView tPuja = new TextView(getContext());
                            String puja = ds2.getValue().toString();
                            float floatValue = Float.parseFloat(puja);
                            puja = String.format("%.2f",floatValue);
                            puja = puja.replace('.', ',');
                            tPuja.setText(cont + " puja: " + puja + "€");
                            if (Float.valueOf(ds2.getValue().toString()) == pujaMasAlta)
                                encontrado = true;
                            layout.addView(tPuja);
                            cont++;
                        }
                        if (encontrado)
                            idPujadorMasAlto = Integer.parseInt(ds.getKey());
                    }
                }else{
                    TextView tTexto = new TextView(getContext());
                    tTexto.setGravity(Gravity.CENTER_HORIZONTAL);
                    tTexto.setText("No existen pujas para este articulo");
                    layout.addView(new TextView(getContext()));
                    layout.addView(tTexto);
                }

                Button btnCerrarPuja = new Button(getContext());
                btnCerrarPuja.setText("Cerrar pujas");
                btnCerrarPuja.setBackgroundColor(Color.argb(0, 138, 221, 45));
                btnCerrarPuja.setGravity(Gravity.CENTER_HORIZONTAL);
                btnCerrarPuja.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dbReference.child("articulos").child(idProducto).child("estado").setValue("cerrado");
                        //dbReference.child("articulosGanados").child(String.valueOf(Usuario.getInstance().getIdusuario()))
                        if(dataSnapshot.child("articulosGanados").child(String.valueOf(idPujadorMasAlto)).exists() && idPujadorMasAlto != 0){
                            int numProductos = (int) dataSnapshot.child("articulosGanados").child(String.valueOf(Usuario.getInstance().getIdusuario())).getChildrenCount() + 1;
                            dbReference.child("articulosGanados").child(String.valueOf(Usuario.getInstance().getIdusuario())).child(String.valueOf(numProductos)).setValue(idProducto);
                        }else if (idPujadorMasAlto != 0){
                            dbReference.child("articulosGanados").child(String.valueOf(idPujadorMasAlto)).child("1").setValue(idProducto);
                        }else
                            dbReference.child("articulosGanados").child(String.valueOf(idPujadorMasAlto)).child("0").setValue(idProducto);

                        pestaniaInicio pInicio = new pestaniaInicio();
                        FragmentManager fragMan = getFragmentManager();
                        FragmentTransaction fragTrans = fragMan.beginTransaction();
                        fragTrans.replace(R.id.content_frame, pInicio);
                        Toast toast = Toast.makeText(getContext(), "A cerrado correctamente las pujas", Toast.LENGTH_SHORT);
                        toast.show();
                        fragTrans.commit();
                    }
                });
                layout.addView(btnCerrarPuja);

                layoutPrincipal.addView(layout);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

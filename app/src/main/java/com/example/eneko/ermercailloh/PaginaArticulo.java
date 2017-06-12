package com.example.eneko.ermercailloh;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

public class PaginaArticulo extends Fragment {

    private int idArticulo;
    private ArrayList<Producto> listaArticulos = ListaProductos.getInstance().getListaProductos();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference();
    private StorageReference storageRef = storage.getReferenceFromUrl("gs://ermercailloh-7702b.appspot.com");

    public PaginaArticulo() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle b = getArguments();
        idArticulo = b.getInt("idArticulo");
        return inflater.inflate(R.layout.fragment_pagina_articulo, container, false);
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

        final LinearLayout layoutPrincipal = (LinearLayout) view.findViewById(R.id.ArticuloLayout);
        final String idProducto = String.valueOf(idArticulo);

        dbReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                LinearLayout layout = new LinearLayout(layoutPrincipal.getContext());
                layout.setOrientation(LinearLayout.VERTICAL);
                final ImageView imImagen = new ImageView(layout.getContext());
                TextView tvEstado = new TextView(layout.getContext());
                tvEstado.setGravity(Gravity.CENTER_HORIZONTAL);
                TextView tvIdProp = new TextView(layout.getContext());
                tvIdProp.setGravity(Gravity.CENTER_HORIZONTAL);
                TextView tvImgUri = new TextView(layout.getContext());
                tvImgUri.setGravity(Gravity.CENTER_HORIZONTAL);
                TextView tvPrecio = new TextView(layout.getContext());
                tvPrecio.setGravity(Gravity.CENTER_HORIZONTAL);
                TextView tvPuja = new TextView(layout.getContext());
                tvPuja.setGravity(Gravity.CENTER_HORIZONTAL);
                final TextView tvTitulo = new TextView(layout.getContext());
                tvTitulo.setGravity(Gravity.CENTER_HORIZONTAL);

                String estado = dataSnapshot.child("articulos").child(idProducto).child("estado").getValue().toString();
                String idProp = dataSnapshot.child("articulos").child(idProducto).child("idPropietario").getValue().toString();
                String imgUri = dataSnapshot.child("articulos").child(idProducto).child("imagenUri").getValue().toString();
                String precio = dataSnapshot.child("articulos").child(idProducto).child("precio").getValue().toString();
                float pMax = 0;
                String pujaMaxima = "";
                if(dataSnapshot.child("pujadoPor").child(idProducto).exists()){
                    Iterator<DataSnapshot> it = dataSnapshot.child("pujadoPor").child(idProducto).getChildren().iterator();
                    String puja = dataSnapshot.child("pujadoPor").child(idProducto).child("0").getValue().toString();
                    it.next();
                    while(it.hasNext()){
                        DataSnapshot ds = (DataSnapshot)it.next();
                        int last = (int) ds.getChildrenCount();
                        float valorPuja = Float.valueOf(puja);
                        pMax = (valorPuja > pMax) ? valorPuja : pMax;
                        pujaMaxima = String.format("%.2f",pMax);
                    }
                }
                else
                    pujaMaxima = "Sin pujas";
                final String titulo = dataSnapshot.child("articulos").child(idProducto).child("titulo").getValue().toString();

                StorageReference imRef = storageRef.child("images/").child(imgUri);

                final long MAX_DOWNLOAD_SIZE = 10000000; // 10MB de descarga maxima
                imRef.getBytes(MAX_DOWNLOAD_SIZE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        bitmap = Bitmap.createScaledBitmap(bitmap, 1024, 768, true);
                        imImagen.setImageBitmap(bitmap);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {

                    }
                });

                tvEstado.setText("Estado: " + estado);
                tvIdProp.setText("Propietario: " + idProp);
                tvImgUri.setText("URI de la imagen: " + imgUri);
                String precioComa = precio.replace('.', ',');
                tvPrecio.setText("Precio inicial: " + precioComa+"€");
                String pujaMaximaComa = pujaMaxima.replace('.', ',');
                tvPuja.setText("Puja mas alta: "+ pujaMaximaComa+"€");
                tvTitulo.setText("Titulo: " + titulo);

                layout.addView(new TextView(layout.getContext()));
                layout.addView(imImagen);
                layout.addView(new TextView(layout.getContext()));
                layout.addView(tvEstado);
                layout.addView(tvIdProp);
                layout.addView(tvImgUri);
                layout.addView(tvPrecio);
                layout.addView(tvPuja);
                layout.addView(tvTitulo);
                layout.addView(new TextView(layout.getContext()));
                layout.addView(new TextView(layout.getContext()));

                if(!dataSnapshot.child("articulosPorUsuario").child(String.valueOf(Usuario.getInstance().getIdusuario())).child(String.valueOf(idArticulo)).exists()) {
                    Button btnPujar = new Button(layout.getContext());
                    btnPujar.setGravity(Gravity.CENTER_HORIZONTAL);
                    btnPujar.setText("Pujar");
                    btnPujar.setBackgroundColor(Color.argb(0, 138, 221, 45));
                    final float finalPMax = pMax;
                    final float finalPMax1 = pMax;
                    final String pPrecio = precio;
                    btnPujar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Pujar pujar = new Pujar();
                            FragmentManager fragMan = getFragmentManager();
                            FragmentTransaction fragTrans = fragMan.beginTransaction();
                            Bundle b = new Bundle();
                            if(finalPMax1 != 0)
                                b.putFloat("pujaMaxima", finalPMax);
                            else
                                b.putFloat("pujaMaxima", Float.parseFloat(pPrecio));
                            b.putInt("idArticulo", Integer.parseInt(idProducto));
                            pujar.setArguments(b);
                            fragTrans.replace(R.id.content_frame, pujar);
                            Toast toast = Toast.makeText(getContext(), "Va a pujar por el articulo "+titulo, Toast.LENGTH_SHORT);
                            toast.show();
                            fragTrans.commit();
                        }
                    });
                    layout.addView(btnPujar);
                }else{
                    Button btnInfo = new Button(layout.getContext());
                    btnInfo.setGravity(Gravity.CENTER_HORIZONTAL);
                    btnInfo.setText("Este articulo es tuyo. Pulsa aqui para ver las pujas o cerrar la venta.");
                    btnInfo.setBackgroundColor(Color.argb(0, 138, 221, 45));

                    btnInfo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PaginaInformacionArticulo pInfoArticulo = new PaginaInformacionArticulo();
                            FragmentManager fragMan = getFragmentManager();
                            FragmentTransaction fragTrans = fragMan.beginTransaction();
                            Bundle b = new Bundle();
                            b.putInt("idArticulo", Integer.parseInt(idProducto));
                            pInfoArticulo.setArguments(b);
                            fragTrans.replace(R.id.content_frame, pInfoArticulo);
                            Toast toast = Toast.makeText(getContext(), "Información de su producto", Toast.LENGTH_SHORT);
                            toast.show();
                            fragTrans.commit();
                        }
                    });

                    layout.addView(btnInfo);
                }
                layoutPrincipal.addView(layout);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

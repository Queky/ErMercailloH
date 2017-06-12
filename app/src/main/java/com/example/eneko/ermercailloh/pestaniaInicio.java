package com.example.eneko.ermercailloh;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
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

import java.util.Iterator;


/**
 * A simple {@link Fragment} subclass.
 */
public class pestaniaInicio extends Fragment {

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference();
    private StorageReference storageRef = storage.getReferenceFromUrl("gs://ermercailloh-7702b.appspot.com");
    private int cont;
    private int numArticulos;
    private Usuario u1 = Usuario.getInstance();
    public pestaniaInicio() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        cont = 1;
        numArticulos = 10;
        return inflater.inflate(R.layout.fragment_pestania_inicio, container, false);
    }
    public void onViewCreated(final View view, final Bundle savedInstanceState) {

        final LinearLayout miVista = (LinearLayout) view.findViewById(R.id.PagIn);

        dbReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterator it = dataSnapshot.child("articulos").getChildren().iterator();
                while (it.hasNext() && cont<=numArticulos){

                    final String idProducto = String.valueOf(cont);
                    // Comprobacion id producto existe && esta abierto a puja
                    if(dataSnapshot.child("articulos").child(idProducto).child("estado").exists()) {
                        if(dataSnapshot.child("articulos").child(idProducto).child("estado").getValue().toString().equals("abierto")) {
                           String  pro = dataSnapshot.child("articulos").child(idProducto).child("idPropietario").getValue().toString();
                            boolean esMiProducto=pro.equals(""+u1.getIdusuario());
                            if((u1.isEstaLogueado()&&!esMiProducto)
                                    ||u1.isEstaLogueado()==false){

                            final LinearLayout lDatos = new LinearLayout(miVista.getContext());

                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.MATCH_PARENT
                            );
                            params.setMargins(10, 10, 10,10);
                            lDatos.setLayoutParams(params);
                           // lDatos.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.bordertrans));
                            lDatos.setOrientation(LinearLayout.HORIZONTAL);
                            lDatos.setPadding(0,0,0,0);
                            LinearLayout lImagen = new LinearLayout(lDatos.getContext());
                            lImagen.setPadding(0,0,15,0);

                            LinearLayout layout = new LinearLayout(lDatos.getContext());
                            layout.setPadding(0,15,0,15);
                            lDatos.addView(lImagen);
                            lDatos.addView(layout);

                            layout.setOrientation(LinearLayout.VERTICAL);
                            final ImageView imImagen = new ImageView(layout.getContext());
                            TextView tvEstado = new TextView(layout.getContext());
                            //tvEstado.setGravity(Gravity.CENTER_HORIZONTAL);
                            TextView tvIdProp = new TextView(layout.getContext());
                            //tvIdProp.setGravity(Gravity.CENTER_HORIZONTAL);
                            TextView tvImgUri = new TextView(layout.getContext());
                            //tvImgUri.setGravity(Gravity.CENTER_HORIZONTAL);
                            TextView tvPrecio = new TextView(layout.getContext());
                            //tvPrecio.setGravity(Gravity.CENTER_HORIZONTAL);
                            final TextView tvTitulo = new TextView(layout.getContext());
                           // tvTitulo.setGravity(Gravity.CENTER_HORIZONTAL);
                            final Button btnLoad = new Button(layout.getContext());
                            btnLoad.setGravity(Gravity.CENTER_HORIZONTAL);
                            btnLoad.setText("Cargar mas articulos");

                            btnLoad.setBackgroundColor(Color.argb(0, 138, 221, 45));
                            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.MATCH_PARENT
                            );
                            params.setMargins(10, 10, 10,10);
                            btnLoad.setLayoutParams(params);

                            String estado = dataSnapshot.child("articulos").child(idProducto).child("estado").getValue().toString();
                            String idProp = dataSnapshot.child("articulos").child(idProducto).child("idPropietario").getValue().toString();
                            String imgUri = dataSnapshot.child("articulos").child(idProducto).child("imagenUri").getValue().toString();
                            String precio = dataSnapshot.child("articulos").child(idProducto).child("precio").getValue().toString();
                            float floatValue = Float.parseFloat(precio);
                            precio = String.format("%.2f",floatValue);
                            String precioComa = precio.replace('.', ',');
                            final String titulo = dataSnapshot.child("articulos").child(idProducto).child("titulo").getValue().toString();

                            StorageReference imRef = storageRef.child("images/").child(imgUri);

                            final long MAX_DOWNLOAD_SIZE = 10000000; // 10MB de descarga maxima
                            imRef.getBytes(MAX_DOWNLOAD_SIZE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                @Override
                                public void onSuccess(byte[] bytes) {
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                    bitmap = Bitmap.createScaledBitmap(bitmap, 350, 300, true);
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
                            tvPrecio.setText("Precio inicial: " + precioComa+"€");
                            tvTitulo.setText("Titulo: " + titulo);

                            //layout.addView(new TextView(layout.getContext()));
                            lImagen.addView(imImagen);
                            //layout.addView(new TextView(layout.getContext()));
                            layout.addView(tvEstado);
                            layout.addView(tvIdProp);
                            layout.addView(tvImgUri);
                            layout.addView(tvPrecio);
                            layout.addView(tvTitulo);
                            //layout.addView(new TextView(layout.getContext()));
                            //layout.addView(new TextView(layout.getContext()));

                            miVista.addView(lDatos);
                            if (cont == numArticulos && it.hasNext())
                                miVista.addView(btnLoad);

                            layout.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {
                                    if (event.getAction() == MotionEvent.ACTION_UP && (event.getEventTime() - event.getDownTime() < 1000)) {
                                        PaginaArticulo pArt = new PaginaArticulo();
                                        pestaniaLogin pLogin = new pestaniaLogin();
                                        FragmentManager fragMan = getFragmentManager();
                                        FragmentTransaction fragTrans = fragMan.beginTransaction();
                                        Bundle b = new Bundle();
                                        b.putInt("idArticulo", Integer.parseInt(idProducto));
                                        pArt.setArguments(b);
                                        pLogin.setArguments(b);
                                        if (Usuario.getInstance().estaLogueado) {
                                            fragTrans.replace(R.id.content_frame, pArt);
                                            Toast toast = Toast.makeText(getContext(), "Articulo: " + titulo, Toast.LENGTH_SHORT);
                                            toast.show();
                                        } else {
                                            Bundle b1 = new Bundle();
                                            b1.putString("FRAG", "IN");

                                            //Añadimos la información al intent
                                            Fragment fr = new pestaniaLogin();
                                            fr.setArguments(b1);
                                            fragTrans.replace(R.id.content_frame, fr);
                                            Toast toast = Toast.makeText(getContext(), "Debe identificarse para ver un articulo", Toast.LENGTH_SHORT);
                                            toast.show();
                                        }
                                        fragTrans.commit();
                                    }
                                    return true;
                                }
                            });

                            btnLoad.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {
                                    btnLoad.setVisibility(View.GONE);
                                    cont = numArticulos;
                                    numArticulos += 1;
                                    onViewCreated(view, savedInstanceState);
                                    return false;
                                }
                            });
                            }
                        }
                    }
                    it.next();
                    cont ++;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}



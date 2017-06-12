package com.example.eneko.ermercailloh;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
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

import java.util.ArrayList;
import java.util.Iterator;


/**
 * A simple {@link Fragment} subclass.
 */
public class pestaniaMisPujas extends Fragment {
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference();
    private StorageReference storageRef = storage.getReferenceFromUrl("gs://ermercailloh-7702b.appspot.com");
    private Usuario u1 = Usuario.getInstance();

    public pestaniaMisPujas() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pestania_mis_pujas, container, false);

        return view;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        obtenerMisPujas();

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


    }

    public ArrayList obtenerMisPujas() {

        final ArrayList<Puja> listMisPujas = new ArrayList();

        dbReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator it1 = dataSnapshot.child("puja").child(""+u1.getIdusuario()).getChildren().iterator();


                //Obtencion del idProducto
                while (it1.hasNext()  ) {
                    Puja puja= new Puja();
                    DataSnapshot ds = (DataSnapshot) it1.next();
                    puja.setIdProducto(Integer.parseInt(ds.getKey()));
                    int cont=(int)ds.getChildrenCount();
                    puja.setMiPrecioPujado(Float.valueOf(ds.child(""+cont).getValue().toString()));
                    listMisPujas.add(puja);
                }
                //Obtencion de datosArticulo
                for(int i=0; i<listMisPujas.size();i++) {

                        listMisPujas.get(i).setTituloProducto(dataSnapshot.child("articulos").child(""+listMisPujas.get(i).getIdProducto()).child("titulo").getValue().toString());
                        listMisPujas.get(i).setIdProducto(Integer.parseInt(dataSnapshot.child("articulos").child(""+listMisPujas.get(i).getIdProducto()).getKey()));
                        listMisPujas.get(i).setPrecioInicial(Float.valueOf(dataSnapshot.child("articulos").child(""+listMisPujas.get(i).getIdProducto()).child("precio").getValue().toString()));
                        listMisPujas.get(i).setImgUri(dataSnapshot.child("articulos").child(""+listMisPujas.get(i).getIdProducto()).child("imagenUri").getValue().toString());
                }
                for(int i =0; i<listMisPujas.size();i++){
                    listMisPujas.get(i).setPujaMaximaArticulo(Float.valueOf(dataSnapshot.child("pujadoPor").child(""+listMisPujas.get(i).getIdProducto()).child("0").getValue().toString()));
                }
                colocarElementos(listMisPujas);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return listMisPujas;
    }
    public void colocarElementos(final ArrayList<Puja> listaPujas){
        LinearLayout miVista = (LinearLayout) getView().findViewById(R.id.DatosPujas);
        for( int i=0;i<listaPujas.size();i++){

            //CONFIGURAMOS LOS LAYOUTS
            LinearLayout lDatos = new LinearLayout(miVista.getContext());
            lDatos.setOrientation(LinearLayout.HORIZONTAL);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
            );
            params.setMargins(10, 10, 10, 0);
            lDatos.setLayoutParams(params);

            lDatos.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.bordertrans));

            LinearLayout lImagen = new LinearLayout(lDatos.getContext());
            lImagen.setPadding(0,0,15,0);
            LinearLayout lInfo = new LinearLayout(lDatos.getContext());
            lInfo.setOrientation(LinearLayout.VERTICAL);
            lDatos.addView(lImagen);
            lDatos.addView(lInfo);
            lInfo.setPadding(0,0,0,5);

            //AÑADIMOS LA INFORMACION REQUERIDA DE LA PUJA
            //Imagen
            final ImageView imagenArticulo = new ImageView(lImagen.getContext());
            StorageReference imRef = storageRef.child("images/").child(listaPujas.get(i).getImgUri());
            final long MAX_DOWNLOAD_SIZE = 10000000; // 10MB de descarga maxima
            imRef.getBytes(MAX_DOWNLOAD_SIZE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    bitmap = Bitmap.createScaledBitmap(bitmap, 400, 350, true);
                    imagenArticulo.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {

                }
            });
            lImagen.addView(imagenArticulo);
            //Titulo
            TextView txtInfoTitulo = new TextView(lDatos.getContext());
            txtInfoTitulo.setText("Titulo del Articulo: "+listaPujas.get(i).getTituloProducto());
            lInfo.addView(txtInfoTitulo);
            //idProducto
            TextView txtInfoId = new TextView(lDatos.getContext());
            txtInfoId.setText("ID del Articulo: "+ listaPujas.get(i).getIdProducto());
            lInfo.addView(txtInfoId);
            //Precios Puja
            TextView txtInfoPrecioInicial = new TextView(lDatos.getContext());
            String precioInicial = String.valueOf(listaPujas.get(i).getPrecioInicial());
           precioInicial =precioInicial.replace('.',',');
            txtInfoPrecioInicial.setText("Precio inicial de la puja: "+precioInicial+"€");
            TextView txtInfoPrecioActual= new TextView(lDatos.getContext());
            String PujaMaximaArticulo=String.valueOf(listaPujas.get(i).getPujaMaximaArticulo());
            PujaMaximaArticulo = PujaMaximaArticulo.replace('.',',');
            txtInfoPrecioActual.setText("Puja maxima por este articulo: "+PujaMaximaArticulo+"€");
            TextView txtInfoPrecioMio= new TextView(lDatos.getContext());
            String MiPrecioPujado = String.valueOf(listaPujas.get(i).getMiPrecioPujado());
            MiPrecioPujado=MiPrecioPujado.replace('.',',');
            txtInfoPrecioMio.setText("Mi puja por este articulo: "+MiPrecioPujado+"€");
            lInfo.addView(txtInfoPrecioInicial);
            lInfo.addView(txtInfoPrecioActual);
            lInfo.addView(txtInfoPrecioMio);

            final int idArt = listaPujas.get(i).getIdProducto();

            miVista.addView(lDatos);
            final String titulo = listaPujas.get(i).getTituloProducto();
            lDatos.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP && (event.getEventTime() - event.getDownTime() < 1000)) {
                        FragmentManager fragMan = getFragmentManager();
                        FragmentTransaction fragTrans = fragMan.beginTransaction();
                        if (Usuario.getInstance().estaLogueado) {
                            PaginaArticulo pArticulo = new PaginaArticulo();
                            Bundle b = new Bundle();
                            b.putInt("idArticulo", idArt);
                            pArticulo.setArguments(b);
                            fragTrans.replace(R.id.content_frame, pArticulo);
                            Toast toast = Toast.makeText(getContext(), "Articulo: " + titulo, Toast.LENGTH_SHORT);
                            toast.show();
                        } else {
                            fragTrans.replace(R.id.content_frame, new pestaniaLogin());
                            Toast toast = Toast.makeText(getContext(), "Debe identificarse para ver un articulo", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                        fragTrans.commit();
                    }
                    return true;
                }
            });
        }


    }

}

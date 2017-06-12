package com.example.eneko.ermercailloh;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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

import static android.R.attr.fragment;
import static android.R.attr.layout;


/**
 * A simple {@link Fragment} subclass.
 */
public class pestaniaSetings extends Fragment  {

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference();
    private StorageReference storageRef = storage.getReferenceFromUrl("gs://ermercailloh-7702b.appspot.com");
    private int cont;
    private int numArticulos;
    private Usuario u1 =Usuario.getInstance();



    public pestaniaSetings() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_pestania_setings, container, false);
        cont = 1;
        numArticulos = 5;

        return view;
    }
    public void onViewCreated(final View view, final Bundle savedInstanceState) {

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        Toast.makeText(getActivity(), "Back Pressed", Toast.LENGTH_SHORT).show();
                        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("ErMercailloH");
                        Fragment fragment2 = new pestaniaInicio();
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.content_frame, fragment2);

                        fragmentTransaction.commit();
                        return true;
                    }
                }
                return false;
            }
        });
        final LinearLayout miVista = (LinearLayout) view.findViewById(R.id.pagInfo);
        final LinearLayout miVista1 = (LinearLayout) view.findViewById(R.id.pagInfo1);
        final EditText txtBusqueda = (EditText) view.findViewById(R.id.editTextDireccionIP);
        final EditText txtPrecio1 = (EditText)view.findViewById(R.id.editTextPrecio1);
        final EditText txtPrecio2 = (EditText)view.findViewById(R.id.editTextPrecio2);


     // final SharedPreferences prefs =
               // this.getActivity().getSharedPreferences("MisPreferencias",Context.MODE_PRIVATE);

        final Button b2 =(Button)view.findViewById(R.id.button2);
        final Button b1 =(Button)view.findViewById(R.id.buttonBuscar);
        b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final String busq= txtBusqueda.getText().toString();
                final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                if (busq.equals("")!=true){
                    final String strPrecio1 = txtPrecio1.getText().toString();
                    final String strPrecio2 = txtPrecio2.getText().toString();
                    final boolean hayPrecio;

                    final int precio1;
                    final int precio2;
                    if((strPrecio1.equals("")!=true&&strPrecio2.equals("")!=true)||(strPrecio1.equals("")==true&&strPrecio2.equals("")==true)) {
                        if(strPrecio1.equals("")!=true&&strPrecio2.equals("")!=true) {
                            precio1 = Integer.parseInt(txtPrecio1.getText().toString());
                            precio2 = Integer.parseInt(txtPrecio2.getText().toString());
                            ((TextView) miVista.findViewById(R.id.textPrecio)).setText(precio1+"€ - "+precio2+"€");
                           hayPrecio = true;
                        }else{
                            hayPrecio = false;
                            precio1=0;
                            precio2=0;

                        }
                        if ((hayPrecio&&precio1<=precio2)||!hayPrecio) {


                            dbReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {


                                    Iterator it = dataSnapshot.child("articulos").getChildren().iterator();
                                   int cuento =0;
                                    while (it.hasNext()) {

                                        final String idProducto = String.valueOf(cont);
                                        // Comprobacion id producto existe && esta abierto a puja
                                        if (dataSnapshot.child("articulos").child(idProducto).child("estado").exists()) {
                                            if (dataSnapshot.child("articulos").child(idProducto).child("estado").getValue().toString().equals("abierto")) {
                                                String  pro = dataSnapshot.child("articulos").child(idProducto).child("idPropietario").getValue().toString();
                                                boolean esMiProducto=pro.equals(""+u1.getIdusuario());
                                                if((u1.isEstaLogueado()&&!esMiProducto)
                                                        ||u1.isEstaLogueado()==false){
                                                if (contieneBusqueda(dataSnapshot.child("articulos").child(idProducto).child("titulo").getValue().toString(), txtBusqueda.getText().toString())) {
                                                    boolean entra = false;
                                                    if (hayPrecio && estaEnPrecio(precio1, precio2, Float.parseFloat(dataSnapshot.child("articulos").child(idProducto).child("precio").getValue().toString())))
                                                        entra = true;
                                                    else if (!hayPrecio)
                                                        entra = true;

                                                    if (entra) {
                                                        cuento++;
                                                        view.findViewById(R.id.sV).setVisibility(View.VISIBLE);
                                                        miVista1.setVisibility(View.INVISIBLE);
                                                        miVista.setVisibility(View.VISIBLE);
                                                        ((TextView) miVista.findViewById(R.id.txtArticulo)).setText(busq);
                                                        final LinearLayout lDatos = new LinearLayout(miVista.getContext());

                                                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                                                LinearLayout.LayoutParams.MATCH_PARENT,
                                                                LinearLayout.LayoutParams.MATCH_PARENT
                                                        );
                                                        params.setMargins(10, 10, 10, 0);
                                                        lDatos.setLayoutParams(params);
                                                        lDatos.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.bordertrans));
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
                                                       // tvEstado.setGravity(Gravity.CENTER_HORIZONTAL);
                                                        TextView tvIdProp = new TextView(layout.getContext());
                                                        //tvIdProp.setGravity(Gravity.CENTER_HORIZONTAL);
                                                        TextView tvImgUri = new TextView(layout.getContext());
                                                        //tvImgUri.setGravity(Gravity.CENTER_HORIZONTAL);
                                                        TextView tvPrecio = new TextView(layout.getContext());
                                                        //tvPrecio.setGravity(Gravity.CENTER_HORIZONTAL);
                                                        final TextView tvTitulo = new TextView(layout.getContext());
                                                        //tvTitulo.setGravity(Gravity.CENTER_HORIZONTAL);
                                                        final Button btnLoad = new Button(layout.getContext());
                                                        //btnLoad.setGravity(Gravity.CENTER_HORIZONTAL);
                                                        btnLoad.setText("Cargar mas articulos");
                                                        btnLoad.setBackgroundColor(Color.argb(0, 138, 221, 45));

                                                        String estado = dataSnapshot.child("articulos").child(idProducto).child("estado").getValue().toString();
                                                        String idProp = dataSnapshot.child("articulos").child(idProducto).child("idPropietario").getValue().toString();
                                                        String imgUri = dataSnapshot.child("articulos").child(idProducto).child("imagenUri").getValue().toString();
                                                        String precio = dataSnapshot.child("articulos").child(idProducto).child("precio").getValue().toString();
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
                                                        precio=precio.replace('.',',');
                                                        tvPrecio.setText("Precio: " + precio+"€");
                                                        tvTitulo.setText("Titulo: " + titulo);

                                                        //layout.addView(new TextView(layout.getContext()));
                                                        lImagen.addView(imImagen);
                                                        //layout.addView(new TextView(layout.getContext()));
                                                        layout.addView(tvEstado);
                                                        layout.addView(tvIdProp);
                                                        layout.addView(tvImgUri);
                                                        layout.addView(tvPrecio);
                                                        layout.addView(tvTitulo);
                                                       // layout.addView(new TextView(layout.getContext()));
                                                        //layout.addView(new TextView(layout.getContext()));
                                                        if (cont == numArticulos && it.hasNext())
                                                            layout.addView(btnLoad);
                                                       miVista.addView(lDatos);

                                                        layout.setOnTouchListener(new View.OnTouchListener() {
                                                            @Override
                                                            public boolean onTouch(View v, MotionEvent event) {


                                                                if (event.getAction() == MotionEvent.ACTION_UP && (event.getEventTime() - event.getDownTime() < 1000)) {


                                                                    FragmentManager fragMan = getFragmentManager();
                                                                    FragmentTransaction fragTrans = fragMan.beginTransaction();
                                                                    if (Usuario.getInstance().estaLogueado) {
                                                                        PaginaArticulo pArt = new PaginaArticulo();
                                                                        Bundle b = new Bundle();
                                                                        b.putInt("idArticulo", Integer.parseInt(idProducto));
                                                                        pArt.setArguments(b);
                                                                        fragTrans.replace(R.id.content_frame, pArt);
                                                                        Toast toast = Toast.makeText(getContext(), "Articulo: " + titulo, Toast.LENGTH_SHORT);
                                                                        toast.show();
                                                                    } else {

                                                                        Bundle b = new Bundle();
                                                                        b.putString("FRAG", "BUSQ");

                                                                        //Añadimos la información al intent
                                                                        Fragment fr = new pestaniaLogin();
                                                                        fr.setArguments(b);
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
                                                                numArticulos += 4;
                                                                onViewCreated(view, savedInstanceState);
                                                                return true;
                                                            }
                                                        });
                                                    }
                                                }}

                                            }
                                        }
                                        cont++;
                                        it.next();

                                    }
                                    if (cuento==0){
                                        Toast toast = Toast.makeText(getContext(), "No existe ningun articulo que se asemeje con su criterio de busqueda", Toast.LENGTH_SHORT);
                                        toast.show();
                                        Fragment fragment2 = new pestaniaSetings();
                                        FragmentManager fragmentManager = getFragmentManager();
                                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                        fragmentTransaction.replace(R.id.content_frame, fragment2);
                                        fragmentTransaction.commit();

                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }

                            });
                        }else{
                            if(hayPrecio) {
                                Toast toast = Toast.makeText(getContext(), "El valor del 'precio1' no puede ser mayor que el valor del 'precio2'", Toast.LENGTH_SHORT);
                                toast.show();
                            }

                        }
                    }else if (strPrecio1.equals("")==true&&strPrecio2.toString().equals("")!=true){
                        Toast toast = Toast.makeText(getContext(), "Itroduce el primer valor del precio", Toast.LENGTH_SHORT);
                        toast.show();
                    }else if (strPrecio1.equals("")!=true&&strPrecio2.equals("")==true){
                        Toast toast = Toast.makeText(getContext(), "Itroduce el segundo valor del precio", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }else {
                    Toast toast = Toast.makeText(getContext(), "Itroduce los datos del articulo a buscar ", Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                miVista1.setVisibility(View.VISIBLE);
                miVista.setVisibility(View.INVISIBLE);
                view.findViewById(R.id.sV).setVisibility(View.INVISIBLE);
                Fragment fragment2 = new pestaniaSetings();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment2);
                fragmentTransaction.commit();
            }
        });

    }
    public boolean contieneBusqueda(String producto, String busqueda){
        char[] listaCaracteres = new char[busqueda.length()];
        for (int i=0;i<listaCaracteres.length;i++){
            listaCaracteres[i]=busqueda.charAt(i);
        }

        int contador = 0;
        boolean encontrado =false;
        int contador2 =0;
        for(int i=0; i<producto.length()&&!encontrado;i++){

            if (producto.charAt(i)==listaCaracteres[contador]&&!encontrado){
                if (i!=producto.length()-1 && producto.length()-2!=0){
                contador++;
                boolean sigueCoincidiendo = true;
                if(listaCaracteres.length!=1){
                    for(int j=i+1;j<=producto.length()&& sigueCoincidiendo&&!encontrado;j++){
                        if(producto.charAt(j)!=listaCaracteres[contador]){
                            sigueCoincidiendo=false;
                            contador=0;
                            contador2=0;
                        }else{
                            contador++;
                            contador2++;
                            if(contador2>=2){
                               encontrado=true;
                            }
                        }
                    }
                }
                }}
            }



    return encontrado;
    }
    public boolean estaEnPrecio(float precioBajo, float precioAlto, float precioProducto){
        boolean estaEnElPrecio = false;
        if((precioProducto>precioBajo && precioProducto<precioAlto)||(Float.compare(precioBajo,precioProducto)==0)||(Float.compare(precioAlto,precioProducto)==0)){
            estaEnElPrecio=true;
        }
        return estaEnElPrecio;
    }

}

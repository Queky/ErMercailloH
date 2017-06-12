package com.example.eneko.ermercailloh;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class articulosUsuario extends Fragment {


    SharedPreferences prefs;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference();
    private int numProducto;
    private StorageReference storageRef = storage.getReferenceFromUrl("gs://ermercailloh-7702b.appspot.com");

    public articulosUsuario() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_articulos_usuario, container, false);
        prefs =getActivity().getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        return view;
    }

    public void onViewCreated(final View view, Bundle savedInstanceState) {
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

        final LinearLayout miVista = (LinearLayout) view.findViewById(R.id.LYN);
        final Usuario u1 = Usuario.getInstance();

        //final  String ip = prefs.getString("iP", "10.0.2.2");
        //final String puerto = prefs.getString("puerto","8084");

        dbReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator it = dataSnapshot.child("articulosPorUsuario").child(String.valueOf(u1.getIdusuario())).getChildren().iterator();
                while (it.hasNext()){

                    DataSnapshot ds = (DataSnapshot) it.next();
                    numProducto = Integer.valueOf(ds.getKey());
                    String estado = dataSnapshot.child("articulos").child(String.valueOf(numProducto)).child("estado").getValue().toString();
                    if(estado.equals("abierto")){
                    final LinearLayout lDatos = new LinearLayout(miVista.getContext());

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT
                    );
                    params.setMargins(10, 10, 10,10);
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
                    TextView tvIdProp= new TextView(layout.getContext());
                    TextView tvImgUri = new TextView(layout.getContext());
                    TextView tvPrecio = new TextView(layout.getContext());
                    final TextView tvTitulo = new TextView(layout.getContext());
                    TextView tvLine = new TextView(layout.getContext());


                    String idProp = dataSnapshot.child("articulos").child(String.valueOf(numProducto)).child("idPropietario").getValue().toString();
                    String imgUri = dataSnapshot.child("articulos").child(String.valueOf(numProducto)).child("imagenUri").getValue().toString();
                    String precio = dataSnapshot.child("articulos").child(String.valueOf(numProducto)).child("precio").getValue().toString();
                    float floatValue = Float.parseFloat(precio);
                    String precioComa = String.format("%.2f",floatValue);
                    precioComa = precioComa.replace('.', ',');
                    final String titulo = dataSnapshot.child("articulos").child(String.valueOf(numProducto)).child("titulo").getValue().toString();

                    StorageReference imRef = storageRef.child("images/").child(imgUri);

                    final long MAX_DOWNLOAD_SIZE = 10000000; // 10MB de descarga maxima
                    imRef.getBytes(MAX_DOWNLOAD_SIZE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            bitmap = Bitmap.createScaledBitmap(bitmap, 400, 350, true);
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
                    tvPrecio.setText("Precio: " + precioComa+"€");
                    tvTitulo.setText("Titulo: " + titulo);


                    lImagen.addView(imImagen);
                    layout.addView(tvEstado);
                    layout.addView(tvIdProp);
                    layout.addView(tvImgUri);
                    layout.addView(tvPrecio);
                    layout.addView(tvTitulo);
                    layout.addView(tvLine);
                    miVista.addView(lDatos);

                    final int num = numProducto;
                    layout.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            if(event.getAction() == MotionEvent.ACTION_UP && (event.getEventTime() - event.getDownTime() < 1000)){

                                PaginaArticulo pArt = new PaginaArticulo();
                                pestaniaLogin pLogin = new pestaniaLogin();
                                FragmentManager fragMan = getFragmentManager();
                                FragmentTransaction fragTrans = fragMan.beginTransaction();
                                Bundle b = new Bundle();
                                b.putInt("idArticulo", num);
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

                                Toast toast = Toast.makeText(getContext(), "Articulo: "+titulo, Toast.LENGTH_SHORT);
                                toast.show();
                            }
                            return true;
                        }
                    });
                }}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}


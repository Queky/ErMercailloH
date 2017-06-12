package com.example.eneko.ermercailloh;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.util.Iterator;


public class Pujar extends Fragment {

    private float pujaMaxima;
    private int idArticulo;
    private EditText etPuja;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference();
    private StorageReference storageRef = storage.getReferenceFromUrl("gs://ermercailloh-7702b.appspot.com");

    public Pujar() {
        // Required empty public constructor
    }

    public static Pujar newInstance(String param1, String param2) {
        Pujar fragment = new Pujar();
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
        pujaMaxima = b.getFloat("pujaMaxima");
        idArticulo = b.getInt("idArticulo");
        return inflater.inflate(R.layout.fragment_pujar, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView  etUltimaPuja = (TextView) view.findViewById(R.id.txtUltimaPuja);
        String p = String.format("%.2f",pujaMaxima);
        etUltimaPuja.setText("Debes introducir un valor mayor que "+p+"€");

        etPuja = (EditText) view.findViewById(R.id.txtPuja);
        etPuja.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                boolean incorrecto = false;
                if (keyCode == KeyEvent.KEYCODE_ENTER){
                    String precio = etPuja.getText().toString();

                    if (precio.isEmpty())
                        precio = "0.00";
                    else{
                        float floatValue = Float.parseFloat(precio);
                        precio = String.format("%.2f",floatValue);
                    }
                    if(Float.valueOf(precio) > pujaMaxima) {
                        etPuja.setText(precio);
                        Toast toast = Toast.makeText(getContext(), "El precio introducido es valido", Toast.LENGTH_SHORT);
                        toast.show();
                        incorrecto = false;
                    }
                    else{
                        Toast toast = Toast.makeText(getContext(), "El precio introducido es menor o igual que el precio de la puja mas alta", Toast.LENGTH_SHORT);
                        toast.show();
                        incorrecto = true;
                    }
                }
                return incorrecto;
            }
        });

        Button btnPuja = (Button) view.findViewById(R.id.btnConfirmarPuja);
        btnPuja.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final float precio = Float.valueOf(etPuja.getText().toString());
                final boolean[] incorrecto = {true};
                if(!String.valueOf(precio).isEmpty() && precio > pujaMaxima) {
                    if (precio > pujaMaxima || precio != 0.0f) {
                        final String idArt = String.valueOf(idArticulo);
                        final String idUsuario = String.valueOf(Usuario.getInstance().getIdusuario());
                        // Meter en tabla puja
                        dbReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                Iterator it = null;
                                if (dataSnapshot.child("pujadoPor").child(idArt).exists()) {
                                    it = dataSnapshot.child("pujadoPor").child(idArt).getChildren().iterator();
                                    it.next();
                                } else {
                                    dbReference.child("pujadoPor").child(idArt).child(idUsuario).child("1").setValue(precio);
                                    dbReference.child("pujadoPor").child(idArt).child("0").setValue(precio);
                                }

                                if (!dataSnapshot.child("puja").child(idUsuario).exists())
                                    dbReference.child("puja").child(idUsuario).child(idArt).child("1").setValue(precio);

                                if (it != null) {
                                    while (it.hasNext()) {
                                        DataSnapshot ds = (DataSnapshot) it.next();
                                        if (ds.exists()) {
                                            int numPujas;
                                            if (dataSnapshot.child("pujadoPor").child(idArt).child(idUsuario).exists()){
                                                numPujas = (int) dataSnapshot.child("pujadoPor").child(idArt).child(idUsuario).getChildrenCount() + 1;
                                                dbReference.child("pujadoPor").child(idArt).child(idUsuario).child(String.valueOf(numPujas)).setValue(etPuja.getText().toString());
                                                dbReference.child("pujadoPor").child(idArt).child("0").setValue(etPuja.getText().toString());
                                            }

                                            if (dataSnapshot.child("puja").child(idUsuario).child(idArt).exists()){
                                                numPujas = (int) dataSnapshot.child("puja").child(idUsuario).child(idArt).getChildrenCount() + 1;
                                                dbReference.child("puja").child(idUsuario).child(idArt).child(String.valueOf(numPujas)).setValue(precio);
                                            }
                                        }
                                    }
                                }
                                Fragment pInicio = new pestaniaInicio();
                                FragmentManager fragmentManager = getFragmentManager();
                                FragmentTransaction fragTrans = fragmentManager.beginTransaction();
                                NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
                                navigationView.getMenu().findItem(R.id.Pagina_Inicio).setChecked(true);
                                fragTrans.replace(R.id.content_frame, pInicio);
                                Toast toast = Toast.makeText(getContext(), "A pujado correctamente por el articulo", Toast.LENGTH_SHORT);
                                toast.show();
                                fragTrans.commit();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    } else {
                        Toast toast = Toast.makeText(getContext(), "El precio introducido es menor o igual que el precio de la puja mas alta", Toast.LENGTH_SHORT);
                        toast.show();
                        incorrecto[0] = true;
                    }
                }else{
                    Toast toast = Toast.makeText(getContext(), "El precio introducido es menor o igual que el precio de la puja mas alta", Toast.LENGTH_SHORT);
                    toast.show();
                }
                return incorrecto[0];
            }
        });
    }
}

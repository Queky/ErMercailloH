package com.example.eneko.ermercailloh;


import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;


/**
 * A simple {@link Fragment} subclass.
 */
public class pestaniaLogin extends Fragment {
    private EditText txtemail;
    private EditText txtcontrasenia;
    private Button b1;
    private Usuario u1;
    private String contrasenia1;
    private ValueEventListener eventListener;
    //SharedPreferences prefs;
    private DatabaseReference bbdd;

    private boolean encontrado = false;
    private boolean entrar =false;

    public pestaniaLogin() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_pestania_login, container, false);
            //prefs =getActivity().getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);


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
                        Bundle e =getArguments();
                        String s = e.getString("FRAG");
                        if(s.equals("BUSQ")) {
                            // Toast.makeText(getActivity(), "Back Pressed", Toast.LENGTH_SHORT).show();
                            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("ErMercailloH");
                            Fragment fragment2 = new pestaniaSetings();
                            FragmentManager fragmentManager = getFragmentManager();
                            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.content_frame, fragment2);
                            NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
                            navigationView.getMenu().findItem(R.id.Pagina_Inicio).setChecked(true);
                            fragmentTransaction.commit();
                            return true;
                        }else {
                            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("ErMercailloH");
                            Fragment fragment2 = new pestaniaInicio();
                            FragmentManager fragmentManager = getFragmentManager();
                            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.content_frame, fragment2);
                            NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
                            navigationView.getMenu().findItem(R.id.Pagina_Inicio).setChecked(true);
                            fragmentTransaction.commit();
                            return true;

                        }
                    }
                }
                return false;
            }
        });

        txtemail = (EditText) view.findViewById(R.id.editTextemail);
        txtcontrasenia = (EditText) view.findViewById(R.id.editTextpaswword);
        b1 =(Button)view.findViewById(R.id.button);
        u1 = Usuario.getInstance();

        //final  String ip = prefs.getString("iP", "10.0.2.2");
        //final String puerto = prefs.getString("puerto","8084");

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        bbdd = database.getReference().child("usuarios");


        b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                //bbdd.addValueEventListener(eventListener);


                bbdd.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        long valor = dataSnapshot.getChildrenCount();

                        for (int itera = 1; itera <= valor && !encontrado; itera++) {
                            final String email = dataSnapshot.child("" + itera + "").child("email").getValue().toString();
                            final String password = dataSnapshot.child("" + itera + "").child("password").getValue().toString();

                            if (txtcontrasenia.getText().toString().equals(password)&& txtemail.getText().toString().equals(email)) {
                                encontrado = true;
                                entrar = true;
                                u1.setEmail(email);
                                u1.setPassword(password);
                                u1.setNombre(dataSnapshot.child("" + itera + "").child("nombre").getValue().toString());
                                u1.setIdUsusario(Integer.parseInt(dataSnapshot.child("" + itera + "").getKey()));
                            } else {
                                encontrado = false;
                            }

                        }
                        if (encontrado) {
                            //Creamos el Intent
                            u1.estaLogueado = true;
                            Intent i = new Intent(getActivity(), MainActivity.class);
                            //Creamos la información a pasar entre actividades
                            Bundle b = new Bundle();
                            b.putString("NOMBRE", u1.nombre);

                            //Añadimos la información al intent
                            i.putExtras(b);
                            Toast toast = Toast.makeText(getContext(), "Bienvenido " + u1.getNombre() +  ".", Toast.LENGTH_SHORT);
                            toast.show();

                            startActivity(i);
                            getActivity().finish();

                        } else {
                            Toast toast = Toast.makeText(getContext(), "El usuario o contraseña no son correctas.", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //Log.e(TAGLOG, "Error!", databaseError.toException());
                    }
                });


            }
                });




    }}


package com.example.eneko.ermercailloh;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

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
    EditText txtemail;
    EditText txtcontrasenia;
    Button b1;
    Usuario u1;
    String contrasenia1;
    SharedPreferences prefs;
    public pestaniaLogin() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_pestania_login, container, false);
            prefs =getActivity().getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);


        return view;




    }
    public void onViewCreated(View view, Bundle savedInstanceState) {
        txtemail = (EditText) view.findViewById(R.id.editTextemail);


        final  String ip = prefs.getString("iP", "55555");
       final String puerto = prefs.getString("puerto","555555");
        txtcontrasenia = (EditText) view.findViewById(R.id.editTextpaswword);

        b1 =(Button)view.findViewById(R.id.button);
        u1 = Usuario.getInstance();
        b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://"+ip+":"+puerto+"/erMercailloHSW/rest/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                Servicio service = retrofit.create(Servicio.class);

                //Call<Usuario> call = service.getUsuario(1);
                Call<List<Usuario>> call = service.findByEmailAndPass(txtemail.getText().toString(),txtcontrasenia.getText().toString());

                call.enqueue(new Callback<List<Usuario>>() {
                    @Override
                    public void onResponse(Response<List<Usuario>> response, Retrofit retrofit) {

                        try {

                            List<Usuario> ListaUsuarios = response.body();

                            for (int i = 0; i < ListaUsuarios.size(); i++) {


                                    u1.setAtri(ListaUsuarios.get(i).getIdusuario(),ListaUsuarios.get(i).getNombre(),
                                            ListaUsuarios.get(i).getApellido(),ListaUsuarios.get(i).getEmail(),ListaUsuarios.get(i).getPassword());
                                   //textViewToChange.setText(StudentData.get(i).getNombre());



                            }
                            if(txtcontrasenia.getText().toString().equals(u1.getPassword())){
                                //Creamos el Intent
                                u1.estaLogueado=true;
                                Intent i=new Intent(getActivity(),MainActivity.class);
                                //Creamos la información a pasar entre actividades
                                Bundle b = new Bundle();
                                b.putString("NOMBRE", u1.nombre);

                                //Añadimos la información al intent
                                i.putExtras(b);
                                startActivity(i);

                            }

                        } catch (Exception e) {
                            //.d("onResponse", "There is an error");
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(Throwable t) {

                        // Log.d("onFailure", t.toString());
                    }
                });

            }
        });


    }
}

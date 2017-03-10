package com.example.eneko.ermercailloh;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

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
    public pestaniaLogin() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_pestania_login, container, false);


        return view;




    }
    public void onViewCreated(View view, Bundle savedInstanceState) {
        txtemail = (EditText) view.findViewById(R.id.editTextemail);

        txtcontrasenia = (EditText) view.findViewById(R.id.editTextpaswword);

        b1 =(Button)view.findViewById(R.id.button);
        u1 = Usuario.getInstance();
        b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://10.0.2.2:8084/erMercailloHSW/rest/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                Servicio service = retrofit.create(Servicio.class);

                Call<Usuario> call = service.getUsuario(1);

                call.enqueue(new Callback<Usuario>() {
                    @Override
                    public void onResponse(Response<Usuario> response, Retrofit retrofit) {

                        try {
                            u1.setAtri(response.body().getIdUsusario(),response.body().getNombre(),
                                    response.body().getApellido(),response.body().getEmail(),response.body().getPassword());



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
            }
        });


    }
}

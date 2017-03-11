package com.example.eneko.ermercailloh;


import android.content.Intent;
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
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class subirArticulo extends Fragment {
    Button b1;
    Usuario u1;
    EditText txtNombre;
    EditText txtDescripcion;
    EditText txtFechaApertura;
    EditText txtFechaCierre;

    public subirArticulo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_subir_articulo, container, false);
        return view;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        txtNombre = (EditText) view.findViewById(R.id.editNombreSA);

        txtDescripcion= (EditText) view.findViewById(R.id.editDesSA);
        txtFechaApertura = (EditText) view.findViewById(R.id.editFaperSA);
        txtFechaCierre= (EditText) view.findViewById(R.id.editFcierreSA);



        b1 =(Button)view.findViewById(R.id.buttonSubirArt);

        u1 = Usuario.getInstance();
        b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://10.0.2.2:8084/erMercailloHSW/rest/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                Servicio service = retrofit.create(Servicio.class);
                //String f1 = txtFechaApertura.getText().toString();

                //Call<Usuario> call = service.getUsuario(1);
               // Producto p1 = new Producto(txtNombre.getText().toString(), txtDescripcion.getText().toString(),u1.getIdusuario(),
                 //       "1489186800000" ,"1489532400000");
                Producto prueba = new Producto(55,"sdff","aedfaf",0,0,u1.getIdusuario(),1,new Date("2017-03-11 00:00:00") ,new Date("2017-03-15 00:00:00"));
                Call<Producto> call = service.create(prueba);

                call.enqueue(new Callback<Producto>() {
                    @Override
                    public void onResponse(Response<Producto> response, Retrofit retrofit) {

                        try {

                          Producto producto = response.body();

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


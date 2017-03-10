package com.example.eneko.ermercailloh;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import retrofit.*;

/**
 * A simple {@link Fragment} subclass.
 */
public class datosGet extends Fragment {
    EditText txtID;
    EditText txtcontrasenia;
    Button b1;
    Usuario u1;

    public datosGet() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_datos_get, container, false);


        return view;
    }
    public void onViewCreated(View view, Bundle savedInstanceState) {
        txtID = (EditText) view.findViewById(R.id.editTextemail);

        final TextView textViewToChange = (TextView) view.findViewById(R.id.nombrete);

        b1 =(Button)view.findViewById(R.id.buttonGET);
        u1 = Usuario.getInstance();
        b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("http://10.0.2.2:8084/erMercailloHSW/rest/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    Servicio service = retrofit.create(Servicio.class);


                //Call<Usuario> call = service.findByEmailAndPass("eko44@gmail.com","123456");
               // Call<Usuario> call = service.getUsuario(1);
                Call<List<Usuario>> call = service.findByEmailAndPass("eko44@gmail.com","123456");

                call.enqueue(new Callback<List<Usuario>>() {
                    @Override
                    public void onResponse(Response<List<Usuario>> response, Retrofit retrofit) {

                        try {

                            List<Usuario> StudentData = response.body();

                            for (int i = 0; i < StudentData.size(); i++) {

                                if (i == 0) {
                                    textViewToChange.setText(StudentData.get(i).getNombre());
                                } else if (i == 1) {

                                }
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

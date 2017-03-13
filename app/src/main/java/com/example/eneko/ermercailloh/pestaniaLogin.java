package com.example.eneko.ermercailloh;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class pestaniaLogin extends Fragment {


    public pestaniaLogin() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_pestania_login, container, false);
        final EditText txtemail = (EditText) getView().findViewById(R.id.emailt);
        final EditText txtcontrasenia = (EditText) getView().findViewById(R.id.paswordt);
        Button b1 =(Button)getView().findViewById(R.id.button);
        final Usuario u1 = Usuario.getInstance();
        b1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                if (txtemail.getText().toString().equals(u1.email) && txtcontrasenia.getText().toString().equals(u1.contraseña)) {
                   u1.estaLogueado=true;




                }


            }
        });

        return view;




    }

}

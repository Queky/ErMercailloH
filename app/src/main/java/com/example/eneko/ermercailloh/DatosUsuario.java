package com.example.eneko.ermercailloh;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class DatosUsuario extends Fragment {


    public DatosUsuario() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_datos_usuario, container, false);

        return view;
    }
    public void onViewCreated(View view, Bundle savedInstanceState) {

        Usuario u1 = Usuario.getInstance();
       // TextView TextId = (TextView) view.findViewById(R.id.tIDusuario);

      //  TextView TextNombreUsuario = (TextView) view.findViewById(R.id.tNombreu);
       // TextView TextEmail = (TextView) view.findViewById(R.id.tEmailu);

        ((TextView) view.findViewById(R.id.tIDusuario)).setText(Integer.toString(u1.getIdusuario()));
        ((TextView) view.findViewById(R.id.tNombreu)).setText(u1.getNombre()+" "+u1.getApellido());
        ((TextView) view.findViewById(R.id.tEmailu)).setText(u1.getEmail());

       // TextId.setText(u1.getIdUsusario());
        //TextNombreUsuario.setText(u1.getNombre()+" "+u1.getApellido());
       // TextEmail.setText(u1.getEmail());
    }
}

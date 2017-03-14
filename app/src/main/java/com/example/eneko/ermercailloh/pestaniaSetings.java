package com.example.eneko.ermercailloh;


import android.content.Context;
import android.content.SharedPreferences;
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
public class pestaniaSetings extends Fragment {


    public pestaniaSetings() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_pestania_setings, container, false);

        return view;
    }
    public void onViewCreated(View view, Bundle savedInstanceState) {

       final EditText txtIP = (EditText) view.findViewById(R.id.editTextDireccionIP);
      final   EditText txtPuerto = (EditText) view.findViewById(R.id.editTextPuerto);
     // final SharedPreferences prefs =
               // this.getActivity().getSharedPreferences("MisPreferencias",Context.MODE_PRIVATE);


        final Button b1 =(Button)view.findViewById(R.id.buttonGuardar);
        b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                final SharedPreferences prefs =
                        getActivity().getSharedPreferences("MisPreferencias",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("iP", txtIP.getText().toString());
                editor.putString("puerto", txtPuerto.getText().toString());
                editor.commit();

                Toast toast = Toast.makeText(getContext(), "Se han actualizado los datos: IP: "+txtIP.getText().toString()+" Puerto: "+ txtPuerto.getText().toString(), Toast.LENGTH_SHORT);
                toast.show();
            }
        });

    }
}

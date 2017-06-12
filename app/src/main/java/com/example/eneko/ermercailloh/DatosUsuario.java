package com.example.eneko.ermercailloh;


import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
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

        Usuario u1 = Usuario.getInstance();
       // TextView TextId = (TextView) view.findViewById(R.id.tIDusuario);

      //  TextView TextNombreUsuario = (TextView) view.findViewById(R.id.tNombreu);
       // TextView TextEmail = (TextView) view.findViewById(R.id.tEmailu);

        ((TextView) view.findViewById(R.id.tIDusuario)).setText(Integer.toString(u1.getIdusuario()));
        ((TextView) view.findViewById(R.id.tNombreu)).setText(u1.getNombre());
        ((TextView) view.findViewById(R.id.tEmailu)).setText(u1.getEmail());

       // TextId.setText(u1.getIdUsusario());
        //TextNombreUsuario.setText(u1.getNombre()+" "+u1.getApellido());
       // TextEmail.setText(u1.getEmail());
    }
}

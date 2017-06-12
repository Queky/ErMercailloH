package com.example.eneko.ermercailloh;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.FragmentTransaction;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class subirArticulo extends Fragment {

    Button b1;
    Button btnImgUpload;
    Usuario u1;
    EditText txtNombre;
    EditText txtDescripcion;
    EditText txtFechaApertura;
    EditText txtFechaCierre;
    EditText txtPrecio;
    SharedPreferences prefs;
    String encoded = "base64,";
    DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference imgUpload = storage.getReferenceFromUrl("gs://ermercailloh-7702b.appspot.com");
    int idProducto;
    Uri imgUri;
    UploadTask uploadTask;

    // He tenido crear esto para que se pueda mostrar la imagen en la app al elegirla
    View view;

    private int PICK_IMAGE_REQUEST = 1;
    private int TAKE_IMAGE_REQUEST = 0;

    public subirArticulo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_subir_articulo, container, false);
        prefs = getActivity().getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        this.view = view;
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
                        // Toast.makeText(getActivity(), "Back Pressed", Toast.LENGTH_SHORT).show();
                        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("ErMercailloH");
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
                return false;
            }
        });

        txtNombre = (EditText) view.findViewById(R.id.editNombreSA);
        txtDescripcion = (EditText) view.findViewById(R.id.editDesSA);
        txtPrecio = (EditText) view.findViewById(R.id.txtPrecio);
        txtPrecio.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER){
                    String precio = txtPrecio.getText().toString();

                    if (precio.isEmpty())
                        precio = "0.00";
                    else{
                        float floatValue = Float.parseFloat(precio);
                        precio = String.format("%.2f",floatValue);
                    }
                    txtPrecio.setText(precio);
                }
                return false;
            }
        });

        //txtFechaApertura = (EditText) view.findViewById(R.id.editFaperSA);
        txtFechaCierre = (EditText) view.findViewById(R.id.editFcierreSA);
        final EditText txtHoraCierre = (EditText) view.findViewById(R.id.editHCierreSA);
        final Calendar calendar = new GregorianCalendar();
        final Calendar calendarActual = new GregorianCalendar();
        prefs = getActivity().getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);

        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                idProducto = Integer.parseInt(dataSnapshot.child("articulos").child("0").child("numeroId").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        b1 = (Button) view.findViewById(R.id.buttonSubirArt);
        final View.OnClickListener dateListener = new View.OnClickListener() {
            final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, monthOfYear);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    String myFormat = "dd/MM/yyyy"; //In which you need put here
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                    txtFechaCierre.setText(sdf.format(calendar.getTime()));
                }
            };

            public void onClick(View v) {
                txtFechaCierre.setFocusable(false);
                DatePickerDialog dialog = new DatePickerDialog(getContext(), date, calendarActual.get(
                        Calendar.YEAR), calendarActual.get(Calendar.MONTH), calendarActual.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        };

        txtFechaCierre.setOnClickListener(dateListener);

        final View.OnClickListener hourListener = new View.OnClickListener() {
            final TimePickerDialog.OnTimeSetListener hour = new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute);
                    String myFormat = "HH:mm"; //In which you need put here
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                    txtHoraCierre.setText(sdf.format(calendar.getTime()));
                }
            };

            public void onClick(View v) {
                txtHoraCierre.setFocusable(false);
                TimePickerDialog dialog = new TimePickerDialog(getContext(), hour, calendarActual.get(
                        Calendar.HOUR_OF_DAY), calendarActual.get(Calendar.MINUTE), true);
                dialog.show();
            }
        };

        txtHoraCierre.setOnClickListener(hourListener);
        u1 = Usuario.getInstance();
        b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(!txtPrecio.getText().toString().isEmpty() && !txtNombre.getText().toString().isEmpty() && !txtDescripcion.getText().toString().isEmpty()
                        && txtPrecio.getText().length() >= 3 && txtNombre.getText().length() >= 3 && txtDescripcion.getText().length() >= 3) {
                    dbReference.child("articulos").child(String.valueOf(idProducto)).child("estado").setValue("abierto");
                    dbReference.child("articulos").child(String.valueOf(idProducto)).child("idPropietario").setValue(Usuario.getInstance().getIdusuario());
                    dbReference.child("articulos").child(String.valueOf(idProducto)).child("imagenUri").setValue("Producto" + String.valueOf(idProducto));
                    dbReference.child("articulos").child(String.valueOf(idProducto)).child("precio").setValue(txtPrecio.getText().toString());
                    dbReference.child("articulos").child(String.valueOf(idProducto)).child("titulo").setValue(txtNombre.getText().toString());
                    dbReference.child("articulos").child(String.valueOf(idProducto)).child("descripcion").setValue(txtDescripcion.getText().toString());
                    dbReference.child("articulos").child("0").child("numeroId").setValue(String.valueOf(idProducto + 1));
                    dbReference.child("articulosPorUsuario").child(String.valueOf(Usuario.getInstance().getIdusuario())).child(String.valueOf(idProducto)).setValue(txtNombre.getText().toString());
                    if(imgUri != null) {
                        StorageReference imgRef = imgUpload.child("images/" + "Producto" + String.valueOf(idProducto));
                        uploadTask = imgRef.putFile(imgUri);
                    }

                    Toast toast = Toast.makeText(getContext(), "Se subio el articulo " + txtNombre.getText().toString() + " correctamente.", Toast.LENGTH_SHORT);
                    toast.show();

                    pestaniaInicio pInicio = new pestaniaInicio();
                    FragmentManager fragMan = getFragmentManager();
                    android.support.v4.app.FragmentTransaction fragTrans = fragMan.beginTransaction();
                    fragTrans.replace(R.id.content_frame, pInicio);
                    fragTrans.commit();
                }else{
                    Toast toast = Toast.makeText(getContext(), "Los campos deben tener al menos 3 caracteres", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });


        btnImgUpload = (Button) view.findViewById(R.id.buttonSubirImg);
        btnImgUpload.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                PickSetup pSetup = new PickSetup()
                        .setTitle("Saca o elige una imagen")
                        .setCameraButtonText("Camara")
                        .setGalleryButtonText("Galeria")
                        .setCancelText("Cancelar");

                PickImageDialog.build(pSetup)
                        .setOnPickResult(new IPickResult() {
                            @Override
                            public void onPickResult(PickResult r) {
                                if (r.getError() == null) {
                                    imgUri = r.getUri();

                                    ImageView imageView = (ImageView) view.findViewById(R.id.imagenArticulo);
                                    if(r.getBitmap() != null)
                                        imageView.setImageBitmap(r.getBitmap());
                                }
                            }
                        }).show(getFragmentManager());
            }
        });
    }
}
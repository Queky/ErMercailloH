package com.example.eneko.ermercailloh;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ImageView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;
import java.util.Date;
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
    SharedPreferences prefs;
    String encoded = "base64,";

    // He tenido crear esto para que se pueda mostrar la imagen en la app al elegirla
    View view;

    private int PICK_IMAGE_REQUEST = 1;

    public subirArticulo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_subir_articulo, container, false);
        prefs =getActivity().getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        this.view = view;
        return view;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        txtNombre = (EditText) view.findViewById(R.id.editNombreSA);

        txtDescripcion= (EditText) view.findViewById(R.id.editDesSA);

        //txtFechaApertura = (EditText) view.findViewById(R.id.editFaperSA);
        txtFechaCierre= (EditText) view.findViewById(R.id.editFcierreSA);
        final EditText txtHoraCierre = (EditText) view.findViewById(R.id.editHCierreSA);
        final Calendar calendar = new GregorianCalendar();
        final Calendar calendarActual = new GregorianCalendar();
        prefs =getActivity().getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);

        final  String ip = prefs.getString("iP", "10.0.2.2");
        final String puerto = prefs.getString("puerto","8084");

        b1 =(Button)view.findViewById(R.id.buttonSubirArt);
        final View.OnClickListener dateListener = new View.OnClickListener() {
            final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    // TODO Auto-generated method stub
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
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://"+ip+":"+puerto+"/erMercailloHSW/rest/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                final Servicio service = retrofit.create(Servicio.class);

                Producto p1 = new Producto(0, txtNombre.getText().toString(), txtDescripcion.getText().toString(),
                        encoded,1,0,0,1, Calendar.getInstance().getTimeInMillis(), calendar.getTimeInMillis());
                //Log.d("subida imagen producto", encoded);
                Call<Producto> call = service.create(p1);

                Toast toast = Toast.makeText(getContext(), "Se subio el articulo "+txtNombre.getText().toString()+" correctamente.", Toast.LENGTH_SHORT);
                toast.show();
                call.enqueue(new Callback<Producto>() {
                    @Override
                    public void onResponse(Response<Producto> response, Retrofit retrofit) {

                        try {


                        } catch (Exception e) {
                            //Log.d("onResponse", "There is an error");
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

        btnImgUpload = (Button)view.findViewById(R.id.buttonSubirImg);
        btnImgUpload.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://"+ip+":"+puerto+"/erMercailloHSW/rest/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                Servicio service = retrofit.create(Servicio.class);

                Intent intent = new Intent();
                // Show only images, no videos or anything else
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                // Always show the chooser (if there are multiple options available)
                startActivityForResult(Intent.createChooser(intent, "Elige una imagen"), PICK_IMAGE_REQUEST);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                // Mostrar imagen
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));
                ImageView imageView = (ImageView)view.findViewById(R.id.imagenArticulo);
                imageView.setImageBitmap(bitmap);
                // Encode to base64
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream .toByteArray();
                encoded += Base64.encodeToString(byteArray, Base64.DEFAULT);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


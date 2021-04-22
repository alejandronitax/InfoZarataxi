package com.example.infozarataxi;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

import static com.example.infozarataxi.MyAsyncTask.hideProgress;

public class MainActivity extends AppCompatActivity implements
        MyDialogFragment.MyDialogoListener,MailDialogFragment.MyDialogoListener, CloseFragment.MyCloseDialogoListener, PriceDialogFragment.MiDialogoListener2, NavigationView.OnNavigationItemSelectedListener,
        DrawerLayout.DrawerListener {

    private FusedLocationProviderClient fusedLocationClient;
    private Context context;
    private CircleImageView btnMap;
    private TextView textView;

    private LocationManager locationManager;
    private AlertDialog alert;

    private DrawerLayout drawerLayout;

    private Intent intent;
    private SharedPreferences prefe;
    private DialogFragment nuevoFragmento;

    private SharedPreferences.Editor editor;
    private boolean respuesta;
    private View parentLayout;

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnMap = findViewById(R.id.btnMap);
        btnMap.setTag(Constants.textButton1);
        textView = findViewById(R.id.indicaBoton);
        textView.setText(Constants.textButton1);

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        drawerLayout.addDrawerListener(this);
        context = MainActivity.this;
        parentLayout = findViewById(android.R.id.content);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);//initi
        checkPermission();

        prefe = getSharedPreferences("preferences", Context.MODE_PRIVATE);
        editor = prefe.edit();

        if (!Objects.requireNonNull(prefe.getString("licencia", "")).isEmpty()) {

            Constants.setLicenseValue(prefe.getString("licencia", ""));
            Constants.setEmailValue(prefe.getString("email", ""));

            if (getEstado() == 0) {

                setEstado(1);


            } else {

                textAndState(getEstado());

            }

        } else {

            intentInsertCabbie();

        }


    }


    private void intentInsertCabbie() {

        intent = new Intent(this, InsertCabbie.class);
        startActivity(intent);

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onResume() {
        super.onResume();

        btnMap.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        changeImagen(Constants.accionDown, 0);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        changeImagen(Constants.accionUp, 0);
                        break;
                }
                return false;
            }
        });

        btnMap.setOnClickListener(new SingleClickListener() {
            @Override
            public void performClick(View view) {
                if (checkPermission() && checkConexionInternet()) {

                    switch (getEstado()) {
                        case 1:

                            lanzarDialogoTipoServicio("miDialogo");

                            break;

                        case 2:

                            getLocation(getEstado());
                            textAndState(3);

                            break;

                        case 3:

                            getLocation(getEstado());
                            lanzarDialogoTipoServicio("precio");

                            break;

                    }

                }

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (alert != null)
            alert.dismiss();

        if (nuevoFragmento != null) {
            nuevoFragmento.dismiss();
        }

        hideProgress();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (alert != null)
            alert.dismiss();

        if (nuevoFragmento != null) {
            nuevoFragmento.dismiss();
        }

        hideProgress();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (!prefe.getString("salidaTrip", "").isEmpty()) {
            textAndState(getEstado());
        }

    }

    private void getLocation(final int loca) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, "Le falta aceptar algún permiso", Toast.LENGTH_SHORT).show();
        } else {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                //check the cordantes

                                String longitude = Double.toString(location.getLongitude());
                                String latitude = Double.toString(location.getLatitude());

                                longitude = longitude.replace(".", ",");
                                latitude = latitude.replace(".", ",");

                                Calendar c = Calendar.getInstance();
                                @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("dd-MM-yy HH:mm:ss");
                                String formattedDate = df.format(c.getTime());
                                // formattedDate have current date/time

                                switch (loca) {
                                    case 1:

                                        editor.putString("salidaTrip", formattedDate + "#" + latitude + "#" + longitude);

                                        if (btnMap.getTag().toString().equalsIgnoreCase(Constants.textButton3)) {

                                            editor.putString("recogidaTrip", formattedDate + "#" + latitude + "#" + longitude);

                                        }

                                        break;
                                    case 2:

                                        editor.putString("recogidaTrip", formattedDate + "#" + latitude + "#" + longitude);

                                        break;

                                    case 3:

                                        editor.putString("finalizacionTrip", formattedDate + "#" + latitude + "#" + longitude);

                                        break;
                                }

                                editor.commit();

                            } else {

                                Snackbar.make(parentLayout, R.string.location_not_found, Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Snackbar.make(parentLayout, R.string.erro_location, Snackbar.LENGTH_SHORT).show();
                        }
                    });
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, R.string.permission_success, Toast.LENGTH_SHORT).show();
            } else {
                //Si deniega los permisos.
                Snackbar.make(parentLayout, R.string.not_permission, Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    private boolean checkPermission() {

        respuesta = false;

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                alertNotGps();

            } else {

                respuesta = true;

            }

        } else {
            //request for the user to give the consent to access
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        100);

            }

            respuesta = false;

        }

        return respuesta;

    }

    private boolean checkConexionInternet() {

        respuesta = false;

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            // Si hay conexión a Internet en este momento
            respuesta = true;

        } else {
            // No hay conexión a Internet en este momento
            Snackbar.make(parentLayout, R.string.off_conexion, Snackbar.LENGTH_SHORT).show();
            respuesta = false;
        }
        return respuesta;

    }


    private void alertNotGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ubicación desactivada")
                .setMessage("El sistema Gps esta desactivado, tiene que activarlo para poder continuar")
                .setCancelable(false)
                .setPositiveButton(R.string.alert_button_message, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();

                    }
                });

        alert = builder.create();
        alert.show();

    }

    @Override
    public void onDialogStreetServicesClick() {
        if (checkPermission()) {
            getLocation(getEstado());
            textAndState(3);

        } else {
            textAndState(1);
        }

    }


    @Override
    public void onDialogStationServicesClick() {
        if (checkPermission()) {
            getLocation(getEstado());
            textAndState(2);

        } else {
            textAndState(1);
        }
        //changeImagen(Constants.sinAccion, R.drawable.recogidaclienteblue);
    }

    @Override
    public void cancelTrip() {

        textAndState(1);
    }

    private void textAndState(int i) {
        switch (i) {
            case 1:
                btnMap.setTag(Constants.textButton1);
                textView.setText(Constants.textButton1);
                changeImagen(Constants.sinAccion, R.drawable.taxigreen);
                break;
            case 2:
                btnMap.setTag(Constants.textButton2);
                textView.setText(Constants.textButton2);
                changeImagen(Constants.sinAccion, R.drawable.recogidaclienteblue);
                break;
            case 3:
                btnMap.setTag(Constants.textButton3);
                textView.setText(Constants.textButton3);
                changeImagen(Constants.sinAccion, R.drawable.finalizacarrera);
                break;
        }

        setEstado(i);

    }

    @Override
    public void acepptTrip(String price) {

        String salida = prefe.getString("salidaTrip", "");
        String recogida = prefe.getString("recogidaTrip", "");
        String finaliza = prefe.getString("finalizacionTrip", "");

        if (checkConexionInternet() && !salida.isEmpty() && !recogida.isEmpty() && !finaliza.isEmpty()) {

            Trip trip = new Trip();
            int idCarreraInterna = prefe.getInt("idCarrera", 1);
            // Llenado de trip
            trip.setCarreraInterna(idCarreraInterna);
            String[] arrayDateLatLong = getArrayDateLatLong(salida);

            trip.setSalidaFechahora(arrayDateLatLong[0]);
            trip.setSalidaLatitud(arrayDateLatLong[1]);
            trip.setSalidaLonguitud(arrayDateLatLong[2]);

            arrayDateLatLong = getArrayDateLatLong(recogida);

            trip.setRecogidaFechahora(arrayDateLatLong[0]);
            trip.setRecogidaLatitud(arrayDateLatLong[1]);
            trip.setRecogidaLonguitud(arrayDateLatLong[2]);

            arrayDateLatLong = getArrayDateLatLong(finaliza);

            trip.setFinalizacionFechahora(arrayDateLatLong[0]);
            trip.setFinalizacionLatitud(arrayDateLatLong[1]);
            trip.setFinalizacionLonguitud(arrayDateLatLong[2]);

            trip.setPrecioFinal(price);
            trip.guardarTrip(context, trip);

            trip.enviarTrips(context);
            idCarreraInterna += 1;
            editor.putInt("idCarrera", idCarreraInterna );

        }

        editor.putString("salidaTrip", "");
        editor.putString("recogidaTrip", "");
        editor.putString("finalizacionTrip", "");
        editor.commit();

        textAndState(1);

    }

    private String[] getArrayDateLatLong(String string) {
        return string.split("#");
    }

    private void lanzarDialogoTipoServicio(String tipoDialogo) {

        if (checkConexionInternet()) {

            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                alertNotGps();

            } else {

                switch (tipoDialogo) {

                    case "miDialogo":

                        nuevoFragmento = new MyDialogFragment();
                        nuevoFragmento.show(getSupportFragmentManager(), "dialogo");

                        break;

                    case "precio":

                        nuevoFragmento = new PriceDialogFragment();
                        nuevoFragmento.show(getSupportFragmentManager(), "dialogo2");

                        break;

                    case "email":

                        nuevoFragmento = new MailDialogFragment();
                        nuevoFragmento.show(getSupportFragmentManager(), "dialogo3");

                        break;
                }

                nuevoFragmento.setCancelable(false);
            }

        }

    }

    @Override
    public void onBackPressed() {

        new CloseFragment().show(getSupportFragmentManager(), "dialogoClose");

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        if (menuItem.getItemId() == R.id.cambiarEmail) {

            if (getEstado() == 1) {

                lanzarDialogoTipoServicio("email");

            } else {

                //    Toast.makeText(context, "Solo puede cambiar el email, si ha terminado la carrera", Toast.LENGTH_SHORT).show();
                Snackbar.make(parentLayout, R.string.email_warning, Snackbar.LENGTH_SHORT).show();
            }

        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(@NonNull View drawerView) {

    }

    @Override
    public void onDrawerClosed(@NonNull View drawerView) {

    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }

    // Cambia el drawable dependiento del estado o dependiendo del drawable
    private void changeImagen(String action, int drawable) {

        int resource = 0;

        switch (action) {
            case Constants.accionDown:

                switch (getEstado()) {
                    case 1:
                        resource = R.drawable.taxigreenpress;
                        break;
                    case 2:
                        resource = R.drawable.recogidaclientebluepress;
                        break;
                    case 3:
                        resource = R.drawable.finalizacarrerapress;
                        break;
                }

                break;

            case Constants.accionUp:

                switch (getEstado()) {
                    case 1:
                        resource = R.drawable.taxigreen;
                        break;
                    case 2:
                        resource = R.drawable.recogidaclienteblue;
                        break;
                    case 3:
                        resource = R.drawable.finalizacarrera;
                        break;
                }

                break;

            case Constants.sinAccion:
                resource = drawable;
                break;

        }

        btnMap.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), resource, null));

    }

    public int getEstado() {

        return prefe.getInt("estado", 1);

    }

    public void setEstado(int i) {

        editor.putInt("estado", i);
        editor.commit();

    }

    @Override
    public void onDialogCloseClick() {
        finish();
    }

    @Override
    public void onDialogAcceptEmail(String email, Boolean validado) {

        String mensaje;

        if(validado){
            VolleyApi.peticionAlServidor(context, Constants.urlAlta, null, null, Constants.licenseValue, email);
            mensaje = "Email actualizado correctamente";

        } else {

            mensaje = "El email "+email+" es un email no valido";

        }

        Snackbar.make(parentLayout, mensaje, Snackbar.LENGTH_SHORT).show();

    }
}
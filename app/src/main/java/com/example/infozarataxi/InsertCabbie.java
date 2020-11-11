package com.example.infozarataxi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.regex.Pattern;

public class InsertCabbie extends AppCompatActivity implements CloseFragment.MyCloseDialogoListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setContentView(R.layout.activity_insert_cabbie);
        final EditText licencia = findViewById(R.id.licencia);
        final EditText email = findViewById(R.id.email);
        Button button = findViewById(R.id.button);

        final Intent intent = new Intent(this, MainActivity.class);
        button.setOnClickListener(new SingleClickListener() {
            @Override
            public void performClick(View v) {

                String dataLicencia = licencia.getText().toString();
                String dataEmail = email.getText().toString();
                Boolean flagLicence, flagEmail = false;

                if (!dataLicencia.isEmpty()) {

                    flagLicence=true;

                    if (dataEmail.isEmpty()) {

                        flagEmail = true;

                    } else {

                        Pattern pattern = Patterns.EMAIL_ADDRESS;

                        if (pattern.matcher(dataEmail).matches()) {

                            flagEmail = true;

                        } else {

                            flagEmail = false;
                            email.setError("Email no válido");

                        }

                    }

                } else {
                    // Mensajes mete numeros melon
                    flagLicence = false;
                    licencia.setError("No puede dejar la licencia vacía");
                }

                if (flagLicence && flagEmail) {
                    Constants.setEmailValue(dataEmail);
                    Constants.setLicenseValue(dataLicencia);
                    new VolleyApi().peticionAlServidor(InsertCabbie.this, Constants.urlAlta, null, intent, dataLicencia, dataEmail);

                }

            }
        });
    }

    @Override
    public void onBackPressed() {

        new CloseFragment().show(getSupportFragmentManager(), "dialogoClose");

    }


    @Override
    public void onDialogCloseClick() {
        finish();
    }

}
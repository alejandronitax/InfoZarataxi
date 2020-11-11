package com.example.infozarataxi;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class MyDialogFragment extends DialogFragment {

    public interface MyDialogoListener {
        void onDialogStationServicesClick();

        void onDialogStreetServicesClick();
    }

    MyDialogoListener miEscuchador;

    // Sobreescribimos el método onAttach() para instanciar el
    //escuchador
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            miEscuchador = (MyDialogoListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement MiDialogoListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_fragment, null);
        View btnAcceptEmail = view.findViewById(R.id.btnEmisora);
        final Button btnCancelPrice = view.findViewById(R.id.btnPuerta);
        builder.setView(view);
        btnAcceptEmail.setOnClickListener(new SingleClickListener() {
            @Override
            public void performClick(View v) {
                miEscuchador.onDialogStationServicesClick();
                dismiss();
            }
        });
        btnCancelPrice.setOnClickListener(new SingleClickListener() {
            @Override
            public void performClick(View v) {
                miEscuchador.onDialogStreetServicesClick();
                dismiss();
            }
        });
        builder.setView(view);
        return builder.create();
    }

}
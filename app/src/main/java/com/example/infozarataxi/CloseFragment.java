package com.example.infozarataxi;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class CloseFragment extends DialogFragment {

    public interface MyCloseDialogoListener {

        void onDialogCloseClick();

    }

    MyCloseDialogoListener miEscuchador;

    // Sobreescribimos el método onAttach() para instanciar el
    //escuchador
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            miEscuchador = (MyCloseDialogoListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement MiDialogoListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.close_fragment, null);
        View btnNo = view.findViewById(R.id.btnNo);

        final Button btnSi = view.findViewById(R.id.btnSi);
        builder.setView(view);
        btnNo.setOnClickListener(new SingleClickListener() {
            @Override
            public void performClick(View v) {
                dismiss();
            }
        });
        btnSi.setOnClickListener(new SingleClickListener() {
            @Override
            public void performClick(View v) {
                miEscuchador.onDialogCloseClick();
                dismiss();
            }
        });
        builder.setView(view);

        return builder.create();
    }

}
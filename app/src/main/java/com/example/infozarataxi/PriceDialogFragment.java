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

import java.text.DecimalFormat;

public class PriceDialogFragment extends DialogFragment {

    private Button btnAcceptPrice;
    private EditText importe;

    public interface MiDialogoListener2 {
        void acepptTrip(String precio);

        void cancelTrip();

    }

    MiDialogoListener2 miEscuchador2;

    @Override
    public void onDetach() {
        super.onDetach();
        miEscuchador2 = null;
    }

    // Sobreescribimos el método onAttach() para instanciar el
    //escuchador
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            miEscuchador2 = (MiDialogoListener2) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement MiDialogoListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.price_dialog_fragment, null);
        importe = view.findViewById(R.id.importe);
        btnAcceptPrice = view.findViewById(R.id.btnAcceptPrice);
        final Button btnCancelPrice = view.findViewById(R.id.btnCancelPrice);

        builder.setView(view);
        builder.setIcon(R.mipmap.ic_launcher);
        btnAcceptPrice.setOnClickListener(new SingleClickListener() {
            @Override
            public void performClick(View v) {
                miEscuchador2.acepptTrip(getPrecioTransformado(importe.getText().toString()));
                dismiss();
            }
        });
        btnCancelPrice.setOnClickListener(new SingleClickListener() {
            @Override
            public void performClick(View v) {
                miEscuchador2.cancelTrip();
                dismiss();
            }
        });

        btnAcceptPrice.setEnabled(false);
        importe.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                // Check if edittext is empty
                if (TextUtils.isEmpty(s) || importe.getText().toString().equals(".")) {
                    // Disable ok button
                    btnAcceptPrice.setEnabled(false);

                } else {
                    // Something into edit text. Enable the button.
                    btnAcceptPrice.setEnabled(true);
                }

            }
        });
        return builder.create();
    }

    private String getPrecioTransformado(String precio) {

        Double valor = Double.parseDouble((precio));

        if (precio.contains(".")) {

            String valueFormat = new DecimalFormat("0.00").format(valor);
            valueFormat = valueFormat.replace(",", ".");
            valor = Double.parseDouble(valueFormat);

        }

        int result = (int) (valor * 100);
        precio = String.valueOf(result);

        return precio;

    }

}

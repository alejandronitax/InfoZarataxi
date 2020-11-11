package com.example.infozarataxi;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.snackbar.Snackbar;

import java.util.regex.Pattern;

public class MailDialogFragment extends DialogFragment {

    private Button btnAcceptEmail;

    public interface MyDialogoListener {
        void onDialogAcceptEmail(String value, Boolean valido);
    }

    MailDialogFragment.MyDialogoListener miEscuchador;

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            miEscuchador = (MailDialogFragment.MyDialogoListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement MiDialogoListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.email_dialog_fragment, null);
        final EditText email = view.findViewById(R.id.email);
        btnAcceptEmail = view.findViewById(R.id.btnAcceptEmail);
        final Button btnCancelPrice = view.findViewById(R.id.btnCancelEmail);

        builder.setView(view);
        btnAcceptEmail.setOnClickListener(new SingleClickListener() {
            @Override
            public void performClick(View v) {
                final String textEmail = email.getText().toString();
                miEscuchador.onDialogAcceptEmail(textEmail,validarEmail(textEmail));
                dismiss();
            }
        });
        btnCancelPrice.setOnClickListener(new SingleClickListener() {
            @Override
            public void performClick(View v) {
                dismiss();
            }
        });
        builder.setView(view);

        btnAcceptEmail.setEnabled(false);
            email.addTextChangedListener(new TextWatcher() {

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
                if (TextUtils.isEmpty(s)) {
                    // Disable ok button
                    btnAcceptEmail.setEnabled(false);

                } else {
                    // Something into edit text. Enable the button.
                    btnAcceptEmail.setEnabled(true);
                }

            }
        });
        return  builder.create();
    }

    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

}

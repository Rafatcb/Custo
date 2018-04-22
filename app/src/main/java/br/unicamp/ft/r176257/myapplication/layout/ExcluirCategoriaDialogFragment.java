package br.unicamp.ft.r176257.myapplication.layout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import br.unicamp.ft.r176257.myapplication.R;

public class ExcluirCategoriaDialogFragment extends DialogFragment {

    private static final int REQUEST_CODE = 2;

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.excluir_categoria)
                .setPositiveButton(R.string.excluir, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Excluir
                        Intent intent = new Intent();
                        intent.putExtra("excluir", true);
                        sendResult(REQUEST_CODE, intent);
                    }
                })
                .setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        Intent intent = new Intent();
                        intent.putExtra("excluir", false);
                        sendResult(REQUEST_CODE, intent);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    private void sendResult(int REQUEST_CODE, Intent intent) {
        getTargetFragment().onActivityResult(
                getTargetRequestCode(), REQUEST_CODE, intent);
    }
}


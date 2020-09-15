package com.physis.foot.stretching.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

public class MyAlertDialog {

    private AlertDialog dialog = null;

    public void show(Context context, String title, View view)
    {
        dismiss();
        AlertDialog.Builder dialogBuilder =  new AlertDialog.Builder(context);
        dialogBuilder.setTitle(title).setView(view)
                .setPositiveButton(android.R.string.cancel, null)
                .setCancelable(false).create();
        dialog = dialogBuilder.show();
    }

    public void show(Context context, String title, View view, DialogInterface.OnClickListener onPositiveButtonClickListener)
    {
        dismiss();
        AlertDialog.Builder dialogBuilder =  new AlertDialog.Builder(context);
        dialogBuilder.setTitle(title).setView(view)
                .setPositiveButton(android.R.string.ok, onPositiveButtonClickListener)
                .setNegativeButton(android.R.string.cancel, null)
                .setCancelable(false).create();
        dialog = dialogBuilder.show();
    }

    public void show(Context context, String title, View view, String btnText, View.OnClickListener onClickListener)
    {
        dismiss();
        AlertDialog.Builder dialogBuilder =  new AlertDialog.Builder(context);
        dialogBuilder.setTitle(title).setView(view)
                .setNegativeButton(btnText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton(android.R.string.cancel, null)
                .setCancelable(false);
        dialog = dialogBuilder.create();
        dialog.show();
        // no dismiss
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(onClickListener);
    }

    public void dismiss(){
        if(dialog != null){
            dialog.dismiss();
            dialog = null;
        }
    }
}

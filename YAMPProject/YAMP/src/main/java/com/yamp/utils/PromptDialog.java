package com.yamp.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.widget.EditText;

/**
 * Created by Lux on 24.12.13.
 */
public class PromptDialog {

    private static String userInput;

    public static String showDialog(Context context, String title) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final EditText inputBox = new EditText(context);
        inputBox.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setTitle(title);
        builder.setView(inputBox);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                userInput = inputBox.getText().toString();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
        return userInput;
    }

    public static String getReply(){
        if(userInput == null)
            return "Empty";
        return userInput;
    }
}

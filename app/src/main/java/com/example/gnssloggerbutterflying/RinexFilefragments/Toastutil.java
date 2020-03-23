package com.example.gnssloggerbutterflying.RinexFilefragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

public class Toastutil {

    private static Toast mToast;

    @SuppressLint("ShowToast")
    public static void showmsg(Context mcontext, String msg){
        if (mToast==null)
        {
            mToast=Toast.makeText(mcontext, msg,Toast.LENGTH_SHORT);

        }else {
            mToast.setText(msg);

        }
        mToast.show();
    }
}

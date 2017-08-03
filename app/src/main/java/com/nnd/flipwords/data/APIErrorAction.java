package com.nnd.flipwords.data;

import android.support.design.widget.Snackbar;
import android.view.View;

import io.reactivex.functions.Consumer;
import retrofit2.HttpException;

import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;

/**
 * Created by Android dev on 8/3/17.
 */

public class APIErrorAction implements Consumer<Throwable> {
    View rootView;

    public APIErrorAction(View rootView) {
        this.rootView = rootView;
    }

    @Override
    public void accept(Throwable t) throws Exception {
        if (t instanceof HttpException) {
            HttpException ex = (HttpException) t;
            Snackbar.make(rootView, "Error (" + ex.code() + ")", Snackbar.LENGTH_INDEFINITE).show();

            switch (ex.code()) {
                case HTTP_UNAUTHORIZED: {
                    //stub
                    break;
                }
                default: {
                    //stub
                    break;
                }
            }
        } else {
            t.printStackTrace();
        }
    }
}

package app.nutrimeat.meat.org.nutrimeat.internetCheck;


import android.app.Application;

import app.nutrimeat.meat.org.nutrimeat.R;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class MyApplication extends Application {

    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Raleway-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }


}
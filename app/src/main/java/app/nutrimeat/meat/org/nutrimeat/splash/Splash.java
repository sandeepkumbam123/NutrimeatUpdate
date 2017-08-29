package app.nutrimeat.meat.org.nutrimeat.splash;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import app.nutrimeat.meat.org.nutrimeat.MainActivity;
import app.nutrimeat.meat.org.nutrimeat.R;

public class Splash extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        setContentView(R.layout.activity_splash);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(Splash.this,MainActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_appear,R.anim.slide_in_disappear);
                finish();
            }
        },2000);
    }

}

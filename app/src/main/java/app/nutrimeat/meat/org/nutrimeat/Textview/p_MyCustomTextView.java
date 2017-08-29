package app.nutrimeat.meat.org.nutrimeat.Textview;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;


public class p_MyCustomTextView extends TextView {

    public p_MyCustomTextView(Context context, AttributeSet arrt) {
        super(context, arrt);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Raleway-Light.ttf"));
    }
}

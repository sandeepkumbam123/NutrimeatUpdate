package app.nutrimeat.meat.org.nutrimeat.Recipies;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import app.nutrimeat.meat.org.nutrimeat.ApiClient;
import app.nutrimeat.meat.org.nutrimeat.R;
import app.nutrimeat.meat.org.nutrimeat.Textview.p_MyCustomTextView_bold;
import app.nutrimeat.meat.org.nutrimeat.Textview.p_MyCustomTextView_regular;
import app.nutrimeat.meat.org.nutrimeat.api.API;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class RecipiesDetailsActivity extends AppCompatActivity {

    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipies_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("");
        position = getIntent().getIntExtra("recipi_id", 1);
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setExpandedTitleTextAppearance(R.style.expandedappbar);
        collapsingToolbar.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
        final ImageView bg_image = (ImageView) findViewById(R.id.product_image);
        final p_MyCustomTextView_bold product_name = (p_MyCustomTextView_bold) findViewById(R.id.product_name);
        final p_MyCustomTextView_regular time_pre = (p_MyCustomTextView_regular) findViewById(R.id.time_pre);
        final p_MyCustomTextView_regular serving = (p_MyCustomTextView_regular) findViewById(R.id.serving);
        final p_MyCustomTextView_regular cals = (p_MyCustomTextView_regular) findViewById(R.id.cals);

        final p_MyCustomTextView_regular ingredients_des = (p_MyCustomTextView_regular) findViewById(R.id.ingredients_des);

        final p_MyCustomTextView_regular directions_des = (p_MyCustomTextView_regular) findViewById(R.id.directions_des);
toolbar.setNavigationOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
     finish();
    }
});

        collapsingToolbar.setTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        API apiService = ApiClient.getClient().create(API.class);
        Call<List<Recipies_Details_Model>> call = apiService.recipie_details(String.valueOf(position));

        call.enqueue(new Callback<List<Recipies_Details_Model>>() {
            @Override
            public void onResponse(Call<List<Recipies_Details_Model>> call, Response<List<Recipies_Details_Model>> response) {
                Recipies_Details_Model product = response.body().get(0);
                Picasso.with(RecipiesDetailsActivity.this).load("http://www.nutrimeat.in/assets/user/wc/" + product.getRe_img_cover())
                        //  .resize(dp2px(220), 0)
                        .into(bg_image);
                product_name.setText(product.getRe_name());
                time_pre.setText(product.getRe_prep_time());
                serving.setText(product.getRe_servings());
                cals.setText(product.getRe_cal());
                String ingredients = product.getRe_ingredients();
                ingredients = ingredients.replace("<li>", "- ");
                ingredients = ingredients.replace("</li>", "");
                ingredients = ingredients.replace("<br>", "");
                String directions = product.getRe_description();
                directions = directions.replace("<li>", "- ");
                directions = directions.replace("</li>", "");
                directions = directions.replace("<br>", "");
                ingredients_des.setText(ingredients);
                directions_des.setText(directions);
            }

            @Override
            public void onFailure(Call<List<Recipies_Details_Model>> call, Throwable t) {
                // Log error here since request failed
                Log.e("Products", t.toString());
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}

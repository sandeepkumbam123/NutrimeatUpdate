package app.nutrimeat.meat.org.nutrimeat.Account;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.IntentCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import app.nutrimeat.meat.org.nutrimeat.ApiClient;
import app.nutrimeat.meat.org.nutrimeat.MainActivity;
import app.nutrimeat.meat.org.nutrimeat.PrefManager;
import app.nutrimeat.meat.org.nutrimeat.R;
import app.nutrimeat.meat.org.nutrimeat.Textview.p_MyCustomTextView_mbold;
import app.nutrimeat.meat.org.nutrimeat.api.API;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;


public class AcountFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView txtViewCount;
    private ProgressBar progressBar;
    private GoogleApiClient mGoogleApiClient;
    OrdersAdapter adapter;
    private PrefManager prefManager;


    public AcountFragment() {
        // Required empty public constructor
    }

    Button login;
    String User_name;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_acount, container, false);
        setHasOptionsMenu(true);
        progressBar = (ProgressBar) rootview.findViewById(R.id.progressBar);
        login = (Button) rootview.findViewById(R.id.login);
        prefManager = new PrefManager(getActivity());
//        User_name = "frescoshashank@gmail.com";

        PrefManager prefManager=new PrefManager(getActivity());
        User_name = prefManager.getEmail();
        if(TextUtils.isEmpty(User_name)){
            User_name=prefManager.getMobile();
        }
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();
        super.onStart();
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getActivity());
        RelativeLayout profile_view = (RelativeLayout) rootview.findViewById(R.id.profile_view);
        RoundedImageView profile_pic = (RoundedImageView) rootview.findViewById(R.id.profile_pic);
        p_MyCustomTextView_mbold user_name = (p_MyCustomTextView_mbold) rootview.findViewById(R.id.user_name);
        final p_MyCustomTextView_mbold emptyview = (p_MyCustomTextView_mbold) rootview.findViewById(R.id.emptyview);
        final RecyclerView mRecycler = (RecyclerView) rootview.findViewById(R.id.products_rv);
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        // mRecycler.setLayoutManager(new GridLayoutManager(this, 4));
        progressBar.setVisibility(View.VISIBLE);
        API apiService = ApiClient.getClient().create(API.class);
        StringBuilder builder = new StringBuilder("");
        if (!TextUtils.isEmpty(prefManager.getName())) {
            builder.append(prefManager.getName());
        }
        if (!TextUtils.isEmpty(prefManager.getEmail())) {
            if (builder.length() > 0) {
                builder.append("\n");
            }
            builder.append(prefManager.getEmail());
        }
        if (!TextUtils.isEmpty(prefManager.getMobile())) {
            if (builder.length() > 0) {
                builder.append("\n");
            }
            builder.append(prefManager.getMobile());
        }
        user_name.setText(builder);
        if (prefManager.getIsGuestLogin()) {
            profile_view.setVisibility(View.GONE);

            mRecycler.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            emptyview.setVisibility(View.VISIBLE);
            login.setVisibility(View.VISIBLE);
        } else {
            profile_view.setVisibility(View.VISIBLE);

            Call<List<Orders_Model>> call = apiService.ordered_products(User_name);
            call.enqueue(new Callback<List<Orders_Model>>() {
                @Override
                public void onResponse(Call<List<Orders_Model>> call, Response<List<Orders_Model>> response) {
                    List<Orders_Model> products = response.body();
                    if (response.body() == null || products.size() == 0) {
                        emptyview.setVisibility(View.VISIBLE);
                        mRecycler.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                    } else {
                        emptyview.setVisibility(View.GONE);
                        mRecycler.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        adapter = new OrdersAdapter(products, R.layout.order_item, AcountFragment.this, User_name);
                        mRecycler.setAdapter(adapter);

                    }

                }

                @Override
                public void onFailure(Call<List<Orders_Model>> call, Throwable t) {
                    // Log error here since request failed
                    Log.e("Products", t.toString());
                }
            });
        }
        login.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                        getActivity().startActivity(intent);
                        getActivity().finish();

                    }
                }
        );
        return rootview;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Account");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            if (mGoogleApiClient.isConnected())
                Auth.GoogleSignInApi.signOut(mGoogleApiClient);
            if (LoginManager.getInstance() != null)
                LoginManager.getInstance().logOut();
            prefManager.clear();
            Toast.makeText(getApplicationContext(), "Logged Out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_account, menu);
        super.onCreateOptionsMenu(menu, inflater);


    }


}

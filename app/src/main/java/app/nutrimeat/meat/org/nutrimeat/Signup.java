package app.nutrimeat.meat.org.nutrimeat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import app.nutrimeat.meat.org.nutrimeat.api.API;
import app.nutrimeat.meat.org.nutrimeat.api.Request;
import app.nutrimeat.meat.org.nutrimeat.api.SendSMS;
import app.nutrimeat.meat.org.nutrimeat.api.ServerResponse;
import app.nutrimeat.meat.org.nutrimeat.api.VerifySMS;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Signup extends AppCompatActivity {

    TextView mm;
    EditText name, email, password, mobile;
    Button signup;
    private PrefManager prefManager;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        prefManager = new PrefManager(this);
        mm = (TextView) findViewById(R.id.sp);
        Typeface welcomeFont = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Medium.ttf");
        mm.setTypeface(welcomeFont);
        signup = (Button) findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                invokeSignUp();
            }
        });


    }

    private void invokeSignUp() {
        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        mobile = (EditText) findViewById(R.id.mobilenumber);

        final String n1 = name.getText().toString();
        final String e1 = email.getText().toString();
        String p1 = password.getText().toString();
        final String m1 = mobile.getText().toString();
        if (n1.length() == 0) {
            name.setError("This field is required");
        }
        if (e1.length() == 0) {
            email.setError("This field is required");
        }
        if (p1.length() == 0) {
            password.setError("This field is required");
        }
        if (m1.length() == 0) {
            mobile.setError("This field is required");
        }
        if (!n1.equalsIgnoreCase("") && !e1.equalsIgnoreCase("") && !p1.equalsIgnoreCase("") && !m1.equalsIgnoreCase("")) {
            progressDialog = ProgressDialog.show(this, "Please wait ...", "Logging User...", true);
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(getString(R.string.api_url))
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

             OkHttpClient.Builder  httpClient = new OkHttpClient.Builder();
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.interceptors().add(interceptor);


            API api = retrofit.create(API.class);
            Request request = new Request();
            request.setName(n1);
            request.setEmail(e1);
            request.setPassword(p1);
            request.setMobile(m1);

            Call<ServerResponse> responseCall = api.getrespone(request);
            responseCall.enqueue(new Callback<ServerResponse>() {
                @Override
                public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                    int statusCode = response.code();
                    ServerResponse response1 = response.body();
                    Log.d("Response_register", "onResponse: " + statusCode);
                    Log.d("Status", response1.getStatus());
                    progressDialog.dismiss();
                    if (response1.getStatus().equalsIgnoreCase("failed")) {
                        Log.d("ERROR", response1.getError());
                        Toast.makeText(getApplicationContext(), response1.getError(), Toast.LENGTH_SHORT).show();
                    } else {
                        prefManager.setName(n1);
                        prefManager.setEmail(e1);
                        prefManager.setMobile(m1);
                        Log.d("NAME_pref", prefManager.getName());
                        Log.d("Email_pref", prefManager.getEmail());
                        Log.d("Mobile_pref", prefManager.getMobile());
                        sendSMS(prefManager.getMobile());

                    }

                }

                @Override
                public void onFailure(Call<ServerResponse> call, Throwable t) {

                    Log.d("Response_register", "onFailure: " + t.getMessage());
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Internal error. Please Try again", Toast.LENGTH_SHORT).show();

                }
            });
        }


    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void sendSMS(final String mob) {
        progressDialog = ProgressDialog.show(this, "Please wait ...", "Sending OTP...", true);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.api_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        API api = retrofit.create(API.class);
        SendSMS sendSMS = new SendSMS();
        sendSMS.setMobile(mob);
        Call<ServerResponse> responseCall = api.sendSms(sendSMS);
        responseCall.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                int statusCode = response.code();
                ServerResponse response1 = response.body();
                Log.d("SENDSMS", "onResponse: " + statusCode);
                Log.d("Status", response1.getStatus());
                progressDialog.dismiss();
                if (response1.getStatus().equalsIgnoreCase("failed")) {
                    Log.d("ERROR", response1.getError());
                    Toast.makeText(getApplicationContext(), response1.getError(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "SMS Sent, Enter Your OTP Above", Toast.LENGTH_SHORT).show();
                    verifySMS(mob);

                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Log.d("SENDSMS", "onFailure: " + t.getMessage());
                Toast.makeText(getApplicationContext(), "Internal error. Please Try again", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void verifySMS(final String mobil) {

        LayoutInflater layoutInflater = LayoutInflater.from(Signup.this);
        View mView = layoutInflater.inflate(R.layout.otp, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Signup.this).setTitle("Mobile Verification");
        alertDialogBuilderUserInput.setView(mView);
        final EditText mob = (EditText) mView.findViewById(R.id.userotp);
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("VERIFY", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        // ToDo get user input here
                        progressDialog = ProgressDialog.show(Signup.this, "Please wait ...", "Verifying OTP...", true);
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(getString(R.string.api_url))
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        API api = retrofit.create(API.class);
                        VerifySMS verifySMS = new VerifySMS();
                        verifySMS.setMobile(mobil);
                        verifySMS.setOtp(mob.getText().toString());
                        Call<ServerResponse> responseCall = api.verifysms(verifySMS);
                        responseCall.enqueue(new Callback<ServerResponse>() {
                            @Override
                            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                                int statusCode = response.code();
                                ServerResponse response1 = response.body();
                                Log.d("VERIFYSMS", "onResponse: " + statusCode);
                                Log.d("Status", response1.getStatus());
                                progressDialog.dismiss();
                                if (response1.getStatus().equalsIgnoreCase("failed")) {
                                    Log.d("ERROR", response1.getError());
                                    Toast.makeText(getApplicationContext(), response1.getError(), Toast.LENGTH_SHORT).show();
                                } else {
                                    Intent loginScreen = new Intent(Signup.this,MainActivity.class);
                                    startActivity(loginScreen);
                                    Toast.makeText(getApplicationContext(), response1.getStatus(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ServerResponse> call, Throwable t) {
                                Log.d("VERIFYSMS", "onFailure: " + t.getMessage());
                                Toast.makeText(getApplicationContext(), "Internal error. Please Try again", Toast.LENGTH_SHORT).show();

                            }

                        });


                    }

                })
                .setNegativeButton("RESEND",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                sendSMS(mobil);
                            }
                        });
        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();

    }

}

package app.nutrimeat.meat.org.nutrimeat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import app.nutrimeat.meat.org.nutrimeat.Account.User_Details_Model;
import app.nutrimeat.meat.org.nutrimeat.api.API;
import app.nutrimeat.meat.org.nutrimeat.api.CheckUser;
import app.nutrimeat.meat.org.nutrimeat.api.ForgotpasswordRequest;
import app.nutrimeat.meat.org.nutrimeat.api.LoginRequest;
import app.nutrimeat.meat.org.nutrimeat.api.Request;
import app.nutrimeat.meat.org.nutrimeat.api.SendSMS;
import app.nutrimeat.meat.org.nutrimeat.api.ServerResponse;
import app.nutrimeat.meat.org.nutrimeat.api.VerifySMS;
import app.nutrimeat.meat.org.nutrimeat.internetCheck.ConnectivityReceiver;
import app.nutrimeat.meat.org.nutrimeat.internetCheck.MyApplication;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{


    TextView welcome, forgot;
    private CheckBox chRememberMe;
    Button login_manual, signup_manual;

    EditText email, password;
    private String full_name, email_id;


    ProgressDialog progressDialog;


    private PrefManager prefManager;

    //Signin constant to check the activity result
    private int RC_SIGN_IN = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefManager = new PrefManager(this);
        // home screen if user login or first time opening

        if (!prefManager.isFirstTimeLaunch()) {
            Intent intent = new Intent(MainActivity.this, Navdrawer.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        setContentView(R.layout.activity_main);
        welcome = (TextView) findViewById(R.id.welcome);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        chRememberMe = (CheckBox) findViewById(R.id.chRememberMe);
//        email.setText(prefManager.getUserLoginId());
//        password.setText(prefManager.getUserLoginPassword());
        Typeface welcomeFont = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Medium.ttf");
        welcome.setTypeface(welcomeFont);
        // Manually checking internet connection
        checkConnection();







        login_manual = (Button) findViewById(R.id.login);
        signup_manual = (Button) findViewById(R.id.name);

        login_manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // login manually

                invokeLoginWithEmail();

            }
        });

        signup_manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // signup manually

                invokeRegisterWithEmail();

            }
        });

        /*skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // skip continue as guest

                invokeSkipUserAsGuest();


            }
        });*/

        forgot = (TextView) findViewById(R.id.forgotpassword);
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
                View mView = layoutInflater.inflate(R.layout.forgotdialog, null);
                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this).setTitle("Forgot Password");
                alertDialogBuilderUserInput.setView(mView);
                final EditText forgotPass = (EditText) mView.findViewById(R.id.forgot);
                alertDialogBuilderUserInput
                        .setCancelable(false)
                        .setPositiveButton("RESET", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                // ToDo get user input here
                                progressDialog = ProgressDialog.show(MainActivity.this, "Please wait ...", "Resetting Password...", true);
                                Retrofit retrofit = new Retrofit.Builder()
                                        .baseUrl(getString(R.string.api_url))
                                        .addConverterFactory(GsonConverterFactory.create())
                                        .build();

                                API api = retrofit.create(API.class);
                                ForgotpasswordRequest forgotpasswordRequest = new ForgotpasswordRequest();
                                forgotpasswordRequest.setIdentity(forgotPass.getText().toString());
                                Call<ServerResponse> responseCall = api.getForgot_response(forgotpasswordRequest);
                                responseCall.enqueue(new Callback<ServerResponse>() {
                                    @Override
                                    public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                                        int statusCode = response.code();
                                        ServerResponse response1 = response.body();
                                        Log.d("Forgotpassword", "onResponse: " + statusCode);
                                        Log.d("Status", response1.getStatus());
                                        progressDialog.dismiss();
                                        if (response1.getStatus().equalsIgnoreCase("failed")) {
                                            Log.d("ERROR", response1.getError());
                                            Toast.makeText(getApplicationContext(), response1.getError(), Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Please Check Your Mail For Instructions.", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ServerResponse> call, Throwable t) {
                                        Log.d("Forgotpassword", "onFailure: " + t.getMessage());
                                        Toast.makeText(getApplicationContext(), "Internal error. Please Try Again", Toast.LENGTH_SHORT).show();

                                    }
                                });

                            }
                        }).setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
                alertDialogAndroid.show();

            }
        });



    }

    /*

    LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
        View mView = layoutInflater.inflate(R.layout.custom, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this).setTitle("Mobile Number");
        alertDialogBuilderUserInput.setView(mView);
        EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Send OTP", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        // ToDo get user input here


                    }
                });
        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();

     */

    private void invokeSkipUserAsGuest() {
        prefManager.setIsGuestLogin(true);
        Intent intent = new Intent(MainActivity.this, Navdrawer.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }

    private void invokeRegisterWithEmail() {
        prefManager.setIsGuestLogin(false);
        //starting of signup activity once user clicks signup
        startActivity(new Intent(getApplicationContext(), Signup.class));

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void invokeLoginWithEmail() {
        prefManager.setIsGuestLogin(false);
        //  login user with manual email and password

        String e1 = email.getText().toString();
        String p1 = password.getText().toString();


        if (email.getText().toString().length() == 0) {
            email.setError("This field is Required");
        }
        if (password.getText().toString().length() == 0) {
            password.setError("This field is Required");
        }
        if (!e1.equalsIgnoreCase("") && !p1.equalsIgnoreCase("")) {

            progressDialog = ProgressDialog.show(this, "Please wait ...", "Logging User...", true);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(getString(R.string.api_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

           final API api = retrofit.create(API.class);
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setIdentity(e1);
            loginRequest.setPassword(p1);
            Call<ServerResponse> responseCall = api.getlogin_response(loginRequest);
            responseCall.enqueue(new Callback<ServerResponse>() {
                @Override
                public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                    int statusCode = response.code();
                    ServerResponse response1 = response.body();
                    Log.d("RESPONSE_Login", "onResponse:  status code" + statusCode);
                    Log.d("RESPONSE_Login", "onResponse: response  " + response1);
                    Log.d("Status", response1.getStatus());
                    progressDialog.dismiss();
                    getUserDetails(api,email.getText().toString());
                    prefManager.setUserId(email.getText().toString());
                    if (response1.getStatus().equalsIgnoreCase("failed")) {
                        Log.d("ERROR", response1.getError());
                        Toast.makeText(getApplicationContext(), response1.getError(), Toast.LENGTH_SHORT).show();
                    } else {

                        String input = email.getText().toString();
                        if (input.contains("@")) {
                            prefManager.setEmail(input);
                        } else {
                            prefManager.setMobile(input);
                        }
                        if (chRememberMe.isChecked()) {
                            prefManager.setLoginUserId(input);
                            prefManager.setLoginPassword(password.getText().toString());
                        } else {
//                            prefManager.setLoginUserId("");
//                            prefManager.setLoginPassword("");
                        }
                        prefManager.setFirstTimeLaunch(false);
//                        Toast.makeText(getApplicationContext(), "User Login Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, Navdrawer.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                }

                @Override
                public void onFailure(Call<ServerResponse> call, Throwable t) {
                    progressDialog.dismiss();
                    Log.d("RESPONSE_Login", "onFailure: " + t.getMessage());
                    Toast.makeText(getApplicationContext(), "Internal error. Please Try Again", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void getUserDetails(API api ,String s) {
        Call<User_Details_Model> call = api.userDetails(s);
        call.enqueue(new Callback<User_Details_Model>() {
            @Override
            public void onResponse(Call<User_Details_Model> call, Response<User_Details_Model> response) {
                User_Details_Model model = response.body();
                prefManager.setEmail(model.getEmail());
                prefManager.setMobile(model.getPhoneNumber());
                prefManager.setName(model.getUserName());
            }

            @Override
            public void onFailure(Call<User_Details_Model> call, Throwable t) {

            }
        });
    }




    // Method to manually check connection status
    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }

    // Showing the status in Snackbar
    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {
            message = "Good! Connected to Internet";
            color = Color.WHITE;
        } else {
            message = "Sorry! Not connected to internet";
            color = Color.RED;
        }

        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.fab), message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(this);
    }




    public void sendSMSFromBox(final String mob) {

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

    /**
     * Callback will be triggered when there is change in
     * network connection
     */

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }


    public void verifySMS(final String mobile) {

        LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
        View mView = layoutInflater.inflate(R.layout.otp, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this).setTitle("Mobile Verification");
        alertDialogBuilderUserInput.setView(mView);
        final EditText mob = (EditText) mView.findViewById(R.id.userotp);
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("VERIFY", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        // ToDo get user input here
                        progressDialog = ProgressDialog.show(MainActivity.this, "Please wait ...", "Verifying OTP...", true);
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(getString(R.string.api_url))
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        API api = retrofit.create(API.class);
                        VerifySMS verifySMS = new VerifySMS();
                        verifySMS.setMobile(mobile);
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
                                sendSMSFromBox(mobile);
                            }
                        });
        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();

    }
}

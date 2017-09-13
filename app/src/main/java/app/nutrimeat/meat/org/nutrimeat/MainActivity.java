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

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

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

public class MainActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener, GoogleApiClient.OnConnectionFailedListener {


    CallbackManager callbackManager;
    LoginButton loginButton;
    TextView welcome, forgot;
    private CheckBox chRememberMe;
    Button login_manual, signup_manual;

    EditText email, password;
    private String full_name, email_id;

    //Signin button
    private SignInButton signInButton;

    ProgressDialog progressDialog;

    //Signing Options
    private GoogleSignInOptions gso;

    //google api client
    private GoogleApiClient mGoogleApiClient;

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
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
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

        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("public_profile email");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {

                AccessToken accessToken = loginResult.getAccessToken();
                Profile profile = Profile.getCurrentProfile();
                // Facebook Email address
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                Log.v("LoginActivity Response ", response.toString());

                                try {
                                    full_name = object.getString("name");

                                    email_id = object.getString("email");

                                    // shared perference name & email saving

                                    prefManager.setName(full_name);
                                    prefManager.setEmail(email_id);
                                    Log.d("Email Shared", prefManager.getEmail());
                                    Log.d("Name Shared", prefManager.getName());
                                    //Toast.makeText(getApplicationContext(), "Name " + full_name, Toast.LENGTH_LONG).show();
                                    Retrofit retrofit = new Retrofit.Builder()
                                            .baseUrl(getString(R.string.api_url))
                                            .addConverterFactory(GsonConverterFactory.create())
                                            .build();

                                    API api = retrofit.create(API.class);
                                    CheckUser checkUser = new CheckUser();
                                    checkUser.setEmail(prefManager.getEmail());
                                    Call<ServerResponse> responseCall = api.checkuser(checkUser);
//                                    progressDialog = ProgressDialog.show(MainActivity.this, "Please wait ...", "Validating Password...", true, false);
                                    responseCall.enqueue(new Callback<ServerResponse>() {
                                        @Override
                                        public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
//                                            progressDialog.dismiss();
                                            int statusCode = response.code();
                                            ServerResponse response1 = response.body();
                                            Log.d("CheckUser_response", "onResponse: " + statusCode);
                                            Log.d("Status", response1.getStatus());
                                            if (response1.getStatus().equalsIgnoreCase("failed")) {
                                                Log.d("CheckUser_Error", response1.getError());
                                                prefManager.setFirstTimeLaunch(false);
                                                Intent intent = new Intent(MainActivity.this, Navdrawer.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                            } else {
                                                //REGISTER ( launch mobile number dialog and then register
                                                LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
                                                View mView = layoutInflater.inflate(R.layout.custom, null);
                                                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this).setTitle("Mobile Number");
                                                alertDialogBuilderUserInput.setView(mView);
                                                final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);
                                                alertDialogBuilderUserInput
                                                        .setCancelable(false)
                                                        .setPositiveButton("Send OTP", new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialogBox, int id) {
                                                                // ToDo get user input here

                                                                prefManager.setMobile(userInputDialogEditText.getText().toString());
                                                                Log.d("FB_NAME", prefManager.getName());
                                                                Log.d("FB_EMAIL", prefManager.getEmail());
                                                                Log.d("FB_MOBILE", prefManager.getMobile());
                                                                registerUser(prefManager.getName(), prefManager.getEmail(), prefManager.getMobile());
                                                            }
                                                        });
                                                AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
                                                alertDialogAndroid.show();
                                            }


                                        }

                                        @Override
                                        public void onFailure(Call<ServerResponse> call, Throwable t) {
                                            Log.d("CheckUser_Res_Google", "onFailure: " + t.getMessage());
                                            Toast.makeText(getApplicationContext(), "Internal error. Please Try again", Toast.LENGTH_SHORT).show();

                                        }
                                    });


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "name,email");
                request.setParameters(parameters);
                request.executeAsync();
                prefManager.setIsGuestLogin(false);
            }

            @Override
            public void onCancel() {
                prefManager.setIsGuestLogin(false);
                LoginManager.getInstance().logOut();
            }

            @Override
            public void onError(FacebookException error) {
                prefManager.setIsGuestLogin(false);
            }


        });

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        //Initializing signinbutton
        signInButton = (SignInButton) findViewById(R.id.google_sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setScopes(gso.getScopeArray());


        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        //Setting onclick listener to signing button
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prefManager.setIsGuestLogin(false);
                //Creating an intent
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);

                //Starting intent for result
                startActivityForResult(signInIntent, RC_SIGN_IN);

            }
        });


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
                        Toast.makeText(getApplicationContext(), "User Login Successful", Toast.LENGTH_SHORT).show();
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        //If signin
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            //Calling a new function to handle signin
            handleSignInResult(result);
        }
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

    //After the signing we are calling this function
    private void handleSignInResult(GoogleSignInResult result) {
        //If the login succeed
        if (result.isSuccess()) {
            //Getting google account
            GoogleSignInAccount acct = result.getSignInAccount();
            String name = acct.getDisplayName();
            String email = acct.getEmail();

            // shared perference name & email saving

            prefManager.setName(name);
            prefManager.setEmail(email);
            Log.d("Email Shared", prefManager.getEmail());
            Log.d("NameShared", prefManager.getName());

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(getString(R.string.api_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            API api = retrofit.create(API.class);
            CheckUser checkUser = new CheckUser();
            checkUser.setEmail(prefManager.getEmail());
            Call<ServerResponse> responseCall = api.checkuser(checkUser);
            responseCall.enqueue(new Callback<ServerResponse>() {
                @Override
                public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                    int statusCode = response.code();
                    ServerResponse response1 = response.body();
                    Log.d("CheckUser_response", "onResponse: " + statusCode);
                    Log.d("Status", response1.getStatus());
                    if (response1.getStatus().equalsIgnoreCase("failed")) {
                        Log.d("CheckUser_Error", response1.getError());
                        //Google  //LOGIN (automatically intent to main screen)
                        prefManager.setFirstTimeLaunch(false);
                        Intent intent = new Intent(MainActivity.this, Navdrawer.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {

                        //REGISTER ( launch mobile number dialog and then register
                        LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
                        View mView = layoutInflater.inflate(R.layout.custom, null);
                        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this).setTitle("Mobile Number");
                        alertDialogBuilderUserInput.setView(mView);
                        final EditText mob = (EditText) mView.findViewById(R.id.userInputDialog);
                        alertDialogBuilderUserInput
                                .setCancelable(false)
                                .setPositiveButton("Send OTP", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogBox, int id) {
                                        // ToDo get user input here

                                        prefManager.setMobile(mob.getText().toString());
                                        Log.d("MOB", mob.getText().toString());
                                        Log.d("FB_NAME", prefManager.getName());
                                        Log.d("FB_EMAIL", prefManager.getEmail());
                                        Log.d("FB_MOBILE", prefManager.getMobile());
                                        registerUser(prefManager.getName(), prefManager.getEmail(), prefManager.getMobile());


                                    }
                                });
                        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
                        alertDialogAndroid.show();
                    }


                }

                @Override
                public void onFailure(Call<ServerResponse> call, Throwable t) {
                    Log.d("CheckUser_Res_Google", "onFailure: " + t.getMessage());
                    Toast.makeText(getApplicationContext(), "Internal error. Please Try again", Toast.LENGTH_SHORT).show();

                }
            });

        } else {
            //If login fails
            Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show();
        }
    }

    private void registerUser(String full_name, String email_id, final String mobile) {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.api_url))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        API api = retrofit.create(API.class);
        Request request = new Request();
        request.setName(full_name);
        request.setEmail(email_id);
        request.setMobile(mobile);
        Call<ServerResponse> responseCall = api.getrespone(request);
//        progressDialog = ProgressDialog.show(MainActivity.this, "Please wait ...", "Registering Account", true, false);
        responseCall.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                int statusCode = response.code();
//                progressDialog.dismiss();
                ServerResponse response1 = response.body();
                Log.d("Response_register", "onResponse: " + statusCode);
                Log.d("Status", response1.getStatus());
                if (response1.getStatus().equalsIgnoreCase("failed")) {
                    Log.d("ERROR", response1.getError());
                    Toast.makeText(getApplicationContext(), response1.getError(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), response1.getStatus(), Toast.LENGTH_SHORT).show();
                    sendSMSFromBox(mobile);
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Log.d("Response_register", "onFailure: " + t.getMessage());
                Toast.makeText(getApplicationContext(), "Internal error. Please Try again", Toast.LENGTH_SHORT).show();


            }
        });
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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

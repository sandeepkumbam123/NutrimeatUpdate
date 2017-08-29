package app.nutrimeat.meat.org.nutrimeat.payment;

/**
 * Created by Innovative Lab on 10-11-2016.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import com.google.android.gms.analytics.ecommerce.Product;
import com.payu.custombrowser.Bank;
import com.payu.custombrowser.CustomBrowser;
import com.payu.custombrowser.PayUCustomBrowserCallback;
import com.payu.custombrowser.PayUWebChromeClient;
import com.payu.custombrowser.PayUWebViewClient;
import com.payu.custombrowser.bean.CustomBrowserConfig;
import com.payu.magicretry.MagicRetryFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import app.nutrimeat.meat.org.nutrimeat.BuildConfig;
import app.nutrimeat.meat.org.nutrimeat.PrefManager;
import app.nutrimeat.meat.org.nutrimeat.R;

public class PaymentActivity extends Activity {
    private static final String TAG = "PaymentActivity";
    private WebView webviewPayment;
    private String totalmaount;
    private String nameofpayee;
    private String emailofpayee;
    private String phoneofpayee;
    private Product productDetails;
    private PrefManager prefManager;
//    private String url = "https://secure.payu.in/_payment";
    private String url = "https://test.payu.in/_payment";//for testing


    /**/
    private static String transaction_Id = System.currentTimeMillis() + "";
    private static String amount = "1.00 ";
    private static String product_info = "Chicken";
    private static String f_Name = "Ramesh";
    private static String email = "sai.ramesh51@gmail.com";
    private static String s_Url = "https://payu.herokuapp.com/success";
    private static String f_Url = "https://payu.herokuapp.com/failure";
    private static String user_credentials = BuildConfig.MERCHANT_KEY + ":" + "guru@guru.com";
    private static String phone = "7893051914";
    private static String udf1 = "";
    private static String udf2 = "";
    private static String udf3 = "";
    private static String udf4 = "";
    private static String udf5 = "";
    //optional feilds for hash generation
    private static String offer_key = " ";
    private static String cardBin = " ";
    private String postData = "";

    /**/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.payment_activity);

        pay();
//        Bundle bundle = getIntent().getExtras();
//        if (bundle != null) {
//            totalmaount = bundle.getString("amount");
//        }
//        prefManager = new PrefManager(this);
//        nameofpayee = prefManager.getName();   // user name
//        emailofpayee = prefManager.getEmail();    // email of the user
//        phoneofpayee = prefManager.getMobile();  //user phone number has to send
//        webviewPayment = (WebView) findViewById(R.id.webview);
//        webviewPayment.getSettings().setJavaScriptEnabled(true);
//        webviewPayment.getSettings().setDomStorageEnabled(true);
//        webviewPayment.getSettings().setLoadWithOverviewMode(true);
//        webviewPayment.getSettings().setUseWideViewPort(true);
//        webviewPayment.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
//        webviewPayment.getSettings().setSupportMultipleWindows(true);
//        webviewPayment.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//        webviewPayment.addJavascriptInterface(new PayUJavaScriptInterface(), "PayUMoney");
//
//        StringBuilder url_s = new StringBuilder();
//
//        url_s.append("https://secure.payu.in/_payment");
//
//        Log.e(TAG, "call url " + url_s);
//

        //  webviewPayment.postUrl(url_s.toString(),EncodingUtils.getBytes(getPostString(), "utf-8"));

//        webviewPayment.postUrl(url_s.toString(), getPostString().getBytes(Charset.forName("UTF-8")));
//
//        webviewPayment.setWebViewClient(new WebViewClient() {
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
//            }
//
//            @SuppressWarnings("unused")
//            public void onReceivedSslError(WebView view, SslErrorHandler handler) {
//                Log.e("Error", "Exception caught!");
//                handler.cancel();
//            }
//
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
//                return true;
//            }
//
//        });

        /*****************************************************************/


//        initiatePayment();

        /*****************************************************************/
    }


    private void setUpPay() {
        CustomBrowserConfig customBrowserConfig = new CustomBrowserConfig(BuildConfig.MERCHANT_KEY, transaction_Id);
        customBrowserConfig.setViewPortWideEnable(false);

        //TODO don't forgot to set AutoApprove and AutoSelectOTP to true for One Tap payments
        customBrowserConfig.setAutoApprove(false);
        customBrowserConfig.setAutoSelectOTP(false);

        //Set below flag to true to disable the default alert dialog of Custom Browser and use your custom dialog
        customBrowserConfig.setDisableBackButtonDialog(false);

        //Below flag is used for One Click Payments. It should always be set to CustomBrowserConfig.STOREONECLICKHASH_MODE_SERVER
        customBrowserConfig.setStoreOneClickHash(CustomBrowserConfig.STOREONECLICKHASH_MODE_SERVER);

        //Set it to true to enable run time permission dialog to appear for all Android 6.0 and above devices
        customBrowserConfig.setMerchantSMSPermission(false);

        //Set it to true to enable Magic retry
        customBrowserConfig.setmagicRetry(true);

        new CustomBrowser().addCustomBrowser(PaymentActivity.this, customBrowserConfig, payUCustomBrowserCallback);

    }

    private void initiatePayment() {
        postData = "&txnid=" + transaction_Id +
                "&device_type=1" +
                "&ismobileview=1" +
                "&productinfo=" + product_info +
                "&user_credentials=" + user_credentials +
                "&key=" + BuildConfig.MERCHANT_KEY +
                "&instrument_type=Put here Device info " +
                "&surl=" + s_Url +
                "&furl=" + f_Url + "" +
                "&instrument_id=7dd17561243c202" +
                "&firstname=" + f_Name +
                "&email=" + email +
                "&phone=" + phone +
                "&amount=" + amount +
//                "&bankcode=PAYUW" + //for PayU Money
//                "&pg=WALLET"+//for PayU Money
                "&hash=";

        MessageDigest digest = null;
        String hash;
        StringBuilder checkSumStr = new StringBuilder();
        try {
            digest = MessageDigest.getInstance("SHA-512");// MessageDigest.getInstance("SHA-256");
            checkSumStr.append(BuildConfig.MERCHANT_KEY);
            checkSumStr.append("|");
            checkSumStr.append(transaction_Id);
            checkSumStr.append("|");
            checkSumStr.append(amount);
            checkSumStr.append("|");
            checkSumStr.append(product_info);
            checkSumStr.append("|");
            checkSumStr.append(f_Name);
            checkSumStr.append("|");
            checkSumStr.append(email);
            checkSumStr.append("|||||||||||");
            checkSumStr.append(BuildConfig.SALT_KEY);

            digest.update(checkSumStr.toString().getBytes());

            hash = bytesToHexString(digest.digest());
            postData += hash;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }


    }

    private final class PayUJavaScriptInterface {
        PayUJavaScriptInterface() {
        }

        @JavascriptInterface
        public void success(long id, final String paymentId) {
            runOnUiThread(new Runnable() {
                public void run() {
                    Log.v("paymentstatus", paymentId);
//                    Intent intent = new Intent(PaymentActivity.this, CheckOutActivity.class);
//                    LoginDetails loginDetails = LocalDb.getLoginDetails();
//                    AddressResponse addressData = LocalDb.getAddressData();
//                    CheckoutReq req = new CheckoutReq();
//                    req.name = loginDetails.name;
//                    req.apikey = loginDetails.apikey;
//                    req.mobile = loginDetails.mobile;
//                    req.ordercode = RandomUtils.getRandomString(8);
//                    req.address = addressData.address;
//                    req.payment = "ONLINE";
//                    intent.putExtra(ConstantUtils.DATA, req);
//                    startActivity(intent);
                }
            });
        }

        @JavascriptInterface
        public void failure(long id, final String paymentId) {
            runOnUiThread(new Runnable() {
                public void run() {
                    Log.v("paymentfauil", paymentId);
                    Toast.makeText(PaymentActivity.this, "Payment failed", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }


    private String getPostString() {
        String key = BuildConfig.MERCHANT_KEY;
        String salt = BuildConfig.SALT_KEY;
        String txnid = "TXN_ID" + String.valueOf(System.currentTimeMillis());
        String amount = totalmaount;
        String firstname = nameofpayee;
        String email = emailofpayee;
        String productInfo = "Product1";

        StringBuilder post = new StringBuilder();
        post.append("service_provider");
        post.append("payu_paisa");
        post.append("&");
        post.append("key=");
        post.append(key);
        post.append("&");
        post.append("txnid=");
        post.append(txnid);
        post.append("&");
        post.append("amount=");
        post.append(amount);
        post.append("&");
        post.append("productinfo=");
        post.append(productInfo);
        post.append("&");
        post.append("firstname=");
        post.append(firstname);
        post.append("&");
        post.append("email=");
        post.append(email);
        post.append("&");
        post.append("phone=");
        post.append(phoneofpayee);
        post.append("&");
        post.append("surl=");
        post.append("https://www.payumoney.com/mobileapp/payumoney/success.php");
        //https://www.payumoney.com/mobileapp/payumoney/success.php
        //https://www.payumoney.com/mobileapp/payumoney/failure.php
        post.append("&");
        post.append("furl=");
        post.append("https://www.payumoney.com/mobileapp/payumoney/failure.php");
        post.append("&");

        StringBuilder checkSumStr = new StringBuilder();
        /* =sha512(key|txnid|amount|productinfo|firstname|email|udf1|udf2|udf3|udf4|udf5||||||salt) */
        MessageDigest digest = null;
        String hash;
        try {
            digest = MessageDigest.getInstance("SHA-512");// MessageDigest.getInstance("SHA-256");

            checkSumStr.append(key);
            checkSumStr.append("|");
            checkSumStr.append(txnid);
            checkSumStr.append("|");
            checkSumStr.append(amount);
            checkSumStr.append("|");
            checkSumStr.append(productInfo);
            checkSumStr.append("|");
            checkSumStr.append(firstname);
            checkSumStr.append("|");
            checkSumStr.append(email);
            checkSumStr.append("|||||||||||");
            checkSumStr.append(salt);

            digest.update(checkSumStr.toString().getBytes());

            hash = bytesToHexString(digest.digest());
            post.append("hash=");
            post.append(hash);
            post.append("&");
            Log.i(TAG, "SHA result is " + hash);
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }

        post.append("service_provider=");
        post.append("payu_paisa");
        return post.toString();
    }

    private JSONObject getProductInfo() {
        try {
            //create payment part object
            JSONObject productInfo = new JSONObject();

            JSONObject jsonPaymentPart = new JSONObject();
            jsonPaymentPart.put("name", "TapFood");
            jsonPaymentPart.put("description", "Lunchcombo");
            jsonPaymentPart.put("value", "500");
            jsonPaymentPart.put("isRequired", "true");
            jsonPaymentPart.put("settlementEvent", "EmailConfirmation");

            //create payment part array
            JSONArray jsonPaymentPartsArr = new JSONArray();
            jsonPaymentPartsArr.put(jsonPaymentPart);

            //paymentIdentifiers
            JSONObject jsonPaymentIdent = new JSONObject();
            jsonPaymentIdent.put("field", "CompletionDate");
            jsonPaymentIdent.put("value", "31/10/2012");

            //create payment part array
            JSONArray jsonPaymentIdentArr = new JSONArray();
            jsonPaymentIdentArr.put(jsonPaymentIdent);

            productInfo.put("paymentParts", jsonPaymentPartsArr);
            productInfo.put("paymentIdentifiers", jsonPaymentIdentArr);

            Log.e(TAG, "product Info = " + productInfo.toString());
            return productInfo;


        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String bytesToHexString(byte[] bytes) {
        // http://stackoverflow.com/questions/332079
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');

            }
            sb.append(hex);
        }
        return sb.toString();
    }


    /****************************************************************************************************/


    private PayUCustomBrowserCallback payUCustomBrowserCallback = new PayUCustomBrowserCallback() {

        /**
         * This method will be called after a failed transaction.
         * @param  payuResponse response sent by PayU in App
         * @param merchantResponse response received from Furl
         * */
        @Override
        public void onPaymentFailure(String payuResponse, String merchantResponse) {

            Intent intent = new Intent();
            intent.putExtra(getString(R.string.cb_result), merchantResponse);
            intent.putExtra(getString(R.string.cb_payu_response), payuResponse);
            setResult(Activity.RESULT_CANCELED, intent);
            finish();

        }

        @Override
        public void onPaymentTerminate() {
            finish();
        }

        /**
         * This method will be called after a successful transaction.
         * @param  payuResponse response sent by PayU in App
         * @param merchantResponse response received from Furl
         * */
        @Override
        public void onPaymentSuccess(String payuResponse, String merchantResponse) {
            Intent intent = new Intent();
            intent.putExtra(getString(R.string.cb_result), merchantResponse);
            intent.putExtra(getString(R.string.cb_payu_response), payuResponse);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }

        @Override
        public void onCBErrorReceived(int code, String errormsg) {
        }

        @Override
        public void setCBProperties(WebView webview, Bank payUCustomBrowser) {
            webview.setWebChromeClient(new PayUWebChromeClient(payUCustomBrowser));
            webview.setWebViewClient(new PayUWebViewClient(payUCustomBrowser, BuildConfig.MERCHANT_KEY));
            webview.postUrl("https://test.payu.in/_payment", postData.getBytes());
        }

        @Override
        public void onBackApprove() {
            PaymentActivity.this.finish();
        }

        @Override
        public void onBackDismiss() {
            super.onBackDismiss();
        }

        /**
         * This callback method will be invoked when setDisableBackButtonDialog is set to true.
         * @param alertDialogBuilder a reference of AlertDialog.Builder to customize the dialog
         * */
        @Override
        public void onBackButton(AlertDialog.Builder alertDialogBuilder) {
            super.onBackButton(alertDialogBuilder);
        }

        @Override
        public void initializeMagicRetry(Bank payUCustomBrowser, WebView webview, MagicRetryFragment magicRetryFragment) {
            webview.setWebViewClient(new PayUWebViewClient(payUCustomBrowser, magicRetryFragment, ""));
            Map<String, String> urlList = new HashMap<String, String>();
            urlList.put("https://test.payu.in/_payment", postData);
            payUCustomBrowser.setMagicRetry(urlList);
        }
    };


    private void pay() {
        postData = "&txnid=" + transaction_Id +
                "&device_type=1" +
                "&ismobileview=1" +
                "&productinfo=" + product_info +
                "&user_credentials=" + user_credentials +
                "&key=" + BuildConfig.MERCHANT_KEY +
                "&instrument_type=Put here Device info " +
                "&surl=" + s_Url +
                "&furl=" + f_Url + "" +
                "&instrument_id=7dd17561243c202" +
                "&firstname=" + f_Name +
                "&email=" + email +
                "&phone=" + phone +
                "&amount=" + amount +
//                "&bankcode=PAYUW" + //for PayU Money
//                "&pg=WALLET"+//for PayU Money
                "&hash=";
        generateHashFromServer();
    }

    public void generateHashFromServer() {

        // lets create the post params

        StringBuffer postParamsBuffer = new StringBuffer();
        postParamsBuffer.append(concatParams("key", BuildConfig.MERCHANT_KEY));
        postParamsBuffer.append(concatParams("amount", amount));
        postParamsBuffer.append(concatParams("txnid", transaction_Id));
        postParamsBuffer.append(concatParams("email", email));
        postParamsBuffer.append(concatParams("productinfo", product_info));
        postParamsBuffer.append(concatParams("firstname", f_Name));
        postParamsBuffer.append(concatParams("udf1", udf1));
        postParamsBuffer.append(concatParams("udf2", udf2));
        postParamsBuffer.append(concatParams("udf3", udf3));
        postParamsBuffer.append(concatParams("udf4", udf4));
        postParamsBuffer.append(concatParams("udf5", udf5));
        postParamsBuffer.append(concatParams("user_credentials", user_credentials));

        // for offer_key(optional)
        postParamsBuffer.append(concatParams("offer_key", offer_key));
        // for check_isDomestic(oprional)
        postParamsBuffer.append(concatParams("card_bin", cardBin));

        String postParams = postParamsBuffer.charAt(postParamsBuffer.length() - 1) == '&' ? postParamsBuffer.substring(0, postParamsBuffer.length() - 1).toString() : postParamsBuffer.toString();
        // make api call
        GetHashesFromServerTask getHashesFromServerTask = new GetHashesFromServerTask();
        getHashesFromServerTask.execute(postParams);

    }

    protected String concatParams(String key, String value) {
        return key + "=" + value + "&";
    }

    class GetHashesFromServerTask extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... postParams) {

            try {


                URL url = new URL("https://payu.herokuapp.com/get_hash");//replace this url with your server url for hahs generation

                // get the payuConfig first
                String postParam = postParams[0];

                byte[] postParamsByte = postParam.getBytes("UTF-8");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("Content-Length", String.valueOf(postParamsByte.length));
                conn.setDoOutput(true);
                conn.getOutputStream().write(postParamsByte);

                InputStream responseInputStream = conn.getInputStream();
                StringBuffer responseStringBuffer = new StringBuffer();
                byte[] byteContainer = new byte[1024];
                for (int i; (i = responseInputStream.read(byteContainer)) != -1; ) {
                    responseStringBuffer.append(new String(byteContainer, 0, i));
                }

                JSONObject response = new JSONObject(responseStringBuffer.toString());
                return response.getString("payment_hash");


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String payment_hash) {
            postData=postData + payment_hash;
            setUpPay();
        }
    }


    /******************************************************************************************************/

}

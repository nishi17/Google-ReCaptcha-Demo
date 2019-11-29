package com.demo.demo_recaptcha;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.demo.demo_recaptcha.Retrofit.RetrofitAdapter;
import com.demo.demo_recaptcha.Retrofit.RetrofitServers;
import com.demo.demo_recaptcha.Retrofit.requestPara.VerifyData;
import com.demo.demo_recaptcha.Retrofit.responsePara.VerifyResponse;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {


    /*Label  - nishi.gq (buy domain nishi.gq for free from https://my.freenom.com )
    reCAPTCHA type  - reCAPTCHA v2 android
    Domains  - 	   com.demo.demo_recaptcha

    SITE KEY -6LfUsboUAAAAAHAoOP5vZvCg4JM-t-wAOM9o94uZ

    SECRET KEY - 6LfUsboUAAAAAGFXUAJJqsO4PjxsfCQ80eSci3uf*/

    Button btnRequest;
    public final String SiteKey = "6LfUsboUAAAAAHAoOP5vZvCg4JM-t-wAOM9o94uZ";
    public final String SiteSecretKey = "6LfUsboUAAAAAGFXUAJJqsO4PjxsfCQ80eSci3uf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnRequest = findViewById(R.id.button);
        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(SafetyNet.API)
                .addConnectionCallbacks(MainActivity.this)
                .addOnConnectionFailedListener(MainActivity.this)
                .build();

        mGoogleApiClient.connect();

    }


    public void onClick(View v) {
       /* SafetyNet.SafetyNetApi.verifyWithRecaptcha(mGoogleApiClient, SiteKey)
                .setResultCallback(new ResultCallback() {
                    @Override
                    public void onResult(@NonNull Result result) {
                        Status status = result.getStatus();
                        if ((status != null) && status.isSuccess()) {
                            Toast.makeText(MainActivity.this, "Verification Successful", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(MainActivity.this, MainActivity2.class);
                            startActivity(i);
                        } else {
                            Log.e("MY_APP_TAG", "Error occurred " +
                                    "when communicating with the reCAPTCHA service.");
                        }
                    }


                });*/

        SafetyNet.getClient(this).verifyWithRecaptcha(SiteKey)
                .addOnSuccessListener(new OnSuccessListener<SafetyNetApi.RecaptchaTokenResponse>() {
                    @Override
                    public void onSuccess(SafetyNetApi.RecaptchaTokenResponse recaptchaTokenResponse) {
                        // Indicates communication with reCAPTCHA service was
                        // successful.
                        String userResponseToken = recaptchaTokenResponse.getTokenResult();
                        if (!userResponseToken.isEmpty()) {
                            verifyTokenOnServer(userResponseToken);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e instanceof ApiException) {
                            // An error occurred when communicating with the
                            // reCAPTCHA service. Refer to the status code to
                            // handle the error appropriately.
                            ApiException apiException = (ApiException) e;
                            int statusCode = apiException.getStatusCode();
                            Log.e("TAG ", "Error: " + CommonStatusCodes
                                    .getStatusCodeString(statusCode));
                        } else {
                            // A different, unknown type of error occurred.
                            Log.e("TAG ", "Error: " + e.getMessage());
                        }
                    }
                });


//        SafetyNet.getClient(this).verifyWithRecaptcha(SiteKey)
//                .addOnSuccessListener((Executor) this,
//                        new OnSuccessListener<SafetyNetApi.RecaptchaTokenResponse>() {
//                            @Override
//                            public void onSuccess(SafetyNetApi.RecaptchaTokenResponse response) {
//                                // Indicates communication with reCAPTCHA service was
//                                // successful.
//                                String userResponseToken = response.getTokenResult();
//                                if (!userResponseToken.isEmpty()) {
//                                    if (userResponseToken.equals(SiteSecretKey)) {
//                                        Toast.makeText(MainActivity.this, "Verification Successful", Toast.LENGTH_SHORT).show();
//                                        Intent i = new Intent(MainActivity.this, MainActivity2.class);
//                                        startActivity(i);
//                                    }
//
//                                    // Validate the user response token using the
//                                    // reCAPTCHA siteverify API.
//                                }
//                            }
//                        })
//                .addOnFailureListener((Executor) this, new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        if (e instanceof ApiException) {
//                            // An error occurred when communicating with the
//                            // reCAPTCHA service. Refer to the status code to
//                            // handle the error appropriately.
//                            ApiException apiException = (ApiException) e;
//                            int statusCode = apiException.getStatusCode();
//                            Log.e("TAG ", "Error: " + CommonStatusCodes
//                                    .getStatusCodeString(statusCode));
//                        } else {
//                            // A different, unknown type of error occurred.
//                            Log.e("TAG ", "Error: " + e.getMessage());
//                        }
//                    }
//                });
    }

    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String ip = Formatter.formatIpAddress(inetAddress.hashCode());
                        Log.e("TAG", "***** IP=" + ip);
                        return ip;
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("TAG", ex.toString());
        }
        return null;
    }


    /**
     * Verifying the captcha token on the server
     * Post param: recaptcha-response
     * Server makes call to https://www.google.com/recaptcha/api/siteverify
     * with SECRET Key and Captcha token
     */
/*    public void verifyTokenOnServer(final String token) {
        Log.e("TAG", "Captcha Token" + token);

        StringRequest strReq = new StringRequest(Request.Method.POST, "https://api.androidhive.info/google-recaptcha-verfication.php"
                , new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("TAG", response.toString());
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    String message = jsonObject.getString("message");

                    if (success) {
                        // Congrats! captcha verified successfully on server
                        // TODO - submit the feedback to your server


                    } else {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Error: " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new
                        HashMap<>();
                params.put("recaptcha-response", token);

                return params;
            }
        };


        MyApplication.getInstance().addToRequestQueue(strReq);
    }*/
    private void verifyTokenOnServer(String userResponseToken) {

        VerifyData verifyData = new VerifyData(SiteSecretKey, userResponseToken, getLocalIpAddress());

        String jsonInStringStr = new Gson().toJson(verifyData);

        Log.e("verifyPhone ", jsonInStringStr);

        String url = "https://www.google.com/recaptcha/api/siteverify?secret=" + SiteSecretKey + "&response=" + userResponseToken;

        Log.e("verifyPhone ", url);

        RetrofitServers retrofitServers = RetrofitAdapter.getFlyrrServices();

        Call<VerifyResponse> call = retrofitServers.verify(url);

        call.enqueue(new Callback<VerifyResponse>() {
            @Override
            public void onResponse(Call<VerifyResponse> call, Response<VerifyResponse> response) {


                int statusCode = response.code();

                Log.e("signUpResponse", String.valueOf(statusCode));
                boolean isSuccess = response.isSuccessful();

                VerifyResponse verifyResponse = response.body();

                Gson gson = new Gson();
//
                String jsonInStringStr = gson.toJson(verifyResponse);

                Log.e("VerifyResponse -  ", jsonInStringStr);


                if (verifyResponse.isSuccess()) {
                    Toast.makeText(MainActivity.this, "Verification Successful", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(MainActivity.this, MainActivity2.class);
                    startActivity(i);

                }


            }

            @Override
            public void onFailure(Call<VerifyResponse> call, Throwable t) {


            }

        });


    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Toast.makeText(this, "onConnected()", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this,
                "onConnectionSuspended: " + i,
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this,
                "onConnectionFailed():\n" + connectionResult.getErrorMessage(),
                Toast.LENGTH_LONG).show();
    }

}


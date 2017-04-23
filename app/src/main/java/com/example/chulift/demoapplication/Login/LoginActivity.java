package com.example.chulift.demoapplication.Login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chulift.demoapplication.MenusActivity;
import com.example.chulift.demoapplication.Permission.PermissionRequest;
import com.example.chulift.demoapplication.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.android.OpenCVLoader;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }
    private static final String TAGOpenCV = "LoginActivity";

    static {
        if (!OpenCVLoader.initDebug()) {

            Log.d(TAGOpenCV, "OpenCV not loaded");
        } else {
            Log.d(TAGOpenCV, "OpenCV loaded");
        }
    }


    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private static User member;
    // --Commented out by Inspection (4/23/2017 7:05 AM):private final String url = "http://158.108.207.4/sp_ScoringSystem/getUser.php";
    String postBody = "";
    ProgressDialog progressDialog;

    @BindView(R.id.err_login)TextView _errLoginTxt;
    @BindView(R.id.input_email)
    EditText _emailText;
    @BindView(R.id.input_password)
    EditText _passwordText;
    @BindView(R.id.btn_login)
    Button _loginButton;
    @BindView(R.id.link_sign_up)
    TextView _signupLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        PermissionRequest.verifyPermissions(this);
        ButterKnife.bind(this);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        _emailText.setText("tangtang@hotmail.com");
        _passwordText.setText("26842684");
        _errLoginTxt.setText("");

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        final String email = _emailText.getText().toString();
        final String password = _passwordText.getText().toString();

        // TODO: Implement your own authentication logic here.

        postBody = "{\""+"user_email\""+":\""+email+"\","+"\"user_password\""+":\""+password+"\"}";
        AsyncTaskGetData taskGetUser = new AsyncTaskGetData("http://158.108.207.4/sp_ScoringSystem/postUser.php", postBody);
        taskGetUser.execute();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }
        return valid;
    }
    public class AsyncTaskGetData extends AsyncTask<Object, Object, JSONObject> {
        String Url,postBody;
        JSONObject result;
        public  AsyncTaskGetData(String url,String postBody){
            this.Url = url;
            this.postBody = postBody;
        }
        @Override
        protected JSONObject doInBackground(Object... params) {
            try {
                final MediaType Json = MediaType.parse("application/json; charset=utf-8");
                Request.Builder builder = new Request.Builder();

                Request request = builder
                        .url(Url)
                        .post(RequestBody.create(Json,postBody))
                        .build();

                OkHttpClient client = new OkHttpClient();

                Response response = client.newCall(request).execute();

                Log.d("Status of sever",response.toString());
                try {
                    result = new JSONObject(response.body().string());
                }catch (Exception e) {
                    Log.e(TAG, "Json Error: " + e.getLocalizedMessage());
                    return null;
                }
                Log.d("Value",result.getString("user_email"));
            } catch (UnknownHostException | UnsupportedEncodingException e) {
                Log.e(TAG, "Error: " + e.getLocalizedMessage());
                return null;
            } catch (Exception e) {
                Log.e(TAG, "Other Error: " + e.getLocalizedMessage());
                return null;
            }
            return result;

        }

        @Override
        protected void onProgressUpdate(Object... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if(jsonObject!=null) {
                String email,password,name,surname;
                try {
                    email = jsonObject.getString("user_email");
                    password = jsonObject.getString("user_password");
                    name = jsonObject.getString("name");
                    surname = jsonObject.getString("surname");
                    member = new User(email,password,name,surname);
                    member.setDataIsSet(true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                onLoginSuccess();
                startActivity(new Intent(getApplicationContext(), MenusActivity.class));
                finish();
            }else {
                onLoginFailed();
                _errLoginTxt.setText("Email or password is incorrect. Please retype.");
            }
            progressDialog.dismiss();
        }
    }
    public static User getUser(){
        return member;
    }
}

package com.physis.foot.stretching;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.physis.foot.stretching.data.HospitalInfo;
import com.physis.foot.stretching.dialog.LoadingDialog;
import com.physis.foot.stretching.http.HttpAsyncTaskActivity;
import com.physis.foot.stretching.http.HttpPacket;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends HttpAsyncTaskActivity implements View.OnClickListener {

    private static final String TAG = LoginActivity.class.getSimpleName();

    private EditText etEmail, etPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();

        etEmail.setText("temp@temp.com");
        etPwd.setText("123456");
    }

    @Override
    protected void onHttpResponse(String url, JSONObject resObj) {
        super.onHttpResponse(url, resObj);
        if(url.equals(HttpPacket.MANAGER_LOGIN_URL)){
            Toast.makeText(getApplicationContext(), "관리자 로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show();
            setHospitalInfo(resObj);
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_login) {
            accessManager();
        }
    }

    private void setHospitalInfo(JSONObject resObj){
        try {
            JSONObject obj = resObj.getJSONArray(HttpPacket.PARAMS_ROWS).getJSONObject(0);
            HospitalInfo.getInstance().setInfo(
                    obj.getString(HttpPacket.PARAMS_HOSPITAL_CODE),
                    obj.getString(HttpPacket.PARAMS_NAME)
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void accessManager() {
        if(etEmail.length() == 0 || etPwd.length() == 0) {
            Toast.makeText(this, "로그인 정보를 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject params = new JSONObject();
        try {
            params.put(HttpPacket.PARAMS_EMAIL, etEmail.getText().toString());
            params.put(HttpPacket.PARAMS_PWD, etPwd.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestAPI(HttpPacket.MANAGER_LOGIN_URL, params);
        LoadingDialog.show(LoginActivity.this, "Manager Login..");
    }


    private void init() {
        etEmail = findViewById(R.id.et_email);
        etPwd = findViewById(R.id.et_pwd);

        Button btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);
    }

}
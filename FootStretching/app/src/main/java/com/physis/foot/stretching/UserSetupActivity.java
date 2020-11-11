package com.physis.foot.stretching;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.physis.foot.stretching.data.HospitalInfo;
import com.physis.foot.stretching.data.UserInfo;
import com.physis.foot.stretching.dialog.LoadingDialog;
import com.physis.foot.stretching.http.HttpAsyncTaskActivity;
import com.physis.foot.stretching.http.HttpPacket;
import com.physis.foot.stretching.list.UserAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class UserSetupActivity extends HttpAsyncTaskActivity implements View.OnClickListener, UserAdapter.OnSelectedUserListener {

    private static final String TAG = UserSetupActivity.class.getSimpleName();

    private EditText etSearchWord, etUserName, etUserPhone;
    private TextView tvNotifyUsers;
    private Button btnSetup;

    private UserAdapter userAdapter;
    private UserInfo selectUser = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setup);

        init();
        initObject();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onHttpResponse(String url, JSONObject resObj) {
        super.onHttpResponse(url, resObj);
        LoadingDialog.dismiss();
        try {
            switch (url) {
                case HttpPacket.GET_USERs_URL:
                    setUserList(resObj.getJSONArray(HttpPacket.PARAMS_ROWS));
                    break;
                case HttpPacket.REGISTER_USER_URL:
                    Toast.makeText(getApplicationContext(), "환자정보가 등록되었습니다.", Toast.LENGTH_SHORT).show();
                    initObject();
                    break;
                case HttpPacket.UPDATE_USER_INFO_URL:
                    Toast.makeText(getApplicationContext(), "환자정보가 갱신되었습니다.", Toast.LENGTH_SHORT).show();
                    initObject();
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSelectUser(UserInfo info) {
        btnSetup.setText("update");
        selectUser = info;
        etUserName.setText(selectUser.getName());
        etUserPhone.setText(selectUser.getPhone());
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_setup) {
            if(etUserName.length() == 0 || etUserPhone.length() == 0){
                Toast.makeText(getApplicationContext(), "환자 이름 및 전화번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                return;
            }
            if(selectUser == null){
                registerUser(etUserName.getText().toString(), etUserPhone.getText().toString());
            }else{
                updateUser(etUserName.getText().toString(), etUserPhone.getText().toString());
            }
        }else if(view.getId() == R.id.iv_btn_renew){
            initObject();
        }
    }

    private void initObject(){
        getUserList();
        btnSetup.setText("register");
        selectUser = null;
        etUserName.setText(null);
        etUserPhone.setText(null);
        etSearchWord.setText(null);
    }

    private void updateUser(String name, String phone){

        JSONObject params = new JSONObject();
        try {
            params.put(HttpPacket.PARAMS_USER_CODE, selectUser.getCode());
            params.put(HttpPacket.PARAMS_USER_NAME, name);
            params.put(HttpPacket.PARAMS_USER_PHONE, phone);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestAPI(HttpPacket.UPDATE_USER_INFO_URL, params);
    }

    private void registerUser(String name, String phone){
        JSONObject params = new JSONObject();
        try {
            params.put(HttpPacket.PARAMS_HOSPITAL_CODE, HospitalInfo.getInstance().getCode());
            params.put(HttpPacket.PARAMS_USER_NAME, name);
            params.put(HttpPacket.PARAMS_USER_PHONE, phone);
            requestAPI(HttpPacket.REGISTER_USER_URL, params);
            LoadingDialog.show(this, "Register User..");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setUserList(JSONArray rows) {
        List<UserInfo> infos = new LinkedList<>();
        for(int i = 0; i < rows.length(); i++){
            try {
                JSONObject obj = rows.getJSONObject(i);
                infos.add(new UserInfo(
                        obj.getString(HttpPacket.PARAMS_USER_CODE),
                        obj.getString(HttpPacket.PARAMS_USER_NAME),
                        obj.getString(HttpPacket.PARAMS_USER_PHONE)
                ));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        tvNotifyUsers.setVisibility(infos.size() == 0 ? View.VISIBLE : View.GONE);
        userAdapter.setItems(infos);
    }

    private void getUserList(){
        JSONObject params = new JSONObject();
        try {
            params.put(HttpPacket.PARAMS_HOSPITAL_CODE, HospitalInfo.getInstance().getCode());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestAPI(HttpPacket.GET_USERs_URL, params);
        LoadingDialog.show(this, "Get patient list..");
    }

    private void init() {
        etSearchWord = findViewById(R.id.et_search_name);
        etUserName = findViewById(R.id.et_name);
        etUserPhone = findViewById(R.id.et_phone);

        btnSetup = findViewById(R.id.btn_setup);
        btnSetup.setOnClickListener(this);
        ImageView ivBtnRenew = findViewById(R.id.iv_btn_renew);
        ivBtnRenew.setOnClickListener(this);

        LinearLayoutManager userLayoutManager = new LinearLayoutManager(getApplicationContext());
        userLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        userLayoutManager.setItemPrefetchEnabled(true);
        RecyclerView rcUser = findViewById(R.id.rv_users);
        rcUser.setLayoutManager(userLayoutManager);
        rcUser.setAdapter(userAdapter = new UserAdapter());
        userAdapter.setOnSelectedUserListener(this);

        tvNotifyUsers = findViewById(R.id.tv_notify_users);

        etSearchWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                userAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

}
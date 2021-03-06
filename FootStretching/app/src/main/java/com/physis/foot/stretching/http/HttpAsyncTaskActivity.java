package com.physis.foot.stretching.http;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.physis.foot.stretching.dialog.LoadingDialog;

import org.json.JSONArray;
import org.json.JSONObject;

public class HttpAsyncTaskActivity extends AppCompatActivity {

    protected void requestAPI(String url, JSONObject params){
        HttpAsyncTask requester = new HttpAsyncTask(url, params.toString());
        requester.setOnResponseListener(responseListener);
        requester.execute();
    }

    protected void requestAPI(String url, JSONArray params){
        HttpAsyncTask requester = new HttpAsyncTask(url, params.toString());
        requester.setOnResponseListener(responseListener);
        requester.execute();
    }

    protected void requestAPI(String url){
        HttpAsyncTask requester = new HttpAsyncTask(url, null);
        requester.setOnResponseListener(responseListener);
        requester.execute();
    }


    final HttpAsyncTask.OnResponseListener responseListener = new HttpAsyncTask.OnResponseListener() {
        @Override
        public void onResponseListener(String url, String responseData) {
            try{
                if(responseData == null){
                    showErrorToast(null);
                }else{
                    JSONObject resObj = new JSONObject(responseData);
                    if(resObj.getString(HttpPacket.PARAMS_RESULT).equals(REQ_SUCCESS)){
                        onHttpResponse(url, resObj);
                    }else {
                        showErrorToast(resObj.getString(HttpPacket.PARAMS_RESULT));
                    }
                }
            }catch (Exception e){
                LoadingDialog.dismiss();
                e.printStackTrace();
            }
        }
    };

    protected void onHttpResponse(String url, JSONObject resObj){

    }

    private static final String REQ_SUCCESS  = "1001";
    private static final String ERR_DB_CONNECT = "1002";
    private static final String ERR_DB_QUERY = "1003";
    private static final String ERR_NO_EXIST_EMAIL = "1102";
    private static final String ERR_PWD = "1103";

    private void showErrorToast(String errCode){
        LoadingDialog.dismiss();
        String errMsg;
        if(errCode == null){
            errMsg = "서버와 통신이 불안정합니다.";
        }else{
            switch (errCode){
                case  ERR_DB_CONNECT:
                    errMsg = "서버 DB와 연결에 실패하였습니다.\n잠시 후 다시 시도해주세요.";
                    break;
                case  ERR_DB_QUERY:
                    errMsg = "올바른 입력 정보가 아닙니다.";
                    break;
                case  ERR_NO_EXIST_EMAIL:
                    errMsg = "등록되지 않은 이메일 정보입니다..";
                    break;
                case  ERR_PWD:
                    errMsg = "비밀번호가 일치하지 않습니다.";
                    break;
                default:
                    errMsg = "알수없는 오류가 발생하였습니다.." + errCode;
            }
        }
        Toast.makeText(getApplicationContext(), errMsg, Toast.LENGTH_SHORT).show();
    }
}

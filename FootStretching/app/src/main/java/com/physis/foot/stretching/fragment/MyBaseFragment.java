package com.physis.foot.stretching.fragment;

import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.physis.foot.stretching.dialog.LoadingDialog;
import com.physis.foot.stretching.http.HttpAsyncTask;
import com.physis.foot.stretching.http.HttpPacket;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Objects;

public class MyBaseFragment extends Fragment {

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
                default:
                    errMsg = "알수없는 오류가 발생하였습니다.";
            }
        }
        Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), errMsg, Toast.LENGTH_SHORT).show();
    }
}

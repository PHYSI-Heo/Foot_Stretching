package com.physis.foot.stretching;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.physis.foot.stretching.data.HospitalInfo;
import com.physis.foot.stretching.data.PatternInfo;
import com.physis.foot.stretching.data.ScheduleInfo;
import com.physis.foot.stretching.data.UserInfo;
import com.physis.foot.stretching.dialog.LoadingDialog;
import com.physis.foot.stretching.dialog.MyAlertDialog;
import com.physis.foot.stretching.http.HttpAsyncTaskActivity;
import com.physis.foot.stretching.http.HttpPacket;
import com.physis.foot.stretching.list.PatternAdapter;
import com.physis.foot.stretching.list.ScheduleAdapter;
import com.physis.foot.stretching.list.UserAdapter;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class ScheduleActivity extends HttpAsyncTaskActivity implements UserAdapter.OnSelectedUserListener, PatternAdapter.OnSelectedPatternListener, ScheduleAdapter.OnSelectedScheduleListener, View.OnClickListener {

    private static final String TAG = ScheduleActivity.class.getSimpleName();

    private MaterialCalendarView mcvCalender;
    private TextView tvNotifyUsers, tvNotifySchedule, tvNotifyPatterns;
    private Button btnDelSchedule;

    private UserAdapter userAdapter;
    private ScheduleAdapter scheduleAdapter;
    private PatternAdapter patternAdapter;
    private MyAlertDialog myAlertDialog;

    private List<ScheduleInfo> scheduleInfos = new LinkedList<>();

    private UserInfo selectedUserInfo = null;
    private PatternInfo selectedPatternInfo = null;
    private int selectScheduleIndex = -1;
    private String selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        init();
        getUserList();
        getPatterList();
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
                case HttpPacket.GET_MY_PATTERNs_URL:
                    setPatternList(resObj.getJSONArray(HttpPacket.PARAMS_ROWS));
                    break;
                case HttpPacket.GET_SCHEDULEs_URL:
                    setScheduleList(resObj.getJSONArray(HttpPacket.PARAMS_ROWS));
                    break;
                case HttpPacket.ADD_SCHEDULE_URL:
                    Toast.makeText(getApplicationContext(), "운통 일정이 추가되었습니다.", Toast.LENGTH_SHORT).show();
                    getUserSchedules(selectedUserInfo.getCode());
                    break;
                case HttpPacket.UPDATE_SCHEDULE_URL:
                    Toast.makeText(getApplicationContext(), "운통 일정이 변경되었습니다.", Toast.LENGTH_SHORT).show();
                    updateScheduleInfo();
                    break;
                case HttpPacket.DELETE_PATTERNs_URL:
                    Toast.makeText(getApplicationContext(), "운통 일정이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    getUserSchedules(selectedUserInfo.getCode());
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_setup_schedule){
            setupSchedule();
        }else if(view.getId() == R.id.btn_del_schedule){
            deleteSchedule();
        }
    }

    @Override
    public void onSelectUser(UserInfo info) {
        selectedUserInfo = info;
        getUserSchedules(info.getCode());
    }

    @Override
    public void onSelectedPattern(PatternInfo info) {
        selectedPatternInfo = info;
    }

    @Override
    public void onEditPattern(PatternInfo info) {
        // --
    }

    @Override
    public void onScheduleInfo(int position) {
        selectScheduleIndex = position;
        btnDelSchedule.setVisibility(position != -1 ? View.VISIBLE : View.GONE);
        if(position != -1){
            String[] dates = scheduleInfos.get(position).getDateTime().split("-");
            mcvCalender.setSelectedDate(CalendarDay.from(Integer.parseInt(dates[0]), Integer.parseInt(dates[1]) - 1, Integer.parseInt(dates[2])));
            patternAdapter.setSelectItem(scheduleInfos.get(position).getPatternCode());
            btnDelSchedule.setEnabled(!scheduleInfos.get(position).getFulfill());
        }
    }

    @Override
    public void onStartSchedule(final ScheduleInfo info) {
        final MyAlertDialog dialog = new MyAlertDialog();
        dialog.show(ScheduleActivity.this, "재활운동 시작",
                info.getDateTime() + " (" + info.getPatternName() + ") 운동 일정을 시작합니다.",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialog.dismiss();
                        startStretchingActivity(info);
                    }
                });
    }


    private void updateUserInfo(String code, String name, String phone) throws JSONException {
        if(name == null || name.length() == 0 || phone == null || phone.length() == 0){
            Toast.makeText(getApplicationContext(), "사용자 이름 / 전화번호를 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject params = new JSONObject();
        params.put(HttpPacket.PARAMS_USER_CODE, code);
        params.put(HttpPacket.PARAMS_USER_NAME, name);
        params.put(HttpPacket.PARAMS_USER_PHONE, phone);
        requestAPI(HttpPacket.UPDATE_USER_INFO_URL, params);
    }

    private void startStretchingActivity(ScheduleInfo info){
        startActivity(new Intent(ScheduleActivity.this, MemberUserActivity.class)
                .putExtra(HttpPacket.PARAMS_USER_NAME, selectedUserInfo.getName())
                .putExtra(HttpPacket.PARAMS_USER_PHONE, selectedUserInfo.getPhone())
                .putExtra(HttpPacket.PARAMS_DATE_TIME, info.getDateTime())
                .putExtra(HttpPacket.PARAMS_PATTERN_NAME, info.getPatternName())
                .putExtra(HttpPacket.PARAMS_PATTERN_CODE, info.getPatternCode()));
    }

    private void deleteSchedule(){
        if(selectScheduleIndex == -1)
            return;

        JSONObject params = new JSONObject();
        try {
            params.put(HttpPacket.PARAMS_NO, scheduleInfos.get(selectScheduleIndex).getNo());
            requestAPI(HttpPacket.DELETE_SCHEDULE_URL, params);
            LoadingDialog.show(ScheduleActivity.this, "Delete Schedule..");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateScheduleInfo(){
        ScheduleInfo info = scheduleInfos.get(selectScheduleIndex);
        info.setDateTime(selectedDate);
        info.setPatternCode(selectedPatternInfo.getCode());
        info.setPatternName(selectedPatternInfo.getName());
        scheduleAdapter.renewSelectItem();
    }

    private void setupSchedule(){
        if(selectedUserInfo == null){
            Toast.makeText(getApplicationContext(), "먼저 사용자를 선택하세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(selectedPatternInfo == null){
            Toast.makeText(getApplicationContext(), "운통패턴을 선택하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        CalendarDay calendarDay = mcvCalender.getSelectedDate();
        selectedDate = calendarDay.getYear() + "-" + (calendarDay.getMonth() + 1) + "-" + calendarDay.getDay();

        JSONObject params = new JSONObject();
        try {
            params.put(HttpPacket.PARAMS_USER_CODE, selectedUserInfo.getCode());
            params.put(HttpPacket.PARAMS_PATTERN_CODE, selectedPatternInfo.getCode());
            params.put(HttpPacket.PARAMS_PATTERN_NAME, selectedPatternInfo.getName());
            params.put(HttpPacket.PARAMS_ORDER, scheduleInfos.size() + 1);
            params.put(HttpPacket.PARAMS_DATE_TIME, selectedDate);

            if(selectScheduleIndex == -1){
                requestAPI(HttpPacket.ADD_SCHEDULE_URL, params);
            }else{
                params.put(HttpPacket.PARAMS_NO, scheduleInfos.get(selectScheduleIndex).getNo());
                requestAPI(HttpPacket.UPDATE_SCHEDULE_URL, params);
            }
            LoadingDialog.show(ScheduleActivity.this, "Setup Schedule..");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void setScheduleList(JSONArray rows) {
        selectScheduleIndex = -1;
        btnDelSchedule.setVisibility(View.GONE);
        scheduleInfos.clear();
        for(int i = 0; i < rows.length(); i++){
            try {
                JSONObject obj = rows.getJSONObject(i);
                scheduleInfos.add(new ScheduleInfo(
                        obj.getString(HttpPacket.PARAMS_NO),
                        obj.getString(HttpPacket.PARAMS_PATTERN_CODE),
                        obj.getString(HttpPacket.PARAMS_PATTERN_NAME),
                        obj.getInt(HttpPacket.PARAMS_ORDER),
                        obj.getString(HttpPacket.PARAMS_DATE_TIME),
                        obj.getInt(HttpPacket.PARAMS_FULFILL)
                ));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.e(TAG, "> Schedule Cnt : " + scheduleInfos.size());
        tvNotifySchedule.setVisibility(scheduleInfos.size() == 0 ? View.VISIBLE : View.GONE);
        scheduleAdapter.setItems(scheduleInfos);
    }

    private void getUserSchedules(String userCode){
        JSONObject params = new JSONObject();
        try {
            params.put(HttpPacket.PARAMS_USER_CODE, userCode);
            requestAPI(HttpPacket.GET_SCHEDULEs_URL, params);
            LoadingDialog.show(ScheduleActivity.this, "Get User Schedule..");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void setPatternList(JSONArray rows){
        List<PatternInfo> infos = new LinkedList<>();
        for(int i = 0; i < rows.length(); i++){
            try {
                JSONObject obj = rows.getJSONObject(i);
                infos.add(new PatternInfo(
                        obj.getString(HttpPacket.PARAMS_PATTERN_CODE),
                        obj.getString(HttpPacket.PARAMS_PATTERN_NAME),
                        obj.getString(HttpPacket.PARAMS_PATTERN_KEYWORD)
                ));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        tvNotifyPatterns.setVisibility(infos.size() == 0 ? View.VISIBLE : View.GONE);
        patternAdapter.setItems(infos);
    }

    private void getPatterList(){
        JSONObject params = new JSONObject();
        try {
            params.put(HttpPacket.PARAMS_HOSPITAL_CODE, HospitalInfo.getInstance().getCode());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestAPI(HttpPacket.GET_MY_PATTERNs_URL, params);
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
        LoadingDialog.show(ScheduleActivity.this, "Get Users..");
    }

    private void init() {
        mcvCalender = findViewById(R.id.mcv_calendar);
        mcvCalender.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2019, 1, 1))
                .setMaximumDate(CalendarDay.from(2022, 1, 1))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        LinearLayoutManager userLayoutManager = new LinearLayoutManager(getApplicationContext());
        userLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        userLayoutManager.setItemPrefetchEnabled(true);
        LinearLayoutManager scheduleLayoutManager = new LinearLayoutManager(getApplicationContext());
        scheduleLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        scheduleLayoutManager.setItemPrefetchEnabled(true);
        LinearLayoutManager patternLayoutManager = new LinearLayoutManager(getApplicationContext());
        patternLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        patternLayoutManager.setItemPrefetchEnabled(true);

        RecyclerView rcUser = findViewById(R.id.rv_users);
        rcUser.setLayoutManager(userLayoutManager);
        rcUser.setAdapter(userAdapter = new UserAdapter());
        userAdapter.setOnSelectedUserListener(this);

        RecyclerView rcSchedule = findViewById(R.id.rv_schedules);
        rcSchedule.setLayoutManager(scheduleLayoutManager);
        rcSchedule.setAdapter(scheduleAdapter = new ScheduleAdapter());
        scheduleAdapter.setOnSelectedScheduleListener(this);

        RecyclerView rcPattern = findViewById(R.id.rv_patterns);
        rcPattern.setLayoutManager(patternLayoutManager);
        rcPattern.setAdapter(patternAdapter = new PatternAdapter());
        patternAdapter.setOnSelectedPatternListener(this);

        tvNotifyUsers = findViewById(R.id.tv_notify_users);
        tvNotifySchedule = findViewById(R.id.tv_notify_schedules);
        tvNotifyPatterns = findViewById(R.id.tv_notify_pattern);

        Button btnSetSchedule = findViewById(R.id.btn_setup_schedule);
        btnSetSchedule.setOnClickListener(this);
        btnDelSchedule = findViewById(R.id.btn_del_schedule);
        btnDelSchedule.setOnClickListener(this);

        EditText etSearchPattern = findViewById(R.id.et_search_pattern);
        etSearchPattern.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                patternAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        EditText etSearchName = findViewById(R.id.et_search_name);
        etSearchName.addTextChangedListener(new TextWatcher() {
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
package net.edrop.edrop_employer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.lljjcoder.style.citylist.Toast.ToastUtils;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import net.edrop.edrop_employer.R;
import net.edrop.edrop_employer.entity.ChatModel;
import net.edrop.edrop_employer.entity.ItemModel;
import net.edrop.edrop_employer.utils.Constant;
import net.edrop.edrop_employer.utils.SystemTransUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import static net.edrop.edrop_employer.utils.Constant.REGISTER_FAIL;
import static net.edrop.edrop_employer.utils.Constant.REGISTER_SUCCESS;

public class RegisterActivity extends AppCompatActivity {
    private TextView etName;
    private TextView etPsd;
    private TextView etPsd2;
    private Button btnReg;
    private OkHttpClient okHttpClient;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == REGISTER_FAIL) {
                Toast.makeText(RegisterActivity.this, msg.obj + "", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new SystemTransUtil().transform(RegisterActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        setLinstener();
        //1.创建OkHttpClient对象
        okHttpClient = new OkHttpClient();
    }

    private void setLinstener() {
        btnReg.setOnClickListener(new MyListener());
    }

    private void initView() {
        getLoginExit();
        etName = findViewById(R.id.et_regName);
        etPsd = findViewById(R.id.et_regPsd);
        etPsd2 = findViewById(R.id.et_regPsd2);
        btnReg = findViewById(R.id.btn_reg);
    }

    private class MyListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_reg:
                    if (etPsd.getText().toString().equals(etPsd2.getText().toString())) {
                        //创建FormBody对象
                        FormBody formBody = new FormBody.Builder()
                                .add("username", etName.getText().toString())
                                .add("password", etPsd.getText().toString())
                                .build();
                        Request request = new Request.Builder()
                                .url(Constant.BASE_URL + "registerEmployeeByUserName")
                                .post(formBody)
                                .build();
                        Call call = okHttpClient.newCall(request);
                        call.enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String string = response.body().string();
                                int state = 0;
                                try {
                                    JSONObject jsonObject = new JSONObject(string);
                                    state = jsonObject.getInt("state");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                if (state == REGISTER_SUCCESS) {
                                    regUser();
                                } else {
                                    Message msg = new Message();
                                    msg.obj = "该用户名已被注册，请更换";
                                    msg.what = REGISTER_FAIL;
                                    mHandler.sendMessage(msg);
                                }
                            }
                        });
                    } else {
                        Toast.makeText(RegisterActivity.this, "密码不一致，请检查", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }

    /**
     * 注册用户（同步需注意）
     */
    private void regUser() {
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    EMClient.getInstance().createAccount(etName.getText().toString().trim(), etPsd.getText().toString().trim());//同步方法
                    subscriber.onNext("注册成功,请登录");
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    subscriber.onNext("注册失败错误码：" + e.getErrorCode());
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Toast.makeText(RegisterActivity.this, s, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
    }

    /**
     * 退出登录
     */
    private void getLoginExit() {
        EMClient.getInstance().logout(true, new EMCallBack() {
            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgress(int progress, String status) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onError(int code, String message) {
                // TODO Auto-generated method stub
            }
        });
    }
}

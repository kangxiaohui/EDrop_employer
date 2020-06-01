package net.edrop.edrop_employer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import net.edrop.edrop_employer.R;
import net.edrop.edrop_employer.model.Model;
import net.edrop.edrop_employer.utils.Constant;
import net.edrop.edrop_employer.utils.SystemTransUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import xyz.bboylin.universialtoast.UniversalToast;

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
                UniversalToast.makeText(RegisterActivity.this, msg.obj + "", Toast.LENGTH_SHORT).showError();
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
                                    regUser(etName.getText().toString());
                                } else {
                                    Message msg = new Message();
                                    msg.obj = "该用户名已被注册，请更换";
                                    msg.what = REGISTER_FAIL;
                                    mHandler.sendMessage(msg);
                                }
                            }
                        });
                    } else {
                        UniversalToast.makeText(RegisterActivity.this, "密码不一致，请检查", Toast.LENGTH_SHORT).showError();
                    }
                    break;
            }
        }
    }

    /**
     * 注册用户（同步需注意）
     */
    private void regUser(final String user) {
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().createAccount(user, user);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            UniversalToast.makeText(RegisterActivity.this, "注册环信成功", Toast.LENGTH_SHORT).showSuccess();
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            UniversalToast.makeText(RegisterActivity.this, "注册环信失败", Toast.LENGTH_SHORT).showError();
                        }
                    });
                }
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

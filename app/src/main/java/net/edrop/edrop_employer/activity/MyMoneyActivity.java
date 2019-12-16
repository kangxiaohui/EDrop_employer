package net.edrop.edrop_employer.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import net.edrop.edrop_employer.R;
import net.edrop.edrop_employer.entity.Wallet;
import net.edrop.edrop_employer.utils.SharedPreferencesUtils;
import net.edrop.edrop_employer.utils.SystemTransUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static net.edrop.edrop_employer.utils.Constant.BASE_URL;

public class MyMoneyActivity extends AppCompatActivity {
    private LinearLayout Kongbai;
    private LinearLayout SaveMoney;
    private LinearLayout GetMoney;
    private LinearLayout One;
    private LinearLayout Two;
    private LinearLayout Three;
    private LinearLayout Four;
    private TextView tvMoney;
    private ImageView ivBack;
    private Button btnGoSaveMoney;
    private OkHttpClient okHttpClient;
    private Button btnGoGetMoney;
    private RadioGroup rgGoSaveMoney = null;
    private RadioGroup rgGoGetMoney = null;
    private SharedPreferencesUtils sharedPreferences;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 666) {

                String json = (String) msg.obj;
                Wallet wallet = new Gson().fromJson(json,Wallet.class);
                tvMoney.setText(wallet.getMoney()+"");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new SystemTransUtil().transform(MyMoneyActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_money);
        initView();
        setLinstener();
        initData();
        getMoney();
    }
    private void addMoney(int money) {
        //2.创建Request对象
        Request request = new Request.Builder().url(BASE_URL + "modifyMoney?uid=" + sharedPreferences.getInt("userId")+"&money="+money+"&state=1").build();
        //3.创建Call对象
        final Call call = okHttpClient.newCall(request);

        //4.发送请求 获得响应数据
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();//打印异常信息
            }

            //请求成功时回调
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();

            }
        });


    }
    private void delMoney(int money) {
        //2.创建Request对象
        Request request = new Request.Builder().url(BASE_URL + "modifyMoney?uid=" + sharedPreferences.getInt("userId")+"&money="+money+"&state=0").build();
        //3.创建Call对象
        final Call call = okHttpClient.newCall(request);

        //4.发送请求 获得响应数据
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();//打印异常信息
            }

            //请求成功时回调
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();

            }
        });


    }
    private void getMoney() {
        //2.创建Request对象
        Request request = new Request.Builder().url(BASE_URL + "getSurplus?uid=" + sharedPreferences.getInt("userId")).build();
        //3.创建Call对象
        final Call call = okHttpClient.newCall(request);

        //4.发送请求 获得响应数据
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();//打印异常信息
            }

            //请求成功时回调
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                Message message = new Message();
                message.what = 666;
                message.obj = str;
                handler.sendMessage(message);
            }
        });


    }

    private void initData() {
        One.setVisibility(View.GONE); // 隐藏
        Two.setVisibility(View.GONE);
        Three.setVisibility(View.GONE);
        Four.setVisibility(View.GONE);
        sharedPreferences = new SharedPreferencesUtils(MyMoneyActivity.this, "loginInfo");

    }

    private void setLinstener() {
        SaveMoney.setOnClickListener(new MyLinstener());
        GetMoney.setOnClickListener(new MyLinstener());
        ivBack.setOnClickListener(new MyLinstener());
        btnGoGetMoney.setOnClickListener(new MyLinstener());
        btnGoSaveMoney.setOnClickListener(new MyLinstener());
        Kongbai.setOnClickListener(new MyLinstener());
    }

    private void initView() {
        rgGoGetMoney = findViewById(R.id.rg_go_get_money);
        rgGoSaveMoney = findViewById(R.id.rg_go_save_money);
        SaveMoney = findViewById(R.id.ll_saveMoney);
        GetMoney = findViewById(R.id.ll_getMoney);
        tvMoney = findViewById(R.id.tv_money);
        ivBack = findViewById(R.id.iv_money_back);
        One = findViewById(R.id.ll_money_one);
        Two = findViewById(R.id.ll_money_two);
        Three = findViewById(R.id.ll_money_three);
        Four = findViewById(R.id.ll_money_four);
        btnGoGetMoney = findViewById(R.id.btn_go_get_money);
        btnGoSaveMoney = findViewById(R.id.btn_go_save_money);
        Kongbai = findViewById(R.id.ll_kongbai);
        okHttpClient = new OkHttpClient();
    }

    private class MyLinstener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.iv_money_back:
                    finish();
                    break;
                case R.id.ll_saveMoney:
                    One.setVisibility(View.VISIBLE);
                    Two.setVisibility(View.VISIBLE);
                    Three.setVisibility(View.GONE);
                    Four.setVisibility(View.GONE);
                    break;
                case R.id.ll_getMoney:
                    Three.setVisibility(View.VISIBLE);
                    Four.setVisibility(View.VISIBLE);
                    One.setVisibility(View.GONE);
                    Two.setVisibility(View.GONE);
                    break;
                case R.id.btn_go_get_money:
                    int money = 0;
                    switch (rgGoGetMoney.getCheckedRadioButtonId()) {
                        case R.id.rb_4:
                            money = 10;
                            delMoney(money);
                            break;
                        case R.id.rb_5:
                            money = 20;
                            delMoney(money);
                            break;
                        case R.id.rb_6:
                            money = 30;
                            delMoney(money);
                            break;
                    }
                    if (Double.valueOf(tvMoney.getText().toString()) < money) {
                        Toast.makeText(MyMoneyActivity.this, "余额不足，禁止提款", Toast.LENGTH_SHORT).show();
                    } else {
                        tvMoney.setText(Double.valueOf(tvMoney.getText().toString()) - money + "");
                        Toast.makeText(MyMoneyActivity.this, "提款成功", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.btn_go_save_money:
                    int money1 = 0;
                    switch (rgGoSaveMoney.getCheckedRadioButtonId()) {
                        case R.id.rb_1:
                            money1 = 10;
                            addMoney(money1);
                            break;
                        case R.id.rb_2:
                            money1 = 20;
                            addMoney(money1);
                            break;
                        case R.id.rb_3:
                            money1 = 30;
                            addMoney(money1);
                            break;
                    }
                    tvMoney.setText(Double.valueOf(tvMoney.getText().toString()) + money1 + "");
                    Toast.makeText(MyMoneyActivity.this, "充值成功", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.ll_kongbai:
                    One.setVisibility(View.GONE); // 隐藏
                    Two.setVisibility(View.GONE);
                    Three.setVisibility(View.GONE);
                    Four.setVisibility(View.GONE);
                    break;
            }
        }
    }
}

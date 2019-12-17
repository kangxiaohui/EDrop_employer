package net.edrop.edrop_employer.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import net.edrop.edrop_employer.R;
import net.edrop.edrop_employer.utils.SystemTransUtil;

import static com.mob.MobSDK.getContext;


/**
 * Created by 李诗凡.
 * User: sifannnn
 * Date: 2019/12/17
 * Time: 14:17
 * TODO：商务合作的Activity
 */
public class CooperationActivity extends AppCompatActivity {
    private ImageView imageViewback;
    private ImageView imageView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        new SystemTransUtil().trans(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cooperation);
        findViews();
        setOnclick();
    }

    //设置点击事件
    private void setOnclick() {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "已分享到你的朋友圈！", Toast.LENGTH_SHORT).show();
            }
        });
        imageViewback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    //获取控件对象
    private void findViews() {
        imageView = findViewById(R.id.iv_cooperation);
        imageViewback = findViewById(R.id.iv_cooperation_back);
    }


}
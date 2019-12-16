package net.edrop.edrop_employer.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import net.edrop.edrop_employer.R;
import net.edrop.edrop_employer.adapter.FeedBackOptionAdapter;
import net.edrop.edrop_employer.utils.SystemTransUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李诗凡.
 * User: sifannnn
 * Date: 2019/12/9
 * Time: 14:33
 * TODO：反馈的详细类别选项页面
 */
public class FeedBackOption extends AppCompatActivity {
    private ListView listView;
    private ImageView imageView;
    private List data;
    private FeedBackOptionAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        new SystemTransUtil().transform(FeedBackOption.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_option);
        initData();
        findView();
        setAdapter();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initData() {
        String[] arr = {"垃圾分类的信息有误", "拍照识别有误", "预约过程出现问题", "我的钱包出现异常", "社区动态无法刷新",
                "我不喜欢现在看到的内容", "客户端闪退，无法使用", "其他"};
        data = new ArrayList();
        for (int i = 0; i < arr.length; i++) {
            data.add(arr[i]);
        }
    }

    private void setAdapter() {
        adapter = new FeedBackOptionAdapter(
                this,
                data,
                R.layout.item_feedback_option
        );
        listView.setAdapter(adapter);
    }

    private void findView() {
        listView = findViewById(R.id.lv_feedbackoption);
        imageView = findViewById(R.id.iv_feedback_option_back);
    }
}

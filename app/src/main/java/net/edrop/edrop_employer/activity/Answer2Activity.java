package net.edrop.edrop_employer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import net.edrop.edrop_employer.R;
import net.edrop.edrop_employer.adapter.AnswerAdapter;
import net.edrop.edrop_employer.callBack.MyCallBack;
import net.edrop.edrop_employer.entity.Competition;
import net.edrop.edrop_employer.utils.SystemTransUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Answer2Activity extends AppCompatActivity {
    private List<Competition> lists = new ArrayList<>();
    private List<Competition> list2 = new ArrayList<>();
    private List listHisAnswer = new ArrayList<>();
    private int a;
    private RecyclerView text_rv;
    private TextView currentNum;
    private AnswerAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new SystemTransUtil().transform(Answer2Activity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer2);
        findViews();
        initData();
        initRv();
    }

    public void initData() {
        a = 0;
        currentNum.setText(a + 1 + "");
        list2 = (ArrayList<Competition>) getIntent().getSerializableExtra("lists");
        Competition competition = new Competition(list2.get(a).getId(),list2.get(a).getQuestion(),
                list2.get(a).getTypeId(),list2.get(a).getType());
        lists.add(competition);
    }

    private void initRv() {
        text_rv.setLayoutManager(new LinearLayoutManager(this));
        myAdapter = new AnswerAdapter(Answer2Activity.this);
        myAdapter.setData(lists);
        text_rv.setAdapter(myAdapter);
    }

    public void findViews() {
        text_rv = findViewById(R.id.text_rv);
        currentNum = findViewById(R.id.cureentNum);
    }

    public void change() {
        if (a < 9) {
            currentNum.setText(a + 2 + "");
        } else {
            currentNum.setText(10 + "");
        }
        a++;
        Log.e("a", a + "");
        judge();
        List<Competition> oldList = myAdapter.getData();
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new MyCallBack(oldList, lists), true);
        myAdapter.setData(lists);
        result.dispatchUpdatesTo(myAdapter);
    }

    public void judge() {
        if (a > 9) {
            Intent intent = new Intent(Answer2Activity.this, net.edrop.edrop_employer.activity.Answer3Activity.class);
            intent.putExtra("lists", (Serializable) list2);
            intent.putExtra("listHis", (Serializable) listHisAnswer);
            startActivity(intent);
            overridePendingTransition(R.anim.anim_in_right, R.anim.anim_out_left);
        } else {
            lists.clear();
            Competition bean1 = list2.get(a);
            lists.add(bean1);
        }
    }


    public void buttonBack(View view) {
        switch (view.getId()) {
            case R.id.pic_recycle:
                listHisAnswer.add(1);
                change();
                break;
            case R.id.pic_gan:
                listHisAnswer.add(2);
                change();
                break;
            case R.id.pic_harm:
                listHisAnswer.add(3);
                change();
                break;
            case R.id.pic_certain:
                listHisAnswer.add(4);
                change();
                break;
        }
    }

}

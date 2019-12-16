package net.edrop.edrop_employer.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.edrop.edrop_employer.R;
import net.edrop.edrop_employer.adapter.RecognitionAdapter;
import net.edrop.edrop_employer.adapter.ServiceAdapter;
import net.edrop.edrop_employer.entity.NewsList;
import net.edrop.edrop_employer.entity.Recognition;
import net.edrop.edrop_employer.entity.RecognitionResult;
import net.edrop.edrop_employer.utils.SystemTransUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecognitionResultActivity extends Activity {
    private ImageView imgPhoto;
    private List<NewsList> dataSource;
    private ListView listView;
    private TextView tvNoFind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new SystemTransUtil().trans(RecognitionResultActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recognition_result);
        initViews();
        setListener();
        Intent intent = getIntent();
        byte buf[] = intent.getByteArrayExtra("photo_bmp");
        Bitmap photo_bmp = BitmapFactory.decodeByteArray(buf, 0, buf.length);
        imgPhoto.setImageBitmap(photo_bmp);
        String json = intent.getStringExtra("json");
        dataSource = jsonArrayStr2ObjectList(json);
        setMyAdapter();
    }

    /**
     * 解析json
     */
    private List<NewsList> jsonArrayStr2ObjectList(String jsonStr) {
        List<NewsList> newsLists = new ArrayList<>();

        try {
            JSONObject object1 = new JSONObject(jsonStr);
            JSONArray jsonArray = object1.getJSONArray("newslist");
            newsLists = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<NewsList>>() {
            }.getType());

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return newsLists;
    }

    /*设置adapter*/
    private void setMyAdapter() {
        RecognitionAdapter adapter = new RecognitionAdapter(
                RecognitionResultActivity.this,
                dataSource,
                RecognitionResultActivity.this,
                R.layout.activity_recognition_result_item
        );
        listView.setAdapter(adapter);
    }


    private void initViews() {
        imgPhoto = findViewById(R.id.iv_photo);
        listView = findViewById(R.id.lv_recognition);
        tvNoFind = findViewById(R.id.tv_nofind);

    }

    private void setListener() {
        tvNoFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognitionResultActivity.this, net.edrop.edrop_employer.activity.SearchRubblishActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                //启动一个意图,回到桌面
                Intent intent = new Intent(RecognitionResultActivity.this,Main2Activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

package net.edrop.edrop_employer.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import net.edrop.edrop_employer.R;
import net.edrop.edrop_employer.utils.SystemTransUtil;

public class RubbishDesc04Activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new SystemTransUtil().trans(RubbishDesc04Activity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rubbish_desc04);
    }
}

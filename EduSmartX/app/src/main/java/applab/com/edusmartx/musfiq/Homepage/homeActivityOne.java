package applab.com.edusmartx.musfiq.Homepage;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import applab.com.edusmartx.R;

public class homeActivityOne extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m_homepage_one);

        TextView txtInfo = (TextView) findViewById(R.id.txtInfo);
        if (getIntent() != null) {
            String info = getIntent().getStringExtra("info");
            txtInfo.setText(info);
        }
    }
}

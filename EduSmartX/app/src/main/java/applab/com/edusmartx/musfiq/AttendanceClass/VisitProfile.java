package applab.com.edusmartx.musfiq.AttendanceClass;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import applab.com.edusmartx.R;

public class VisitProfile extends Activity {
//    oh ho
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.m_visit_profile);

        Intent intent = getIntent();
        String profileLink = intent.getStringExtra("profileLink");
//        System.out.println(value);

        goToUrl(profileLink);
    }

    public void goToSo(View view) {
        goToUrl("http://stackoverflow.com/");
    }

    public void goToSu(View view) {
        goToUrl("https://github.com/Musfiqshohan");
    }

    private void goToUrl(String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }

}
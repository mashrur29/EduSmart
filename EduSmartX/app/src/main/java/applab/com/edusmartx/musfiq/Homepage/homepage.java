package applab.com.edusmartx.musfiq.Homepage;


import android.content.Intent;
        import android.graphics.Color;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.support.v7.widget.CardView;
        import android.view.View;
        import android.widget.GridLayout;
import android.widget.Toast;

import applab.com.edusmartx.R;
import applab.com.edusmartx.musfiq.AttendanceClass.AttendanceActivity;
import applab.com.edusmartx.musfiq.Classroom.CourseListActivity;
import applab.com.edusmartx.musfiq.Scanner.CamScanner;

public class homepage extends AppCompatActivity {

    GridLayout mainGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m_homepage_grid);

        mainGrid = (GridLayout) findViewById(R.id.mainGrid);

        //Set Event
        setSingleEvent(mainGrid);
        //setToggleEvent(mainGrid);
    }

    private void setToggleEvent(GridLayout mainGrid) {
        //Loop all child item of Main Grid
        for (int i = 0; i < mainGrid.getChildCount(); i++) {
            //You can see , all child item is CardView , so we just cast object to CardView
            final CardView cardView = (CardView) mainGrid.getChildAt(i);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (cardView.getCardBackgroundColor().getDefaultColor() == -1) {
                        //Change background color
                        cardView.setCardBackgroundColor(Color.parseColor("#FF6F00"));
                        Toast.makeText(homepage.this, "State : True", Toast.LENGTH_SHORT).show();

                    } else {
                        //Change background color
                        cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        Toast.makeText(homepage.this, "State : False", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void setSingleEvent(GridLayout mainGrid) {
        //Loop all child item of Main Grid
        for (int i = 0; i < mainGrid.getChildCount(); i++) {
            //You can see , all child item is CardView , so we just cast object to CardView
            CardView cardView = (CardView) mainGrid.getChildAt(i);
            final int finalI = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent;
                    if(finalI==0){
                        Toast.makeText(homepage.this, "Classroom!!!", Toast.LENGTH_SHORT).show();
                        intent = new Intent(homepage.this, CourseListActivity.class);
                    }
                    else if(finalI==1){
                        Toast.makeText(homepage.this, "Attendance!!!", Toast.LENGTH_SHORT).show();
                        intent = new Intent(homepage.this, AttendanceActivity.class);
                    }
                    else if(finalI==2){
                        Toast.makeText(homepage.this, "Attendance!!!", Toast.LENGTH_SHORT).show();
                        intent = new Intent(homepage.this, CamScanner.class);
                    }




                    else {

                        intent = new Intent(homepage.this, homeActivityOne.class);
                        intent.putExtra("info", "This is activity from card item index  " + finalI);
                    }
                    startActivity(intent);

                }
            });
        }
    }
}
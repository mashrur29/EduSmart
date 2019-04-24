package applab.com.edusmartx.musfiq.AttendanceClass;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

import applab.com.edusmartx.R;

public class AttendanceActivity extends AppCompatActivity {

    Button mOrder;
    TextView mItemSelected;
    String[] nameListItems;
    String[] linkListItems;
    boolean[] checkedItems;
    ArrayList<Integer> mUserItems = new ArrayList<>();


    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    //RecyclerAdapter adapter;
    AttendanceAdapter adapter;

    private TextView thedate;
    private Button btngocalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m_attendance);

        mOrder = (Button) findViewById(R.id.btnOrder);
        mItemSelected = (TextView) findViewById(R.id.tvItemSelected);

        nameListItems = getResources().getStringArray(R.array.studnt_name);
        linkListItems = getResources().getStringArray(R.array.student_link);
        checkedItems = new boolean[nameListItems.length];



        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
//        show_worker_map= (Button) findViewById(R.id.show_worker_map);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new AttendanceAdapter(AttendanceActivity.this);

        recyclerView.setAdapter(adapter);

//        here is for choosing date
        thedate = (TextView) findViewById(R.id.date);
        btngocalendar = (Button) findViewById(R.id.btngocalendar);

        Firebase.setAndroidContext(this);



        btngocalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AttendanceActivity.this,CalendarActivity.class);
                startActivityForResult(intent,100);
            }
        });


        mOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(AttendanceActivity.this);
                mBuilder.setTitle(R.string.dialog_title);
                mBuilder.setMultiChoiceItems(nameListItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
//                        if (isChecked) {
//                            if (!mUserItems.contains(position)) {
//                                mUserItems.add(position);
//                            }
//                        } else if (mUserItems.contains(position)) {
//                            mUserItems.remove(position);
//                        }
                        if (isChecked) {
                            mUserItems.add(position);



                        } else {
                            mUserItems.remove((Integer.valueOf(position)));
                        }
                    }
                });

                mBuilder.setCancelable(false);
                mBuilder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {



                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {

                        adapter.clearList();

                        String item = "";
                        for (int i = 0; i < mUserItems.size(); i++) {
                            item = item + nameListItems[mUserItems.get(i)];

//                            my code here
                            String name=nameListItems[mUserItems.get(i)];
                            String roll="Roll:"+(mUserItems.get(i));
                            String link=linkListItems[mUserItems.get(i)];

                            System.out.println("xxx"+name+" "+roll+" "+link);
                            AttendedStudentInfo student=new AttendedStudentInfo(link,name, roll);
                            adapter.updateWorkerList(student);



                            if (i != mUserItems.size() - 1) {
                                item = item + ", ";
                            }
                        }
//                        mItemSelected.setText(item);
                    }
                });

                mBuilder.setNegativeButton(R.string.dismiss_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                mBuilder.setNeutralButton(R.string.clear_all_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        for (int i = 0; i < checkedItems.length; i++) {
                            checkedItems[i] = false;
                            mUserItems.clear();
//                            mItemSelected.setText("");
                        }
                    }
                });

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


                if (requestCode == 100 && resultCode == Activity.RESULT_OK) {

                    String date = data.getStringExtra("date");
                    thedate = (TextView) findViewById(R.id.date);
                    thedate.setText(date);
                    Toast.makeText(AttendanceActivity.this, date, Toast.LENGTH_SHORT).show();
                    loadAttendance();

            }

    }


    public  void addDoneClicked(View v)  // I will be able to create a new task
    {


        saveinDatabase();


        finish();




    }


    public void saveinDatabase(){



        Firebase FDataBaseRef = new Firebase("https://edusmart-8a0e7.firebaseio.com/attendance");
        Firebase DateRef=FDataBaseRef.child((String)thedate.getText());

        DateRef.removeValue();

        ArrayList<String>studentnameList=adapter.getStudentName();
        ArrayList<String>studentrollList=adapter.getStudentRoll();
        ArrayList<String>studentlinkList=adapter.getProfileLink();


        for (int i=0;i<studentrollList.size();i++) {
            String studentRoll=studentrollList.get(i);
            String studentName=studentnameList.get(i);
            String studentLink=studentlinkList.get(i);

            System.out.println(studentRoll+",,,,,"+studentName+","+studentLink);

            Firebase attenRef = DateRef.child(studentRoll);


            attenRef.child("studentName").setValue(studentName);
            attenRef.child("studentRoll").setValue(studentRoll);
            attenRef.child("profileLink").setValue(studentLink);

        }



    }





    public void loadAttendance()
    {
        Firebase FDataBaseRef=new Firebase("https://edusmart-8a0e7.firebaseio.com/attendance");
        Firebase dateRef= FDataBaseRef.child((String)thedate.getText());





        dateRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot dsp: dataSnapshot.getChildren()){

                    System.out.println("adding: "+ dsp.getValue());
                    AttendedStudentInfo studentInfo= dsp.getValue(AttendedStudentInfo.class);
                    //System.out.println("adding: "+employeeProfile.toString());
                    adapter.updateWorkerList(studentInfo);
                    adapter.notifyDataSetChanged();

//                    System.out.println("adding: "+studentInfo.toString());

                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }








}
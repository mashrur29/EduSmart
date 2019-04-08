package applab.com.edusmartx;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;



public class CourseListActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    //RecyclerAdapter adapter;
    RecyclerAdapter adapter;
    public String currCatagory;

    private Button show_worker_map;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.courselist);
        // setContentView(R.layout.activity_main);

//        Intent intent = getIntent();
//        String value = intent.getStringExtra("key");
//        System.out.println(value);
//
//        String newCatagory =intent.getStringExtra("newCatagory");
//
//        System.out.println("heres newcatagory-> "+newCatagory);
//        currCatagory=newCatagory;

//        if(newCatagory.length()==0)
//        getSupportActionBar().setTitle("Programmer");
//        else
//        getSupportActionBar().setTitle(newCatagory);

        recyclerView =
                (RecyclerView) findViewById(R.id.recycler_view);
//        show_worker_map= (Button) findViewById(R.id.show_worker_map);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RecyclerAdapter(CourseListActivity.this,currCatagory);


        recyclerView.setAdapter(adapter);

//        if(newCatagory.length()==0)
//        loadWorkerList("Programmer");
//        else
//        loadWorkerList(newCatagory);


//        show_worker_map.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(EmployeeListActivity.this, NearWorkerMapActivity.class);
//                startActivityForResult(intent, 500);
//            }
//        });


    }

    public  void addEmpClicked(View v)  // I will be able to create a new task
    {
        Intent intent = new Intent(CourseListActivity.this, EditCourse.class);
//        intent.putExtra("currentCatagory", currCatagory);
//        intent.putExtra("NID", "-1");
        startActivityForResult(intent, 500);




    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 500 && resultCode == RESULT_OK && data != null) {
            String course,instructor;
            course = data.getStringExtra("course");
            instructor = data.getStringExtra("instructor");

            adapter.updateWorkerList(course,instructor);
//            adapter.notifyDataSetChanged();

        }


    }

//
//    public void loadWorkerList(String catagoryName)
//    {
//        Firebase FDataBaseRef=new Firebase("https://newsfeed-5e0ae.firebaseio.com/Work_Catagories");
//        Firebase workCat= FDataBaseRef.child(catagoryName);
//        final Firebase employeeList= workCat.child("EmployeeList");
//
//
//        System.out.println();
//
//
//        employeeList.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                for(DataSnapshot dsp: dataSnapshot.getChildren()){
//
//                    //System.out.println("adding: "+ dsp.getValue());
//                    EmployeeProfileClass employeeProfile= dsp.getValue(EmployeeProfileClass.class);
//                    //System.out.println("adding: "+employeeProfile.toString());
//                    adapter.updateWorkerList(employeeProfile);
//                    adapter.notifyDataSetChanged();
//
//                    System.out.println("adding: "+employeeProfile.toString());
//
//
//                }
//
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//
//            }
//        });
//
//
//    }





}

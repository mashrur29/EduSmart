package applab.com.edusmartx;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

/**
 * Created by musfiq on 4/9/19.
 */

public class AssignmentShow extends AppCompatActivity {


    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    //RecyclerAdapter adapter;
    assignmentListAdapter adapter;
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

        adapter = new assignmentListAdapter(AssignmentShow.this);


        recyclerView.setAdapter(adapter);

        Firebase.setAndroidContext(this);
        loadCourseList();



    }



    public void loadCourseList()
    {
        Firebase FDataBaseRef=new Firebase("https://edusmart-8a0e7.firebaseio.com/");
        Firebase courseRef= FDataBaseRef.child("assignment");



        System.out.println();


        courseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot dsp: dataSnapshot.getChildren()){

                    //System.out.println("adding: "+ dsp.getValue());
                    String key=dsp.getKey();
                    String val=dsp.getValue(String.class);
                    //System.out.println("adding: "+employeeProfile.toString());
                    adapter.updateWorkerList(new CourseInfo(val,""));
                    adapter.notifyDataSetChanged();

                    System.out.println("adding: "+val.toString());

                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }


}
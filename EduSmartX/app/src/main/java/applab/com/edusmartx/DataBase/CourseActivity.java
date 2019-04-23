package applab.com.edusmartx.DataBase;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import applab.com.edusmartx.R;

public class CourseActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    FirebaseAuth auth;
    EditText edit1,edit2;
    TextView text;
    List<Course> courseList,timeDList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        auth = FirebaseAuth.getInstance();
//        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Courses");
        edit1 = findViewById(R.id.edit1);
        edit2 = findViewById(R.id.edit2);
        text = findViewById(R.id.textview);
        courseList = new ArrayList<>();
        timeDList = new ArrayList<>();

    }

    public void Insert(View v)
    {
        firebaseUser = auth.getCurrentUser();

        User tempUser = new User(edit1.getText().toString(),edit2.getText().toString());
//      databaseReference.child(firebaseUser.getUid()).setValue(tempUser);
        databaseReference.child(edit1.getText().toString()+"*").setValue(tempUser);
    }


    public void Display(View v)
    {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> keys = new ArrayList<>();
                for (DataSnapshot dataSnapshotitem : dataSnapshot.getChildren())
                    {
                            keys.add(dataSnapshotitem.getKey());
                            Course course = dataSnapshotitem.getValue(Course.class);
                            courseList.add(course);
//                            timeDList.add(dataSnapshotitem.getValue(String.class));
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

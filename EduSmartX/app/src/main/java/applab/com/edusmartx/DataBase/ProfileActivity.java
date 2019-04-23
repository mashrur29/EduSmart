package applab.com.edusmartx.DataBase;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

public class ProfileActivity extends AppCompatActivity {


    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    FirebaseAuth auth;
    EditText edit1,edit2;
    TextView text;
    List<String> timeList,timeDList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        auth = FirebaseAuth.getInstance();
//        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("time");
        edit1 = findViewById(R.id.edit1);
        edit2 = findViewById(R.id.edit2);
        text = findViewById(R.id.textview);
        timeList = new ArrayList<>();
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
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Courses");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String coursename = dataSnapshot.child("abc").getValue().toString();
                edit2.append(coursename+" ");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

//    public void Insert(View v)
//    {
//        timeList.add("01 PM");
//        timeList.add("02 PM");
//        timeList.add("03 PM");
//        timeList.clear();
//        databaseReference.setValue(timeList).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if(task.isSuccessful())
//                {
//                    Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_SHORT).show();
//                }
//
//                else
//                    Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

//    public void Display(View v)
//    {
////        databaseReference = FirebaseDatabase.getInstance().getReference().child("Time");
//
//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if(dataSnapshot.exists())
//                {
//                    timeDList.clear();
//
//                    for (DataSnapshot dataSnapshotitem : dataSnapshot.getChildren())
//                    {
//                        timeDList.add(dataSnapshotitem.getValue(String.class));
//                    }
//
//                    text.setText(timeDList.toString());
////                    text.setText("123");
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//    }
}

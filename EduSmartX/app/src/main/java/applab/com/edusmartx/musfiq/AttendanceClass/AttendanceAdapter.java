package applab.com.edusmartx.musfiq.AttendanceClass;

/**
 * Created by Shade on 5/9/2016.
 */

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import applab.com.edusmartx.R;
import applab.com.edusmartx.musfiq.Classroom.CourseDetails;
import applab.com.edusmartx.musfiq.Classroom.CourseListActivity;

//import com.firebase.client.Firebase;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.ViewHolder> {


    Context context;
    ArrayList<String> studentName, studentRoll,profileLink;
    String Catagory;

    int EDIT_WORK_INTENT=2;

    public AttendanceAdapter(Context mainActivity) {

        context = mainActivity;
        studentName =new ArrayList<>();
        studentRoll =new ArrayList<>();
        profileLink =new ArrayList<>();

        }




    private static final int REQUEST_CALL = 1;
    private EditText mEditTextNumber;

    public ArrayList<String> getStudentName() {
        return studentName;
    }

    public ArrayList<String> getStudentRoll() {
        return studentRoll;
    }

    public ArrayList<String> getProfileLink() {
        return profileLink;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public int currentItem;
        public ImageView itemImage;
        public ImageView image_call;
        public TextView itemTitle;
        public TextView itemDetail;
        public TextView worker_optionsText;

        public ViewHolder(View itemView) {
            super(itemView);
           // itemImage = (ImageView)itemView.findViewById(R.id.item_image);
            itemTitle = (TextView)itemView.findViewById(R.id.item_title);
            itemDetail =(TextView)itemView.findViewById(R.id.item_detail);
            worker_optionsText = (TextView) itemView.findViewById(R.id.edit_options);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    int position = getAdapterPosition();

                    Snackbar.make(v, "Click detected on itemxxxxxx " + position,
                            Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                    if(context instanceof CourseListActivity)
                    {
//                    Intent myIntent = new Intent(context, ProfileActivity.class);
//                    myIntent.putExtra("key", "hello"); //Optional parameters
//                    context.startActivity(myIntent);
                        Intent myIntent = new Intent(context, CourseDetails.class);
//                        myIntent.putExtra("Catagory", Catagory); //Optional parameters
                        myIntent.putExtra("CourseName", studentName.get(position)); //Optional parameters
                        context.startActivity(myIntent);


                    }
//                    else if(context instanceof CatagoryActivity)
//                    {
//                        Intent myIntent = new Intent(context, EmployeeListActivity.class);
//                        myIntent.putExtra("Catagory", studentName.get(position)); //Optional parameters
//                        context.startActivity(myIntent);
//                    }

                }
            });

        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        viewHolder.itemTitle.setText(studentName.get(i));
        viewHolder.itemDetail.setText(studentRoll.get(i));
       // viewHolder.itemImage.setImageResource(images[i]);


        ///here is problem
        viewHolder.worker_optionsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Log.d("RecyclerAdapter", "clicking on option"+i);
                //creating a popup menu
                PopupMenu popup = new PopupMenu(context, viewHolder.worker_optionsText);
                //inflating menu from xml resource
                popup.inflate(R.menu.edit_option_menu2);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {


                            case R.id.Edit:


                                Intent intent = new Intent(context, VisitProfile.class);
                                intent.putExtra("profileLink", profileLink.get(i));
                                System.out.println("name"+studentName.get(i)+"link"+profileLink.get(i));

                                ((AttendanceActivity)context).startActivityForResult(intent, EDIT_WORK_INTENT);

//                                DeleteFromList(i,2);
                                break;


//                            case R.id.Edit:
//
////                                Intent intent = new Intent(mCtx, EditFieldClass.class);
////                                ((MainActivity)mCtx).startActivityForResult(intent, Intent_Constants.INTENT_REQUEST_CODE);
//                                Intent intent = new Intent(context, EditWorkerActivity.class);
//                                intent.putExtra("currentCatagory", Catagory);
//                                intent.putExtra("NID", NID.get(i));
//                                ((EmployeeListActivity)context).startActivityForResult(intent, EDIT_WORK_INTENT);
//
////                                DeleteFromList(i,2);
//                                break;

                            case R.id.Delete:
                                DeleteFromList(i);
//                                Intent intentx = new Intent(context, GraphDrawActivity.class);
//                                ((MainActivity)mCtx).startActivityForResult(intentx, Intent_Constants.INTENT_GRAPH_CODE);

                                Toast.makeText(context, "employee deleted", Toast.LENGTH_SHORT).show();

                                break;



                        }
                        return false;
                    }
                });
                //displaying the popup
                popup.show();


            }
        });

    }



    @Override
    public int getItemCount() {
        return studentName.size();
    }


    public  void DeleteFromList(int pos)
    {
        Log.d("RecyclerAdapter","deleted delete "+pos);
       // deleteRecord(pos);
        studentName.remove(pos);
        studentRoll.remove(pos);
        profileLink.remove(pos);

        this.notifyDataSetChanged();

    }

    public void clearList(){

        studentName.clear();
        studentRoll.clear();
        profileLink.clear();

    }

//    void deleteRecord(int pos)
//    {
//        ///this part for deleting whole information from firedatabase
//        Firebase employeeList=new Firebase("https://newsfeed-5e0ae.firebaseio.com/Work_Catagories/"+Catagory+"/EmployeeList");
//        System.out.println("catagory-> "+Catagory+" deleted Nid-> "+NID.get(pos)+" ref"+employeeList.child(NID.get(pos)));
//        employeeList.child(NID.get(pos)).removeValue();
//
//      //admin does not change the current geo location
//
//
//        ///This part for deleting image of unique nid form firebase storage
//         StorageReference storageReference;
//         FirebaseStorage storage;
//
//        storage = FirebaseStorage.getInstance();
//        storageReference = storage.getReference();
//
//        StorageReference photoRef = storageReference.child("ProfilePictures/"+Catagory+"/" + NID.get(pos));
//
//
//        photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                // File deleted successfully
//                Log.d("ok", "onSuccess: deleted file");
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Uh-oh, an error occurred!
//                Log.d("notok", "onFailure: did not delete file");
//            }
//        });
//
//    }



    public void updateWorkerList(AttendedStudentInfo student)
    {
        this.studentName.add(student.studentName);
        this.studentRoll.add(student.studentRoll);
        this.profileLink.add(student.profileLink);

        System.out.println("yyyy"+student.studentName+" "+student.studentRoll+" "+student.profileLink);

        notifyDataSetChanged();

    }

}
package applab.com.edusmartx.musfiq.Classroom;

/**
 * Created by musfiq on 4/9/19.
 */


import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

import applab.com.edusmartx.R;

public class AssignmentSelection extends AppCompatActivity implements View.OnClickListener /*  implementing click listener */ {
    //a constant to track the file chooser intent
    private static final int PICK_IMAGE_REQUEST = 234;

    //Buttons
    private Button buttonChoose;
    private Button buttonUpload,buttonShow;
    private TextView notification;
    //ImageView
    private ImageView imageView;


    //a Uri object to store file path
    private Uri filePath;

    Uri pdfUri;
    private StorageReference storageReference;
    private FirebaseStorage storage;

    String currCatagory,filename,realfilename;


    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    //RecyclerAdapter adapter;
    assignmentListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_choose);


        Intent intent = getIntent();
        currCatagory = intent.getStringExtra("currentCatagory");


        Toast.makeText(getApplicationContext(),"Select your "+currCatagory,Toast.LENGTH_SHORT).show();

        //getting views from layout
        buttonChoose = (Button) findViewById(R.id.buttonChoose);
        buttonUpload = (Button) findViewById(R.id.buttonUpload);
//        buttonShow=(Button) findViewById(R.id.assshow);

//        imageView = (ImageView) findViewById(R.id.imageView);
        notification=(TextView)findViewById(R.id.fileurl);

        //attaching listener
        buttonChoose.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);




//        for assignment showlist
        recyclerView =
                (RecyclerView) findViewById(R.id.recycler_view);
//        show_worker_map= (Button) findViewById(R.id.show_worker_map);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new assignmentListAdapter(AssignmentSelection.this);


        recyclerView.setAdapter(adapter);

        Firebase.setAndroidContext(this);
        loadFileList();

    }



    public void loadFileList()
    {

        adapter.clearList();
        Firebase FDataBaseRef=new Firebase("https://edusmart-8a0e7.firebaseio.com/");
        Firebase courseRef= FDataBaseRef.child(currCatagory);



        System.out.println();


        courseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot dsp: dataSnapshot.getChildren()){

                    //System.out.println("adding: "+ dsp.getValue());
                    String key=dsp.getKey();
                    AssignmentInfo val=dsp.getValue(AssignmentInfo.class);
                    //System.out.println("adding: "+employeeProfile.toString());
                    adapter.updateWorkerList(val);
                    adapter.notifyDataSetChanged();

                    System.out.println("adding: "+val.toString());

                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }







    //method to show file chooser
    private void showFileChooser() {


        Intent intent = new Intent();

        if(currCatagory.equals("assignment")) {
            intent.setType("application/pdf");
        }
        else {
            intent.setType("application/zip");
        }
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,86);



//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {


                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                notification.setText("Selected file: "+data.getData().getLastPathSegment());
//                imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(requestCode == 86 && resultCode == RESULT_OK && data != null && data.getData() != null){
            ///here construction

            filePath=data.getData();
//            notification.setText("Selected file: "+data.getData().getLastPathSegment());
            realfilename=getFileName(filePath);
            notification.setText("Selected file: "+realfilename);


            filename=""+System.currentTimeMillis();
        }
    }


//    public void showClicked(View v){
//
//            Intent intent = new Intent(AssignmentSelection.this, AssignmentShow.class);
//            intent.putExtra("currentCatagory", "assignment");
//            startActivityForResult(intent, 500);
//
//    }

    @Override
    public void onClick(View view) {
        //if the clicked button is choose
        if (view == buttonChoose) {
            showFileChooser();
        }
        //if the clicked button is upload
        else if (view == buttonUpload) {
            uploadFile();
        }

    }

    //this method will upload the file
    private void uploadFile() {

        storage = FirebaseStorage.getInstance();
        System.out.println("here->"+storage.toString());
        storageReference = storage.getReference();


        //if there is a file to upload
        if (filePath != null) {
            //displaying a progress dialog while upload is going on
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();



            final String fileinput;
            if(currCatagory.equals("assignment")){
                fileinput="assignment/"+filename;
            }
            else
                fileinput="project/"+filename;
//
            final StorageReference riversRef = storageReference.child(fileinput);
            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    final Uri downloadUrl = uri;
                                    storeFileUrl(downloadUrl,fileinput);
                                    progressDialog.dismiss();

                                    //and displaying a success toast
                                    Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                                    loadFileList();

                                }
                            });
                            //if the upload is successfull
                            //hiding the progress dialog



                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //if the upload is not successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying error message
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //calculating progress percentage
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                            //displaying percentage in progress dialog
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        }
        //if there is not any file
        else {
            //you can display an error toast
        }
    }



    //storing image in firedatabase the link of each worker where pk is NID
    public void storeFileUrl(Uri downloadUri,String fileinput) {
        Firebase FDataBaseRef = new Firebase("https://edusmart-8a0e7.firebaseio.com/"+fileinput);
        String urid = downloadUri.toString();


        FDataBaseRef.child("fileName").setValue(realfilename);
        FDataBaseRef.child("fileLink").setValue(urid);
//        Firebase catagoryLogoRef = FDataBaseRef.child(filename);
//        catagoryLogoRef.setValue(urid);

    }




    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }



}
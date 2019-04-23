package applab.com.edusmartx.musfiq;

/**
 * Created by musfiq on 4/9/19.
 */


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

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

    String currCatagory,filename;

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
        buttonShow=(Button) findViewById(R.id.assshow);

        imageView = (ImageView) findViewById(R.id.imageView);
        notification=(TextView)findViewById(R.id.fileurl);

        //attaching listener
        buttonChoose.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);
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
            notification.setText("Selected file: "+data.getData().getLastPathSegment());
            filename=""+System.currentTimeMillis();
        }
    }


    public void showClicked(View v){

            Intent intent = new Intent(AssignmentSelection.this, AssignmentShow.class);
            intent.putExtra("currentCatagory", "assignment");
            startActivityForResult(intent, 500);

    }

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
                                    storeImageUrl(downloadUrl,fileinput);
                                    progressDialog.dismiss();

                                    //and displaying a success toast
                                    Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();


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
    public void storeImageUrl(Uri downloadUri,String fileinput) {
        Firebase FDataBaseRef = new Firebase("https://edusmart-8a0e7.firebaseio.com/"+fileinput);
        String urid = downloadUri.toString();

        FDataBaseRef.setValue(urid);
//        Firebase catagoryLogoRef = FDataBaseRef.child(filename);
//        catagoryLogoRef.setValue(urid);

    }



}
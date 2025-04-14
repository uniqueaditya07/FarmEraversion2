package com.example.farmera;



import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UploadContentActivity extends AppCompatActivity {

    private EditText blogEditText;
    private Button uploadButton, selectVideoButton;
    private DatabaseReference databaseRef;
    private StorageReference storageRef;
    private Uri videoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_content);

        blogEditText = findViewById(R.id.blogEditText);
        uploadButton = findViewById(R.id.uploadButton);
        selectVideoButton = findViewById(R.id.selectVideoButton);

        databaseRef = FirebaseDatabase.getInstance().getReference("content");
        storageRef = FirebaseStorage.getInstance().getReference("videos");

        // Open file picker to select a video
        selectVideoButton.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("video/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, 1); // Request code 1
        });

        // Upload content to Firebase
        uploadButton.setOnClickListener(v -> {
            String blog = blogEditText.getText().toString().trim();

            if (!blog.isEmpty() && videoUri != null) {
                String key = databaseRef.push().getKey();
                StorageReference videoRef = storageRef.child(key + ".mp4");

                // Upload video to Firebase Storage
                videoRef.putFile(videoUri)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // Get download URL of the uploaded video
                                videoRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                    String videoUrl = uri.toString();
                                    ContentModel content = new ContentModel(blog, videoUrl);
                                    databaseRef.child(key).setValue(content)
                                            .addOnCompleteListener(task1 -> {
                                                if (task1.isSuccessful()) {
                                                    Toast.makeText(UploadContentActivity.this, "Content uploaded successfully", Toast.LENGTH_SHORT).show();
                                                    blogEditText.setText("");
                                                    videoUri = null;
                                                } else {
                                                    Toast.makeText(UploadContentActivity.this, "Upload failed", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                });
                            } else {
                                Toast.makeText(UploadContentActivity.this, "Video upload failed", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(this, "Please enter blog and select a video", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            videoUri = data.getData(); // Get the selected video URI
            Toast.makeText(this, "Video selected", Toast.LENGTH_SHORT).show();
        }
    }
}
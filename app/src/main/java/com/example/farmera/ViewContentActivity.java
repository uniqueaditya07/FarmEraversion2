package com.example.farmera;




import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewContentActivity extends AppCompatActivity {

    private ListView contentListView;
    private ArrayList<ContentModel> contentList;
    private ContentAdapter adapter;
    private DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_content);

        contentListView = findViewById(R.id.contentListView);
        contentList = new ArrayList<>();
        adapter = new ContentAdapter(this, contentList);
        contentListView.setAdapter(adapter);

        databaseRef = FirebaseDatabase.getInstance().getReference("content");

        // Fetch data from Firebase
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                contentList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ContentModel content = dataSnapshot.getValue(ContentModel.class);
                    contentList.add(content);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors
            }
        });
    }
}
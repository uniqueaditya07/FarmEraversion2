package com.example.farmera;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ContentAdapter extends ArrayAdapter<ContentModel> {

    public ContentAdapter(Context context, ArrayList<ContentModel> contentList) {
        super(context, 0, contentList); // Use ContentModel instead of String
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Inflate the layout if it's null
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_content, parent, false);
        }

        // Get references to the TextViews
        TextView blogTextView = convertView.findViewById(R.id.blogTextView);
        TextView videoTextView = convertView.findViewById(R.id.videoTextView);

        // Get the current ContentModel object
        ContentModel content = getItem(position);

        // Set the blog text
        if (content != null) {
            blogTextView.setText(content.getBlog());

            // Set the video URL and make it clickable
            String videoUrl = content.getVideo();
            if (videoUrl != null && !videoUrl.isEmpty()) {
                videoTextView.setText("Watch Video");
                videoTextView.setOnClickListener(v -> {
                    // Open the VideoPlayerActivity to play the video
                    Intent intent = new Intent(getContext(), VideoPlayerActivity.class);
                    intent.putExtra("videoUrl", videoUrl);
                    getContext().startActivity(intent);
                });
            } else {
                videoTextView.setText("No Video Available");
            }
        }

        return convertView;
    }
}
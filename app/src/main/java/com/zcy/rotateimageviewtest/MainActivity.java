package com.zcy.rotateimageviewtest;

import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.zcy.rotateimageview.RotateImageView;

public class MainActivity extends AppCompatActivity {

    private RotateImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        image = (RotateImageView) findViewById(R.id.image);
        image.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.head));
        image.setSpeed(40);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image.setRotate(!image.isRotating());
            }
        });
    }
}

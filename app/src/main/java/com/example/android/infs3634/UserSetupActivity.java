package com.example.android.infs3634;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class UserSetupActivity extends AppCompatActivity {

    EditText firstNameEdit;
    EditText lastNameEdit;
    Button continueButton;
    ImageButton imageButton;

    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setup);

        final UserSetupActivity userSetupActivity = this;

        getSupportActionBar().setTitle("Set User");

        firstNameEdit = findViewById(R.id.firstNameEdit);
        lastNameEdit = findViewById(R.id.lastNameEdit);
        continueButton = findViewById(R.id.continuePressed);
        imageButton = findViewById(R.id.imageButton);

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String first = firstNameEdit.getText().toString();
                String last = lastNameEdit.getText().toString();

                if (first != null && first != "" & last != null & last != "") {
                    if (uri == null) {
                        uri = Uri.parse("android.resource://your.package.here/mipmap/account.png");
                    }
                    DataService.instance.createUserDetails(first, last, uri, UserSetupActivity.this);
                }
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                final int ACTIVITY_SELECT_IMAGE = 1234;
                startActivityForResult(i, ACTIVITY_SELECT_IMAGE);
            }
        });

    }

    // handle photo gallery
    // https://stackoverflow.com/questions/6016000/how-to-open-phones-gallery-through-code
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            uri = data.getData();
            final InputStream imageStream = getContentResolver().openInputStream(uri);
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getResources(), selectedImage);
            drawable.setCircular(true);
            imageButton.setImageBitmap(selectedImage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    };

}

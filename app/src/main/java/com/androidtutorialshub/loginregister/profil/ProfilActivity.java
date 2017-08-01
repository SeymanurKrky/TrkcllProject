package com.androidtutorialshub.loginregister.profil;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.Toast;

import com.androidtutorialshub.loginregister.R;
import com.androidtutorialshub.loginregister.model.User;

import java.io.IOException;

public class ProfilActivity extends AppCompatActivity {

    User u;
    AppCompatTextView tvNameGiris,tvEmailGiris,tvAddressGiris,tvMobileGiris,tvBtypeGiris;
    AppCompatButton btnEdit;
    AppCompatImageView ivProfileImage;
    Bitmap yourSelectedImage;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        u=new User();
        tvNameGiris=(AppCompatTextView)findViewById(R.id.tvNameGiris);
        tvEmailGiris=(AppCompatTextView)findViewById(R.id.tvEmailGiris);
        tvAddressGiris=(AppCompatTextView)findViewById(R.id.tvAddressGiris);
        tvMobileGiris=(AppCompatTextView)findViewById(R.id.tvMobileGiris);
        tvBtypeGiris=(AppCompatTextView)findViewById(R.id.tvBtypeGiris);
        btnEdit=(AppCompatButton)findViewById(R.id.btnEdit);
        ivProfileImage=(AppCompatImageView)findViewById(R.id.ivProfileImage);



        ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                final int ACTIVITY_SELECT_IMAGE = 1234;
                startActivityForResult(i, ACTIVITY_SELECT_IMAGE);
                ivProfileImage.setImageBitmap(yourSelectedImage);
            }
        });



    /*

    tvNameGiris.setText(u.getName().toString());
        tvEmailGiris.setText(u.getEmail().toString());
        tvAddressGiris.setText(u.getAddress().toString());
        tvMobileGiris.setText(u.getMobile().toString());
        tvBtypeGiris.setText(u.getBtype().toString());





    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                if (data != null)
                {
                    try
                    {

                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());

                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }

                }
            } else if (resultCode == Activity.RESULT_CANCELED)
            {
                Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_SHORT).show();
            }
        }*/
    }





    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case 1234:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String filePath = cursor.getString(columnIndex);
                    cursor.close();


                     yourSelectedImage = BitmapFactory.decodeFile(filePath);
            /* Now you have choosen image in Bitmap format in object "yourSelectedImage". You can use it in way you want! */
                }
        }

    };

}

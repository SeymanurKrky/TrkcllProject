package com.androidtutorialshub.loginregister.fragments;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.ImageView;

import com.androidtutorialshub.loginregister.R;
import com.androidtutorialshub.loginregister.activities.NavigaActivity;
import com.androidtutorialshub.loginregister.sql.DatabaseHelper;

import java.io.ByteArrayOutputStream;

/**
 * Created by Seymanur on 16.08.2017.
 */

public class ProfileFragment extends Fragment {

    private DatabaseHelper databaseHelper;

    private TextInputEditText tvNameGiris, tvEmailGiris, tvAddressGiris, tvMobileGiris;
    private AppCompatTextView tvBtypeGiris;
    private AppCompatButton btnEdit, btnUpdate;
    private ImageView ivProfileImage;
    private static final int IMAGE_PICK = 1;
    private SQLiteDatabase db;
    Bitmap resized;
    private static final String TABLE_USER = "ssby";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_USER_EMAIL = "user_email";
    private static final String COLUMN_USER_ADDRESS = "user_address";
    private static final String COLUMN_USER_MOBILE = "user_mobile";
    private static final String COLUMN_USER_BLOODTYPE = "user_btype";
    private static final String COLUMN_USER_PASSWORD = "user_password";
    private static final String COLUMN_USER_IMAGE= "user_image";

    private String email;
    View view;
    @Nullable
    @Override


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {


        view=inflater.inflate(R.layout.fragment_profile,container,false);

        databaseHelper = new DatabaseHelper(getContext());
        tvNameGiris = (TextInputEditText) view.findViewById(R.id.tvNameGiris);
        tvEmailGiris = (TextInputEditText) view.findViewById(R.id.tvEmailGiris);
        tvAddressGiris = (TextInputEditText) view.findViewById(R.id.tvAddressGiris);
        tvMobileGiris = (TextInputEditText) view.findViewById(R.id.tvMobileGiris);
        tvBtypeGiris = (AppCompatTextView) view.findViewById(R.id.tvBtypeGiris);
        btnEdit = (AppCompatButton) view.findViewById(R.id.btnEdit);
        btnUpdate = (AppCompatButton) view.findViewById(R.id.btnUpdate);
        ivProfileImage = (ImageView) view.findViewById(R.id.ivProfileImage);

        db = databaseHelper.getReadableDatabase();


        disableEditText(tvNameGiris);
        disableEditText(tvEmailGiris);
        disableEditText(tvAddressGiris);
        disableEditText(tvMobileGiris);

        btnUpdate.setVisibility(View.GONE);

        //login sayfasından gelen email
        email=this.getArguments().getString("EMAIL").toString();

        kayitGetir(email);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivProfileImage.setEnabled(true);

                ivProfileImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(Intent.createChooser(intent, "Bir Fotoðraf Seçin"), IMAGE_PICK);
                    }
                });

                enableEditText(tvNameGiris);
                enableEditText(tvAddressGiris);
                enableEditText(tvMobileGiris);

                btnEdit.setVisibility(View.GONE);
                btnUpdate.setVisibility(View.VISIBLE);

            }
        });


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byte[] img=getBytes(resized);
                editDatabase(tvNameGiris.getText().toString(),tvEmailGiris.getText().toString(),tvAddressGiris.getText().toString(),
                        tvMobileGiris.getText().toString(),img);
                btnUpdate.setVisibility(View.GONE);
                btnEdit.setVisibility(View.VISIBLE);
                disableEditText(tvNameGiris);
                disableEditText(tvAddressGiris);
                disableEditText(tvMobileGiris);
                ivProfileImage.setEnabled(false);
            }
        });


        return view;
    }
    //Edittext görünümünü textview gibi gösterme
    private void disableEditText(TextInputEditText editText) {
        //editText.setFocusable(false);
        editText.setEnabled(false);
        // editText.setCursorVisible(false);
        // editText.setKeyListener(null);
        // editText.setBackgroundColor(Color.TRANSPARENT);
    }
    // convert from bitmap to byte array
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }


    private void enableEditText(TextInputEditText editText) {
        editText.setFocusable(true);
        editText.setEnabled(true);
        editText.setCursorVisible(true);
        // editText.setKeyListener();
        // editText.setBackgroundColor(Color.TRANSPARENT);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        this.imageFromGallery(resultCode, data);
    }
    //galeriden resim çekme
    private void imageFromGallery(int resultCode, Intent data) {
        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Context applicationContext = NavigaActivity.getContextOfApplication();
        Cursor cursor = applicationContext.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String filePath = cursor.getString(columnIndex);
        Bitmap imgBitmap = BitmapFactory.decodeFile(filePath);
        resized = Bitmap.createScaledBitmap(imgBitmap, 100, 100, true);
        this.ivProfileImage.setImageBitmap(resized);
        cursor.close();

    }
    private void kayitGetir(String email) {
        String[] columns = {
                COLUMN_USER_NAME,
                COLUMN_USER_EMAIL,
                COLUMN_USER_ADDRESS,
                COLUMN_USER_MOBILE,
                COLUMN_USER_BLOODTYPE,
                COLUMN_USER_IMAGE

        };

        // selection criteria
        String selection = COLUMN_USER_EMAIL + " = ?";

        // selection argument
        String[] selectionArgs = {email};

        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order

        if (cursor.moveToFirst()) {
            do {

                //user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID))));
                tvNameGiris.setText(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)));
                tvEmailGiris.setText(cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL)));
                tvAddressGiris.setText(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ADDRESS)));
                tvMobileGiris.setText(cursor.getString(cursor.getColumnIndex(COLUMN_USER_MOBILE)));
                tvBtypeGiris.setText(cursor.getString(cursor.getColumnIndex(COLUMN_USER_BLOODTYPE)));
                byte[] image =cursor.getBlob(cursor.getColumnIndex(COLUMN_USER_IMAGE));

                ivProfileImage.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.length));

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();


    }
    //update database
    public void editDatabase(String name, String email, String address, String mobile, byte[] image) {
        db = databaseHelper.getWritableDatabase();
        String where = COLUMN_USER_EMAIL+ " = ?";
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_USER_NAME, name);
        cv.put(COLUMN_USER_ADDRESS,address);
        cv.put(COLUMN_USER_MOBILE,mobile);
        cv.put(COLUMN_USER_IMAGE,image);

        db.update(TABLE_USER, cv,  where, new String[]{String.valueOf(email)});

    }



}

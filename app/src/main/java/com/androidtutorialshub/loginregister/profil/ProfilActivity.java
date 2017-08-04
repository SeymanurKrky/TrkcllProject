package com.androidtutorialshub.loginregister.profil;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidtutorialshub.loginregister.R;

import com.androidtutorialshub.loginregister.sql.DatabaseHelper;

public class ProfilActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;

    private TextInputEditText tvNameGiris, tvEmailGiris, tvAddressGiris, tvMobileGiris;
    private AppCompatTextView tvBtypeGiris;
    private AppCompatButton btnEdit, btnUpdate;
    private ImageView ivProfileImage;
    private static final int IMAGE_PICK = 1;
    private SQLiteDatabase db;
    private static final String TABLE_USER = "ssby";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_USER_EMAIL = "user_email";
    private static final String COLUMN_USER_ADDRESS = "user_address";
    private static final String COLUMN_USER_MOBILE = "user_mobile";
    private static final String COLUMN_USER_BLOODTYPE = "user_btype";
    private static final String COLUMN_USER_PASSWORD = "user_password";

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        databaseHelper = new DatabaseHelper(this);
        tvNameGiris = (TextInputEditText) findViewById(R.id.tvNameGiris);
        tvEmailGiris = (TextInputEditText) findViewById(R.id.tvEmailGiris);
        tvAddressGiris = (TextInputEditText) findViewById(R.id.tvAddressGiris);
        tvMobileGiris = (TextInputEditText) findViewById(R.id.tvMobileGiris);
        tvBtypeGiris = (AppCompatTextView) findViewById(R.id.tvBtypeGiris);
        btnEdit = (AppCompatButton) findViewById(R.id.btnEdit);
        btnUpdate = (AppCompatButton) findViewById(R.id.btnUpdate);
        ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);

        db = databaseHelper.getReadableDatabase();


        disableEditText(tvNameGiris);
        disableEditText(tvEmailGiris);
        disableEditText(tvAddressGiris);
        disableEditText(tvMobileGiris);

        btnUpdate.setVisibility(View.GONE);

        //login sayfasından gelen email
        String emailFromIntent = getIntent().getStringExtra("EMAIL");


        kayitGetir(emailFromIntent);


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
                editDatabase(tvNameGiris.getText().toString(),tvEmailGiris.getText().toString(),tvAddressGiris.getText().toString(),
                        tvMobileGiris.getText().toString());
                btnUpdate.setVisibility(View.GONE);
                btnEdit.setVisibility(View.VISIBLE);
                disableEditText(tvNameGiris);
                disableEditText(tvAddressGiris);
                disableEditText(tvMobileGiris);
                ivProfileImage.setEnabled(false);
            }
        });

    }


    //Edittext görünümünü textview gibi gösterme
    private void disableEditText(TextInputEditText editText) {
        //editText.setFocusable(false);
        editText.setEnabled(false);
        // editText.setCursorVisible(false);
        // editText.setKeyListener(null);
        // editText.setBackgroundColor(Color.TRANSPARENT);
    }

    private void enableEditText(TextInputEditText editText) {
         editText.setFocusable(true);
        editText.setEnabled(true);
        editText.setCursorVisible(true);
        // editText.setKeyListener();
        // editText.setBackgroundColor(Color.TRANSPARENT);
    }

    //galeriden resim çekme
    private void imageFromGallery(int resultCode, Intent data) {
        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String filePath = cursor.getString(columnIndex);
        Bitmap imgBitmap = BitmapFactory.decodeFile(filePath);
        Bitmap resized = Bitmap.createScaledBitmap(imgBitmap, 100, 100, true);
        this.ivProfileImage.setImageBitmap(resized);
        cursor.close();

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        this.imageFromGallery(resultCode, data);
    }


    //edit textteki verileri doldurmak için veritabınından verileri çekme
    private void kayitGetir(String email) {
        String[] columns = {
                COLUMN_USER_NAME,
                COLUMN_USER_EMAIL,
                COLUMN_USER_ADDRESS,
                COLUMN_USER_MOBILE,
                COLUMN_USER_BLOODTYPE,

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


            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();


    }


    //update database
    private void editDatabase(String name, String email, String address, String mobile) {
        db = databaseHelper.getWritableDatabase();
        String where = COLUMN_USER_EMAIL+ " = ?";
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_USER_NAME, name);
        cv.put(COLUMN_USER_ADDRESS,address);
        cv.put(COLUMN_USER_MOBILE,mobile);

        db.update(TABLE_USER, cv,  where, new String[]{String.valueOf(email)});

    }




}

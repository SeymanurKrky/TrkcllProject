package com.androidtutorialshub.loginregister.profil;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.androidtutorialshub.loginregister.R;
import com.androidtutorialshub.loginregister.adapters.UsersRecyclerAdapter;
import com.androidtutorialshub.loginregister.model.User;
import com.androidtutorialshub.loginregister.sql.DatabaseHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProfilActivity extends AppCompatActivity {

    /*private AppCompatActivity activity = ProfilActivity.this;
    private RecyclerView recyclerViewUsers;
    private List<User> listUsers;
    private UsersRecyclerAdapter usersRecyclerAdapter;
    private DatabaseHelper databaseHelper;*/
    private DatabaseHelper databaseHelper;
    //User u;
    private AppCompatTextView tvNameGiris,tvEmailGiris,tvAddressGiris,tvMobileGiris,tvBtypeGiris;
    private AppCompatButton btnEdit;
    private AppCompatImageView ivProfileImage;
    private Bitmap yourSelectedImage;
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
        databaseHelper=new DatabaseHelper(this);
        tvNameGiris=(AppCompatTextView)findViewById(R.id.tvNameGiris);
        tvEmailGiris=(AppCompatTextView)findViewById(R.id.tvEmailGiris);
        tvAddressGiris=(AppCompatTextView)findViewById(R.id.tvAddressGiris);
        tvMobileGiris=(AppCompatTextView)findViewById(R.id.tvMobileGiris);
        tvBtypeGiris=(AppCompatTextView)findViewById(R.id.tvBtypeGiris);
        btnEdit=(AppCompatButton)findViewById(R.id.btnEdit);
        ivProfileImage=(AppCompatImageView)findViewById(R.id.ivProfileImage);



        db=databaseHelper.getReadableDatabase();


        String emailFromIntent = getIntent().getStringExtra("EMAIL");

        kayitGetir(emailFromIntent);



        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });
       // initViews();
       // initObjects();




    }

    private void kayitGetir(String email){
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

     protected void onActivityResult(int requestCode, int resultCode, Intent data) {
         super.onActivityResult(requestCode, resultCode, data);

         switch (requestCode) {
             case 1234:
                 if (resultCode == RESULT_OK) {
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
     }
   /* private void initViews() {

        recyclerViewUsers = (RecyclerView) findViewById(R.id.recyclerViewUsers);
    }*/
    /*private void initObjects() {
        listUsers = new ArrayList<>();
        usersRecyclerAdapter = new UsersRecyclerAdapter(listUsers);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewUsers.setLayoutManager(mLayoutManager);
        recyclerViewUsers.setItemAnimator(new DefaultItemAnimator());
        recyclerViewUsers.setHasFixedSize(true);
        recyclerViewUsers.setAdapter(usersRecyclerAdapter);
        databaseHelper = new DatabaseHelper(activity);



        getDataFromSQLite();
    }*/
   /* private void getDataFromSQLite() {
        // AsyncTask is used that SQLite operation not blocks the UI Thread.
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                listUsers.clear();
                listUsers.addAll(databaseHelper.getAllUser());

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                usersRecyclerAdapter.notifyDataSetChanged();
            }
        }.execute();
    }*/








      /*  u=new User();
        tvNameGiris=(AppCompatTextView)findViewById(R.id.tvNameGiris);
        tvEmailGiris=(AppCompatTextView)findViewById(R.id.tvEmailGiris);
        tvAddressGiris=(AppCompatTextView)findViewById(R.id.tvAddressGiris);
        tvMobileGiris=(AppCompatTextView)findViewById(R.id.tvMobileGiris);
        tvBtypeGiris=(AppCompatTextView)findViewById(R.id.tvBtypeGiris);
        btnEdit=(AppCompatButton)findViewById(R.id.btnEdit);
        ivProfileImage=(AppCompatImageView)findViewById(R.id.ivProfileImage);

        tvNameGiris.setText(u.getName().toString());
        tvEmailGiris.setText(u.getEmail().toString());
        tvAddressGiris.setText(u.getAddress().toString());
        tvMobileGiris.setText(u.getMobile().toString());
        tvBtypeGiris.setText(u.getBtype().toString());


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






   /* protected void onActivityResult(int requestCode, int resultCode, Intent data)
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
//}
       // }

  //  };

}

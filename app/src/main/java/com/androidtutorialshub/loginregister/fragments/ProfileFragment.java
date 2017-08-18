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
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidtutorialshub.loginregister.R;
import com.androidtutorialshub.loginregister.activities.LoginActivity;
import com.androidtutorialshub.loginregister.activities.NavigaActivity;
import com.androidtutorialshub.loginregister.model.User;
import com.androidtutorialshub.loginregister.sql.DatabaseHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Seymanur on 16.08.2017.
 */

public class ProfileFragment extends Fragment {

    private DatabaseHelper databaseHelper;

    private TextInputEditText tvNameGiris, tvEmailGiris, tvSurnameGiris, tvMobileGiris;
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

    private String email,userId;
    private DatabaseReference mDatabase;
    private DatabaseReference myDatabaseReference;
    User myUser;
    FirebaseAuth auth;
    View view;
    @Nullable
    @Override


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {


        view=inflater.inflate(R.layout.fragment_profile,container,false);

        databaseHelper = new DatabaseHelper(getContext());
        tvNameGiris = (TextInputEditText) view.findViewById(R.id.tvNameGiris);
        tvEmailGiris = (TextInputEditText) view.findViewById(R.id.tvEmailGiris);
        tvSurnameGiris = (TextInputEditText) view.findViewById(R.id.tvSurnameGiris);
        tvMobileGiris = (TextInputEditText) view.findViewById(R.id.tvMobileGiris);
        btnEdit = (AppCompatButton) view.findViewById(R.id.btnEdit);
        btnUpdate = (AppCompatButton) view.findViewById(R.id.btnUpdate);
        ivProfileImage = (ImageView) view.findViewById(R.id.ivProfileImage);

        //db = databaseHelper.getReadableDatabase();


        disableEditText(tvNameGiris);
        disableEditText(tvEmailGiris);
        disableEditText(tvSurnameGiris);
        disableEditText(tvMobileGiris);

        btnUpdate.setVisibility(View.GONE);

        //login sayfasından gelen email
        //email=this.getArguments().getString("EMAIL").toString();

        //kayitGetir(email);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Bundle bundle = getArguments();
        myUser = (User)bundle.get("User");
        userId=(String)bundle.get("UserId");
        tvEmailGiris.setText(user.getEmail());
        tvNameGiris.setText(myUser.getName());
        tvSurnameGiris.setText(myUser.getSurname());
        tvMobileGiris.setText(myUser.getMobile());
        if(myUser.getImage()!=null){
            String fileData=myUser.getImage().toString();
            Bitmap imgBitmap = BitmapFactory.decodeFile(fileData);
            Bitmap resized = Bitmap.createScaledBitmap(imgBitmap, 100, 100, true);
            ivProfileImage.setImageBitmap(resized);
        }
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                enableEditText(tvNameGiris);
                enableEditText(tvSurnameGiris);
                enableEditText(tvMobileGiris);

                btnEdit.setVisibility(View.GONE);
                btnUpdate.setVisibility(View.VISIBLE);

            }
        });


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //byte[] img=getBytes(resized);
                updatePerson(tvNameGiris.getText().toString(),tvEmailGiris.getText().toString(),tvSurnameGiris.getText().toString(),tvMobileGiris.getText().toString());
                btnUpdate.setVisibility(View.GONE);
                btnEdit.setVisibility(View.VISIBLE);
                disableEditText(tvNameGiris);
                disableEditText(tvSurnameGiris);
                disableEditText(tvMobileGiris);
                ivProfileImage.setEnabled(false);
                Toast.makeText(getActivity(), "Update is success!", Toast.LENGTH_SHORT).show();
                 auth = FirebaseAuth.getInstance();
                if (auth.getCurrentUser() != null) {
                   auth.getCurrentUser().getUid();
                }
                mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.
                        if(auth.getCurrentUser()!=null){
                            User myUser= LoginActivity.getUserInfo(dataSnapshot);
                            Intent accountsIntent = new Intent(getActivity(), NavigaActivity.class);
                            Bundle b = new Bundle();
                            b.putSerializable("User", myUser);
                            accountsIntent.putExtras(b);
                            startActivity(accountsIntent);
                        }

                    }


                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                    }
                });

                /*byte[] img=getBytes(resized);
                editDatabase(tvNameGiris.getText().toString(),tvEmailGiris.getText().toString(), tvSurnameGiris.getText().toString(),
                        tvMobileGiris.getText().toString(),img);
                btnUpdate.setVisibility(View.GONE);
                btnEdit.setVisibility(View.VISIBLE);
                disableEditText(tvNameGiris);
                disableEditText(tvSurnameGiris);
                disableEditText(tvMobileGiris);
                ivProfileImage.setEnabled(false);*/
            }
        });


        return view;
    }

    public  void updatePerson(String name,String email,String surname,String mobile){
        String usersId;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        //myDatabaseReference=FirebaseDatabase.getInstance().getReference("Users");
        DatabaseReference mFirebaseDatabase = FirebaseDatabase.getInstance().getReference("Users");
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getActivity(), "Enter email name!", Toast.LENGTH_SHORT).show();
            return;
        }else{
            mDatabase.child("Users").child(userId).child("name").setValue(name);
        }

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getActivity(), "Enter email!", Toast.LENGTH_SHORT).show();
            return;
        }else{
            mDatabase.child("Users").child(userId).child("email").setValue(email);
        }
        if (TextUtils.isEmpty(surname)) {
            Toast.makeText(getActivity(), "Enter surname!", Toast.LENGTH_SHORT).show();
            return;
        }else{
            mDatabase.child("Users").child(userId).child("surname").setValue(surname);
        }
        if (TextUtils.isEmpty(mobile)) {
            Toast.makeText(getActivity(), "Enter mobile!", Toast.LENGTH_SHORT).show();
            return;
        }else{
            mDatabase.child("Users").child(userId).child("mobile").setValue(mobile);
        }
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
                tvSurnameGiris.setText(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ADDRESS)));
                tvMobileGiris.setText(cursor.getString(cursor.getColumnIndex(COLUMN_USER_MOBILE)));
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

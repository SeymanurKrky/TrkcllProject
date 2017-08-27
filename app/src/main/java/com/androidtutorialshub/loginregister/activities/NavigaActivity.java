package com.androidtutorialshub.loginregister.activities;

import android.content.Context;

import android.content.Intent;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.androidtutorialshub.loginregister.R;
import com.androidtutorialshub.loginregister.fragments.ProfileFragment;
import com.androidtutorialshub.loginregister.fragments.NavigaMainFragment;
import com.androidtutorialshub.loginregister.model.User;
import com.androidtutorialshub.loginregister.sql.DatabaseHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class NavigaActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private DrawerLayout drawer;
    private static final int IMAGE_PICK = 1;
    private View navHeader;
    TextView tvUserName,tvUserEmail;
    CircleImageView profileImage;
    NavigationView navigationView=null;
    Toolbar toolbar=null;
    public static Context contextOfApplication;

    private String emailFromIntent; //login user mail
    private static final String TABLE_USER = "ssby";
    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_USER_EMAIL = "user_email";
    private static final String COLUMN_USER_IMAGE= "user_image";
    Bitmap resized;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    User  myUser;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;

    private FirebaseDatabase mFirebaseDatabase;

    String userID;
    User user;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_naviga);

        NavigaMainFragment fragment=new NavigaMainFragment();
             android.support.v4.app.FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,fragment);
        fragmentTransaction.commit();


        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        emailFromIntent = getIntent().getStringExtra("EMAIL");
        contextOfApplication = getApplicationContext();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(NavigaActivity.this, LoginActivity.class));
                    finish();
                }

                tvUserEmail.setText(user.getEmail());
            }
        };

        /*databaseHelper = new DatabaseHelper(this);
        db = databaseHelper.getReadableDatabase();
        emailFromIntent = getIntent().getStringExtra("EMAIL");*/
        Bundle bn = new Bundle();
        bn = getIntent().getExtras();
        myUser = (User) bn.getSerializable("User");
        //tvUserEmail.setText("slm@sml.com");
         toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

         navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navHeader = navigationView.getHeaderView(0);
        tvUserName=(TextView)navHeader.findViewById(R.id.tv_userName);
        if(myUser!=null){
            tvUserName.setText(myUser.getName());
            tvUserEmail=(TextView)navHeader.findViewById(R.id.tvUserEmail);
            tvUserEmail.setText(myUser.getEmail());
        }
        profileImage=(CircleImageView)navHeader.findViewById(R.id.ivCircleProfileImg);
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Bir Fotoðraf Seçin"), IMAGE_PICK);
            }
        });
       //kayitGetir(emailFromIntent);
        if(user.getPhotoUrl()!=null){
            String fileData=user.getPhotoUrl().getEncodedPath();
            Bitmap imgBitmap = BitmapFactory.decodeFile(fileData);
            Bitmap resize=Bitmap.createScaledBitmap(imgBitmap, 100, 100, true);
            profileImage.setImageBitmap(resize);
            myUser.setImage(user.getPhotoUrl().getEncodedPath());
        }

    }

    public static String getBitMap(Uri data){
        Uri selectedImage = data;
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Context applicationContext = NavigaActivity.getContextOfApplication();
        Cursor cursor = applicationContext.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String filePath = cursor.getString(columnIndex);
        cursor.close();
        return filePath;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        this.imageFromGallery(resultCode, data);
    }
    //galeriden resim çekme
    private void imageFromGallery(int resultCode, Intent data) {
        String fileData=getBitMap(data.getData());
        Bitmap imgBitmap = BitmapFactory.decodeFile(fileData);
        resized = Bitmap.createScaledBitmap(imgBitmap, 100, 100, true);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(myUser.getName())
                .setPhotoUri(Uri.parse(fileData))
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("", "User profile updated.");
                        }
                    }
                });
        myUser.setImage(fileData);
        this.profileImage.setImageBitmap(resized);
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       getMenuInflater().inflate(R.menu.naviga, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            //set the fragment initially
            NavigaMainFragment fragment=new NavigaMainFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,fragment);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_gallery) {
            final Bundle bundle=new Bundle();
            bundle.putSerializable("User", myUser);
            bundle.putString("EMAIL",emailFromIntent);
            String userId=getUserId( FirebaseAuth.getInstance());
            bundle.putString("UserId",userId);
            ProfileFragment fragment=new ProfileFragment();
            fragment.setArguments(bundle);
            android.support.v4.app.FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,fragment).commit();

//set the fragment initially




        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }else if(id==R.id.nav_logout){
            auth = FirebaseAuth.getInstance();
            auth.signOut();
            startActivity(new Intent(NavigaActivity.this, LoginActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public static Context getContextOfApplication()
    {
        return contextOfApplication;
    }
    private void kayitGetir(String email) {
        String[] columns = {
                COLUMN_USER_NAME,
                COLUMN_USER_EMAIL,
                COLUMN_USER_IMAGE

        };

        // selection criteria
        String selection = COLUMN_USER_EMAIL + " = ?";

        // selection argument
        String[] selectionArgs = {email};

        /*Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order

        if (cursor.moveToFirst()) {
            do {

                //user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID))));

                tvUserName.setText(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)));
                tvUserEmail.setText(cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL)));
                byte[] image =cursor.getBlob(cursor.getColumnIndex(COLUMN_USER_IMAGE));

                profileImage.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.length));


            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();*/


    }

    public static String getUserId(FirebaseAuth auth){
        if(auth.getCurrentUser()!=null){
            return auth.getCurrentUser().getUid();
        }
        return "0";
    }
}

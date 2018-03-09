package com.example.devendra.sihapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {


    public static final int RC_SIGN_IN =1;






    //Auth
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    //Auth Ends here
    private Button btnSignOut;
    private TextView tvType;

    private StorageReference storageReference;
    private FirebaseFirestore firestore;
    private RecyclerView rcv;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference fireRef;

    LinearLayoutManager linearLayoutManager;
    List<Crop> cropsList;
    private CropsListAdapter cropsListAdapter;
    public static Context mcontext;
    public static Context getContext()
    {
        return mcontext;

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSignOut = (Button) findViewById(R.id.btnSignOut);
        tvType = (TextView)findViewById(R.id.tvType);

        //Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        //Auth ends


        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthUI.getInstance().signOut(MainActivity.this);
            }
        });
        //Auth
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null)
                {



                        if (user.getDisplayName()==null || user.getDisplayName().equals("")) {

                            LayoutInflater layoutInflaterAndroid = LayoutInflater.from(MainActivity.this);
                            View mView = layoutInflaterAndroid.inflate(R.layout.pop_layout, null, false);
                            AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this);
                            alertDialogBuilderUserInput.setView(mView);

                            final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.etPop);


                            alertDialogBuilderUserInput
                                    .setCancelable(false)
                                    .setPositiveButton("Set User Name", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            final String input = userInputDialogEditText.getText().toString();
                                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                    .setDisplayName(input).build();
                                            FirebaseUser u = firebaseAuth.getCurrentUser();
                                            u.updateProfile(profileUpdates);

//                                        Toast.makeText(MainActivity.this, input, Toast.LENGTH_SHORT).show();


                                        }
                                    });


                            AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
                            alertDialogAndroid.show();


                        }
                        Toast.makeText(MainActivity.this, "Welcome: " + user.getDisplayName(), Toast.LENGTH_SHORT).show();


                }
                else
                {
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.EmailBuilder().build(),
                                            new AuthUI.IdpConfig.PhoneBuilder().build(),
                                            new AuthUI.IdpConfig.GoogleBuilder().build()
                                    ))
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };
        //Auth ended

        Intent i = getIntent();
        String type = i.getStringExtra("type");
        tvType.setText(type);




    }









//Auth helping method for AuthStateListener

//Auth ended





    @Override
    protected void onPause() {
        super.onPause();
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN)
        {

            if(resultCode == RESULT_OK)
            {
                Toast.makeText(this, "You are signed in", Toast.LENGTH_SHORT).show();
            }
            else if(resultCode == RESULT_CANCELED)
            {
                Toast.makeText(this, "Sign in cancelled", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }



}


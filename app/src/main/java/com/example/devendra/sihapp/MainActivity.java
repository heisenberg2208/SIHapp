package com.example.devendra.sihapp;



        import android.content.Context;
        import android.content.Intent;
        import android.os.Bundle;
        import android.support.annotation.Nullable;
        import android.support.design.widget.Snackbar;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.bumptech.glide.Glide;
        import com.firebase.ui.auth.AuthUI;
        import com.firebase.ui.auth.ErrorCodes;
        import com.firebase.ui.auth.IdpResponse;
        import com.firebase.ui.auth.ResultCodes;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.database.ChildEventListener;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.firestore.DocumentChange;
        import com.google.firebase.firestore.DocumentSnapshot;
        import com.google.firebase.firestore.EventListener;
        import com.google.firebase.firestore.FirebaseFirestore;
        import com.google.firebase.firestore.FirebaseFirestoreException;
        import com.google.firebase.firestore.Query;
        import com.google.firebase.firestore.QuerySnapshot;
        import com.google.firebase.storage.FirebaseStorage;
        import com.google.firebase.storage.StorageReference;


        import java.util.ArrayList;
        import java.util.Arrays;
        import java.util.List;

public class MainActivity extends AppCompatActivity {
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        cropsList=new ArrayList<>();
        cropsListAdapter = new CropsListAdapter(cropsList);
        mcontext=getApplicationContext();

        FirebaseAuth auth = FirebaseAuth.getInstance();

        storageReference = FirebaseStorage.getInstance().getReference();
        firestore = FirebaseFirestore.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        rcv =findViewById(R.id.rcv);
        rcv.setHasFixedSize(true);
        rcv.setLayoutManager(new LinearLayoutManager(this));
        rcv.setAdapter(cropsListAdapter);
        fireRef=firebaseDatabase.getReference();

        firestore.collection("Crop_info").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    Log.d("Erroe", e.toString());
                } else {
                    fireRef.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            String var = dataSnapshot.getValue(String.class);
                            Toast.makeText(MainActivity.this, var, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                        if (doc.getType() == DocumentChange.Type.ADDED) {
                            Crop crop = doc.getDocument().toObject(Crop.class);
                            cropsList.add(crop);
                            cropsListAdapter.notifyDataSetChanged();
                            String title = doc.getDocument().getString("Title");

                        }
                    }


                }
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

                firestore.collection("Crop_info").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        if(e!=null){
                            Log.d("Erroe",e.toString());
                        }
                        else
                        {
                            for(DocumentChange doc: documentSnapshots.getDocumentChanges()){
                                if(doc.getType()==DocumentChange.Type.ADDED)
                                {
                                    Crop crop = doc.getDocument().toObject(Crop.class);
                                    cropsList.add(crop);
                                    cropsListAdapter.notifyDataSetChanged();
                                    String title =doc.getDocument().getString("Title");
                                    Log.d("Title",title);
                                }
                            }
                        }
                    }
                });



        }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }
}
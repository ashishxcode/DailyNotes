package co.ashishsonani.dailynotes.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import co.ashishsonani.dailynotes.Adapter.NotesAdapter;
import co.ashishsonani.dailynotes.Model.Listdata;
import co.ashishsonani.dailynotes.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class mainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    List<Listdata> list = new ArrayList<>();
    Context context;
    FloatingActionButton fabButton;
    TextView nothingToShow;
    CircleImageView userProfile;
    boolean doubleBackToExitPressedOnce = false;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // initialize views
        fabButton = findViewById(R.id.fabButton);
        recyclerView = findViewById(R.id.recycleview);
        nothingToShow = findViewById(R.id.nothingToShow);
        userProfile = findViewById(R.id.userProfile);
        //  firebase instance
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        // current user
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        final NotesAdapter notesAdapter = new NotesAdapter(list, this);
        // set adapter
        recyclerView.setAdapter(notesAdapter);

        databaseReference = firebaseDatabase.getReference(currentUser.getUid()).child("Notes");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Listdata listdata = dataSnapshot1.getValue(Listdata.class);
                    list.add(listdata);
                    nothingToShow.setVisibility(View.GONE);
                }
                notesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        // add new notes
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), addNoteActivity.class));
                finish();
            }
        });

        //getting user profile photo
        try {
            FirebaseUser user = mAuth.getCurrentUser();

            assert user != null;
            Glide.with(this)
                    .load(user.getPhotoUrl())
                    .into(userProfile);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }

        // user profile intent
        userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), profileActivity.class));
            }
        });

        // Check internet connection
        ConnectivityManager ConnectionManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert ConnectionManager != null;
        NetworkInfo networkInfo = ConnectionManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            nothingToShow.setVisibility(View.VISIBLE);
        } else {
            nothingToShow.setText("Network Not Available");

        }
    }


    // perform back pressed event
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "click back again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}

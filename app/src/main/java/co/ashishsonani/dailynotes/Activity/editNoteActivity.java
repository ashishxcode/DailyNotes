package co.ashishsonani.dailynotes.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import co.ashishsonani.dailynotes.Model.Listdata;
import co.ashishsonani.dailynotes.R;

public class editNoteActivity extends AppCompatActivity {

    TextInputEditText editNoteTitle, editNoteDescription;
    String titleSend, descriptionSend, currentDateSend, currentDate;
    private DatabaseReference mDatabase;
    private Listdata listdata;
    Button editButton, deleteButton;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        //initialize views
        editButton = findViewById(R.id.editButton);
        deleteButton = findViewById(R.id.deleteButton);
        editNoteTitle = findViewById(R.id.noteTitleEditText);
        editNoteDescription = findViewById(R.id.noteDescriptionEditText);
        // current user
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        //get data from intent
        final Intent i = getIntent();
        String gettitle = i.getStringExtra("title");
        String getdesc = i.getStringExtra("desc");
        final String id = i.getStringExtra("id");

        mDatabase = FirebaseDatabase.getInstance().getReference();
        editNoteTitle.setText(gettitle);
        editNoteDescription.setText(getdesc);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editNotes(id);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteNote(id);
            }
        });


        SimpleDateFormat sdf = new SimpleDateFormat("dd,MM,yyyy 'at' h:mm a", Locale.getDefault());
        currentDate = sdf.format(new Date());
    }

    private void editNotes(String id) {
        titleSend = editButton.getText().toString();
        descriptionSend = Objects.requireNonNull(editNoteDescription.getText()).toString();
        currentDateSend = currentDate.trim();
        Listdata listdata = new Listdata(id, titleSend, descriptionSend, currentDateSend);
        mDatabase.child(currentUser.getUid()).child("Notes").child(id).setValue(listdata).
                addOnCompleteListener(
                        new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getApplicationContext(), "Notes Edited", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), mainActivity.class));
                                finish();
                            }
                        });

    }

    private void deleteNote(String id) {
        mDatabase.child(currentUser.getUid()).child("Notes").child(id).setValue(listdata)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Notes Deleted", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), mainActivity.class));
                        finish();
                    }
                });
    }
}

package co.ashishsonani.dailynotes;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import co.ashishsonani.dailynotes.Model.Listdata;

public class editNoteActivity extends AppCompatActivity {

    TextInputEditText editNoteTitle, editNoteDescription;
    String titleSend, descriptionSend;
    private DatabaseReference mDatabase;
    private Listdata listdata;
    Button editButton, deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        //initialize views
        editButton = findViewById(R.id.editButton);
        deleteButton = findViewById(R.id.deleteButton);
        editNoteTitle = findViewById(R.id.noteTitleEditText);
        editNoteDescription = findViewById(R.id.noteDescriptionEditText);

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
    }

    private void editNotes(String id) {
        titleSend = editButton.getText().toString();
        descriptionSend = editNoteDescription.getText().toString();
        Listdata listdata = new Listdata(id, titleSend, descriptionSend);
        mDatabase.child("Notes").child(id).setValue(listdata).
                addOnCompleteListener(
                        new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getApplicationContext(), "Notes Edited", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();
                            }
                        });

    }

    private void deleteNote(String id) {
        mDatabase.child("Notes").child(id).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Notes Deleted", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();

                    }
                });
    }
}

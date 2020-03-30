package co.ashishsonani.dailynotes.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
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

public class addNoteActivity extends AppCompatActivity {

    TextInputEditText titleEditText, descriptionEditText;
    TextInputLayout tilTitle ,tilDescription;
    String titleSend;
    String descriptionSend;
    String currentDateSend;
    String currentDate;
    FirebaseUser currentUser;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        //initialize view
        titleEditText = findViewById(R.id.noteTitleEditText);
        descriptionEditText = findViewById(R.id.noteDescriptionEditText);
        tilTitle = findViewById(R.id.title);
        tilDescription = findViewById(R.id.description);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        SimpleDateFormat sdf = new SimpleDateFormat("dd,MM,yyyy 'at' h:mm a", Locale.getDefault());
        currentDate = sdf.format(new Date());
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void AddNotes(View view) {
        titleSend = Objects.requireNonNull(titleEditText.getText()).toString();
        descriptionSend = Objects.requireNonNull(descriptionEditText.getText()).toString();
        currentDateSend = currentDate.trim();
        if (TextUtils.isEmpty(titleSend) || TextUtils.isEmpty(descriptionSend)) {
            tilTitle.setError("Please Enter Note Title");
            tilDescription.setError("Please Enter Note Description");
            return;
        }
        AddNotes(titleSend, descriptionSend,currentDateSend);

    }

    private void AddNotes(String titleSend, String descriptionSend,String currentDateSend)
    {

        String id=mDatabase.push().getKey();
        Listdata listdata = new Listdata(id,titleSend, descriptionSend,currentDateSend);
        assert id != null;
        mDatabase.child(currentUser.getUid()).child("Notes").child(id).setValue(listdata).
                addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(), "Notes Added", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), mainActivity.class));
                        finish();
                    }
                });

    }

}
package co.ashishsonani.dailynotes.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import co.ashishsonani.dailynotes.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class profileActivity extends AppCompatActivity {

    CircleImageView imageView;
    TextView textName, textEmail;
    Button singout;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();

        imageView = findViewById(R.id.userProfile);
        textName = findViewById(R.id.textViewName);
        textEmail = findViewById(R.id.textViewEmail);
        singout = findViewById(R.id.singOut);

        try {
            FirebaseUser user = mAuth.getCurrentUser();

            assert user != null;
            Glide.with(this)
                    .load(user.getPhotoUrl())
                    .into(imageView);
            textName.setText(user.getDisplayName());
            textEmail.setText(user.getEmail());
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }

        singout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getApplicationContext(), "Sign out ", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), signInActivity.class));
                finish();
            }
        });
    }
}

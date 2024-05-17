package com.example.complain;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private EditText quoteEditText;
    private EditText authorEditText;
    private Button addButton;
    private Button logoutButton; // Added logout button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        quoteEditText = findViewById(R.id.editTextQuote);
        authorEditText = findViewById(R.id.editTextAuthor);
        addButton = findViewById(R.id.addButton);
        logoutButton = findViewById(R.id.logoutButton); // Initialize logout button

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quote = quoteEditText.getText().toString();
                String author = authorEditText.getText().toString();

                if (quote.isEmpty()) {
                    quoteEditText.setError("Cannot be empty");
                    return;
                }
                if (author.isEmpty()) {
                    authorEditText.setError("Cannot be empty");
                    return;
                }

                addQuoteToDB(quote, author);
            }
        });

        // Logout button click listener
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform logout action
                FirebaseAuth.getInstance().signOut();
                // Redirect to the login page
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
                finish(); // Finish the current activity
            }
        });
    }

    private void addQuoteToDB(String quote, String author) {
        HashMap<String, Object> quoteHashmap = new HashMap<>();
        quoteHashmap.put("quote", quote);
        quoteHashmap.put("author", author);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference quotesRef = database.getReference("complains");

        String key = quotesRef.push().getKey();
        quoteHashmap.put("key", key);
        quotesRef.child(key).setValue(quoteHashmap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(MainActivity.this, "Added", Toast.LENGTH_SHORT).show();
                quoteEditText.getText().clear();
                authorEditText.getText().clear();
            }
        });
    }
}

package org.androidtown.dietapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserInfoActivity extends AppCompatActivity {
    TextView textViewName;
    TextView textViewAge;
    TextView textViewWeight;
    TextView textViewBasicCalorie;
    TextView textViewGender;

    EditText editTextName;
    EditText editTextAge;
    EditText editTextWeight;
    EditText editTextBasicCalorie;
    EditText editTextGender;

    Button buttonSubmit;
    Button buttonSignIn;

    DatabaseReference mRoofRef;
    DatabaseReference mUserRef;
    String uid;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid=user.getUid();
        mRoofRef = FirebaseDatabase.getInstance().getReference();
        mUserRef=mRoofRef.child("user").child(uid);

        textViewName=(TextView)findViewById(R.id.textViewName);
        textViewAge=(TextView)findViewById(R.id.textViewAge);
        textViewWeight=(TextView)findViewById(R.id.textViewWeight);
        textViewBasicCalorie=(TextView)findViewById(R.id.textViewBasicCalorie);
        textViewGender=(TextView)findViewById(R.id.textViewGender);

        editTextName=(EditText) findViewById(R.id.editTextName);
        editTextAge=(EditText)findViewById(R.id.editTextAge);
        editTextWeight=(EditText)findViewById(R.id.editTextWeight);
        editTextBasicCalorie=(EditText)findViewById(R.id.editTextBasicCalorie);
        editTextGender=(EditText)findViewById(R.id.editTextGender);

        buttonSubmit=(Button)findViewById(R.id.buttonSubmit);
        buttonSignIn=(Button)findViewById(R.id.buttonSignin);

        mUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UsersItem curUsersItem = dataSnapshot.getValue(UsersItem.class);
                if(curUsersItem!=null) {
                    editTextName.setText(curUsersItem.getName());
                    editTextAge.setText(curUsersItem.getAge() + "");
                    editTextWeight.setText(curUsersItem.getWeight() + "");
                    editTextBasicCalorie.setText(curUsersItem.getBasicCalorie() + "");
                    editTextGender.setText(curUsersItem.getGender());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String email = user.getEmail();
                String name = editTextName.getText().toString();
                int age = Integer.parseInt(editTextAge.getText().toString());
                int weight = Integer.parseInt(editTextWeight.getText().toString());
                int basicCalorie = Integer.parseInt(editTextBasicCalorie.getText().toString());
                String gender = editTextGender.getText().toString();
                mUserRef.setValue(new UsersItem(email, uid, name, age, weight, basicCalorie, gender));
                finish();
            }
        });

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent AuthIntent = new Intent(UserInfoActivity.this,EmailPasswordActivity.class);
                startActivity(AuthIntent);
                finish();
            }
        });

    }
}

package com.delaroystudios.alarmreminder;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class loginActivity extends AppCompatActivity {


    private Button loginBtn;
    private EditText email;
    private EditText password;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private Intent intentToHome;
    //
    private FirebaseUser user;
    //
    public static final String shP ="login";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        email = (EditText) findViewById(R.id.loginemailTxt);
        password = (EditText) findViewById(R.id.loginpassTxt);
        loginBtn = (Button) findViewById(R.id.btnLogin);

        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();

        intentToHome = new Intent(loginActivity.this,homeActivity.class);


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userEmail =  email.getText().toString();
                String userPass =  password.getText().toString();

                if(TextUtils.isEmpty(userEmail)){
                    //email is empty
                    Toast.makeText(loginActivity.this,"Please enter your e-mail",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(userPass)){
                    //email is empty
                    Toast.makeText(loginActivity.this,"Please enter your password",Toast.LENGTH_SHORT).show();
                    return;
                }

                //validation is ok
                //create progressdialog
                progressDialog.setMessage("login...");
                progressDialog.show();

                //firebase
                firebaseAuth.signInWithEmailAndPassword(userEmail,userPass).addOnCompleteListener(loginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            //unique id
                            user =  firebaseAuth.getCurrentUser();
                            //
                            Toast.makeText(loginActivity.this,"login successfully",Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            //save login in shared preference

                            SharedPreferences setting = getSharedPreferences(shP,0);
                            SharedPreferences.Editor  edit=  setting.edit();
                            edit.putString("flag","true");
                            if ( user.getEmail()!= null) {
                                edit.putString("email", user.getEmail());

                            }
                            edit.commit();
                            if (user.getEmail() != null) {
                                Toast.makeText(loginActivity.this, user.getEmail(), Toast.LENGTH_SHORT).show();
                                intentToHome.putExtra("userId",user.getEmail());

                            }


                            startActivity(intentToHome);

                            finish();
                        }
                        else{
                            Toast.makeText(loginActivity.this," couldn't login, pls. try again...",Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }

                    }
                });














            }
        });






    }
}

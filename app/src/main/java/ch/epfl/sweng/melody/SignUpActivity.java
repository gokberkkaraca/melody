package ch.epfl.sweng.melody;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import ch.epfl.sweng.melody.util.NavigationHandler;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText inputEmail;
    private EditText inputPassword;
    private EditText inputDisplayName;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        findViewById(R.id.register_button).setOnClickListener(this);
        findViewById(R.id.already_have_account_login_button).setOnClickListener(this);
        inputEmail = (EditText) findViewById(R.id.create_new_email);
        inputPassword = (EditText) findViewById(R.id.create_new_password);
        inputDisplayName = (EditText) findViewById(R.id.create_display_name);
        progressBar = (ProgressBar) findViewById(R.id.sign_up_processbar);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        NavigationHandler.goToLogInActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.already_have_account_login_button:
                NavigationHandler.goToLogInActivity(SignUpActivity.this);
                break;
            case R.id.register_button:
                emailPasswordsSignUp();
        }
    }

    public void emailPasswordsSignUp() {
        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();
        final String displayname = inputDisplayName.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), R.string.email_is_empty, Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), R.string.password_is_empty, Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(getApplicationContext(), R.string.minimum_password, Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(displayname)) {
            Toast.makeText(getApplicationContext(), R.string.nickname_is_empty, Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        MainActivity.getFirebaseAuthInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (!task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this, "Authentication failed." + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SignUpActivity.this, "Create your melody account successfully!", Toast.LENGTH_SHORT).show();
                            FirebaseUser firebaseUser = MainActivity.getFirebaseAuthInstance().getCurrentUser();
                            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(displayname).build();
                            assert firebaseUser != null;
                            firebaseUser.updateProfile(profileChangeRequest);
                            NavigationHandler.goToLogInActivity(SignUpActivity.this);
                        }
                    }
                });
    }
}

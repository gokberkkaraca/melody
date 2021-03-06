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

import ch.epfl.sweng.melody.util.NavigationHandler;

public class ResetPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText inputEmail;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        inputEmail = (EditText) findViewById(R.id.reset_email);
        progressBar = (ProgressBar) findViewById(R.id.reset_password_processbar);
        findViewById(R.id.reset_password_button).setOnClickListener(this);
        findViewById(R.id.go_back_button).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.go_back_button:
                super.onBackPressed();
                NavigationHandler.goToLogInActivity(this);
                break;
            case R.id.reset_password_button:
                sendResetPasswordEmail();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        NavigationHandler.goToPublicMemoryActivity(this);
    }

    private void sendResetPasswordEmail() {
        String email = inputEmail.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplication(), R.string.reset_password_empty, Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        MainActivity.getFirebaseAuthInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ResetPasswordActivity.this, R.string.send_reset_password_email, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ResetPasswordActivity.this, R.string.fail_send_rest_password_email, Toast.LENGTH_SHORT).show();
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }
}


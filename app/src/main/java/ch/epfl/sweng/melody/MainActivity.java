package ch.epfl.sweng.melody;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import ch.epfl.sweng.melody.R;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void toCreateMemory(View view) {
        Intent intent = new Intent(MainActivity.this, CreateMemoryActivity.class);
        startActivity(intent);
    }
}

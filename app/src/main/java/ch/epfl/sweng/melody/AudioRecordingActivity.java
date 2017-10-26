package ch.epfl.sweng.melody;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class AudioRecordingActivity extends AppCompatActivity {

    private static final int RequestPermissionCode = 1;
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private String audioSavingPath;
    private Button startButton, stopButton, playButton, stopPlayButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_recording);

        startButton = findViewById(R.id.start_button);
        stopButton = findViewById(R.id.stop_button);
        playButton = findViewById(R.id.play_button);
        stopPlayButton = findViewById(R.id.stop_play_button);

        stopButton.setEnabled(false);
        playButton.setEnabled(false);
        stopPlayButton.setEnabled(false);


        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String randomFileName = UUID.randomUUID().toString().substring(0,10);
                if (checkPermission()) {
                    audioSavingPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
                            randomFileName + "AudioRecording.3gp";

                    prepareRecorder();

                    try {
                        mediaRecorder.prepare();
                        mediaRecorder.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } //What to do when catching an exception ?

                    startButton.setEnabled(false);
                    stopButton.setEnabled(true);

                    Toast.makeText(AudioRecordingActivity.this, "Recording started", Toast.LENGTH_SHORT).show();
                } else {
                    requestPermission();
                }
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mediaRecorder.stop();

                startButton.setEnabled(true);
                stopButton.setEnabled(false);
                playButton.setEnabled(true);
                stopPlayButton.setEnabled(false);

                Toast.makeText(AudioRecordingActivity.this, "Recording Completed",
                        Toast.LENGTH_SHORT).show();
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startButton.setEnabled(false);
                stopButton.setEnabled(false);
                stopPlayButton.setEnabled(true);

                mediaPlayer = new MediaPlayer();

                try {
                    mediaPlayer.setDataSource(audioSavingPath);
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                } //How to handle it ?

                mediaPlayer.start();
                Toast.makeText(AudioRecordingActivity.this, "Recording Playing", Toast.LENGTH_SHORT).show();
            }
        });

        stopPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopButton.setEnabled(false);
                startButton.setEnabled(true);
                playButton.setEnabled(true);
                stopPlayButton.setEnabled(false);

                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    prepareRecorder();
                }
            }
        });

    }

    private void prepareRecorder() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(audioSavingPath);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(AudioRecordingActivity.this, new String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length > 0) {
                    boolean StoragePermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(AudioRecordingActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AudioRecordingActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    private boolean checkPermission() {
        int write = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int record = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);

        return write == PackageManager.PERMISSION_GRANTED && record == PackageManager.PERMISSION_GRANTED;
    }

    private void submitAudio() {
        Intent intent = new Intent(AudioRecordingActivity.this, CreateMemoryActivity.class);
        intent.putExtra("audioPath", audioSavingPath);
        startActivity(intent);
    }

}

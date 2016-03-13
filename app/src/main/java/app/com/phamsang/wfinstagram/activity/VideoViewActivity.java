package app.com.phamsang.wfinstagram.activity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import app.com.phamsang.wfinstagram.R;

public class VideoViewActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener{
    public static final String EXTRA_VIDEO_URL = "video_url";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);


        VideoView v = (VideoView) findViewById(R.id.videoView);

        String url = null;
        if (getIntent().getExtras() != null) {
            url = getIntent().getExtras().getString(EXTRA_VIDEO_URL);

            if (url != null) {
                v.setMediaController(new MediaController(this));
                v.setOnCompletionListener(this);
                v.setVideoURI(Uri.parse(url));
                v.start();
            }
        }

        if (url == null) {
            throw new IllegalArgumentException("Must set url extra paremeter in intent.");
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        finish();
    }

    public static void showRemoteVideo(Context c, String url) {
        Intent i = new Intent(c, VideoViewActivity.class);

        i.putExtra(VideoViewActivity.EXTRA_VIDEO_URL, url);
        c.startActivity(i);
    }
}

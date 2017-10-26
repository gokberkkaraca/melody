package ch.epfl.sweng.melody.account;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;

public class GoogleProfilePictureAsync extends AsyncTask<Void, Void, Bitmap> {

    private final ImageView img;
    private final Uri uri;

    public GoogleProfilePictureAsync(ImageView img, Uri uri) {
        this.img = img;
        this.uri = uri;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {

        try {
            URL ulr = new URL(uri.toString());
            InputStream inp = ulr.openConnection().getInputStream();
            return BitmapFactory.decodeStream(inp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void onPostExecute(Bitmap result) {
        this.img.setImageBitmap(result);
    }
}


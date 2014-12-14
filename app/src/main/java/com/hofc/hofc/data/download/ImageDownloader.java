package com.hofc.hofc.data.download;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.hofc.hofc.data.ActusBDD;
import com.hofc.hofc.data.DataSingleton;
import com.hofc.hofc.vo.ActuVO;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {

	private ImageView imageView;
	private ActuVO actuVo;
    private ProgressBar progressBar;
	public ImageDownloader(ImageView image, ActuVO actuVO) {
		this.imageView = image;
		this.actuVo = actuVO;
	}

    public ImageDownloader(ImageView image, ActuVO actuVO, ProgressBar progressBar) {
        this.imageView = image;
        this.actuVo = actuVO;
        this.progressBar = progressBar;
    }
	
	@Override
	protected Bitmap doInBackground(String... params) {
		String imageUrl = params[0];
		URL url = null;
		Bitmap bmp = null;
        bmp = DataSingleton.getCachedImage(imageUrl);
        if(bmp == null) {
            try {
                url = new URL(imageUrl);
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (MalformedURLException e) {
                Log.e("ImageDownloader", "Problem with URL format", e);
            } catch (IOException e) {
                Log.e("ImageDownloader", "Problem when downloading image at url " + url, e);
            }
            if(bmp != null && this.actuVo != null) {
                this.actuVo.setBitmapImage(bmp);
                ActusBDD.updateImageBitmap(this.actuVo.getPostId(), bmp);
            }
            DataSingleton.insertImageCache(imageUrl, bmp);
        }
        return bmp;
	}
	
	@Override
	protected void onPostExecute(Bitmap result) {
		imageView.setImageBitmap(result);
        imageView.setVisibility(View.VISIBLE);
        if(this.progressBar != null) {
            this.progressBar.setVisibility(View.GONE);
        }
	}

}

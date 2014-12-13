package com.hofc.hofc.data.download;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.hofc.hofc.data.ActusBDD;
import com.hofc.hofc.vo.ActuVO;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {

	private ImageView imageView;
	private ActuVO actuVo;
	public ImageDownloader(ImageView image, ActuVO actuVO) {
		this.imageView = image;
		this.actuVo = actuVO;
	}
	
	@Override
	protected Bitmap doInBackground(String... params) {
		String imageUrl = params[0];
		URL url = null;
		Bitmap bmp = null;
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
        return bmp;
	}
	
	@Override
	protected void onPostExecute(Bitmap result) {
		imageView.setImageBitmap(result);
	}

}

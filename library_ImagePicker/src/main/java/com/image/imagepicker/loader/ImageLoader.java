package com.image.imagepicker.loader;

import android.app.Activity;
import android.widget.ImageView;

public interface ImageLoader {

    public void displayImage(Activity activity, String path, ImageView imageView);

    public void displayImage(Activity activity, String path, ImageView imageView, int width, int height);

}

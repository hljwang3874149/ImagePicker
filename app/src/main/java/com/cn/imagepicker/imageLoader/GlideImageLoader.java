package com.cn.imagepicker.imageLoader;

import android.app.Activity;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.image.imagepicker.loader.ImageLoader;

import java.io.File;
import java.net.URI;

/**
 * ==================================================
 * 项目名称：ImagePicker
 * 创建人：wangxiaolong
 * 创建时间：2016/12/13 下午2:31
 * 修改时间：2016/12/13 下午2:31
 * 修改备注：
 * Version：
 * ==================================================
 */
public class GlideImageLoader implements ImageLoader {
    @Override
    public void displayImage(Activity activity, String path, ImageView imageView) {

        Glide.with(activity).load(Uri.fromFile(new File(path))).into(imageView);
    }

    @Override
    public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
        Glide.with(activity).load(Uri.fromFile(new File(path))).override(width, height).into(imageView);
    }
}

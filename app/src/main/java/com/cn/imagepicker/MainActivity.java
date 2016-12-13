package com.cn.imagepicker;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.cn.imagepicker.imageLoader.GlideImageLoader;
import com.image.imagepicker.ImagePicker;
import com.image.imagepicker.bean.ImageItem;
import com.image.imagepicker.ui.ImageGridActivity;
import com.image.imagepicker.utils.PermissionCheckerUtil;
import com.image.imagepicker.view.CropImageView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Button mButton;
    private ImageView mImageView;
    ImagePicker imagePicker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpPicker();
        initView();

    }

    private void initView() {
        mButton = (Button) findViewById(R.id.open);
        mImageView = (ImageView) findViewById(R.id.show);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //6.0判断读取外部存储权限  lib已适配7.0 contenturi
                if (PermissionCheckerUtil.isOnRequestPermissions(MainActivity.this,
                        0, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE})) {
                    openGallery();
                }

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 0:
                PermissionCheckerUtil.onRequestPermissionResult(grantResults, new PermissionCheckerUtil.onPermissionListener() {
                    @Override
                    public void onDenyListener() {
                        Toast.makeText(MainActivity.this, "拒绝开启权限，无法读取图片", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onGrantListener() {
                        openGallery();
                    }
                });
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    private void openGallery() {
        Intent intent = new Intent(this, ImageGridActivity.class);
        startActivityForResult(intent, 10);
    }

    private void setUpPicker() {
        imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);  //显示拍照按钮 6.0 lib内部判断camera 权限
        imagePicker.setCrop(false);        //允许裁剪（单选才有效）
//        imagePicker.setSaveRectangle(true); //是否按矩形区域保存
        imagePicker.setSelectLimit(1);    //选中数量限制
//        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
//        imagePicker.setFocusWidth(800);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
//        imagePicker.setFocusHeight(800);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
//        imagePicker.setOutPutX(1000);//保存文件的宽度。单位像素
//        imagePicker.setOutPutY(1000);//保存文件的高度。单位像素
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == 10) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);

                imagePicker.getImageLoader().displayImage(this, images.get(0).path, mImageView);
            } else {
                Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

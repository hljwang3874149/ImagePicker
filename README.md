# ImagePicker
the image library  fork from https://github.com/jeasonlzy/ImagePicker   . do some change  ,now support  android 7.0 
Usage : i copy from original libary 

1.用法

使用前，对于Android Studio的用户，可以选择添加:

    compile 'com.lzy.widget:imagepicker:0.3.2'  //指定版本

    compile 'com.lzy.widget:imagepicker:+'      //最新版本
    为了支持 android 7.0 添加 supportv4 包
    支持项目效果添加 support v7, 可以自行修改。
2.功能和参数含义

温馨提示:目前库中的预览界面有个原图的复选框,暂时只做了UI,还没有做压缩的逻辑

配置参数	参数含义
multiMode	图片选着模式，单选/多选
selectLimit	多选限制数量，默认为9
showCamera	选择照片时是否显示拍照按钮
crop	是否允许裁剪（单选有效）
style	有裁剪时，裁剪框是矩形还是圆形
focusWidth	矩形裁剪框宽度（圆形自动取宽高最小值）
focusHeight	矩形裁剪框高度（圆形自动取宽高最小值）
outPutX	裁剪后需要保存的图片宽度
outPutY	裁剪后需要保存的图片高度
isSaveRectangle	裁剪后的图片是按矩形区域保存还是裁剪框的形状，例如圆形裁剪的时候，该参数给true，那么保存的图片是矩形区域，如果该参数给fale，保存的图片是圆形区域
imageLoader	需要使用的图片加载器，自需要实现ImageLoader接口即可
3.代码参考

0.首先你需要继承 com.lzy.imagepicker.loader.ImageLoader 这个接口,实现其中的方法,比如以下代码是使用 Picasso 三方加载库实现的

    public class PicassoImageLoader implements ImageLoader {

        @Override
        public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
            Picasso.with(activity)//
                    .load(new File(path))//
                    .placeholder(R.mipmap.default_image)//
                    .error(R.mipmap.default_image)//
                    .resize(width, height)//
                    .centerInside()//
                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)//
                    .into(imageView);
        }

        @Override
        public void clearMemoryCache() {
            //这里是清除缓存的方法,根据需要自己实现
        }
    }
1.然后配置图片选择器，一般在Application初始化配置一次就可以,这里就需要将上面的图片加载器设置进来,其余的配置根据需要设置

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_picker);

        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new PicassoImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);  //显示拍照按钮
        imagePicker.setCrop(true);        //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true); //是否按矩形区域保存
        imagePicker.setSelectLimit(9);    //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);//保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);//保存文件的高度。单位像素
    }
2.以上配置完成后，在适当的方法中开启相册，例如点击按钮时

    public void onClick(View v) {
    //android 6.0 添加权限检查  
            Intent intent = new Intent(this, ImageGridActivity.class);
            startActivityForResult(intent, IMAGE_PICKER);  
        }
    }
3.重写onActivityResult方法,回调结果

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == IMAGE_PICKER) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                MyAdapter adapter = new MyAdapter(images);
                gridView.setAdapter(adapter);
            } else {
                Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
            }
        }
    }

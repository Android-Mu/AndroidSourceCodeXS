package com.codexiaosheng.opensysmedia;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int ALBUM_REQUEST_CODE = 2;
    private static final int VIDEO_REQUEST_CODE = 3;
    private static final int RECORDING_VIDEO_REQUEST_CODE = 4;
    private static final int FILE_MANAGER_REQUEST_CODE = 5;
    private static final int RECORDING_AUDIO_REQUEST_CODE = 6;
    private static final int AUDIO_REQUEST_CODE = 7;

    private String strMediaName = ""; // 媒体文件名
    private String strMediaFilePath = ""; // 媒体文件（保存）路径

    private Button btnCamera;
    private Button btnAlbum;
    private Button btnChooseVideo;
    private Button btnRecordingVideo;
    private Button btnRecordingAudio;
    private Button btnChooseAudio;
    private Button btnFm;

    private TextView tvShowPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        strMediaFilePath = Environment.getExternalStorageDirectory().toString() + "/";
        initView();
    }

    private void initView() {
        btnCamera = (Button) findViewById(R.id.btn_camera);
        btnAlbum = (Button) findViewById(R.id.btn_album);
        btnChooseVideo = (Button) findViewById(R.id.btn_choose_video);
        btnRecordingVideo = (Button) findViewById(R.id.btn_recording_video);
        btnRecordingAudio = (Button) findViewById(R.id.btn_recording_audio);
        btnChooseAudio = (Button) findViewById(R.id.btn_choose_audio);
        btnFm = (Button) findViewById(R.id.btn_fm);
        tvShowPath = (TextView) findViewById(R.id.tv_show_path);

        btnCamera.setOnClickListener(this);
        btnAlbum.setOnClickListener(this);
        btnChooseVideo.setOnClickListener(this);
        btnRecordingVideo.setOnClickListener(this);
        btnRecordingAudio.setOnClickListener(this);
        btnChooseAudio.setOnClickListener(this);
        btnFm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        tvShowPath.setText("");
        switch (v.getId()) {
            case R.id.btn_camera:
                makePhoto();
                break;
            case R.id.btn_album:
                openAlbum();
                break;
            case R.id.btn_choose_video:
                chooseVideo();
                break;
            case R.id.btn_recording_video:
                recordingVideo();
                break;
            case R.id.btn_recording_audio:
                recordingAudio();
                break;
            case R.id.btn_choose_audio:
                Toast.makeText(this, "未实现", Toast.LENGTH_SHORT).show();
//                chooseAudio();
                break;
            case R.id.btn_fm:
                openFileManager();
                break;
        }
    }

    /**
     * 系统相机
     */
    private void makePhoto() {
        strMediaName = System.currentTimeMillis() + "cxs.jpg";
        Intent camera_intent = new Intent("android.media.action.IMAGE_CAPTURE");
        camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, getMediaPath());
        camera_intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
        startActivityForResult(camera_intent, CAMERA_REQUEST_CODE);
    }

    /**
     * 系统相册
     */
    private void openAlbum() {
        Intent album_intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        album_intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(album_intent, ALBUM_REQUEST_CODE);
    }

    /**
     * 选择本地视频
     */
    private void chooseVideo() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("video/*");
        startActivityForResult(intent, VIDEO_REQUEST_CODE);
    }

    /**
     * 调用系统相机录像
     */
    private void recordingVideo() {
        strMediaName = System.currentTimeMillis() + "cxs.mp4";
        Intent intent = new Intent();
        intent.setAction("android.media.action.VIDEO_CAPTURE");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(strMediaFilePath, strMediaName)));
        startActivityForResult(intent, RECORDING_VIDEO_REQUEST_CODE);
    }

    /**
     * 调用系统录音
     */
    private void recordingAudio() {
        strMediaName = System.currentTimeMillis() + "cxs.mp3"; // amr
        Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(strMediaFilePath, strMediaName)));
        startActivityForResult(intent, RECORDING_AUDIO_REQUEST_CODE);
    }

    /**
     * 调用系统选择音频
     */
    private void chooseAudio() {
        Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
        intent.setType("audio/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        intent.putExtra("return-data", true);
        startActivityForResult(intent, AUDIO_REQUEST_CODE);
    }

    /**
     * 跳转文件管理界面
     */
    private void openFileManager() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        intent.putExtra("return-data", true);
        startActivityForResult(intent, FILE_MANAGER_REQUEST_CODE);
    }

    /**
     * 获取文件保存地址的Uri
     *
     * @return
     */
    public Uri getMediaPath() {
        return Uri.fromFile(new File(strMediaFilePath, strMediaName));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        } else {
            switch (requestCode) {
                case CAMERA_REQUEST_CODE:
                    Toast.makeText(this, "camera", Toast.LENGTH_SHORT).show();
                    tvShowPath.setText(strMediaFilePath + strMediaName);
                    break;
                case ALBUM_REQUEST_CODE:
                    Toast.makeText(this, "album", Toast.LENGTH_SHORT).show();
                    getResultPath(data);
                    break;
                case VIDEO_REQUEST_CODE:
                    Toast.makeText(this, "local video", Toast.LENGTH_SHORT).show();
                    break;
                case RECORDING_VIDEO_REQUEST_CODE:
                    Toast.makeText(this, "recording video", Toast.LENGTH_SHORT).show();
                    tvShowPath.setText(strMediaFilePath + strMediaName);
                    break;
                case RECORDING_AUDIO_REQUEST_CODE:
                    Toast.makeText(this, "recording audio", Toast.LENGTH_SHORT).show();
                    tvShowPath.setText(strMediaFilePath + strMediaName);
                    break;
                case AUDIO_REQUEST_CODE:
                    Toast.makeText(this, "local audio", Toast.LENGTH_SHORT).show();
                    getResultPath(data);
                    break;
                case FILE_MANAGER_REQUEST_CODE:
                    Toast.makeText(this, "local file", Toast.LENGTH_SHORT).show();
                    getResultPath(data);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 显示返回结果地址
     *
     * @param data
     */
    private void getResultPath(Intent data) {
        Uri uri = data.getData();
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            String a_path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
            tvShowPath.setText(a_path);
        }
    }

    /**
     * 获取视频的缩略图
     * <p>
     * 先通过ThumbnailUtils来创建一个视频的缩略图，然后再利用ThumbnailUtils来生成指定大小的缩略图。
     * 如果想要的缩略图的宽和高都小于MICRO_KIND，则类型要使用MICRO_KIND作为kind的值，这样会节省内存。
     *
     * @param videoPath 视频的路径
     * @param width     指定输出视频缩略图的宽度
     * @param height    指定输出视频缩略图的高度度
     * @param kind      参照MediaStore.Images.Thumbnails类中的常量MINI_KIND和MICRO_KIND。
     *                  其中，MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96
     * @return 指定大小的视频缩略图
     */
    private Bitmap getVideoThumbnail(String videoPath, int width, int height,
                                     int kind) {
        Bitmap bitmap = null;
        // 获取视频的缩略图
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

}

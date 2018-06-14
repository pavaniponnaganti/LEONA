package com.leona.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.BuildConfig;

import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


import com.leona.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class PicSelectActivity extends AppCompatActivity {
    static final int REQUEST_TAKE_PHOTO = 0;
    static final int REQUEST_GALLERY_PHOTO = 1;
    public final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 4;
    private static final String TAG = "CamActivity";
    String gallery_album = "gallery_album";
    String camera_album = "camera_album";
    File storagePicFile = null;
    String mCurrentPhotoPath = "";
    String appPackageName;

    public static String[] thumbColumns = {MediaStore.Video.Thumbnails.DATA};
    public static String[] mediaColumns = {MediaStore.Video.Media._ID};

    private static float exifOrientationToDegrees(int exifOrientation) {
        try {
            if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
                return 90;
            } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
                return 180;
            } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
                return 270;
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return 0;
    }

    public static String intentToString(Intent intent) {
        if (intent == null) {
            return null;
        }

        String result = intent.toString() + " " + bundleToString(intent.getExtras());
        Log.d(TAG, "intentToString: result : " + result);
        return result;
    }

//    public void initUI(){
//        cam_image_iv = (ImageView) findViewById(R.id.cam_image_iv);
//
//        cam_image_iv.setOnClickListener(this);
//    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.cam_image_iv:
//                photoTakeOption();
//                break;
//        }
//    }

    public static String bundleToString(Bundle bundle) {
        StringBuilder out = new StringBuilder("Bundle[");

        if (bundle == null) {
            out.append("null");
        } else {
            boolean first = true;
            for (String key : bundle.keySet()) {
                if (!first) {
                    out.append(", ");
                }

                out.append(key).append('=');

                Object value = bundle.get(key);

                if (value instanceof int[]) {
                    out.append(Arrays.toString((int[]) value));
                } else if (value instanceof byte[]) {
                    out.append(Arrays.toString((byte[]) value));
                } else if (value instanceof boolean[]) {
                    out.append(Arrays.toString((boolean[]) value));
                } else if (value instanceof short[]) {
                    out.append(Arrays.toString((short[]) value));
                } else if (value instanceof long[]) {
                    out.append(Arrays.toString((long[]) value));
                } else if (value instanceof float[]) {
                    out.append(Arrays.toString((float[]) value));
                } else if (value instanceof double[]) {
                    out.append(Arrays.toString((double[]) value));
                } else if (value instanceof String[]) {
                    out.append(Arrays.toString((String[]) value));
                } else if (value instanceof CharSequence[]) {
                    out.append(Arrays.toString((CharSequence[]) value));
                } else if (value instanceof Parcelable[]) {
                    out.append(Arrays.toString((Parcelable[]) value));
                } else if (value instanceof Bundle) {
                    out.append(bundleToString((Bundle) value));
                } else {
                    out.append(value);
                }

                first = false;
            }
        }

        out.append("]");
        return out.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_cam);

//        initUI();
        appPackageName = getApplicationContext().getPackageName();
        getIntentData();
    }

    public void getIntentData() {
        try {
            Intent intent = getIntent();
            String type = (intent.getExtras().getString(AppStrings.uploadPic.type_from));
            if (type.equals(AppStrings.uploadPic.camera)) {
                dispatchTakePictureIntent();
            } else if (type.equals(AppStrings.uploadPic.gallery)) {
                getImageFromGallery();
            }else if (type.equals(AppStrings.uploadPic.camera_video)) {
                captureVideo();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void captureVideo() {



        Intent captureintent = new Intent(android.provider.MediaStore.ACTION_VIDEO_CAPTURE);
//        captureintent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 15);
        // set the video image quality to high
        captureintent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
//        captureintent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 5242880L);
        startActivityForResult(captureintent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);

    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private File createImageFile() throws IOException {

        File storageDir = createCameraAlbumDirectory();
        Log.e(TAG, "createImageFile: " + storageDir);
        if (storageDir.mkdir()) {
            System.out.println("Directory created");
        } else {
            System.out.println("Directory is not created or exists");
        }

        storagePicFile = new File(storageDir, "/" + getString(R.string.app_name)
                + String.valueOf(System.currentTimeMillis()) + ".jpg");

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = storagePicFile.getAbsolutePath();
        return storagePicFile;
    }


    // image Rotation

    private void dispatchTakePictureIntent() {


        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (Exception ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(PicSelectActivity.this,
//                        BuildConfig.APPLICATION_ID + ".provider",
                        appPackageName + ".provider",
                        photoFile);


                AppLog.LOGV("BuildConfig--->"+ BuildConfig.APPLICATION_ID);
                AppLog.LOGV("BuildConfig--->"+ appPackageName);

//                output1 = FileProvider.getUriForFile(CameraPickerActivity.this, BuildConfig.APPLICATION_ID + ".provider",
//                        file);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);

                List<ResolveInfo> resolvedIntentActivities = getPackageManager().queryIntentActivities(takePictureIntent, PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo resolvedIntentInfo : resolvedIntentActivities) {
                    String packageName = resolvedIntentInfo.activityInfo.packageName;

                    grantUriPermission(packageName, photoURI, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
            }
        }




    }

    public void getImageFromGallery() {
        Log.i("getImageFromGallery", "Gallery");
        final Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        startActivityForResult(intent, REQUEST_GALLERY_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(TAG, "onActivityResult() called with: requestCode = [" + requestCode + "], resultCode = [" + resultCode + "], data = [" + data + "]");
        if (resultCode == Activity.RESULT_CANCELED) {
            Log.e("pic cancel", "Selecting picture cancelled");
            finish();
        } else if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            cam_image_iv.setImageBitmap(imageBitmap);

//            Log.d(TAG, "onActivityResult: "+extras.toString());

            try {
                intentToString(data);
                Log.d(TAG, "onActivityResult: file path : " + storagePicFile.getAbsolutePath());

                final Bitmap b = getImage1(storagePicFile.getAbsolutePath());

//                final long time = System.currentTimeMillis();

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

                final String time = timeStamp;

                // final Bitmap bit = getResizedBitmap(b, 900, 1600);


                String savedImagePath = saveBitmapIntoSdcard(b, time + ".png");

                updatePhotoItem(savedImagePath);

//                cam_image_iv.setImageURI(Uri.parse(storagePicFile.getAbsolutePath()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (requestCode == REQUEST_GALLERY_PHOTO) {

            try {


                Uri selectedImageUri = data.getData();

                Log.d(TAG, "selectedImageUri: " + selectedImageUri.getPath());
                Log.d(TAG, "getRealPathFromURI: " + getRealPathFromURI(selectedImageUri));

                final Bitmap b = getImage1(getRealPathFromURI(selectedImageUri));

                final long time = System.currentTimeMillis();

                // final Bitmap bit = getResizedBitmap(b, 900, 1600);


                String savedImagePath = saveBitmapIntoSdcard(b, time + ".png");

                updatePhotoItem(savedImagePath);


            } catch (final Exception e) {

                return;
            }

        }else  if(requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE ) {
            Uri selectedVideo_uri = null;
            String finalselected_realpath = null;
            try {
                selectedVideo_uri = data.getData();

                finalselected_realpath = getRealPathFromURI(PicSelectActivity.this, selectedVideo_uri);

                Log.e(TAG, "onActivityResult: finalselected_realpath :" + finalselected_realpath);
//                Log.e(TAG, "onActivityResult: finalselected_thumbnail :" + thumbnail_path);

            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                updateVideoItem(finalselected_realpath, selectedVideo_uri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static String getThumbnailPathForLocalFile(Activity context,
                                                      Uri fileUri) {

        long fileId = getFileId(context, fileUri);

        MediaStore.Video.Thumbnails.getThumbnail(context.getContentResolver(),
                fileId, MediaStore.Video.Thumbnails.MICRO_KIND, null);

        Cursor thumbCursor = null;
        try {

            thumbCursor = context.managedQuery(
                    MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI,
                    thumbColumns, MediaStore.Video.Thumbnails.VIDEO_ID + " = "
                            + fileId, null, null);

            if (thumbCursor.moveToFirst()) {
                String thumbPath = thumbCursor.getString(thumbCursor
                        .getColumnIndex(MediaStore.Video.Thumbnails.DATA));

                return thumbPath;
            }

        } finally {
        }

        return null;
    }

    public static long getFileId(Activity context, Uri fileUri) {

        Cursor cursor = context.managedQuery(fileUri, mediaColumns, null, null,
                null);

        if (cursor.moveToFirst()) {
            int columnIndex = cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media._ID);
            int id = cursor.getInt(columnIndex);

            return id;
        }

        return 0;
    }

    public static String getRealPathFromURI(Activity activity, Uri contentUri) {
        int column_index = 0;
        Cursor cursor = null;

        try {
            String[] proj = {MediaStore.Audio.Media.DATA};
            cursor = activity.managedQuery(contentUri, proj, null, null, null);

            column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cursor.getString(column_index);
    }

    public Bitmap getImage1(String path) throws IOException {
        Bitmap pqr = null;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);
            int srcWidth = options.outWidth;
            int srcHeight = options.outHeight;
            final int REQUIRED_SIZE = 380;

            int inSampleSize = 1;
            while (srcWidth / 2 >= REQUIRED_SIZE) {
                srcWidth /= 2;
                srcHeight /= 2;
                inSampleSize *= 2;
            }

            options.inJustDecodeBounds = false;
            options.inDither = false;
            options.inSampleSize = inSampleSize;
            options.inScaled = false;
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap sampledSrcBitmap = BitmapFactory.decodeFile(path, options);
            ExifInterface exif = new ExifInterface(path);
            String s = exif.getAttribute(ExifInterface.TAG_ORIENTATION);

            Matrix matrix = new Matrix();
            float rotation = rotationForImage(PicSelectActivity.this, Uri.fromFile(new File(path)));
            if (rotation != 0f) {
                matrix.preRotate(rotation);
            }

            pqr = Bitmap.createBitmap(sampledSrcBitmap, 0, 0, sampledSrcBitmap.getWidth(), sampledSrcBitmap.getHeight(), matrix, true);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }

        return pqr;
    }

  /*  protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("file", pathin);

    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        pathin = savedInstanceState.getString("file");
        //  Log.d("enregistred value", savedUser);

    }*/

    public float rotationForImage(Context context, Uri uri) {
        try {
            if (uri.getScheme().equals("content")) {
                String[] projection = {MediaStore.Images.ImageColumns.ORIENTATION};
                Cursor c = context.getContentResolver().query(uri, projection,
                        null, null, null);
                if (c.moveToFirst()) {
                    return c.getInt(0);
                }
            } else if (uri.getScheme().equals("file")) {
                try {
                    ExifInterface exif = new ExifInterface(uri.getPath());
                    int rotation = (int) exifOrientationToDegrees(exif
                            .getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                    ExifInterface.ORIENTATION_NORMAL));
                    return rotation;
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return 0f;
    }

    public String getRealPathFromURI(Uri contentUri) {
        int column_index = 0;
        Cursor cursor = null;

        try {
            String[] proj = {MediaStore.Audio.Media.DATA};
            cursor = managedQuery(contentUri, proj, null, null, null);
            // Cursor cursor = getContentResolver().query(contentUri, proj,
            // null, null, null); //Since manageQuery is deprecated
            column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cursor.getString(column_index);
    }

    public File createBaseDirectory() {
        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        File storageDir = new File(extStorageDirectory + "/" + getString(R.string.app_name));
        if (storageDir.mkdir()) {
            System.out.println("Base Directory created");
        } else {
            System.out.println("Base Directory is not created or exists");
        }

        return storageDir;
    }

    public File createGalleryAlbumDirectory() {
        String extStorageDirectory = createBaseDirectory().getAbsolutePath();
        File storageDir = new File(extStorageDirectory + "/" + gallery_album);
        if (storageDir.mkdir()) {
            System.out.println("Gallery Directory created");
        } else {
            System.out.println("Gallery Directory is not created or exists");
        }

        return storageDir;
    }

    public File createCameraAlbumDirectory() {
        String extStorageDirectory = createBaseDirectory().getAbsolutePath();
        File storageDir = new File(extStorageDirectory + "/" + camera_album);
        if (storageDir.mkdir()) {
            System.out.println("Camera Directory created");
        } else {
            System.out.println("Camera Directory is not created or exists");
        }

        return storageDir;
    }

    private String saveBitmapIntoSdcard(Bitmap bitmap, String filename) throws IOException {
        /*
         * check the path and create if needed
		 */
        File baseDirectory = createGalleryAlbumDirectory();

        try {

            new Date();

            OutputStream out = null;
            File file = new File(baseDirectory, "/" + getString(R.string.app_name) + filename);

            out = new FileOutputStream(file);

            bitmap.compress(Bitmap.CompressFormat.PNG, 70, out);

            out.flush();
            out.close();
            // Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
            return file.getAbsolutePath();
        } catch (final Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void updateProfilepic(String pic_name) {
        Intent intent = new Intent();
        intent.putExtra("pic_name", pic_name);
        setResult(1, intent);
        finish();
        overridePendingTransition(0, 0);

    }

    public void updatePhotoItem(final String path) {
        Intent intent = new Intent();
        intent.putExtra(AppStrings.uploadPic.selected_image_path, path);
        setResult(1, intent);
        finish();
        overridePendingTransition(0, 0);
        Log.e(TAG, "updatePhotoItem: ");
    }

    public void updateVideoItem(final String path, Uri selectedVideo_uri) {
        String thumbnail_path = getThumbnailPathForLocalFile(PicSelectActivity.this, selectedVideo_uri);
        Intent intent = new Intent();
        intent.putExtra(AppStrings.uploadPic.selected_video_thumbnail_path, thumbnail_path);
        intent.putExtra(AppStrings.uploadPic.selected_video_path, path);
        setResult(1, intent);
        finish();
        overridePendingTransition(0, 0);
        Log.e(TAG, "updateVideoItem:thumbnail_path---> "+thumbnail_path);
        Log.e(TAG, "updateVideoItem:path---> "+path);
    }


    public void shareViaIntent() {
        String fileName = "image-3116.jpg";//Name of an image
        String externalStorageDirectory = Environment.getExternalStorageDirectory().toString();
        String myDir = externalStorageDirectory + "/saved_images/"; // the file will be in saved_images
        Uri uri = Uri.parse("file:///" + myDir + fileName);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/html");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Test Mail");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Launcher");
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(shareIntent, "Share Deal"));
    }

    public void contentFileUri(Uri fileUri){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> resolvedIntentActivities = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolvedIntentInfo : resolvedIntentActivities) {
            String packageName = resolvedIntentInfo.activityInfo.packageName;

            grantUriPermission(packageName, fileUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
    }
}

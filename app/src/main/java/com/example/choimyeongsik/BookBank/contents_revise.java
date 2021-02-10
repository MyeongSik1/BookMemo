package com.example.choimyeongsik.BookBank;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class contents_revise extends AppCompatActivity {
    EditText editText;
    Button revise_button;
    Button camera;
    Button gild;
    Button del;
    private Boolean isCamera = false;
    final static int IMAGE_PICK = 300;
    final static int IMAGE_CAMEAR = 400;
    private File tempFile;
    private Uri photoUri;
    private Bitmap originalBm;
    ImageView imageView1;
    private Boolean isImage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contents_revise);
        getWindow().setStatusBarColor(Color.parseColor("#D8D8D8"));
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_revise);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);





        Intent intent = getIntent();
        final String number = intent.getExtras().getString("number");
        editText = (EditText) findViewById(R.id.revise_contents);
        revise_button = (Button) findViewById(R.id.revise_button);
        gild = (Button) findViewById(R.id.gild_revise);
        camera = (Button) findViewById(R.id.camear_revise);
        imageView1 = (ImageView) findViewById(R.id.revise_imageview1);
        del = (Button)findViewById(R.id.del_revise);
        BookDatabase bookDatabase = new BookDatabase(getApplication());
        SQLiteDatabase db = bookDatabase.getWritableDatabase();
        Cursor cs = db.rawQuery("select * from  tb_book where  _id =  '" + number + "' ", null);
        cs.moveToNext();
        Log.i("확인", number);
        String title = cs.getString(8);
        String content = cs.getString(11);
        byte[] image = cs.getBlob(9);
        editText.setText(content);
        if (image != null) {        // 해당 _id에 이미지가 있으면
            imageView1.setVisibility(View.VISIBLE);
            Bitmap bmp = BitmapFactory.decodeByteArray(image, 0, image.length);
            Log.d("레코드어댑터", String.valueOf(bmp));
            Glide.with(imageView1.getContext()).load(bmp).override(300, 300).into(imageView1);
        }
        revise_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookDatabase bookDatabase = new BookDatabase(getApplication());
                SQLiteDatabase db = bookDatabase.getWritableDatabase();
                String revise_content = editText.getText().toString().trim();
                ContentValues cv = new ContentValues();
                if(isImage) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    originalBm.compress(Bitmap.CompressFormat.PNG, 100, out);
                    cv.put("image", out.toByteArray());
                    db.execSQL("UPDATE tb_book SET contents = '" + revise_content + "' where _id = '" + number + "'");
                    db.update("tb_book", cv, "_id =" + number, null);
                } else {
                    db.execSQL("UPDATE tb_book SET contents = '" + revise_content + "' where _id = '" + number + "'");
                }
                finish();
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isImage = true;
                takePhoto();
            }
        });

        gild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isImage = true;
                isCamera = false;
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, IMAGE_PICK);

            }
        });
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookDatabase bookDatabase = new BookDatabase(getApplicationContext());
                SQLiteDatabase db = bookDatabase.getWritableDatabase();
                Cursor cs = db.rawQuery("select * from  tb_book where  _id =  '" + number + "' ", null);
                cs.moveToNext();
                String content = cs.getString(11);
                byte[] image = cs.getBlob(9);
                db.execSQL("DELETE FROM tb_book WHERE contents = '" + content + "';");
                db.execSQL("DELETE FROM tb_book WHERE image = '" + image + "';");


                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            isImage = false;
            if (tempFile != null) {
                if (tempFile.exists()) {
                    if (tempFile.delete()) {
                        Log.e("GD", tempFile.getAbsolutePath() + " 삭제 성공");
                        tempFile = null;
                    }
                }
            }

            return;
        }

        if (requestCode == IMAGE_PICK) {

            photoUri = data.getData();

            Cursor cursor = null;

            try {

                /*
                 *  Uri 스키마를
                 *  content:/// 에서 file:/// 로  변경한다.
                 */
                String[] proj = {MediaStore.Images.Media.DATA};

                assert photoUri != null;
                cursor = getContentResolver().query(photoUri, proj, null, null, null);

                assert cursor != null;
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                cursor.moveToFirst();

                tempFile = new File(cursor.getString(column_index));

            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }

            setImage();

        } else if (requestCode == IMAGE_CAMEAR) {
            setImage();
        }
    }

    private void setImage() {
        ImageResizeUtils.resizeFile(tempFile, tempFile, 1280, isCamera);
        BitmapFactory.Options options = new BitmapFactory.Options();
        originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);
        Log.d("GD", "setImage : " + tempFile.getAbsolutePath());
        imageView1.setImageBitmap(originalBm);


    }


    private void takePhoto() {
        isCamera = true;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try {
            tempFile = createImageFile();
        } catch (IOException e) {
            Toast.makeText(this, "이미지 처리 오류! 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
            finish();
            e.printStackTrace();
        }
        if (tempFile != null) {

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {

                photoUri = FileProvider.getUriForFile(this,
                        "com.example.choimyeongsik.myapplication.provider", tempFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intent, IMAGE_CAMEAR);

            } else {

                photoUri = Uri.fromFile(tempFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intent, IMAGE_CAMEAR);

            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
        String imageFileName = "Largo_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/Largo/");
        if (!storageDir.exists()) storageDir.mkdirs();
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        return image;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

}
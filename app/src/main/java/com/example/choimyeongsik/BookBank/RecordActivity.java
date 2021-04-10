package com.example.choimyeongsik.BookBank;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.choimyeongsik.BookBank.DB.BookDatabase;
import com.example.choimyeongsik.BookBank.Utils.ImageResizeUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
//
public class RecordActivity extends AppCompatActivity {
    EditText editText;
    Button write_button;
    Button camera;
    Button gild;


    private Boolean isCamera = false;
    final static int IMAGE_PICK=300;
    final static int IMAGE_CAMEAR=400;
    private File tempFile;
    private Uri photoUri;
    private Bitmap originalBm;
    ImageView imageView1;
    private  Boolean isImage = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contents_record);
        getWindow().setStatusBarColor(Color.parseColor("#D8D8D8"));
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_content);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        final String title_ = intent.getExtras().getString("title");
        editText = (EditText)findViewById(R.id.record_contents);
        write_button = (Button)findViewById(R.id.contents_button);
        gild = (Button)findViewById(R.id.gild);
        camera = (Button)findViewById(R.id.camear);
        imageView1 = (ImageView)findViewById(R.id.imageview1);

        write_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String contents1 = editText.getText().toString().trim();
                BookDatabase bookDatabase = new BookDatabase(getApplication());
                SQLiteDatabase db = bookDatabase.getWritableDatabase();
                ContentValues cv = new ContentValues();
                if (isImage) {         // 이미지 저장할땐 ContenValues 사용
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    originalBm.compress(Bitmap.CompressFormat.PNG, 100, out);
                    cv.put("image", out.toByteArray());
                    cv.put("name", title_);
                    cv.put("contents", contents1);
                    db.insert("tb_book", null, cv);
                    db.close();
                    finish();
                } else {
                    cv.put("name", title_);
                    cv.put("contents", contents1);
                    db.insert("tb_book", null, cv);
                    db.close();
                    finish();
                }
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



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            // 카메라 or 갤러리에서 취소하였을때
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



        imageView1.setVisibility(View.VISIBLE);
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

        // 이미지 파일 이름
        String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
        String imageFileName = "Largo_" + timeStamp + "_";

        // 이미지가 저장될 폴더 이름
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/Largo/");
        if (!storageDir.exists()) storageDir.mkdirs();

        // 빈 파일 생성
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        return image;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

}

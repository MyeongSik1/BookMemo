package com.example.choimyeongsik.BookBank;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.example.choimyeongsik.BookBank.Fragment.FragmentGallery;
import com.example.choimyeongsik.BookBank.Fragment.FragmentRecord;
import com.example.choimyeongsik.BookBank.Fragment.Fragmentlibrary;
import com.example.choimyeongsik.BookBank.Utils.BottomNavigationHelper;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import static com.kakao.util.helper.Utility.getPackageInfo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
//
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "123";
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private FragmentHome fragmentHome = new FragmentHome();
    private FragmentRecord fragmentRecord = new FragmentRecord();
    private FragmentGallery fragmentGallery = new FragmentGallery();
    private Fragmentlibrary fragmentlibrary = new Fragmentlibrary();
    private Fragment fa, fb, fc, fd;
    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;
    private AdView mAdView;
    private BannerDialog bannerDialog;


    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setStatusBarColor(Color.parseColor("#D8D8D8"));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
        fa = new FragmentHome();
        fragmentManager.beginTransaction().replace(R.id.frameLayout, fa).commit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener((BottomNavigationView.OnNavigationItemSelectedListener) new ItemSelectedListener());
        BottomNavigationHelper.disableShiftMode(bottomNavigationView);


        View.OnClickListener bannerListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdView.setAdListener(new AdListener() {
                    @Override
                    public void onAdLoaded() {
                        // Code to be executed when an ad finishes loading.
                        // 광고가 문제 없이 로드시 출력됩니다.
                        Log.d("@@@", "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                        // Code to be executed when an ad request fails.
                        // 광고 로드에 문제가 있을시 출력됩니다.
                        Log.d("@@@", "onAdFailedToLoad " + errorCode);
                    }

                    @Override
                    public void onAdOpened() {
                        // Code to be executed when an ad opens an overlay that
                        // covers the screen.
                    }

                    @Override
                    public void onAdClicked() {
                        // Code to be executed when the user clicks on an ad.
                    }

                    @Override
                    public void onAdLeftApplication() {
                        // Code to be executed when the user has left the app.
                    }

                    @Override
                    public void onAdClosed() {

                        // Code to be executed when the user is about to return

                        // to the app after tapping on an ad.

                    }

                });

            }
        };
        View.OnClickListener positiveListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bannerDialog.dismiss();
                finish();
            }
        };
        View.OnClickListener negativeListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bannerDialog.dismiss();
            }
        };
        bannerDialog = new BannerDialog(this, positiveListener, negativeListener, bannerListener);
        bannerDialog.setCancelable(false);
        bannerDialog.setCanceledOnTouchOutside(false);


    }


    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch (menuItem.getItemId()) {
                case R.id.navigaticon_menu:
                    if (fa == null) {
                        fa = new FragmentHome();
                        fragmentManager.beginTransaction().add(R.id.frameLayout, fa).commit();

                    }
                    if (fa != null) fragmentManager.beginTransaction().show(fa).commit();
                    if (fb != null) fragmentManager.beginTransaction().hide(fb).commit();
                    if (fc != null) fragmentManager.beginTransaction().hide(fc).commit();
                    if (fd != null) fragmentManager.beginTransaction().hide(fd).commit();
                    fd = null;
                    break;


                case R.id.navigaticon_menu2:
                    if (fb == null) {
                        fb = new FragmentRecord();
                        fragmentManager.beginTransaction().add(R.id.frameLayout, fb).commit();

                    }
                    if (fa != null) fragmentManager.beginTransaction().hide(fa).commit();
                    if (fb != null) fragmentManager.beginTransaction().show(fb).commit();
                    if (fc != null) fragmentManager.beginTransaction().hide(fc).commit();
                    if (fd != null) fragmentManager.beginTransaction().hide(fd).commit();
                    fd = null;
                    break;


                case R.id.navigaticon_menu3:
                    if (fc == null) {
                        fc = new FragmentGallery();
                        fragmentManager.beginTransaction().add(R.id.frameLayout, fc).commit();
                    }
                    if (fa != null) fragmentManager.beginTransaction().hide(fa).commit();
                    if (fb != null) fragmentManager.beginTransaction().hide(fb).commit();
                    if (fc != null) fragmentManager.beginTransaction().show(fc).commit();
                    if (fd != null) fragmentManager.beginTransaction().hide(fd).commit();
                    fd = null;
                    break;

                case R.id.navigaticon_menu4:
                    if (fd == null) {
                        fd = new Fragmentlibrary();
                        fragmentManager.beginTransaction().add(R.id.frameLayout, fd).commit();
                    }
                    if (fa != null) fragmentManager.beginTransaction().hide(fa).commit();
                    if (fb != null) fragmentManager.beginTransaction().hide(fb).commit();
                    if (fc != null) fragmentManager.beginTransaction().hide(fc).commit();
                    if (fd != null) fragmentManager.beginTransaction().show(fd).commit();

                    break;

            }
            return true;
        }
    }


    @Override

    public void onBackPressed() {
        bannerDialog.show();
    }


    public class BannerDialog extends Dialog {

        private Button mPositiveButton;
        private Button mNegativeButton;
        private AdView mAdView;

        private View.OnClickListener mPositiveListener;
        private View.OnClickListener mNegativeListener;
        private View.OnClickListener mBannerListener;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);


            //다이얼로그 밖의 화면은 흐리게 만들어줌
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            layoutParams.dimAmount = 0.8f;
            getWindow().setAttributes(layoutParams);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
            getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            setContentView(R.layout.bannerdialog);

            //셋팅
            mPositiveButton = (Button) findViewById(R.id.btn_ok);
            mNegativeButton = (Button) findViewById(R.id.btn_no);
            mAdView = findViewById(R.id.adView);
            MobileAds.initialize(getApplicationContext(), getString(R.string.admob_app_id));
            mAdView = findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);

            //클릭 리스너 셋팅 (클릭버튼이 동작하도록 만들어줌.)
            mPositiveButton.setOnClickListener(mPositiveListener);
            mNegativeButton.setOnClickListener(mNegativeListener);
            mAdView.setOnClickListener(mBannerListener);
        }

        //생성자 생성
        public BannerDialog(@NonNull Context context,
                            View.OnClickListener positiveListener,
                            View.OnClickListener negativeListener,
                            View.OnClickListener bannerListener

        ) {
            super(context);
            this.mPositiveListener = positiveListener;
            this.mNegativeListener = negativeListener;
            this.mBannerListener = bannerListener;
        }
    }


}
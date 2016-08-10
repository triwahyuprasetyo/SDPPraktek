package com.example.why.sdppraktek;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.why.sdppraktek.retrofit.KtpService;
import com.example.why.sdppraktek.retrofit.KtpWrapper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.utils.L;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TEST_FILE_NAME = "Universal Image Loader @#&=+-_.,!()~'%20.png";
    public static final int INDEX = 1;
    GridView grid;

    private Button buttonNewPost, buttonLogin;

    static String[] daftarUrl;
    static String[] daftarTitle;
    static MainActivity ma;

    public static void startIntentPrev(int pos) {
        Intent i = new Intent(MainActivity.ma.getApplicationContext(), PreviewActivity.class);
        i.putExtra("posisi", pos);
        MainActivity.ma.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ma = this;

        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(this);
        buttonNewPost = (Button) findViewById(R.id.buttonCreate);
        buttonNewPost.setOnClickListener(this);

//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://soesapto.hantoro.web.id/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        KtpService service = retrofit.create(KtpService.class);
//
//
//        Call<KtpWrapper> call = service.listKtps();
//        call.enqueue(new Callback<KtpWrapper>() {
//            @Override
//            public void onResponse(Call<KtpWrapper> call, Response<KtpWrapper> response) {
//                Log.i("MainActivity", "onResponse");
//                daftarUrl = new String[response.body().getKtp().size()];
//                daftarTitle = new String[response.body().getKtp().size()];
//                int i = 0;
//                for (KtpWrapper.Ktp k : response.body().getKtp()) {
//                    Log.d("SDP", "KTP :: " + k.getId() + ", " + k.getTitle() + ", " + k.getUrl());
//                    daftarUrl[i] = k.getUrl();
//                    daftarTitle[i] = k.getTitle();
//                    i++;
//                }
//                File testImageOnSdCard = new File("/mnt/sdcard", TEST_FILE_NAME);
//                if (!testImageOnSdCard.exists()) {
//                    copyTestImageToSdCard(testImageOnSdCard);
//                }
//                grid = (GridView) findViewById(R.id.grid);
//                grid.setAdapter(new ImageAdapter(ma));
//            }
//
//            @Override
//            public void onFailure(Call<KtpWrapper> call, Throwable t) {
//                Log.i("MainActivity", "onFailure");
//            }
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://soesapto.hantoro.web.id/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        KtpService service = retrofit.create(KtpService.class);


        Call<KtpWrapper> call = service.listKtps();
        call.enqueue(new Callback<KtpWrapper>() {
            @Override
            public void onResponse(Call<KtpWrapper> call, Response<KtpWrapper> response) {
                Log.i("MainActivity", "onResponse");
                daftarUrl = new String[response.body().getKtp().size()];
                daftarTitle = new String[response.body().getKtp().size()];
                int i = 0;
                for (KtpWrapper.Ktp k : response.body().getKtp()) {
                    Log.d("SDP", "KTP :: " + k.getId() + ", " + k.getTitle() + ", " + k.getUrl());
                    daftarUrl[i] = k.getUrl();
                    daftarTitle[i] = k.getTitle();
                    i++;
                }
                File testImageOnSdCard = new File("/mnt/sdcard", TEST_FILE_NAME);
                if (!testImageOnSdCard.exists()) {
                    copyTestImageToSdCard(testImageOnSdCard);
                }
                grid = (GridView) findViewById(R.id.grid);
                grid.setAdapter(new ImageAdapter(ma));
            }

            @Override
            public void onFailure(Call<KtpWrapper> call, Throwable t) {
                Log.i("MainActivity", "onFailure");
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == buttonLogin.getId()) {

        } else if (v.getId() == buttonNewPost.getId()) {
            Intent i = new Intent(MainActivity.this, NewPostActivity.class);
            startActivity(i);
        }
    }

    private void copyTestImageToSdCard(final File testImageOnSdCard) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream is = getAssets().open(TEST_FILE_NAME);
                    FileOutputStream fos = new FileOutputStream(testImageOnSdCard);
                    byte[] buffer = new byte[8192];
                    int read;
                    try {
                        while ((read = is.read(buffer)) != -1) {
                            fos.write(buffer, 0, read);
                        }
                    } finally {
                        fos.flush();
                        fos.close();
                        is.close();
                    }
                } catch (IOException e) {
                    L.w("Can't copy test image onto SD card");
                }
            }
        }).start();
    }


    private static class ImageAdapter extends BaseAdapter {

//        private static final String[] IMAGE_URLS = Constants.IMAGES;

        private static final String[] IMAGE_URLS = MainActivity.daftarUrl;

        private LayoutInflater inflater;

        private DisplayImageOptions options;

        ImageAdapter(Context context) {
            inflater = LayoutInflater.from(context);

            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.mipmap.ic_launcher)
                    .showImageForEmptyUri(R.mipmap.ic_launcher)
                    .showImageOnFail(R.mipmap.ic_launcher)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
        }

        @Override
        public int getCount() {
            return IMAGE_URLS.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            View view = convertView;
            if (view == null) {
                view = inflater.inflate(R.layout.item_grid_image, parent, false);
                holder = new ViewHolder();
                assert view != null;
                holder.imageView = (ImageView) view.findViewById(R.id.image);
                holder.progressBar = (ProgressBar) view.findViewById(R.id.progress);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

//            ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(getBaseCont‌​ext()));

            ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(view.getContext());
            config.threadPriority(Thread.NORM_PRIORITY - 2);
            config.denyCacheImageMultipleSizesInMemory();
            config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
            config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
            config.tasksProcessingOrder(QueueProcessingType.LIFO);
            config.writeDebugLogs(); // Remove for release app

            // Initialize ImageLoader with configuration.
            ImageLoader.getInstance().init(config.build());

            ImageLoader.getInstance()
                    .displayImage(IMAGE_URLS[position], holder.imageView, options, new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                            holder.progressBar.setProgress(0);
                            holder.progressBar.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                            holder.progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            holder.progressBar.setVisibility(View.GONE);
                        }
                    }, new ImageLoadingProgressListener() {
                        @Override
                        public void onProgressUpdate(String imageUri, View view, int current, int total) {
                            holder.progressBar.setProgress(Math.round(100.0f * current / total));
                        }
                    });
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("Pos", "" + position);

                    MainActivity.startIntentPrev(position);
                }
            });

            return view;
        }
    }

    static class ViewHolder {
        ImageView imageView;
        ProgressBar progressBar;
    }
}




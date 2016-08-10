package com.example.why.sdppraktek;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.why.sdppraktek.retrofit.KtpService;
import com.example.why.sdppraktek.retrofit.KtpWrapper;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewPostActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etTitle;
    private Button btSend;
    private ImageView imgVIewTap;

    private final int PICKFILE_RESULT_CODE = 42;
    private String mCurrentPhotoPath = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        etTitle = (EditText) findViewById(R.id.editTextTitle);
        btSend = (Button) findViewById(R.id.buttonSend);
        btSend.setOnClickListener(this);
        imgVIewTap = (ImageView) findViewById(R.id.imageViewTap);
        imgVIewTap.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == btSend.getId()) {
            if (mCurrentPhotoPath.equals("")) {

                Toast.makeText(getApplicationContext(), "Foto kosong", Toast.LENGTH_SHORT).show();

            } else {
                if (etTitle.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Title kosong", Toast.LENGTH_SHORT).show();
                } else {
                    uploadFoto(mCurrentPhotoPath, etTitle.getText().toString().trim());
                }
            }

        } else if (v.getId() == imgVIewTap.getId()) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICKFILE_RESULT_CODE);
        }
    }

    private void uploadFoto(String uri, String title) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://soesapto.hantoro.web.id/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        KtpService service = retrofit.create(KtpService.class);
        File file = new File(uri);
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("photo", file.getName(), requestFile);
        String descriptionString = title;

        RequestBody description =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), descriptionString);

        Call<ResponseBody> call = service.uploadFoto2(description, body);
        call.enqueue(new Callback<ResponseBody>() {
                         @Override
                         public void onResponse(Call<ResponseBody> call,
                                                Response<ResponseBody> response) {
                             Log.v("Upload", "success");
                             Toast.makeText(getApplicationContext(), "Upload success", Toast.LENGTH_SHORT).show();
                             Log.v("Upload", "" + response.message());
                             Log.v("Upload", response.code() + "");
                         }

                         @Override
                         public void onFailure(Call<ResponseBody> call, Throwable t) {
                             Log.e("Upload error:", t.getMessage());
                             Toast.makeText(getApplicationContext(), "Upload error", Toast.LENGTH_SHORT).show();
//
                         }
                     }
        );

//        Call<KtpWrapper> call = service.uploadFoto(description, body);
//
//        call.enqueue(new Callback<KtpWrapper>() {
//                         @Override
//                         public void onResponse(Call<KtpWrapper> call,
//                                                Response<KtpWrapper> response) {
//                             Log.v("Upload", "success");
//                             Toast.makeText(getApplicationContext(), "Upload success", Toast.LENGTH_SHORT).show();
//                             Log.v("Upload", "" + response.message());
//                             Log.v("Upload", response.code() + "");
//                             List<KtpWrapper.Ktp> liskatepe = response.body().getKtp();
//                             Log.v("Upload", response.isSuccessful() + "");
//                             if (liskatepe.size() > 0) {
//
//                                 Log.v("Upload", "ada");
//                             } else {
//
//                                 Log.v("Upload", "tidakada");
//                             }
//                             Log.v("Upload", "Heloo");
//
//                         }
//
//                         @Override
//                         public void onFailure(Call<KtpWrapper> call, Throwable t) {
//                             Log.e("Upload error:", t.getMessage());
//                             Toast.makeText(getApplicationContext(), "Upload error", Toast.LENGTH_SHORT).show();
//                         }
//                     }
//        );

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICKFILE_RESULT_CODE:
                if (resultCode == RESULT_OK) {
                    Uri selectedImageURI = data.getData();
                    File imageFile = new File(getRealPathFromURI(selectedImageURI));
                    String path = imageFile.getPath();

                    if (path.contains("Exception")) {
                        Log.i("SDP UPLOAD", "Gagal Ambil Path");
                    } else {
                        imgVIewTap.setImageURI(selectedImageURI);
                        Log.i("SDP PATH", path);
                        mCurrentPhotoPath = path;
                    }
                }
                break;
        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result = "";
        try {
            Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
            if (cursor == null) { // Source is Dropbox or other similar local file path
                result = contentURI.getPath();
            } else {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                result = cursor.getString(idx);
                cursor.close();
            }
        } catch (IllegalStateException e) {
            result = "Exception " + e.getMessage();
        } catch (RuntimeException e) {
            result = "Exception " + e.getMessage();
        } catch (Exception e) {
            result = "Exception " + e.getMessage();
        }
        return result;
    }
}

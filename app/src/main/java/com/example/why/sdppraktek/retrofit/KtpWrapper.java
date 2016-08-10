package com.example.why.sdppraktek.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by why on 8/1/16.
 */
public class KtpWrapper {

//    @SerializedName(value = "success")
//    public String success;

    @SerializedName(value = "result")
    public List<Ktp> ktp;

//    public String getSuccess() {
//        return success;
//    }
//
//    public void setSuccess(String success) {
//        this.success = success;
//    }

    public List<Ktp> getKtp() {
        return ktp;
    }

    public void setKtp(List<Ktp> ktp) {
        this.ktp = ktp;
    }

    public static class Ktp {
        @Expose
        @SerializedName("id")
        private int id;
        @Expose
        @SerializedName("url")
        private String url;
        @Expose
        @SerializedName("title")
        private String title;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}

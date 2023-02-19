package com.example.appweather;

import java.io.Serializable;

public class DetailHistory implements Serializable {
    private String imgTrangThaiDetailHW;
    private String timeDetailHW, nhietDoDetailHW, trangThaiDetailHW, camGiacNhuDetailHW, tocDoGioDetailHW, huongGioDetailHW, apSuatDetailHW, doAmDetailHW;

    public DetailHistory(String imgTrangThaiDetailHW, String timeDetailHW, String nhietDoDetailHW, String trangThaiDetailHW, String camGiacNhuDetailHW, String tocDoGioDetailHW, String huongGioDetailHW, String apSuatDetailHW, String doAmDetailHW) {
        this.imgTrangThaiDetailHW = imgTrangThaiDetailHW;
        this.timeDetailHW = timeDetailHW;
        this.nhietDoDetailHW = nhietDoDetailHW;
        this.trangThaiDetailHW = trangThaiDetailHW;
        this.camGiacNhuDetailHW = camGiacNhuDetailHW;
        this.tocDoGioDetailHW = tocDoGioDetailHW;
        this.huongGioDetailHW = huongGioDetailHW;
        this.apSuatDetailHW = apSuatDetailHW;
        this.doAmDetailHW = doAmDetailHW;
    }

    public String getImgTrangThaiDetailHW() {
        return imgTrangThaiDetailHW;
    }

    public void setImgTrangThaiDetailHW(String imgTrangThaiDetailHW) {
        this.imgTrangThaiDetailHW = imgTrangThaiDetailHW;
    }

    public String getTimeDetailHW() {
        return timeDetailHW;
    }

    public void setTimeDetailHW(String timeDetailHW) {
        this.timeDetailHW = timeDetailHW;
    }

    public String getNhietDoDetailHW() {
        return nhietDoDetailHW;
    }

    public void setNhietDoDetailHW(String nhietDoDetailHW) {
        this.nhietDoDetailHW = nhietDoDetailHW;
    }

    public String getTrangThaiDetailHW() {
        return trangThaiDetailHW;
    }

    public void setTrangThaiDetailHW(String trangThaiDetailHW) {
        this.trangThaiDetailHW = trangThaiDetailHW;
    }

    public String getCamGiacNhuDetailHW() {
        return camGiacNhuDetailHW;
    }

    public void setCamGiacNhuDetailHW(String camGiacNhuDetailHW) {
        this.camGiacNhuDetailHW = camGiacNhuDetailHW;
    }

    public String getTocDoGioDetailHW() {
        return tocDoGioDetailHW;
    }

    public void setTocDoGioDetailHW(String tocDoGioDetailHW) {
        this.tocDoGioDetailHW = tocDoGioDetailHW;
    }

    public String getHuongGioDetailHW() {
        return huongGioDetailHW;
    }

    public void setHuongGioDetailHW(String huongGioDetailHW) {
        this.huongGioDetailHW = huongGioDetailHW;
    }

    public String getApSuatDetailHW() {
        return apSuatDetailHW;
    }

    public void setApSuatDetailHW(String apSuatDetailHW) {
        this.apSuatDetailHW = apSuatDetailHW;
    }

    public String getDoAmDetailHW() {
        return doAmDetailHW;
    }

    public void setDoAmDetailHW(String doAmDetailHW) {
        this.doAmDetailHW = doAmDetailHW;
    }
}

package com.example.appweather;

import java.io.Serializable;

public class History implements Serializable {
    private String imgTrangThaiHW;
    private String detailDayHW, nhietDoHW, trangThaiHW, camGiacNhuHW, tocDoGioHW, huongGioHW, apSuatHW, doAmHW;

    public History(String imgTrangThaiHW, String detailDayHW, String nhietDoHW, String trangThaiHW, String camGiacNhuHW, String tocDoGioHW, String huongGioHW, String apSuatHW, String doAmHW) {
        this.imgTrangThaiHW = imgTrangThaiHW;
        this.detailDayHW = detailDayHW;
        this.nhietDoHW = nhietDoHW;
        this.trangThaiHW = trangThaiHW;
        this.camGiacNhuHW = camGiacNhuHW;
        this.tocDoGioHW = tocDoGioHW;
        this.huongGioHW = huongGioHW;
        this.apSuatHW = apSuatHW;
        this.doAmHW = doAmHW;
    }

    public String getImgTrangThaiHW() {
        return imgTrangThaiHW;
    }

    public void setImgTrangThaiHW(String imgTrangThaiHW) {
        this.imgTrangThaiHW = imgTrangThaiHW;
    }

    public String getDetailDayHW() {
        return detailDayHW;
    }

    public void setDetailDayHW(String detailDayHW) {
        this.detailDayHW = detailDayHW;
    }

    public String getNhietDoHW() {
        return nhietDoHW;
    }

    public void setNhietDoHW(String nhietDoHW) {
        this.nhietDoHW = nhietDoHW;
    }

    public String getTrangThaiHW() {
        return trangThaiHW;
    }

    public void setTrangThaiHW(String trangThaiHW) {
        this.trangThaiHW = trangThaiHW;
    }

    public String getCamGiacNhuHW() {
        return camGiacNhuHW;
    }

    public void setCamGiacNhuHW(String camGiacNhuHW) {
        this.camGiacNhuHW = camGiacNhuHW;
    }

    public String getTocDoGioHW() {
        return tocDoGioHW;
    }

    public void setTocDoGioHW(String tocDoGioHW) {
        this.tocDoGioHW = tocDoGioHW;
    }

    public String getHuongGioHW() {
        return huongGioHW;
    }

    public void setHuongGioHW(String huongGioHW) {
        this.huongGioHW = huongGioHW;
    }

    public String getApSuatHW() {
        return apSuatHW;
    }

    public void setApSuatHW(String apSuatHW) {
        this.apSuatHW = apSuatHW;
    }

    public String getDoAmHW() {
        return doAmHW;
    }

    public void setDoAmHW(String doAmHW) {
        this.doAmHW = doAmHW;
    }
}

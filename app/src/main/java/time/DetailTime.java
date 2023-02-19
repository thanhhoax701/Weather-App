package time;

public class DetailTime {
    private String imgTrangThaiTime;
    private String detailDay, detailTime, nhietDoTime, trangThaiTime, gioTime, apSuatTime, doAmTime, chiSoUVTime;

    public DetailTime(String imgTrangThaiTime, String detailDay, String detailTime, String nhietDoTime, String trangThaiTime, String gioTime, String apSuatTime, String doAmTime, String chiSoUVTime) {
        this.imgTrangThaiTime = imgTrangThaiTime;
        this.detailDay = detailDay;
        this.detailTime = detailTime;
        this.nhietDoTime = nhietDoTime;
        this.trangThaiTime = trangThaiTime;
        this.gioTime = gioTime;
        this.apSuatTime = apSuatTime;
        this.doAmTime = doAmTime;
        this.chiSoUVTime = chiSoUVTime;
    }

    public String getImgTrangThaiTime() {
        return imgTrangThaiTime;
    }

    public void setImgTrangThaiTime(String imgTrangThaiTime) {
        this.imgTrangThaiTime = imgTrangThaiTime;
    }

    public String getDetailDay() {
        return detailDay;
    }

    public void setDetailDay(String detailDay) {
        this.detailDay = detailDay;
    }

    public String getDetailTime() {
        return detailTime;
    }

    public void setDetailTime(String detailTime) {
        this.detailTime = detailTime;
    }

    public String getNhietDoTime() {
        return nhietDoTime;
    }

    public void setNhietDoTime(String nhietDoTime) {
        this.nhietDoTime = nhietDoTime;
    }

    public String getTrangThaiTime() {
        return trangThaiTime;
    }

    public void setTrangThaiTime(String trangThaiTime) {
        this.trangThaiTime = trangThaiTime;
    }

    public String getGioTime() {
        return gioTime;
    }

    public void setGioTime(String gioTime) {
        this.gioTime = gioTime;
    }

    public String getApSuatTime() {
        return apSuatTime;
    }

    public void setApSuatTime(String apSuatTime) {
        this.apSuatTime = apSuatTime;
    }

    public String getDoAmTime() {
        return doAmTime;
    }

    public void setDoAmTime(String doAmTime) {
        this.doAmTime = doAmTime;
    }

    public String getChiSoUVTime() {
        return chiSoUVTime;
    }

    public void setChiSoUVTime(String chiSoUVTime) {
        this.chiSoUVTime = chiSoUVTime;
    }
}

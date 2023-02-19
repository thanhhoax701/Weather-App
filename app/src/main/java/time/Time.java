package time;

public class Time {
    private String imgIcon;
    private String time, temp, speed;

    public Time (String imgIcon, String time, String temp, String speed) {
        this.imgIcon = imgIcon;
        this.time = time;
        this.temp = temp;
        this.speed = speed;
    }

    public String getImgIcon() {
        return imgIcon;
    }

    public void setImgIcon(String imgIcon) {
        this.imgIcon = imgIcon;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }
}

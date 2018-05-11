package jp.ac.jec.cm0209.fenrir;

/**
 * Created by guest on 2018/05/07.
 */

public class Item {
    private String name;
    private String address;
    private String category;
    private String tel;
    private String openTime;
    private String line;
    private String station;
    private String station_exit;
    private String latitude;
    private String longitude;
    private String imgUrl;
    private String holiday;
    private String hp;
    private String coupon;
    private String pr_short;
    private String pr_long;

    public Item(String name, String address, String category, String tel, String openTime, String line,
                String station, String station_exit, String latitude, String longitude, String imgUrl,
                String holiday, String hp, String coupon, String pr_short, String pr_long) {
        this.name = name;
        this.address = address;
        this.category = category;
        this.tel = tel;
        this.openTime = openTime;
        this.line = line;
        this.station = station;
        this.station_exit = station_exit;
        this.latitude = latitude;
        this.longitude = longitude;
        this.imgUrl = imgUrl;
        this.holiday = holiday;
        this.hp = hp;
        this.coupon = coupon;
        this.pr_short = pr_short;
        this.pr_long = pr_long;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getStation_exit() {
        return station_exit;
    }

    public void setStation_exit(String station_exit) {
        this.station_exit = station_exit;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getHoliday() {
        return holiday;
    }

    public void setHoliday(String holiday) {
        this.holiday = holiday;
    }

    public String getHp() {
        return hp;
    }

    public void setHp(String hp) {
        this.hp = hp;
    }

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

    public String getPr_short() {
        return pr_short;
    }

    public void setPr_short(String pr_short) {
        this.pr_short = pr_short;
    }

    public String getPr_long() {
        return pr_long;
    }

    public void setPr_long(String pr_long) {
        this.pr_long = pr_long;
    }
}

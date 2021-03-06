package jp.ac.jec.cm0209.fenrir;

import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.InputStream;


public class detailInfoActivity extends AppCompatActivity implements OnMapReadyCallback{
    private String imgUrl;
    private String name;
    private String category;
    private String address;
    private String opentime;
    private String holiday;
    private String tel;
    private String line;
    private String station;
    private String station_exit;
    private String access;
    private String hp;
    private String coupon;
    private String latitude;
    private String longitude;
    private String pr_short;
    private String pr_long;
    private String pr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_info);

        ImageView shopImg = findViewById(R.id.shopImg);
        TextView txtName = findViewById(R.id.txtName);
        TextView txtCategory = findViewById(R.id.txtCategory);
        TextView txtAddress = findViewById(R.id.txtAddress);
        TextView txtOpentime = findViewById(R.id.txtOpentime);
        TextView txtHoliday = findViewById(R.id.txtHoliday);
        TextView txtTel = findViewById(R.id.txtTel);
        TextView txtAccess = findViewById(R.id.txtAccess);
        TextView txtHp = findViewById(R.id.txtHp);
        TextView txtCoupon = findViewById(R.id.txtCoupon);
        TextView txtPr = findViewById(R.id.txtPr);

        //Set MapView
        FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment = (MapFragment)fragmentManager.findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        imgUrl = intent.getStringExtra("imgUrl");
        name = intent.getStringExtra("name");
        category = intent.getStringExtra("category");
        address = intent.getStringExtra("address");
        opentime = intent.getStringExtra("opentime");
        holiday = intent.getStringExtra("holiday");
        tel = intent.getStringExtra("tel");
        line = intent.getStringExtra("line");
        station = intent.getStringExtra("station");
        station_exit = intent.getStringExtra("station_exit");
        hp = intent.getStringExtra("hp");
        coupon = intent.getStringExtra("coupon");
        latitude = intent.getStringExtra("latitude");
        longitude = intent.getStringExtra("longitude");
        pr_short = intent.getStringExtra("pr_short");
        pr_long = intent.getStringExtra("pr_long");

//        if (category.equals("{}")){

        if ("{}".equals(category)){
            category = "";
        }
        if ("{}".equals(address)){
            address = "";
        }
        if ("{}".equals(opentime)){
            opentime = "情報なし";
        }
        if ("{}".equals(holiday)){
            holiday = "情報なし";
        }
        if ("{}".equals(tel)){
            tel = "";
        }
        if ("{}".equals(line)){
            line = "";
        }
        if ("{}".equals(station)){
            station = "";
        }
        if ("{}".equals(station_exit)){
            station_exit = "";
        }
        if ("{}".equals(hp)){
            hp = "";
        }
        if ("{}".equals(coupon)){
            coupon = "なし";
        }
        if ("{}".equals(pr_short)){
            pr_short = "";
        }
        if ("{}".equals(pr_long)){
            pr_long = "";
        }

        line = line.replace("（", "(");
        line = line.replace("）", ")");
        station = station.replace("（", "(");
        station = station.replace("）", ")");
        station_exit = station_exit.replace("（", "(");
        station_exit = station_exit.replace("）", ")");
        access = line + station + station_exit;
        access = access.replace("駅","駅 ");
        opentime = opentime.replace("<BR>", "\n");
        holiday = holiday.replace("<BR>", "\n");
        pr_short = pr_short.replace("<BR>", "\n");
        pr_long = pr_long.replace("<BR>", "\n");
        Log.d("TAG", "onCreate: "+ pr_short + pr_long);
        if (pr_short.equals("") && pr_long.equals("")){
            pr = "情報なし\n\n";
        }else {
            pr = pr_short + "\n\n" + pr_long + "\n";
        }

        if (shopImg != null){
            if ("{}".equals(imgUrl)){
                shopImg.setImageResource(R.drawable.noimageicon);
            }else {
                new detailInfoActivity.DownloadImageTask(shopImg).execute(imgUrl);
            }
        }
        if (name != null){
            txtName.setText(name);
        }
        if (category != null){
            txtCategory.setText(category);
        }
        if (address != null){
            txtAddress.setText(address);
        }
        if (opentime != null){
            txtOpentime.setText(opentime);
        }
        if (holiday != null){
            txtHoliday.setText(holiday);
        }
        if (tel != null){
            txtTel.setText(tel);
        }
        if (access != null){
            txtAccess.setText(access);
        }
        if (hp != null){
            txtHp.setText(hp);
        }
        if (coupon != null){
            txtCoupon.setText(coupon);
        }
        if (pr != null){
            txtPr.setText(pr);
        }
    }

    //Set Map
    @Override
    public void onMapReady(GoogleMap map) {
        double dLatitude = Double.parseDouble(latitude);
        double dLongitude = Double.parseDouble(longitude);
        Log.d("TAG", "map: "+ latitude + longitude);
        LatLng shopMap = new LatLng(dLatitude, dLongitude);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(shopMap);
        markerOptions.title(name);
        markerOptions.snippet(category);
        map.addMarker(markerOptions);

        map.moveCamera(CameraUpdateFactory.newLatLng(shopMap));
        map.animateCamera(CameraUpdateFactory.zoomTo(17));
    }

    //Download Shopimage from Url
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView shopImage;

        public DownloadImageTask(ImageView shopImage) {
            this.shopImage = shopImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            Bitmap img = null;
            try {
                InputStream in = new java.net.URL(url).openStream();
                img = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return img;
        }

        protected void onPostExecute(Bitmap result) {
            shopImage.setImageBitmap(result);
        }
    }
}

package jp.ac.jec.cm0209.fenrir;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private FusedLocationProviderClient client;
    private TextView txtLiveAddress;
    private ImageButton btnNoSmoking;
    private ImageButton btnParking;
    private ImageButton btnTakeOut;
    private ImageButton btnWifi;
    private ImageButton btnRange;
    private ListView listView;
    private ImageButton btnSearch;
    private ItemAdapter adapter;
    private Geocoder geocoder;
    private double latitude = 0;
    private double longitude = 0;
    private String line;
    private String station;
    private String station_exit;
    private int range = 1;
    private int noSmoking = 0;
    private int takeout = 0;
    private int parking = 0;
    private int wifi = 0;
    private int hitPerPage = 10;
    private Item item;
    private List<android.location.Address> addresses;
    AppCompatDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtLiveAddress = findViewById(R.id.txtLiveAddress);

        btnNoSmoking = findViewById(R.id.btnNoSmoking);
        btnNoSmoking.setOnClickListener(this);
        btnParking = findViewById(R.id.btnParking);
        btnParking.setOnClickListener(this);
        btnTakeOut = findViewById(R.id.btnTakeOut);
        btnTakeOut.setOnClickListener(this);
        btnWifi = findViewById(R.id.btnWifi);
        btnWifi.setOnClickListener(this);
        btnRange = findViewById(R.id.btnRange);
        btnRange.setOnClickListener(this);

        btnRange.setImageResource(R.drawable.rangeicon300);

        btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(this);

        listView = findViewById(R.id.listView);
        adapter = new ItemAdapter(this);
        listView.setAdapter(adapter);

        //Send Item to DetailActivity
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView listView = (ListView) parent;
                Item sendItem = (Item) listView.getItemAtPosition(position);
                Intent intent = new Intent(MainActivity.this,detailInfoActivity.class);
                intent.putExtra("imgUrl",sendItem.getImgUrl());
                intent.putExtra("name",sendItem.getName());
                intent.putExtra("category",sendItem.getCategory());
                intent.putExtra("address",sendItem.getAddress());
                intent.putExtra("opentime",sendItem.getOpenTime());
                intent.putExtra("holiday",sendItem.getHoliday());
                intent.putExtra("tel",sendItem.getTel());
                intent.putExtra("line",sendItem.getLine());
                intent.putExtra("station",sendItem.getStation());
                intent.putExtra("station_exit",sendItem.getStation_exit());
                intent.putExtra("hp",sendItem.getHp());
                intent.putExtra("coupon",sendItem.getCoupon());
                intent.putExtra("latitude",sendItem.getLatitude());
                intent.putExtra("longitude",sendItem.getLongitude());
                intent.putExtra("pr_short",sendItem.getPr_short());
                intent.putExtra("pr_long",sendItem.getPr_long());

                startActivity(intent);
            }
        });

        requestPermission();
        getLocation();
        progressON(MainActivity.this);

    }

    //Set Adapter to Listview
    class ItemAdapter extends ArrayAdapter<Item>{
        ItemAdapter(Context context){
            super(context, R.layout.listview_item);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Item item = getItem(position);

            if (convertView == null){
                LayoutInflater inflater = getLayoutInflater();
                convertView = inflater.inflate(R.layout.listview_item, null);
            }

            if (item != null){
                ImageView img = convertView.findViewById(R.id.img);
                TextView txtName = convertView.findViewById(R.id.txtName);
                TextView txtCategory = convertView.findViewById(R.id.txtCategory);
                TextView txtTel = convertView.findViewById(R.id.txtTel);
                TextView txtAccess = convertView.findViewById(R.id.txtAccess);

                if (img != null){
                    if (item.getImgUrl().equals("{}")){
                        img.setImageResource(R.drawable.noimageicon);
                    }else {
                        new DownloadImageTask(img).execute(item.getImgUrl());
                    }
                }
                if (txtName != null){
                    txtName.setText(item.getName());
                }
                if (txtCategory != null){
                    txtCategory.setText(item.getCategory());
                }
                if (txtTel != null){
                    txtTel.setText(item.getTel());
                }
                if (txtAccess != null){
                    line = item.getLine();
                    station = item.getStation();
                    station_exit = item.getStation_exit();

                    if (item.getLine().equals("{}")){
                        line = "";
                    }
                    if (item.getStation().equals("{}")){
                        station = "";
                    }
                    if (item.getStation_exit().equals("{}")){
                        station_exit = "";
                    }
                    txtAccess.setText(line + "\n" + station + " " + station_exit);

                }
            }
            return convertView;
        }
    }

    //Get Latitude, Longitude
    public void getLocation(){
        client = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        client.getLastLocation().addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();

                    try{
                        geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                        addresses = geocoder.getFromLocation(latitude,longitude,1);
                    }catch (IOException e){
                        txtLiveAddress.setText("現在位置を取得できません");
                        progressOFF();
                        e.printStackTrace();
                        return;
                    }

                    String jsonUrl = "https://api.gnavi.co.jp/RestSearchAPI/20150630/?keyid=9c75b92f795ca6428fc8c3bb23b7a304&format=json&" +
                            "latitude=" + latitude +
                            "&longitude=" + longitude +
                            "&range=" + range +
                            "&no_smoking=" + noSmoking +
                            "&takeout=" + takeout +
                            "&parking=" + parking +
                            "&wifi=" + wifi +
                            "&hit_per_page=" + hitPerPage;
                    new JsonTask().execute(jsonUrl);
                }
            }
        });
    }

    //Get Location Permission
    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
    }

    //get Json
    class JsonTask extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            super.onPreExecute();

        }
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null){

                JsonHelper helper = new JsonHelper(MainActivity.this,MainActivity.this);

                ArrayList list =  helper.parseJson(result);

                adapter.clear();
                adapter.addAll(list);

                String address = addresses.get(0).getAddressLine(0);
                String[] addressFix = address.split(" ");

                txtLiveAddress = findViewById(R.id.txtLiveAddress);
                txtLiveAddress.setSelected(true);

                if (2 < addressFix.length){
                    txtLiveAddress.setText(addressFix[1] + "\n" + addressFix[2]);
                }else if (addressFix.length == 2){
                    txtLiveAddress.setText(addressFix[1]);
                }
                progressOFF();
            }else {
                txtLiveAddress.setText("現在位置を取得できません");
                adapter.clear();
                adapter.notifyDataSetChanged();
                progressOFF();
            }
        }
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

    //Show progress
    public void progressON(Activity activity) {
        if (activity == null || activity.isFinishing()) {
            return;
        }

        progressDialog = new AppCompatDialog(activity);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.progress_loading);
        progressDialog.show();

        final ImageView img_frame_loading = progressDialog.findViewById(R.id.img_frame_loading);
        final AnimationDrawable frameAnimation = (AnimationDrawable) img_frame_loading.getBackground();
        img_frame_loading.post(new Runnable() {
            @Override
            public void run() {
                frameAnimation.start();
            }
        });
    }

    //Unshow Progress
    public void progressOFF() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @RequiresApi(api = 26)
    @Override
    public void onClick(View v) {

        //Set Vibrator
        Vibrator vibrator = (Vibrator) getSystemService(this.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            vibrator.vibrate(VibrationEffect.createOneShot(30, VibrationEffect.DEFAULT_AMPLITUDE));
        }else {
            vibrator.vibrate(30);
        }

        if (v.getId() == R.id.btnNoSmoking){
            if (noSmoking == 0){
                noSmoking = 1;
                btnNoSmoking.setImageResource(R.drawable.nosmokingicon_clicked);
            }else if (noSmoking == 1){
                noSmoking = 0;
                btnNoSmoking.setImageResource(R.drawable.nosmokingicon);
            }
        }else if (v.getId() == R.id.btnParking){
            if (parking == 0){
                parking = 1;
                btnParking.setImageResource(R.drawable.parkingicon_clicked);
            }else if (parking == 1){
                parking = 0;
                btnParking.setImageResource(R.drawable.parkingicon);
            }
        }else if (v.getId() == R.id.btnTakeOut){
            if (takeout == 0){
                takeout = 1;
                btnTakeOut.setImageResource(R.drawable.takeouticon_clicked);
            }else if (takeout == 1){
                takeout = 0;
                btnTakeOut.setImageResource(R.drawable.takeouticon);
            }
        }else if (v.getId() == R.id.btnWifi){
            if (wifi == 0){
                wifi = 1;
                btnWifi.setImageResource(R.drawable.wifiicon_clicked);
            }else if (wifi == 1){
                wifi = 0;
                btnWifi.setImageResource(R.drawable.wifiicon);
            }
        }else if (v.getId() == R.id.btnRange){
            if (range == 1){
                range = 2;
                hitPerPage = 50;
                btnRange.setImageResource(R.drawable.rangeicon500);
            }else if (range == 2){
                range = 3;
                hitPerPage = 100;
                btnRange.setImageResource(R.drawable.rangeicon1000);
            }else if (range == 3){
                range = 1;
                hitPerPage = 10;
                btnRange.setImageResource(R.drawable.rangeicon300);
            }
        }else if (v.getId() == R.id.btnSearch){
            requestPermission();
            getLocation();
            progressON(MainActivity.this);
        }
    }
}


package jp.ac.jec.cm0209.fenrir;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by guest on 2018/05/07.
 */

public class JsonHelper {

    MainActivity activity ;
    Context context;

    public JsonHelper(MainActivity activity, Context context) {
        this.activity = activity;
        this.context = context;
    }

    public ArrayList<Item> parseJson(String strJson){
        ArrayList<Item> list = new ArrayList<>();
        try{
            JSONObject json = new JSONObject(strJson);
            JSONArray rest = new JSONArray();
            rest = json.getJSONArray("rest");

            for (int i = 0; i < rest.length(); i++){
                JSONObject temp = rest.getJSONObject(i);

                String name = temp.getString("name");

                String address = temp.getString("address");

                String category = temp.getString("category");

                String tel = temp.getString("tel");

                String opentime = temp.getString("opentime");

                JSONObject access = temp.getJSONObject("access");
                String line = access.getString("line");
                String station = access.getString("station");
                String station_exit = access.getString("station_exit");

                String latitude = temp.getString("latitude");

                String longitude = temp.getString("longitude");

                JSONObject imageUrl = temp.getJSONObject("image_url");
                String imgUrl = imageUrl.getString("shop_image1");

                String holiday = temp.getString("holiday");

                String hp = temp.getString("url_mobile");

                JSONObject coupon_url = temp.getJSONObject("coupon_url");
                String coupon = coupon_url.getString("mobile");

                JSONObject pr = temp.getJSONObject("pr");
                String pr_short = pr.getString("pr_short");
                String pr_long = pr.getString("pr_long");



                Item item = new Item(name, address, category, tel, opentime, line, station, station_exit,
                        latitude, longitude, imgUrl, holiday, hp, coupon, pr_short, pr_long);
                list.add(item);
            }
        }catch (JSONException e){
            if (this.activity != null){
                activity.progressOFF();
            }
            Log.e("JsonHelper", e.getMessage(), e);


        }
        return list;
    }

}

package app.nutrimeat.meat.org.nutrimeat;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import app.nutrimeat.meat.org.nutrimeat.product.ModelCart;



public class CommonFunctions {
    public static void setSharedPreferenceStringList(Context pContext, String pKey, List<String> pData) {
        SharedPreferences.Editor editor = pContext.getSharedPreferences(PrefManager.PREF_NAME, Activity.MODE_PRIVATE).edit();
        editor.putInt(pKey + "size", pData.size());
        editor.commit();

        for (int i = 0; i < pData.size(); i++) {
            SharedPreferences.Editor editor1 = pContext.getSharedPreferences(PrefManager.PREF_NAME, Activity.MODE_PRIVATE).edit();
            editor1.putString(pKey + i, (pData.get(i)));
            editor1.commit();
        }
    }

    public static List<String> getSharedPreferenceStringList(Context pContext, String pKey) {
        int size = pContext.getSharedPreferences(PrefManager.PREF_NAME, Activity.MODE_PRIVATE).getInt(pKey + "size", 0);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(pContext.getSharedPreferences(PrefManager.PREF_NAME, Activity.MODE_PRIVATE).getString(pKey + i, ""));
        }
        return list;
    }

    public static void setSharedPreferenceProductList(Context pContext, String pKey, List<ModelCart> pData) {
        SharedPreferences.Editor editor = pContext.getSharedPreferences(PrefManager.PREF_NAME, Activity.MODE_PRIVATE).edit();
        Gson gson = new Gson();
        String jsonProduct = gson.toJson(pData);
        editor.putString(pKey, jsonProduct);
        editor.commit();
    }

    public static List<ModelCart> getSharedPreferenceProductList(Context pContext, String pKey) {
        String  json_string = pContext.getSharedPreferences(PrefManager.PREF_NAME, Activity.MODE_PRIVATE).getString(pKey,"");
        Gson gson = new Gson();
        Type type = new TypeToken<List<ModelCart>>(){}.getType();
        List<ModelCart> products= gson.fromJson(json_string, type);
        if(products==null){
            products=new ArrayList<>();
        }
        return products;
    }
}

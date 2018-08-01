package com.manju7.retrofit_list;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private List<Items> itemsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }


    private void loadJSON(){

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading data ....");
        progressDialog.show();

        Service serviceApi = Client.getClient();
        Call<JsonArray> loadItemCall = serviceApi.readItemsArray();

        loadItemCall.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                progressDialog.dismiss();

                String itemStr = response.body().toString();
                Type type = new TypeToken<List<Items>>() {}.getType();

                itemsList = getItemListFromJson(itemStr,type);
                adapter = new MyAdapter(getApplicationContext(),itemsList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

                Toast.makeText(MainActivity.this,"Error",Toast.LENGTH_LONG).show();
            }
        });

    }


    public static <T> List<T> getItemListFromJson(String jsonString, Type type){

        if (!isValid(jsonString)){
            return null;
        }
        return new Gson().fromJson(jsonString,type);
    }

    private static boolean isValid(String jsonString) {
        try {
            new JsonParser().parse(jsonString);
            return true;
        }catch (JsonSyntaxException j){
            return false;
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        loadJSON();
    }
}

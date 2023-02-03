package com.example.russianholidaysapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.russianholidaysapp.model.ListHolidaysInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements GetDataFromInternet.AsyncResponse, MyAdapter.ListItemClickListener{

    //https://calendarific.com/api/v2/holidays?api_key=46dd48f74b62308c71cc530ea13d54966d773fb7&country=RU&year=2023
    private static final String TAG = "MainActivity";
    private Toast toast;
    private ListHolidaysInfo listHolidaysInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            URL url = new URL("https://calendarific.com/api/v2/holidays?api_key=46dd48f74b62308c71cc530ea13d54966d773fb7&country=RU&year=2023");

            new GetDataFromInternet(this).execute(url);

        } catch (MalformedURLException e){
            e.printStackTrace();
        }

    }

    @Override
    public void processFinished(String output) {
        Log.d(TAG, "processFinished: "+output);

        try {
            JSONObject outputJSON = new JSONObject(output);
            JSONObject responseJSON = outputJSON.getJSONObject("response");
            JSONArray array = responseJSON.getJSONArray("holidays");

            int lengthArray = array.length();

            listHolidaysInfo = new ListHolidaysInfo(lengthArray); // массив праздников

            ArrayList <String> namesHolidays = new ArrayList<String>();

            for (int i = 0; i < lengthArray; i++) {
                JSONObject obj = array.getJSONObject(i);
                String name = obj.getString("name");

                JSONObject objDate = obj.getJSONObject("date");
                String date_iso = objDate.getString("iso");

                namesHolidays.add(name);
                Log.d(TAG, "processFinished: "+name+" "+date_iso);
                listHolidaysInfo.addHoliday(name, date_iso, i);

            }

            RecyclerView recyclerView = findViewById(R.id.recycler_view);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(new MyAdapter(listHolidaysInfo, lengthArray, this));

            //ArrayAdapter <String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, namesHolidays);
            //ListView listHolidays = findViewById(R.id.lvListOfHolidays);
            //listHolidays.setAdapter(adapter); // накидываем адаптер



        } catch (JSONException e){
            e.printStackTrace();
        }

    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        CharSequence text = listHolidaysInfo.listHolidaysInfo[clickedItemIndex].getHoliday_name();
        int duration = Toast.LENGTH_SHORT;
        if (toast != null){
            toast.cancel();
        }
        toast = Toast.makeText(this, text, duration);
        toast.show();
    }
}
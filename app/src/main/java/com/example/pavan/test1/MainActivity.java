package com.example.pavan.test1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private ProgressDialog pDialog;
    private TextView txt;
    private ListView lv;
    public static final String Urlis =" http://api1.yuktix.com:8080/sensordb/v1/module/device/archive/latest";
    public static final String Ukey="1868cac0-a92b-4b17-9cef-c540274f9cf0";
    ArrayList<HashMap<String, String>> contactList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn =(Button)findViewById(R.id.button);
        txt = (TextView) findViewById(R.id.text1);
        lv = (ListView) findViewById(R.id.list_item1);
        contactList = new ArrayList<>();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new JSONTask().execute(" http://api1.yuktix.com:8080/sensordb/v1/module/device/archive/latest");
            }
        });
    }
    public class JSONTask extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            BufferedReader reader = null;
            HttpURLConnection connection=null;
           // String input="{\"map\": {\"name\" : \"AWS\"}}";
            String ip ="{\"map\" : {\"module\": \"AWS\" , \"serialNumber\" : \"yuktix1\", \"limit\" : \"1\" }}";


            try {
                URL url = new URL(Urlis);
                connection =(HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Authorization","Signature="+Ukey);
                OutputStream os =connection.getOutputStream();
                os.write(ip.getBytes());
                os.flush();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                String line="";
                StringBuffer buffer = new StringBuffer();
                while ((line = reader.readLine())!= null){
                    buffer.append(line);
                }
                String finalJson = buffer.toString();
                JSONObject parentobject = new JSONObject(finalJson);
                JSONArray parentarray = parentobject.getJSONArray("result");
                //for (int i = 0; i < parentarray.length(); i++) {
                    JSONObject finalobj = parentarray.getJSONObject(0);
                    String ids = finalobj.getString("id");
                    // int yr  =finalobj.getInt("id");
                    String tempereature = finalobj.getString("T");
                    String humidity = finalobj.getString("RH");
                    String rain = finalobj.getString("Rain");
                    HashMap<String, String> contact = new HashMap<>();
                    contact.put("ids",ids);
                    contact.put("temperature",tempereature);
                    contact.put("humidity",humidity);
                    contact.put("rain",rain);
                    contactList.add(contact);
                    //return moviename + "-" + email;
               //}
            }catch (MalformedURLException e){
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if(connection!= null) {
                    connection.disconnect();
                }
                try {
                    if (reader !=null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(
                    MainActivity.this, contactList, R.layout.list_item,
                    new String[]{"ids", "temperature","humidity","Rain"}, new int[]{R.id.aws_id, R.id.temperature,R.id.humid,R.id.rain});

            lv.setAdapter(adapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_setting:
                //Toast.makeText(getApplicationContext(),"Refresh Clicked",Toast.LENGTH_LONG).show();
                new JSONTask().execute();
                break;
            case R.id.action_test:
                startActivity(new Intent("com.example.pavan.test1.Main2Activity"));
                break;
            case R.id.action_test2:
                Toast.makeText(getApplicationContext(),"Clicked 2nd",Toast.LENGTH_LONG).show();
                break;
        }
        return true;
    }
}

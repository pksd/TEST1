package com.example.pavan.test1;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.pavan.test1.modles.MovieModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView = (ListView)findViewById(R.id.movies);

               // new JSONTask().execute("https://jsonparsingdemo-cec5b.firebaseapp.com/jsonData/moviesDemoItem.txt");
    }
     public class JSONTask extends AsyncTask<String,String,List<MovieModel>>{
        @Override
        protected List<MovieModel> doInBackground(String... params) {
            BufferedReader reader = null;
            HttpURLConnection connection=null;
            try {
                URL url = new URL(params[0]);
                connection =(HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                String line="";
                StringBuffer buffer = new StringBuffer();
                while ((line = reader.readLine())!= null){
                    buffer.append(line);
                }
                String finalJSON =buffer.toString();
                JSONObject object = new JSONObject(finalJSON);
                JSONArray array = object.getJSONArray("movies");
                List <MovieModel> movieModelslist= new ArrayList<>();
                for(int i=0;i<array.length();i++){
                    JSONObject jsonObject =array.getJSONObject(i);
                    MovieModel movieModel =new MovieModel();
                    movieModel.setMovie(jsonObject.getString("movie"));
                    movieModel.setYear( jsonObject.getInt("year"));
                    movieModel.setRating((float)jsonObject.getDouble("rating"));
                    movieModel.setDirector(jsonObject.getString("director"));
                    movieModel.setDuration(jsonObject.getString("duration"));
                    movieModel.setTagline(jsonObject.getString("tagline"));
                    movieModel.setImage(jsonObject.getString("image"));
                    movieModel.setStory(jsonObject.getString("story"));
                    List<MovieModel.Cast> castList=  new ArrayList<>();
                    for(int j=0;j<jsonObject.getJSONArray("cast").length();j++){
                        JSONObject jsonObject1 = jsonObject.getJSONArray("cast").getJSONObject(j) ;
                        MovieModel.Cast cast = new MovieModel.Cast();
                        cast.setName(jsonObject1.getString("name"));
                        castList.add(cast);
                    }
                    movieModel.setCastList(castList);
                    movieModelslist.add(movieModel);
                }

                return movieModelslist;

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
        protected void onPostExecute(List<MovieModel> s) {
            super.onPostExecute(s);

        }
    }

}

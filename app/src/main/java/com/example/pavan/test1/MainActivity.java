package com.example.pavan.test1;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuItemWrapperICS;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
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
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView)findViewById(R.id.movies);

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
            MovieAdapter adpater = new MovieAdapter(getApplicationContext(),R.layout.row,s);
            listView.setAdapter(adpater);

        }
    }

    public class MovieAdapter extends ArrayAdapter  {
        private final int resource;
        private List<MovieModel> movieModelList;
        private LayoutInflater inflater;
        private MovieAdapter(Context context,int resource, List<MovieModel> objects) {
            super(context, resource, objects);
            movieModelList = objects;
            this.resource = resource;
            inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if(convertView== null)
            {
                convertView = inflater.inflate(R.layout.row,null);
            }
            ImageView imageview;
            TextView txtmovie;
            TextView txttagline;
            TextView txtyear;
            TextView txtduration;
            TextView txtdirector;
            TextView txtcast;
            TextView txtstory;
            RatingBar rtbar;

            imageview = (ImageView)convertView.findViewById(R.id.imageView);
            txtmovie =(TextView)convertView.findViewById(R.id.movienameid);
            txttagline=(TextView)convertView.findViewById(R.id.taglineid);
            txtyear=(TextView)convertView.findViewById(R.id.yearid);
            txtduration=(TextView)convertView.findViewById(R.id.durationid);
            txtdirector=(TextView)convertView.findViewById(R.id.directorid);
            txtcast=(TextView)convertView.findViewById(R.id.castid);
            txtstory=(TextView)convertView.findViewById(R.id.storyid);
            rtbar =(RatingBar)convertView.findViewById(R.id.ratingbar);

            txtmovie.setText(movieModelList.get(position).getMovie());
            txttagline.setText("Year: "+movieModelList.get(position).getTagline());
            txtyear.setText(movieModelList.get(position).getYear());
            txtdirector.setText(movieModelList.get(position).getDirector());
            txtduration.setText(movieModelList.get(position).getDuration());

            StringBuffer stringbuffer = new StringBuffer();

            for(MovieModel.Cast cast:movieModelList.get(position).getCastList())
            {
                stringbuffer.append(cast.getName()+",");
            }
            txtcast.setText(stringbuffer);
            txtstory.setText(movieModelList.get(position).getStory());

            //Rating bar
            rtbar.setRating(movieModelList.get(position).getRating()/2);
            return  convertView;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_activity, menu);
        return true;
    }

    @Override
    public  boolean onOptionsItemSelected(MenuItem item){
        int id= item.getItemId();
        if (id==R.id.actionsetting) {
            new JSONTask().execute("https://jsonparsingdemo-cec5b.firebaseapp.com/jsonData/moviesData.txt");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

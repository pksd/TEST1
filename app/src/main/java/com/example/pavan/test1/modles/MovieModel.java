package com.example.pavan.test1.modles;

import java.util.List;

/**
 * Created by pavan on 10/3/17.
 */

public class MovieModel {
    private String movie,duration,director,tagline,image,story;
    private int year;
    private  float rating;
    private List<Cast> castList;

    public void setMovie(String movie) {
        this.movie = movie;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setCastList(List<Cast> castList) {
        this.castList = castList;
    }

    public String getMovie() {
        return movie;
    }

    public String getDuration() {
        return duration;
    }

    public String getDirector() {
        return director;
    }

    public String getTagline() {
        return tagline;
    }

    public String getImage() {
        return image;
    }

    public String getStory() {
        return story;
    }

    public int getYear() {
        return year;
    }

    public float getRating() {
        return rating;
    }

    public List<Cast> getCastList() {
        return castList;
    }

    public static  class  Cast{
        private String name;

       public String getName() {
           return name;
       }

       public void setName(String name) {
           this.name = name;
       }
   }
}

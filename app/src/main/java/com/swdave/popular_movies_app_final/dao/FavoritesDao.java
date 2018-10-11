package com.swdave.popular_movies_app_final.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.swdave.popular_movies_app_final.model.MovieResults;

import java.util.List;

@Dao
public interface FavoritesDao {

    @Insert
    void insert(MovieResults movieResults);

    @Query("DELETE FROM favorites_table")
    void deleteAllFavorites();

    @Query("SELECT * FROM favorites_table ORDER BY id DESC")
    LiveData<List<MovieResults>> getAllFavorites();

    @Query("DELETE FROM favorites_table WHERE id = :id")
    void deleteThisMovie(int id);

    @Query("SELECT COUNT(id) FROM favorites_table WHERE id = :id")
    Integer ifExists(int id);

}

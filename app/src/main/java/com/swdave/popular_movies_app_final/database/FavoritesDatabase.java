package com.swdave.popular_movies_app_final.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.swdave.popular_movies_app_final.dao.FavoritesDao;
import com.swdave.popular_movies_app_final.model.MovieResults;

@Database(entities = MovieResults.class, version = 1, exportSchema = false)
public abstract class FavoritesDatabase extends RoomDatabase {

    private static FavoritesDatabase instance;

    public abstract FavoritesDao favoritesDao();

    public static synchronized FavoritesDatabase getInstance(Context context){

        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    FavoritesDatabase.class, "favorites_database.db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}

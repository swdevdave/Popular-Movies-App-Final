package com.swdave.popular_movies_app_final.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.swdave.popular_movies_app_final.dao.FavoritesDao;
import com.swdave.popular_movies_app_final.database.FavoritesDatabase;
import com.swdave.popular_movies_app_final.model.MovieResults;

import java.util.List;

public class FavoritesRepository {

    private FavoritesDao favoritesDao;

    private LiveData<List<MovieResults>> allFavorites;


    public FavoritesRepository(Application application) {
        FavoritesDatabase database = FavoritesDatabase.getInstance(application);
        favoritesDao = database.favoritesDao();
        allFavorites = favoritesDao.getAllFavorites();

    }

    public void insert(MovieResults fav){
        new InsertFavoritesAsyncTask(favoritesDao).execute(fav);

    }

    public void delete(){
        new DeleteFavoritesAsyncTask(favoritesDao).execute();
    }

    public void deleteAllFavorites(){
        new DeleteAllFavoritesAsyncTask(favoritesDao).execute();
    }

    public LiveData<List<MovieResults>> getAllFavorites(){
        return allFavorites;
    }


    private static class InsertFavoritesAsyncTask extends AsyncTask<MovieResults,Void, Void>{
        private FavoritesDao favoritesDao;

        private InsertFavoritesAsyncTask(FavoritesDao favoritesDao){
            this.favoritesDao = favoritesDao;
        }

        @Override
        protected Void doInBackground(MovieResults... movieResults) {
            favoritesDao.insert(movieResults[0]);
            return null;
        }
    }

    private static class DeleteFavoritesAsyncTask extends AsyncTask<MovieResults,Void, Void>{
        private FavoritesDao favoritesDao;

        private DeleteFavoritesAsyncTask(FavoritesDao favoritesDao){
            this.favoritesDao = favoritesDao;
        }

        @Override
        protected Void doInBackground(MovieResults... movieResults) {
            favoritesDao.delete(movieResults[0]);
            return null;
        }
    }

    private static class DeleteAllFavoritesAsyncTask extends AsyncTask<Void,Void, Void>{
        private FavoritesDao favoritesDao;

        private DeleteAllFavoritesAsyncTask(FavoritesDao favoritesDao){
            this.favoritesDao = favoritesDao;
        }

        @Override
        protected Void doInBackground(Void... Voids) {
            favoritesDao.deleteAllFavorites();
            return null;
        }
    }
}

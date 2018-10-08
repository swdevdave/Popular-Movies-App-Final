package com.swdave.popular_movies_app_final.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.swdave.popular_movies_app_final.model.MovieResults;
import com.swdave.popular_movies_app_final.repository.FavoritesRepository;

import java.util.List;

public class FavoritesViewModel extends AndroidViewModel {

    private FavoritesRepository favoritesRepository;
    private LiveData<List<MovieResults>> allFavorites;

    public FavoritesViewModel(@NonNull Application application) {
        super(application);
        favoritesRepository = new FavoritesRepository(application);
        allFavorites = favoritesRepository.getAllFavorites();
    }


    public void insert(MovieResults fav){
        favoritesRepository.insert(fav);
    }

    public void delete(MovieResults fav){
        favoritesRepository.delete(fav);
    }

    public void deleteAllFavorites() {
        favoritesRepository.deleteAllFavorites();
    }

    public LiveData<List<MovieResults>> getAllFavorites(){
        return allFavorites;
    }


}

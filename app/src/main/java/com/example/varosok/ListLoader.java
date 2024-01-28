package com.example.varosok;

import android.os.AsyncTask;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListLoader {
    public static List<City> cities = new ArrayList<>();

    public interface ListLoaderCallback {
        void onListLoaded(List<City> cities, int lastId);
    }

    public static class ListLoaderclass extends AsyncTask<Void, Void, Response> {
        private String requestUrl;
        private ListLoaderCallback callback;

        public ListLoaderclass(String requestUrl, ListLoaderCallback callback) {
            this.requestUrl = requestUrl;
            this.callback = callback;
        }

        @Override
        protected Response doInBackground(Void... voids) {
            Response response = null;
            try {
                response = RequestHandler.get(requestUrl);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return response;
        }

        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            Gson converter = new Gson();
            City[] cityArray = converter.fromJson(response.getContent(), City[].class);
            cities.clear();
            cities.addAll(Arrays.asList(cityArray));
            City lastCity = cities.get(cities.size() - 1);
            int lastId = lastCity.getId();

            // Notify the callback
            if (callback != null) {
                callback.onListLoaded(cities, lastId);
            }
        }
    }
}




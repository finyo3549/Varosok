package com.example.varosok;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RequestTask extends AsyncTask<Void, Void, Response> {
    String requestUrl;
    String requestType;
    String requestParams;
    private RequestCallback callback;
    private static List<City> updatedCities = Arrays.asList();
    private Context context;
    public RequestTask(String requestUrl, String requestType, String requestParams,RequestCallback callback, Context context) {
        this.requestUrl = requestUrl;
        this.requestType = requestType;
        this.requestParams = requestParams;
        this.callback = callback;
        this.context = context;
    }

    public RequestTask(String requestUrl, String requestType, RequestCallback callback,Context context) {
        this.requestUrl = requestUrl;
        this.requestType = requestType;
        this.callback = callback;
        this.context = context;
    }

    @Override
    protected Response doInBackground(Void... voids) {
        Response response = null;
        try {
            switch (requestType) {
                case "GET":
                    response = RequestHandler.get(requestUrl);

                    break;
                case "POST":
                    response = RequestHandler.post(requestUrl, requestParams);
                    break;
                case "PUT":
                    response = RequestHandler.put(requestUrl, requestParams);
                    break;
                case "DELETE":
                    response = RequestHandler.delete(requestUrl + "/" + requestParams);
                    break;
            }
        } catch (IOException e) {
            return null;
        }
        return response;
    }

    @Override
    protected void onPostExecute(Response response) {
        if (response != null) {
            Gson converter = new Gson();
            switch (requestType) {
                case "GET":
                    handleGetResponse(converter, response);
                        break;
                case "POST":
                    handlePostResponse(converter, response);
                    break;
                case "PUT":
                    handlePutResponse(converter, response);
                    break;
                case "DELETE":
                    handleDeleteResponse(response);
                    break;
            }
        } else {
            callback.onFailure(new IOException("IO Exception occurred"));
        }
    }

    private void handleDeleteResponse(Response response) {
        int id = Integer.parseInt(requestParams);

        updatedCities.removeIf(city1 -> city1.getId() == id);
        if (context != null) {
            Toast.makeText(context.getApplicationContext(), "Sikeres törlés", Toast.LENGTH_SHORT).show();
                callback.onSuccess(response, updatedCities);
        }
    }

    private void handlePutResponse(Gson converter, Response response) {
        City city = converter.fromJson(
                response.getContent(), City.class);
        updatedCities.replaceAll(city1 ->
                city1.getId() == city.getId() ? city : city1);
        if (context != null) {
            Toast.makeText(context.getApplicationContext(), "Sikeres adatlekérdezés", Toast.LENGTH_SHORT).show();
            callback.onSuccess(response, updatedCities);
        }
    }

    private void handlePostResponse(Gson converter, Response response) {
        City city = converter.fromJson(
                response.getContent(), City.class);
        updatedCities.add(0,city);
        if (context != null) {
            Toast.makeText(context.getApplicationContext(), "Sikeres adatlekérdezés", Toast.LENGTH_SHORT).show();
            callback.onSuccess(response, updatedCities);
        }
    }

    private void handleGetResponse(Gson converter, Response response) {

            City[] cityArray = converter.fromJson(response.getContent(), City[].class);
        updatedCities = new ArrayList<>(Arrays.asList(cityArray));
        if (context != null) {
                Toast.makeText(context.getApplicationContext(), "Sikeres adatlekérdezés", Toast.LENGTH_SHORT).show();
                callback.onSuccess(response, updatedCities);
            }
        }
    }



package com.example.varosok;

import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.util.List;

public interface RequestCallback {
    void onSuccess(Response response, List<City> updatedCities);

    void onFailure(IOException e);
}

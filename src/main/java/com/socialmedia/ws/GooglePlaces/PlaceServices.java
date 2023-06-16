package com.socialmedia.ws.GooglePlaces;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlaceDetails;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;

@Service
public class PlaceServices {

    PlaceRepository placeRepository;
    private final GeoApiContext geoApiContext;
    private final String apiKey = "AIzaSyCg2-A6lzPwvHBij9kNCKdDYYNZrjy6n3s";

    public PlaceServices(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
        this.geoApiContext = new GeoApiContext.Builder()
                .apiKey(this.apiKey)
                .build();
    }

    public Places savePlaces(double latitude, double longitude, int radius, String formattedAddress) {
        Places places = new Places();
        places.setLatitude(latitude);
        places.setLongitude(longitude);
        places.setRadius(radius);
        places.setFormattedAddress(formattedAddress);
        placeRepository.save(places);
        return places;
    }

    public Places searchPlaces(double latitude, double longitude, int radius) throws Exception {
        ArrayList<PlaceDetails> placeDetails = new ArrayList<>();
        ;
        LatLng location = new LatLng(latitude, longitude);
        PlacesSearchResponse response = PlacesApi.nearbySearchQuery(geoApiContext, location)
                .radius(radius)
                .await();

        if (response.results != null && response.results.length > 0) {

            for (PlacesSearchResult result : response.results) {
                String placeId = result.placeId;
                PlaceDetails placeDetail = PlacesApi.placeDetails(geoApiContext, placeId).await();
                placeDetails.add(placeDetail);
            }
        }

        if (placeDetails.size() > 0) {
            Places place = savePlaces(latitude, longitude, radius, placeDetails.get(0).formattedAddress);
            return place;
        } else {
            return null;
        }

    }

}

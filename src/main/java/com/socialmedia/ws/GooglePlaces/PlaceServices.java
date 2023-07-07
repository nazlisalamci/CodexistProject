package com.socialmedia.ws.GooglePlaces;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlaceDetails;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;
import java.util.ResourceBundle;

@Service
public class PlaceServices {

    PlaceRepository placeRepository;
    private final GeoApiContext geoApiContext;
    @Value("${apiKey}") private String apiKey;


    public PlaceServices(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
        this.geoApiContext = new GeoApiContext.Builder()
                .apiKey(this.apiKey)
                .build();
    }

       public Places savePlaces(double latitude, double longitude, int radius, String formattedAddress,Boolean isDatabase) {
        Places places = new Places();
        places.setLatitude(latitude);
        places.setLongitude(longitude);
        places.setRadius(radius);
        places.setFormattedAddress(formattedAddress);
        places.setIsDatabase(isDatabase);
        placeRepository.save(places);
        return places;
    }

      public Places searchPlaces(double latitude, double longitude, int radius) throws Exception {
        
        Places p = new Places();
        p = placeRepository.findPlaces(latitude, longitude, radius);
        if (p != null) {
            p.setIsDatabase(true);
            return p;
        } else {
            ArrayList<PlaceDetails> placeDetails = new ArrayList<>();
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
                Places place = savePlaces(latitude, longitude, radius, placeDetails.get(0).formattedAddress,false);               
                return place;
            } else {
                return null;
            }
        }

    }
}

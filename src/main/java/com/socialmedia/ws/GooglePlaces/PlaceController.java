package com.socialmedia.ws.GooglePlaces;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.maps.model.PlaceDetails;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@CrossOrigin
public class PlaceController {

   @Autowired
   PlaceServices placeServices;

   @GetMapping("/api/1.0/place")
   public String postPlace() {
      System.out.println("merhaalar");
      String message = "Başarılı";
      return message;
   }

   @PostMapping
   ("/api/2.0/place")
   public Places postPalaces(@RequestBody Map<String, String> requestParams) throws Exception {  
        String latitudeStr = requestParams.get("latitude");
        String longitudeStr = requestParams.get("longitude");
        double latitude = Double.parseDouble(latitudeStr);
        double longitude = Double.parseDouble(longitudeStr);
        int radius = Integer.parseInt(requestParams.get("radius"));    
       return placeServices.searchPlaces(latitude, longitude, radius); 
   }



}

package com.socialmedia.ws.GooglePlaces;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PlaceRepository extends JpaRepository<Places,Long>{

        @Query(value = "SELECT Top(1) * FROM PLACES u WHERE u.latitude = ?1 and u.longitude = ?2 and u.radius = ?3 ",
    nativeQuery = true)
    Places findPlaces(double latitude, double longitude, int radius );
    
}


package com.socialmedia.ws.GooglePlaces;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Places {
    @Id
    @GeneratedValue
    private long id;

    private double latitude;

    private double longitude;

    private double radius;

    private String formattedAddress;

    private  Boolean isDatabase ;
    //database=> 1
    //database değilse 0

}

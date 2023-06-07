package clone.carrotMarket.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Place {

    private String place;
    private int latitude;
    private int longitude;
    public Place(){
    }
    public Place(String place, int latitude, int longitude) {
        this.place = place;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}

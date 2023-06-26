package clone.carrotMarket.domain;

public enum Category {
    Digital("디지털기기"), Life("생활/가전"), Interior("가구/인테리어"),
    Food("생활/가공식품"), Plant("식물"), Other("기타 중고물품");
    private final String value;



    Category(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

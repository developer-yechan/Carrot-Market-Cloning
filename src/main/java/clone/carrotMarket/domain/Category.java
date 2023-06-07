package clone.carrotMarket.domain;

public enum Category {
    Digital("디지털기기"), Life("생활/가전"), Interior("가구/인테리어");
    private final String value;



    Category(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

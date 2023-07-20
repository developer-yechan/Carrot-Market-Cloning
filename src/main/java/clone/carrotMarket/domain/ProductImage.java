package clone.carrotMarket.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_image_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sell_id")
    private Sell sell;

    private String imageUrl;

    private ImageRank imageRank;


    public ProductImage(String imageUrl, ImageRank imageRank) {
        this.imageUrl = imageUrl;
        this.imageRank = imageRank;
    }

//    @Override
//    public String toString() {
//        return "ProductImage{" +
//                "id=" + id +
//                ", sell=" + sell +
//                ", imageUrl='" + imageUrl + '\'' +
//                ", imageRank=" + imageRank +
//                '}';
//    }
}

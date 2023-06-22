package clone.carrotMarket.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Sell {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sell_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY )
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "sell",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<ProductImage> productImages = new ArrayList<>();

    @OneToMany(mappedBy = "sell",cascade = CascadeType.REMOVE,orphanRemoval = true)
    private List<SellLike> sellLikes = new ArrayList<>();

    @OneToMany(mappedBy = "sell",cascade = CascadeType.REMOVE,orphanRemoval = true)
    private List<ChatRoom> chatRooms = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "purchase_id")
    private Purchase purchase;
    private String title;
    private String content;
    private int price;
    @Enumerated(EnumType.STRING)
    private Category category;
    @Embedded
    private Place tradePlace;
    @Enumerated(EnumType.STRING)
    private SellStatus sellStatus;
    private int views;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 연관관계 편의 메서드
    public void addProductImage(ProductImage productImage){
        productImages.add(productImage);
        productImage.setSell(this);
    }

    public static Sell createSell(Member member, List<ProductImage> productImages, String title, String content,
                                  int price, Category category, Place tradePlace) {

        Sell sell = new Sell();
        sell.setMember(member);
        for (ProductImage productImage : productImages) {
            sell.addProductImage(productImage);
        }
        sell.setSellStatus(SellStatus.판매중);
        sell.setTitle(title);
        sell.setContent(content);
        sell.setPrice(price);
        sell.setCategory(category);
        sell.setTradePlace(tradePlace);
        sell.setCreatedAt(LocalDateTime.now());
        return sell;
    }

    @Override
    public String toString() {
        return "Sell{" +
                "sellStatus=" + sellStatus +
                '}';
    }
}

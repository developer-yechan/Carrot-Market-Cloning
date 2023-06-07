package clone.carrotMarket.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatRoom_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY )
    @JoinColumn(name = "seller_id")
    private Member seller;

    @ManyToOne(fetch = FetchType.LAZY )
    @JoinColumn(name = "purchaser_id")
    private Member purchaser;

    @ManyToOne(fetch = FetchType.LAZY )
    @JoinColumn(name = "sell_id")
    private Sell sell;

    @OneToMany(mappedBy = "chatRoom",cascade = CascadeType.REMOVE)
    private List<ChatMessage> chatMessages = new ArrayList<>();
}

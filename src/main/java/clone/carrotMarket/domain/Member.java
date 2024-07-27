package clone.carrotMarket.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @OneToMany(mappedBy = "member",cascade = CascadeType.REMOVE)
    private List<Sell> sells = new ArrayList<>();

    @OneToMany(mappedBy = "member",cascade = CascadeType.REMOVE)
    private List<Purchase> purchases = new ArrayList<>();

    @OneToMany(mappedBy = "member",cascade = CascadeType.REMOVE)
    private List<SellLike> sellLikes = new ArrayList<>();

    private String name;

    @Column(unique = true)
    private String email;
    private String password;
    private String phoneNumber;
    private String nickname;
    private String profileImage;
    @Embedded
    private Place myPlace;

    private int manner;

    public Member(String email, String password, String phoneNumber, String nickname, Place myPlace) {
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.nickname = nickname;
        this.myPlace = myPlace;
    }

    public void removeProfileImage(){
        if(this.profileImage != null){
            this.profileImage = null;
        }else{
            throw new IllegalStateException("프로필 사진이 이미 삭제되었습니다.");
        }
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", sells=" + sells +
                ", purchases=" + purchases +
                ", sellLikes=" + sellLikes +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", nickname='" + nickname + '\'' +
                ", profileImage='" + profileImage + '\'' +
                ", myPlace=" + myPlace +
                ", manner=" + manner +
                '}';
    }
}

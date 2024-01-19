package combus.backend.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Entity
@Table
@Getter
@Setter
@ToString
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long vehId;
    private boolean boardingStatus;
    private boolean dropStatus;
    private Long boardingStop;
    private Long dropStop;
    private String busRouteName;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Reservation(Long userId, Long vehId, Long boardingStop, Long dropStop, String busRouteName) {
        this.userId = userId;
        this.vehId = vehId;
        this.boardingStop = boardingStop;
        this.dropStop = dropStop;
        this.busRouteName = busRouteName;
    }

    public boolean getBoardingStatus() {
        return boardingStatus;
    }

    public boolean getDropStatus() {
        return dropStatus;
    }
}

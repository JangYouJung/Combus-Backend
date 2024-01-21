package combus.backend.domain;

import combus.backend.dto.ReservationResponseDto;
import combus.backend.repository.BusStopRepository;
import combus.backend.repository.UserRepository;
import combus.backend.service.BusStopService;
import combus.backend.service.UserService;
import jakarta.persistence.*;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;

@Entity
@Table
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long vehId;
    private boolean boardingStatus;
    private boolean dropStatus;
    private String busRouteName;


    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boarding_stop")
    private BusStop boardingStop;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "drop_stop")
    private BusStop dropStop;

    @CreatedDate
    private LocalDate createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;


    public Reservation(@NotNull User user,
                       Long vehId,
                       @NotNull BusStop boardingStop,
                       @NotNull BusStop dropStop,
                       String busRouteName) {
        this.user = user;
        this.vehId = vehId;
        this.boardingStop = boardingStop;
        this.dropStop = dropStop;
        this.busRouteName = busRouteName;
    }
}

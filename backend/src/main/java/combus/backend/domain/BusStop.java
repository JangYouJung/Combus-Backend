package combus.backend.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BusStop {
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private String arsId;     // 정류장 고유 번호

    private Long nodeId;
    private String name;    // 정류장명
    private boolean gpsX;   // 정류장 X좌표
    private boolean gpsY;   // 정류장 Y좌표
    private String type;

}

package combus.backend.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BoardingBusStopRequest {
    private double gpsX;
    private double gpsY;
}

package combus.backend.service;

import combus.backend.domain.BusStop;
import combus.backend.domain.Driver;
import combus.backend.domain.User;
import combus.backend.repository.BusStopRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
public class BusStopService {

    @Autowired
    private final BusStopRepository busStopRepository;

    @Autowired
    public BusStopService(BusStopRepository busStopRepository) {
        this.busStopRepository = busStopRepository;
    }

    public BusStop findByArsId(Long ArsId) {
        Optional<BusStop> getBusStop = busStopRepository.findByArsId(ArsId);
        BusStop busStop;
        System.out.println("버스정류장: "+ getBusStop);

        if(getBusStop.isPresent()){
            busStop = getBusStop.get();
            return busStop;
        }
        else return null;
    }

    public String getBusStopNameByArsId(Long arsId) {
        Optional<BusStop> optionalBusStop = busStopRepository.findByArsId(arsId);
        BusStop busStop = optionalBusStop.orElse(null);

        return (busStop != null) ? busStop.getName() : null;
    }
}

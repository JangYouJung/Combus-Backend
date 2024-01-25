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
@RequiredArgsConstructor
public class BusStopService {

    @Autowired
    private final BusStopRepository busStopRepository;

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

    public BusStop findByStId(Long stId) {
        Optional<BusStop> getBusStop = busStopRepository.findByStId(stId);
        BusStop busStop;

        if(getBusStop.isPresent()){
            busStop = getBusStop.get();
            return busStop;
        }
        else return null;
    }
}

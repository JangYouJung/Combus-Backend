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

    public BusStop findByArsId(String ArsId) {
        Optional<BusStop> getBusStop = busStopRepository.findByArsId(ArsId);
        BusStop busStop;
        System.out.println("버스정류장: "+ getBusStop);

        if(getBusStop.isPresent()){
            busStop = getBusStop.get();
            return busStop;
        }
        else return null;
    }

    public BusStop findByNodeId(Long nodeId) {
        Optional<BusStop> getBusStop = busStopRepository.findByNodeId(nodeId);
        BusStop busStop;

        if(getBusStop.isPresent()){
            busStop = getBusStop.get();
            return busStop;
        }
        else return null;
    }

    public BusStop findByName(String stName) {
        Optional<BusStop> getBusStop = busStopRepository.findByName(stName);
        BusStop busStop;

        if(getBusStop.isPresent()){
            busStop = getBusStop.get();
            return busStop;
        }
        else return null;
    }
}

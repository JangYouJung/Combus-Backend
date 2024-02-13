package combus.backend.service;

import combus.backend.domain.Bus;
import combus.backend.repository.BusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BusService {

    @Autowired
    private final BusRepository busRepository;

    public Bus findByRouteName(String busRouteName) {
        Optional<Bus> getBus = busRepository.findByRouteName(busRouteName);
        Bus bus;
        System.out.println("버스: "+ getBus);

        if(getBus.isPresent()){
            bus = getBus.get();
            return bus;
        }
        else return null;
    }
}

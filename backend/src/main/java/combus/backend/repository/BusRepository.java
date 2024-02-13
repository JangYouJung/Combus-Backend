package combus.backend.repository;

import combus.backend.domain.Bus;

import combus.backend.domain.BusStop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BusRepository  extends JpaRepository<Bus, Long> {
    Optional<Bus> findByRouteName(String routeName);
}

package combus.backend.repository;

import combus.backend.domain.BusStop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BusStopRepository extends JpaRepository<BusStop, Long> {
    Optional<BusStop> findByArsId(Long arsId);
    Optional<BusStop> findByStId(Long stId);
}

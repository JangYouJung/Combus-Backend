package combus.backend.repository;

import combus.backend.domain.BusStop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface BusStopRepository extends JpaRepository<BusStop, Long> {
    Optional<BusStop> findByArsId(String arsId);
    Optional<BusStop> findByNodeId(Long nodeId);
    Optional<BusStop> findByName(String stName);
}

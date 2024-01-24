package combus.backend.repository;

import combus.backend.domain.Bus;
import combus.backend.domain.BusMatch;
import combus.backend.domain.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BusMatchRepository extends JpaRepository<BusMatch, Long> {
    Optional<BusMatch> findBusMatchByDriverId(Long driverId);
}
package combus.backend.repository;

import combus.backend.domain.Reservation;
import combus.backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Optional<Reservation> findAllByUserId(Long userId);
    Optional<Reservation> findByUserIdAndDropStatus(Long userId, boolean dropStatus);

}

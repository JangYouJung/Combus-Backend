package combus.backend.repository;

import combus.backend.domain.Reservation;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long>, JpaSpecificationExecutor<Reservation> {
    Optional<Reservation> findByUserIdAndDropStatus(Long userId, boolean dropStatus);
    Optional<Reservation> findById(Long id);

    List<Reservation> findAllByBoardingStopArsIdAndBoardingStatusAndDropStatus(String boardingStopId, boolean boardingStatus, boolean dropStatus);

    List<Reservation> findAllByDropStopArsIdAndBoardingStatusAndDropStatus(String DropStopId, boolean boardingStatus, boolean dropStatus);

    @Modifying
    @Transactional
    @Query(value = "UPDATE reservation r SET r.boarding_status = true WHERE r.id = :id", nativeQuery = true)
    void updateBoardingStatus(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE reservation r SET r.drop_status = true WHERE r.id = :id", nativeQuery = true)
    void updateDropStatus(@Param("id") Long id);

}

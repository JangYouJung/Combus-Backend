package combus.backend.controller;

import combus.backend.domain.BusMatch;
import combus.backend.dto.*;
import combus.backend.repository.BusMatchRepository;
import combus.backend.service.BusRouteService;
import combus.backend.service.ReservationService;
import combus.backend.util.ResponseCode;
import combus.backend.util.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/drivers")
public class BusRouteController {

    private final BusRouteService busRouteService;
    private final BusMatchRepository busMatchRepository;
    private final ReservationService reservationService;

    @Value("${serviceKey}")
    String serviceKey;

    // 버스 노선 ID 사용해서 정류장 정보 가져오기
    // Get the Bus Stop information with Bus ID from Korean Public Data portal
    String getRouteInfoURL = "http://ws.bus.go.kr/api/rest/busRouteInfo/getStaionByRoute?";

    // 버스 실시간 위치 정보 가져오기
    // Get the real time bus position from Korean Public Data portal
    String getBusPosURL= "http://ws.bus.go.kr/api/rest/buspos/getBusPosByVehId?";

    @GetMapping("/home/{driverId}")
    public ResponseEntity<ResponseData<DriverHomeResponseDto>> getBusRoutesByDriverId(
            @PathVariable("driverId") Long driverId
            ) throws Exception {

        System.out.println("현재 로그인한 버스 기사: "+ driverId);

        //현재 로그인한 버스기사가 운전하는 버스의 vehID 가져오기
        // Get the driver's bus's vehId(unique number)
        Optional<BusMatch> busMatchOptional = busMatchRepository.findBusMatchByDriverId(driverId);

        if (busMatchOptional.isPresent()) {
            BusMatch busMatch = busMatchOptional.get();

            String busRouteId = busMatch.getBus().getRouteId();
            String busRouteName = busMatch.getBus().getRouteName();
            Long vehId = busMatch.getBus().getVehId();

            // busRouteID를 사용해서 해당 버스의 노선 리턴
            // Fetch the bus's route information from Korean Public Data portal.
            String url = getRouteInfoURL + "ServiceKey=" + serviceKey + "&busRouteId=" + busRouteId;
            System.out.println(url);

            URI uri = new URI(url);
            RestTemplate restTemplate = new RestTemplate();
            String xmlData = restTemplate.getForObject(uri, String.class);

            // 버스 노선 정류소 정보 가져오기
            // Fetch the information with List type
            List<DriverHomeBusStopDto> busStopList = busRouteService.getDriverRouteInfo(xmlData, vehId);

            int total_reserved = 0;     // 총 예약 건수: Total boarding passengers count
            int total_drop = 0;         // 총 하차 건수: Total drop off passengers count

            for(DriverHomeBusStopDto busStopDto : busStopList){ // 총 예약 건수 구하기: Total reservation count
                total_reserved += busStopDto.getReserved_cnt();
                total_drop += busStopDto.getDrop_cnt();
            }

            BusPosDto busPos = GetBusPosDto(vehId);

            DriverHomeResponseDto driverHomeResponseDto =
                    new DriverHomeResponseDto(vehId, busRouteName, total_reserved, total_drop, busPos, busStopList);

            return ResponseData.toResponseEntity(ResponseCode.DRIVER_HOME_SUCCESS, driverHomeResponseDto);

        } else {
            return ResponseData.toResponseEntity(ResponseCode.DRIVER_HOME_FAILED,null);
        }

    }

    @GetMapping("/home/busStop/{arsId}")

    public ResponseEntity<ResponseData<BusStopReserveInfoDto>> getBusStopReservationInfo(
            @PathVariable("arsId") String arsId
    ) throws Exception {

        int boardingBlindCnt = 0;       // 승차 예정 시각 장애인 수: Count of Blind passengers who will board
        int boardingWheelchairCnt = 0;  // 승차 예정 휠체어 탑승객 수: Count of Passengers riding wheelchair who will board
        int dropBlindCnt = 0;           // 하차 예정 시각 장애인 수: Count of Blind passengers who will drop off
        int dropWheelchairCnt = 0;      // 하차 예정 휠체어 탑승객 수: Count of Passengers riding wheelchair who will drop off

        // 해당 정류장 승차 예정 승객 정보 찾아오기
        List<PassengerInfoDto> boardingPassengers = reservationService.findPassengers(arsId,false);
        for(PassengerInfoDto passengerInfoDto : boardingPassengers){
            if(passengerInfoDto.getType().equals("휠체어")) boardingWheelchairCnt++;
            else boardingBlindCnt++;
        }

        // 해당 정류장 하차 예정 승객 정보 찾아오기
        List<PassengerInfoDto> dropPassengers = reservationService.findPassengers(arsId,true);
        for(PassengerInfoDto passengerInfoDto : dropPassengers){
            if(passengerInfoDto.getType().equals("휠체어")) dropWheelchairCnt++;
            else dropBlindCnt++;
        }

        BusStopReserveInfoDto busStopReserveInfoDto =
                new BusStopReserveInfoDto(boardingPassengers,boardingBlindCnt,boardingWheelchairCnt,
                        dropPassengers,dropBlindCnt,dropWheelchairCnt);

        return ResponseData.toResponseEntity(ResponseCode.BUS_STOP_DETAIL_SUCCESS, busStopReserveInfoDto);
    }

    public BusPosDto GetBusPosDto(Long vehId) throws Exception {
        String url = getBusPosURL + "ServiceKey=" + serviceKey + "&vehId=" + vehId.toString();
        System.out.println(url);

        URI uri = new URI(url);
        RestTemplate restTemplate = new RestTemplate();
        String xmlData = restTemplate.getForObject(uri, String.class);

        BusPosDto busPosDto = busRouteService.getBusPosParseXml(xmlData);

        return busPosDto;
    }
}

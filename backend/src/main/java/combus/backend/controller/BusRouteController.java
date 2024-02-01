package combus.backend.controller;

import combus.backend.domain.Bus;
import combus.backend.domain.BusMatch;
import combus.backend.dto.*;
import combus.backend.repository.BusMatchRepository;
import combus.backend.service.BusRouteService;
import combus.backend.service.ReservationService;
import combus.backend.util.ResponseCode;
import combus.backend.util.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
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
    String getRouteInfoURL = "http://ws.bus.go.kr/api/rest/busRouteInfo/getStaionByRoute?";

    //버스 실시간 위치 정보 가져오기
    String getBusPosURL= "http://ws.bus.go.kr/api/rest/buspos/getBusPosByVehId?";

    @GetMapping("/home")
    public ResponseEntity<ResponseData<DriverHomeResponseDto>> getBusRoutesByDriverId(
            @SessionAttribute(name = "userId", required = false)Long driverId
    ) throws Exception {

        //현재 로그인한 버스기사가 운전하는 버스의 vehID 가져오기
        Optional<BusMatch> busMatchOptional = busMatchRepository.findBusMatchByDriverId(driverId);

        if (busMatchOptional.isPresent()) {
            BusMatch busMatch = busMatchOptional.get();

            String busRouteId = busMatch.getBus().getRouteId();
            String busRouteName = busMatch.getBus().getRouteName();
            Long vehId = busMatch.getBus().getVehId();

            // busRouteID를 사용해서 해당 버스의 노선 리턴
            String url = getRouteInfoURL + "ServiceKey=" + serviceKey + "&busRouteId=" + busRouteId;
            System.out.println(url);

            URI uri = new URI(url);
            RestTemplate restTemplate = new RestTemplate();
            String xmlData = restTemplate.getForObject(uri, String.class);

            // 버스 노선 정류소 정보 가져오기
            List<DriverHomeBusStopDto> busStopList = busRouteService.getDriverRouteInfo(xmlData, vehId);

            int total_reserved = 0;     // 총 예약 건수
            int total_drop = 0;         // 총 하차 건수

            for(DriverHomeBusStopDto busStopDto : busStopList){ // 총 예약 건수 구하기
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

    @GetMapping("/home/{arsId}")
    public ResponseEntity<ResponseData<BusStopReserveInfoDto>> getBusStopReservationInfo(
            @SessionAttribute(name = "userId", required = false) Long driverId,
            @PathVariable("arsId") Long arsId
    ) throws Exception {

        int boardingBlindCnt = 0;       // 승차 예정 시각 장애인 수
        int boardingWheelchairCnt = 0;  // 승차 예정 휠체어 탑승객 수
        int dropBlindCnt = 0;           // 하차 예정 시각 장애인 수
        int dropWheelchairCnt = 0;      // 하차 예정 휠체어 탑승객 수

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
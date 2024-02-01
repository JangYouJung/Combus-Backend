package combus.backend.controller;

import combus.backend.dto.StartBusStopDto;
import combus.backend.dto.BusResponseDto;
import combus.backend.dto.EndBusStopDto;
import combus.backend.request.BoardingBusStopRequest;
import combus.backend.service.ReservationApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.List;
@RestController
@RequiredArgsConstructor
@RequestMapping("/reservation")
public class ReservationApiController {//예약 기능에서 공공데이터 API다루는 경우를 모든 컨트롤러
    @Autowired
    ReservationApiService apiService;

    @Value("${serviceKey}")     // 보안을 위해 application.properties에 저장해둠
    String serviceKey;

    // 지정된 좌표와 반경 범위 내의 정류소 목록을 넘겨주는 공공 데이터 url
    String getStationByPos = "http://ws.bus.go.kr/api/rest/stationinfo/getStationByPos?";

    // 버스 정류장 ID로 해당 정류장을 경유하는 버스 데이터를 넘겨주는 공공 데이터 url
    String getStationByUidItemURL = "http://ws.bus.go.kr/api/rest/stationinfo/getStationByUid?";

    // 버스 노선 번호로 해당 버스가 경유하는 정류장 리스트를 넘겨주는 공공 데이터 url
    String getBusRouteInfoURL = "http://ws.bus.go.kr/api/rest/busRouteInfo/getStaionByRoute?";


    // 예약하기 1단계: 승객 위치 기반으로 주위 버스 정류장 리스트 반환
    @GetMapping("/startst")
    public ResponseEntity<List<StartBusStopDto>> StartBusStopList(
            @RequestBody BoardingBusStopRequest boardingBusStopRequest
    ) throws Exception {

        HttpURLConnection urlConnection = null;
        InputStream stream = null;
        String result = null;
        RestTemplate restTemplate = new RestTemplate();

        double gpsX = boardingBusStopRequest.getGpsX();
        double gpsY = boardingBusStopRequest.getGpsY();

        // 공공데이터 API 요청을 보낼 url 생성
        String urlStr = getStationByPos + "ServiceKey=" + serviceKey +
                "&tmX=" + gpsX + "&tmY=" + gpsY + "&radius=100";
        System.out.println(urlStr);

        URI uri = new URI(urlStr);
        String xmlData = restTemplate.getForObject(uri, String.class);

        List<StartBusStopDto> NearbyStopList = apiService.StartBusStopApiParseXml(xmlData);

        return new ResponseEntity<>(NearbyStopList, HttpStatus.OK);
    }

    // 예약하기 2단계: 승객이 1단계에서 설정한 승차 정류소를 경유하는 버스 리스트 반환
    @GetMapping("/bus")
    public ResponseEntity<List<BusResponseDto>> BusList(
            @RequestParam(value = "arsId") String arsId //승차 정류소 번호
    ) throws Exception {
        HttpURLConnection urlConnection = null;
        InputStream stream = null;
        String result = null;
        RestTemplate restTemplate = new RestTemplate();

        // 공공데이터 API 요청을 보낼 url 생성
        String urlStr = getStationByUidItemURL + "ServiceKey=" + serviceKey + "&arsId=" + arsId;
        System.out.println(urlStr);

        URI uri = new URI(urlStr);
        String xmlData = restTemplate.getForObject(uri, String.class);

        List<BusResponseDto> BusListByStartStation = apiService.BusApiParseXml(xmlData);

        return new ResponseEntity<>(BusListByStartStation, HttpStatus.OK);
    }


    // 예약 하기 3단계: 승객이 1단계에서 설정한 승차 정류장과 2단계에서 설정한 버스를 사용해 하차할 정류장 후보 리스트 반환
    @GetMapping("/endst")
    public ResponseEntity<List<EndBusStopDto>> EndBusStopList(
            @RequestParam(value = "arsId") String arsId, //승차 정류소 번호
            @RequestParam(value = "busRouteId") String busRouteId // 탑승할 버스 노선 ID
    ) throws Exception {
        HttpURLConnection urlConnection = null;
        InputStream stream = null;
        String result = null;
        RestTemplate restTemplate = new RestTemplate();

        // 공공데이터 API 요청을 보낼 url 생성
        String urlStr = getBusRouteInfoURL + "ServiceKey=" + serviceKey + "&busRouteId=" + busRouteId;
        System.out.println(urlStr);

        URI uri = new URI(urlStr);
        String xmlData = restTemplate.getForObject(uri, String.class);

        List<EndBusStopDto> EndStopList = apiService.EndBusStopApiParseXml(xmlData, arsId);

        return new ResponseEntity<>(EndStopList, HttpStatus.OK);
    }

}

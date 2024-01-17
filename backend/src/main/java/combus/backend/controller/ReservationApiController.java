package combus.backend.controller;

import combus.backend.dto.BusResponseDto;
import combus.backend.dto.BusStopDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservation")
public class ReservationApiController {//예약 기능에서 공공데이터 API다루는 경우를 모든 컨트롤러

    // 보안을 위해 application.properties에 저장해둠
    @Value("${serviceKey}")
    String serviceKey;


    // 버스 정류장 ID로 해당 정류장을 경유하는 버스 데이터를 넘겨주는 공공 데이터 url
    String getStationByUidItemURL = "http://ws.bus.go.kr/api/rest/stationinfo/getStationByUid?";

    // 버스 노선 번호로 해당 버스가 경유하는 정류장 리스트를 넘겨주는 공공 데이터 url
    String getBusRouteInfoURL = "http://ws.bus.go.kr/api/rest/busRouteInfo/getStaionByRoute?";

    
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

        List<BusResponseDto> BusListByStartStation = BusApiParseXml(xmlData);

        return new ResponseEntity<>(BusListByStartStation, HttpStatus.OK);
    }


    // 예약 하기 3단계: 승객이 1단계에서 설정한 승차 정류장과 2단계에서 설정한 버스를 사용해 하차할 정류장 후보 리스트 반환
    @GetMapping("/endst")
    public ResponseEntity<List<BusStopDto>> EndBusStopList(
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

        List<BusStopDto> EndStopList = EndBusStopApiParseXml(xmlData, arsId);

        return new ResponseEntity<>(EndStopList, HttpStatus.OK);
    }


    private List<BusResponseDto> BusApiParseXml(String xmlData) throws Exception {
        List<BusResponseDto> busList = new ArrayList<>();

        //Open API 에서 추출한 xml 데이터에서 원하는 정보 추출하기
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new InputSource(new StringReader(xmlData)));

        NodeList itemListNodes = document.getElementsByTagName("itemList");
        System.out.println("itemlist개수: "+itemListNodes.getLength());

        for (int i = 0; i < itemListNodes.getLength(); i++) {
            Node itemListNode = itemListNodes.item(i);

            if (itemListNode.getNodeType() == Node.ELEMENT_NODE) {
                Element itemListElement = (Element) itemListNode;
                BusResponseDto busResponseDto;

                String firstBusTimeInfo = getElementValue(itemListElement, "arrmsg1");
                // arrmsg1: 첫 번째 도착 예정 버스의 도착 정보 메시지 - ex) 2분후[2번째 전]

                if(firstBusTimeInfo.charAt(0) - 48 > 4 ){

                    // 첫 번째 도착 예정 버스의 남은 시간이 5분보다 커야 예약 가능
                    String vehId = getElementValue(itemListElement, "vehId1");
                    if(vehId.equals("0")) break; // 버스 ID가 0이면 PASS

                    String busRouteId = getElementValue(itemListElement, "busRouteId");
                    String busRouteAbrv = getElementValue(itemListElement, "busRouteAbrv");
                    int low = Integer.parseInt(Objects.requireNonNull(getElementValue(itemListElement, "busType1")));

                    busResponseDto = new BusResponseDto(vehId,busRouteId,busRouteAbrv,low);

                }
                else{
                    // 첫 번째 도착 예정 버스의 남은 시간이 5분 이하면 다음 버스(두 번째 도착 버스) 예약하도록
                    String vehId = getElementValue(itemListElement, "vehId2");
                    if(vehId.equals("0")) break; // 버스 ID가 0이면 PASS

                    String busRouteId = getElementValue(itemListElement, "busRouteId");
                    String busRouteAbrv = getElementValue(itemListElement, "busRouteAbrv");
                    int low = Integer.parseInt(Objects.requireNonNull(getElementValue(itemListElement, "busType2")));

                    busResponseDto = new BusResponseDto(vehId,busRouteId,busRouteAbrv,low);
                }

                busList.add(busResponseDto);
            }
        }
        return busList;
    }

    private List<BusStopDto> EndBusStopApiParseXml(String xmlData, String startArsId) throws Exception {
        List<BusStopDto> EndBusStopList = new ArrayList<>();

        //Open API 에서 추출한 xml 데이터에서 원하는 정보 추출하기
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new InputSource(new StringReader(xmlData)));

        NodeList itemListNodes = document.getElementsByTagName("itemList");
        System.out.println("itemlist개수: "+itemListNodes.getLength());


        int startStopSeq = -1;     // 승차 정류소의 순번

        //[1] 버스가 경유하는 정류장 목록 중 승차 정류장을 찾아서 해당 정류장의 순번 저장
        for (int i = 0; i < itemListNodes.getLength(); i++) {
            Node itemListNode = itemListNodes.item(i);

            if (itemListNode.getNodeType() == Node.ELEMENT_NODE) {
                Element itemListElement = (Element) itemListNode;

                String tmpArsId = getElementValue(itemListElement, "arsId");

                if(tmpArsId.equals(startArsId)){
                    startStopSeq = Integer.parseInt(getElementValue(itemListElement, "seq"));
                    break;
                }
            }
        }

        // [2] 버스의 seq값이 승차 정류장보다 큰 것 정류장들만 추출 (하차 정류장 후보)
        for (int i = 0; i < itemListNodes.getLength(); i++) {
            Node itemListNode = itemListNodes.item(i);

            if (itemListNode.getNodeType() == Node.ELEMENT_NODE) {
                Element itemListElement = (Element) itemListNode;

                BusStopDto busStopDto;

                int seq = Integer.parseInt(getElementValue(itemListElement, "seq"));

                if(seq>startStopSeq){
                    String arsId = getElementValue(itemListElement, "arsId");
                    String name = getElementValue(itemListElement, "stationNm");
                    String direction = getElementValue(itemListElement, "direction");
                    double gpsX = Double.parseDouble(getElementValue(itemListElement, "gpsX"));
                    double gpsY = Double.parseDouble(getElementValue(itemListElement, "gpsY"));

                    busStopDto = new BusStopDto(arsId, name, direction, gpsX, gpsY, seq);
                    EndBusStopList.add(busStopDto);
                }
            }
        }

        return EndBusStopList;
    }

    private String getElementValue(Element element, String tagName) {
        NodeList nodeList = element.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            Node node = nodeList.item(0);
            return node.getTextContent();
        }
        return null;
    }
}

package combus.backend.controller;

import combus.backend.dto.BusResponseDto;
import combus.backend.repository.ReservationRepository;
import combus.backend.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservation")
public class ReservationController {
    @Autowired
    ReservationService reservationService;
    @Autowired
    ReservationRepository reservationRepository;

    @Value("${serviceKey}")
    String serviceKey;

    String getStationByUidItemURL = "http://ws.bus.go.kr/api/rest/stationinfo/getStationByUid?";

    @GetMapping("/bus")
    public ResponseEntity<List<BusResponseDto>> BusList(
            @RequestParam(value = "arsId") String arsId //승차 정류소 ID
    ) throws Exception {
        HttpURLConnection urlConnection = null;
        InputStream stream = null;
        String result = null;
        RestTemplate restTemplate = new RestTemplate();

        String urlStr = getStationByUidItemURL + "ServiceKey=" + serviceKey + "&arsId=" + arsId;
        System.out.println(urlStr);

        URI uri = new URI(urlStr);
        String xmlData = restTemplate.getForObject(uri, String.class);

        List<BusResponseDto> vehIds = parseXml(xmlData);
        System.out.println(xmlData);

        return new ResponseEntity<>(vehIds, HttpStatus.OK);
    }

    private List<BusResponseDto> parseXml(String xmlData) throws Exception {
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
                    if(vehId.equals("0")) break;

                    String busRouteId = getElementValue(itemListElement, "busRouteId");
                    String busRouteAbrv = getElementValue(itemListElement, "busRouteAbrv");


                    String low2 = getElementValue(itemListElement,"busType1");
                    System.out.println("low2: "+low2);
                    int low = Integer.parseInt(getElementValue(itemListElement,"busType1"));
                    System.out.println("low:"+ low);

                    busResponseDto = new BusResponseDto(vehId,busRouteId,busRouteAbrv,low);

                }
                else{
                    // 첫 번째 도착 예정 버스의 남은 시간이 5분 이하면 다음 버스(두 번째 도착 버스) 예약하도록
                    String vehId = getElementValue(itemListElement, "vehId2");
                    if(vehId.equals("0")) break;

                    String busRouteId = getElementValue(itemListElement, "busRouteId");
                    String busRouteAbrv = getElementValue(itemListElement, "busRouteAbrv");

                    String low2 = getElementValue(itemListElement,"busType2");
                    System.out.println("low2: "+low2);
                    int low = Integer.parseInt(getElementValue(itemListElement,"busType2"));
                    System.out.println("low:"+ low);


                    busResponseDto = new BusResponseDto(vehId,busRouteId,busRouteAbrv,low);
                }

                busList.add(busResponseDto);
            }
        }
        return busList;
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
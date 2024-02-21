package combus.backend.service;

import combus.backend.dto.StartBusStopDto;
import combus.backend.dto.BusResponseDto;
import combus.backend.dto.EndBusStopDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ReservationApiService {

    public List<StartBusStopDto> StartBusStopApiParseXml(String xmlData) throws Exception {
        List<StartBusStopDto> startBusStopList = new ArrayList<>();

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

                StartBusStopDto startBusStop;

                String arsId = getElementValue(itemListElement, "arsId");
                String name = getElementValue(itemListElement, "stationNm");
                double gpsX = Double.parseDouble(getElementValue(itemListElement, "gpsX"));
                double gpsY = Double.parseDouble(getElementValue(itemListElement, "gpsY"));

                startBusStop = new StartBusStopDto(arsId, name, gpsX, gpsY);

                startBusStopList.add(startBusStop);
            }
        }

        return startBusStopList;
    }


    public List<BusResponseDto> BusApiParseXml(String xmlData) throws Exception {
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

    public List<EndBusStopDto> EndBusStopApiParseXml(String xmlData, String startArsId) throws Exception {
        List<EndBusStopDto> EndBusStopList = new ArrayList<>();

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

                EndBusStopDto endBusStopDto;

                int seq = Integer.parseInt(getElementValue(itemListElement, "seq"));

                if(seq>startStopSeq){
                    String arsId = getElementValue(itemListElement, "arsId");
                    String name = getElementValue(itemListElement, "stationNm");
                    String direction = getElementValue(itemListElement, "direction");
                    double gpsX = Double.parseDouble(getElementValue(itemListElement, "gpsX"));
                    double gpsY = Double.parseDouble(getElementValue(itemListElement, "gpsY"));

                    endBusStopDto = new EndBusStopDto(arsId, name, direction, gpsX, gpsY, seq);
                    EndBusStopList.add(endBusStopDto);
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

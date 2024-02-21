package combus.backend.service;

import combus.backend.domain.BusStop;
import combus.backend.domain.Reservation;
import combus.backend.dto.BusPosDto;
import combus.backend.dto.DriverHomeBusStopDto;
import combus.backend.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BusRouteService {

    @Autowired
    ReservationService reservationService;

    @Autowired
    BusStopService busStopService;

    public List<DriverHomeBusStopDto> getDriverRouteInfo(String xml, Long vehId) throws Exception {
        List<DriverHomeBusStopDto> driverHomeBusStopDtoList = new ArrayList<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new InputSource(new StringReader(xml)));

        NodeList nodeList = document.getElementsByTagName("itemList");
        System.out.println("itemlist개수: "+ nodeList.getLength());


        for (int i = 0; i < nodeList.getLength(); i++) {
            Node itemListNode = nodeList.item(i);

            if (itemListNode.getNodeType() == Node.ELEMENT_NODE) {
                Element itemListElement = (Element) itemListNode;

                String arsId =getElementValue(itemListElement, "arsId");
                if(arsId.equals(" ")) continue;
                String name = getElementValue(itemListElement, "stationNm");
                double gpsX = Double.parseDouble(getElementValue(itemListElement, "gpsX"));
                double gpsY = Double.parseDouble(getElementValue(itemListElement, "gpsY"));
                int seq = Integer.parseInt(getElementValue(itemListElement, "seq"));


                // 휠체어 탑승객, 시각 장애인 탑승 여부
                boolean wheelchair = false;
                boolean blind = false;

                // 예약 내역 DB에서 정류장 승차 예약 내역 가져오기
                List<Reservation> boardingReservations = reservationService.findDetailOfBoardingStop(arsId);
                int reserved_cnt = boardingReservations.size(); // 예약 건수
                System.out.println("승차 예정 건수: "+reserved_cnt);


                for(Reservation reservation : boardingReservations){
                    if(reservation.getUser().isWheelchair()) {
                        wheelchair = true;
                        break;
                    }
                    if(reservation.getUser().isBlindness()){
                        blind = true;
                        break;
                    }
                }

                // 예약 내역 DB에서 정류장 하차 예약 내역 가져오기
                List<Reservation> dropReservations = reservationService.findDetailOfDropStop(arsId);
                int drop_cnt = dropReservations.size(); // 하차 예정 건수
                System.out.println("하차 예정 건수: "+drop_cnt);

                for(Reservation reservation : dropReservations){
                    if(reservation.getUser().isWheelchair()) {
                        wheelchair = true;
                        break;
                    }
                }

                DriverHomeBusStopDto driverHomeBusStopDto =
                        new DriverHomeBusStopDto(arsId, name, gpsX, gpsY, seq, reserved_cnt, drop_cnt, wheelchair, blind);
                driverHomeBusStopDtoList.add(driverHomeBusStopDto);
                System.out.println(driverHomeBusStopDto.toString());

            }
        }
        return driverHomeBusStopDtoList;
    }


    private String getElementValue(Element element, String tagName) {
        NodeList nodeList = element.getElementsByTagName(tagName).item(0).getChildNodes();
        Node node = nodeList.item(0);
        return node.getNodeValue();
    }

    public BusPosDto getBusPosParseXml(String xmlData) throws Exception {

        //Open API 에서 추출한 xml 데이터에서 원하는 정보 추출하기
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new InputSource(new StringReader(xmlData)));

        NodeList itemListNodes = document.getElementsByTagName("itemList");
        System.out.println("itemlist개수: "+itemListNodes.getLength());


        Node itemListNode = itemListNodes.item(0);

        if (itemListNode.getNodeType() == Node.ELEMENT_NODE) {
            Element itemListElement = (Element) itemListNode;

            BusPosDto busPosDto;

            long stId = Long.parseLong(getElementValue(itemListElement, "stId"));
            int stSeq = Integer.parseInt(getElementValue(itemListElement, "stOrd"));
            Boolean stopFlag = Boolean.parseBoolean(getElementValue(itemListElement, "stopFlag"));

            BusStop busStop = busStopService.findByNodeId(stId);

            busPosDto = new BusPosDto(busStop.getArsId(),stSeq,stopFlag);
            return busPosDto;
        }
        else return null;
    }
}
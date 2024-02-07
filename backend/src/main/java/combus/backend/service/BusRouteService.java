package combus.backend.service;

import combus.backend.domain.Reservation;
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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BusRouteService {

    @Autowired
    ReservationService reservationService;

    @Autowired
    ReservationRepository reservationRepository;

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

                Long arsId = Long.parseLong(getElementValue(itemListElement, "arsId"));

                String name = getElementValue(itemListElement, "stationNm");
                double gpsX = Double.parseDouble(getElementValue(itemListElement, "gpsX"));
                double gpsY = Double.parseDouble(getElementValue(itemListElement, "gpsY"));
                int seq = Integer.parseInt(getElementValue(itemListElement, "seq"));


                // 휠체어 탑승객 탑승 여부
                boolean wheelchair = false;

                // 예약 내역 DB에서 정류장 승차 예약 내역 가져오기
                List<Reservation> boardingReservations = reservationService.findDetailOfBoardingStop(arsId);
                int reserved_cnt = boardingReservations.size(); // 예약 건수

                for(Reservation reservation : boardingReservations){
                    if(reservation.getUser().isWheelchair()) {
                        wheelchair = true;
                        break;
                    }
                }

                // 예약 내역 DB에서 정류장 하차 예약 내역 가져오기
                List<Reservation> dropReservations = reservationService.findDetailOfDropStop(arsId);
                int drop_cnt = dropReservations.size(); // 하차 예정 건수

                for(Reservation reservation : dropReservations){
                    if(reservation.getUser().isWheelchair()) {
                        wheelchair = true;
                        break;
                    }
                }

                DriverHomeBusStopDto driverHomeBusStopDto =
                        new DriverHomeBusStopDto(arsId, name, gpsX, gpsY, seq, reserved_cnt, drop_cnt, wheelchair);
                driverHomeBusStopDtoList.add(driverHomeBusStopDto);
                System.out.println(driverHomeBusStopDto.toString());

                driverHomeBusStopDtoList.add(driverHomeBusStopDto);
            }
        }
        return driverHomeBusStopDtoList;
    }

    public DriverInfoBusStopDto getBusStopInfo(Long arsId) {
        // 특정 정류장의 정보를 가져오는 코드 추가
        List<Reservation> boardingReservations = reservationService.findDetailOfBoardingStop(arsId);
        List<Reservation> dropReservations = reservationService.findDetailOfDropStop(arsId);


        int totalReservedUsers = boardingReservations.size();
        int totalOffUsers = dropReservations.size();
        String busStopName = busStopService.getBusStopNameByArsId(arsId);
        boolean wheelchair = boardingReservations.stream().anyMatch(reservation -> reservation.getUser().isWheelchair());
        boolean blindness = boardingReservations.stream().anyMatch(reservation -> reservation.getUser().isBlindness());


        // DriverInfoBusStopDto 생성
        DriverInfoBusStopDto driverInfoBusStopDto =
                new DriverInfoBusStopDto(
                        arsId, busStopName, totalReservedUsers, totalOffUsers, wheelchair, blindness);
        System.out.println(driverInfoBusStopDto.toString());

        return driverInfoBusStopDto;
    }

    private String getElementValue(Element element, String tagName) {
        NodeList nodeList = element.getElementsByTagName(tagName).item(0).getChildNodes();
        Node node = nodeList.item(0);
        return node.getNodeValue();
    }
}
package combus.backend.dto;

import combus.backend.domain.BusStop;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BusPosDto {
    private long arsId;          // 현재 버스 정류소 ID
    private int stSeq;          // 현재 버스 정류소 순번
    private boolean stopFlag;   // 정류소 도착 여부

    public BusPosDto(long arsId, int stSeq, boolean stopFlag){
        this.arsId = arsId;
        this.stSeq = stSeq;
        this.stopFlag = stopFlag;
    }
}

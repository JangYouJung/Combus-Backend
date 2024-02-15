package combus.backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class VerifyBusNumberDto {
    private boolean isCorrect;

    public VerifyBusNumberDto(boolean isCorrect) {
        this.isCorrect = isCorrect;
    }

}

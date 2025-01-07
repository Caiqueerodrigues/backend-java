package webb_lanches.webb_lanches.Commons.DTO;

import java.time.LocalDate;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;

public record DateDTO(
    @NotBlank(message = "A data não pode estar vazia!")
    String data
) {

}

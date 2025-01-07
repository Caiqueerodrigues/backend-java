package webb_lanches.webb_lanches.Caixa.DTO;

import java.time.LocalDate;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;

public record GetPagamentos(
    @NotBlank(message = "A data não pode estar vazia!")
    String data
) {

}

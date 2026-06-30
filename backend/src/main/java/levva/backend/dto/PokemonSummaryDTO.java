package levva.backend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PokemonSummaryDTO {
    private Integer id;
    private String name;
    private String spriteUrl;
}

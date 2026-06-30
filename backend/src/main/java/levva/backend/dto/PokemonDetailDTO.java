package levva.backend.dto;


import java.util.List;

public record PokemonDetailDTO(
        Integer id,
        String name,
        String spriteUrl,
        List<String> types,
        List<StatDTO> stats,
        List<String> abilities
){}

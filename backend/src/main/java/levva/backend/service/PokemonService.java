package levva.backend.service;

import levva.backend.client.PokeApiClient;
import levva.backend.dto.PokemonDetailDTO;
import levva.backend.dto.PokemonSummaryDTO;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class PokemonService {

    private final PokeApiClient pokeApiClient;

    public PokemonService(PokeApiClient pokeApiClient) {
        this.pokeApiClient = pokeApiClient;
    }

    public Mono<List<PokemonSummaryDTO>> listPokemons(int limit, int offset) {
        return pokeApiClient.listPokemons(limit, offset);
    }

    public Mono<PokemonDetailDTO> getPokemonDetail(String name) {
        return pokeApiClient.getPokemonDetail(name);
    }
}

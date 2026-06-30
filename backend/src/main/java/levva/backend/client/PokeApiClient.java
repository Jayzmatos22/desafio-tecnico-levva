package levva.backend.client;


import levva.backend.dto.PokemonDetailDTO;
import levva.backend.dto.PokemonSummaryDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import levva.backend.dto.StatDTO;


import java.util.List;
import java.util.Map;

@Component
public class PokeApiClient {

    private final WebClient webClient;

    // Base url
    public PokeApiClient(WebClient.Builder builder) {
        this.webClient = builder
                .baseUrl("https://pokeapi.co/api/v2")
                .build();
    }

    // Lista de pokemons
    public Mono<List<PokemonSummaryDTO>> listPokemons(int limit, int offset) {
        return webClient.get()
                .uri("/pokemon?limit={limit}&offset={offset}", limit, offset)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> {
                    List<Map<String, String>> results = (List<Map<String, String>>) response.get("results");
                    return results.stream()
                            .map(p -> {
                                String url = p.get("url");
                                Integer id = extractIdFromUrl(url);
                                String sprite = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" + id + ".png";
                                return new PokemonSummaryDTO(id, p.get("name"), sprite);
                            })
                            .toList();
                });
    }

    public Mono<PokemonDetailDTO> getPokemonDetail(String name) {
        return webClient.get()
                .uri("/pokemon/{name}", name)
                .retrieve()
                .bodyToMono(Map.class)
                .map(data -> {
                    Integer id = (Integer) data.get("id");

                    // Extrai o ID e monta a URL do sprite diretamente.
                    String sprite = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" + id + ".png";

                    List<Map<String, Map<String, String>>> typesRaw = (List<Map<String, Map<String, String>>>) data.get("types");
                    List<String> types = typesRaw.stream()
                            .map(t -> t.get("type").get("name"))
                            .toList();

                    List<Map<String, Object>> statsRaw = (List<Map<String, Object>>) data.get("stats");
                    List<StatDTO> stats = statsRaw.stream()
                            .map(s -> new StatDTO(
                                    (String) ((Map<String, String>) s.get("stat")).get("name"),
                                    (Integer) s.get("base_stat")
                            ))
                            .toList();

                    List<Map<String, Map<String, String>>> abilitiesRaw = (List<Map<String, Map<String, String>>>) data.get("abilities");
                    List<String> abilities = abilitiesRaw.stream()
                            .map(a -> a.get("ability").get("name"))
                            .toList();

                    return new PokemonDetailDTO(id, (String) data.get("name"), sprite, types, stats, abilities);
                });
    }

    private Integer extractIdFromUrl(String url) {
        String[] parts = url.split("/");
        return Integer.parseInt(parts[parts.length - 1]);
    }
}

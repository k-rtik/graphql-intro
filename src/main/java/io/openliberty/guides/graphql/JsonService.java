package io.openliberty.guides.graphql;

import io.openliberty.guides.graphql.models.Artist;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JsonService {

    private static Logger logger = Logger.getLogger(JsonService.class.getName());
    private static Map<String, Artist> artistMap = new HashMap<>();

    private static void initialize() {
        try (InputStream inputStream = JsonService.class
                .getClassLoader()
                .getResourceAsStream("artists.json")) {

            // JSON-B to convert each Artist object in JSON object from file
            Jsonb jsonb = JsonbBuilder.create();

            // JSON-P to convert file to generic JSON object of artist name --> Artist
            JsonObject artistsJson = Json.createReader(inputStream)
                    .readObject();

            // Read from file
            for (String artistName : artistsJson.keySet()) {
                artistMap.put(artistName,
                        jsonb.fromJson(artistsJson.getJsonObject(artistName).toString(), Artist.class));
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Cannot initialize artists from JSON file");
        }
    }

    public static Map<String, Artist> getArtistMap() {
        if (artistMap.size() == 0) {
            initialize();
        }
        return new HashMap<>(artistMap);
    }
}

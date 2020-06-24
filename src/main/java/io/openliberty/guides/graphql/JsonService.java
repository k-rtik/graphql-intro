package io.openliberty.guides.graphql;

import io.openliberty.guides.graphql.models.Album;
import io.openliberty.guides.graphql.models.Artist;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

import java.io.IOException;
import java.io.InputStream;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JsonService {

    private static Logger logger = Logger.getLogger(JsonService.class.getName());

    private static List<Artist> artists;
    private static List<Album> albums;

    private static void initialize() {
        Jsonb jsonb = JsonbBuilder.create();

        try (InputStream artistStream = JsonService.class.getClassLoader().getResourceAsStream("artists.json");
        InputStream albumStream = JsonService.class.getClassLoader().getResourceAsStream("albums.json")) {
            // Read artists
            artists = jsonb.fromJson(artistStream,
                    new ArrayList<Artist>(){}.getClass().getGenericSuperclass());
            // Read albums
            albums = jsonb.fromJson(albumStream,
                    new ArrayList<Album>(){}.getClass().getGenericSuperclass());
        } catch (IOException e) {
            logger.severe("Cannot initialize artists from JSON file");
            logger.severe(Arrays.toString(e.getStackTrace()));
        }
    }

    public static List<Artist> getArtists() {
        if (artists == null || artists.size() == 0)
            initialize();

        Logger.getLogger("JsonService").info("Artists length: " + artists.size());

        List<Artist> artistsClone = new ArrayList<>(artists.size());
        artistsClone.addAll(artists);

        return artistsClone;
    }

    public static List<Album> getAlbums() {
        if (albums == null || albums.size() == 0)
            initialize();

        Logger.getLogger("JsonService").info("Albums length: " + albums.size());

        List<Album> albumsClone = new ArrayList<>(albums.size());
        albumsClone.addAll(albums);

        return albumsClone;
    }
}

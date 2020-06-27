package io.openliberty.guides.graphql;

import io.openliberty.guides.graphql.models.Album;
import io.openliberty.guides.graphql.models.Artist;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

import java.io.IOException;
import java.io.InputStream;

import java.util.*;
import java.util.logging.Logger;

public class JsonService {

    private static Logger logger = Logger.getLogger(JsonService.class.getName());

    private static Map<String, Artist> artists = new HashMap<>();
    private static Map<String, List<Album>> albums = new HashMap<>();

    public static class AlbumWrapper extends Album {
        private String artistName;

        public AlbumWrapper(){}

        public String getArtistName() {
            return artistName;
        }

        public void setArtistName(String artistName) {
            this.artistName = artistName;
        }
    }

    private static void initialize() {
        // Read JSON files
        Jsonb jsonb = JsonbBuilder.create();
        try (InputStream artistStream = JsonService.class.getClassLoader().getResourceAsStream("artists.json");
        InputStream albumStream = JsonService.class.getClassLoader().getResourceAsStream("albums.json")) {
            // Read artists
            List<Artist> artistsList = jsonb.fromJson(artistStream,
                    new ArrayList<Artist>(){}.getClass().getGenericSuperclass());
            // Read albums
            List<AlbumWrapper>albumsList = jsonb.fromJson(albumStream,
                    new ArrayList<AlbumWrapper>(){}.getClass().getGenericSuperclass());

            for (Artist artist: artistsList) {
                artists.put(artist.getName(), artist);
                albums.put(artist.getName(), new ArrayList<>());
            }

            for (AlbumWrapper album: albumsList) {
                albums.get(album.getArtistName()).add(album);
            }
        } catch (IOException e) {
            logger.severe("Cannot initialize artists from JSON file");
            logger.severe(Arrays.toString(e.getStackTrace()));
        }
    }

    public static Map<String, Artist> getArtists() {
        if (artists.size() == 0)
            initialize();

        Logger.getLogger("JsonService").info("Artists length: " + artists.size());
        return new HashMap<>(artists);
    }

    public static Map<String, List<Album>> getAlbums() {
        if (albums.size() == 0)
            initialize();

        Logger.getLogger("JsonService").info("Albums length: " + albums.size());

        return new HashMap<>(albums);
    }
}

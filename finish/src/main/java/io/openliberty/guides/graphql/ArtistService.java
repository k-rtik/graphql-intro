package io.openliberty.guides.graphql;

import io.openliberty.guides.graphql.models.Album;
import io.openliberty.guides.graphql.models.Artist;
import org.eclipse.microprofile.graphql.*;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import java.util.*;
import java.util.logging.Logger;

@GraphQLApi
@ApplicationScoped
public class ArtistService {

    private Logger logger = Logger.getLogger(getClass().getName());

    private Map<String, Artist> artists = new HashMap<>();
    private Map<String, List<Album>> albums = new HashMap<>();

    @PostConstruct
    public void initialize() {
       artists = JsonService.getArtists();
       albums = JsonService.getAlbums();
    }

    // Get one object
    // Query means top level
    @Query("artist")
    public Artist getArtist(@Name("name") String artistName) throws GraphQLException {

        if (!artists.containsKey(artistName) || !albums.containsKey(artistName)) {
            logger.severe("Cannot find " + artistName);
            throw new GraphQLException("Cannot find " + artistName);
        }

        Artist toReturn = artists.get(artistName);
        toReturn.setAlbums(albums.get(artistName));

        return toReturn;
    }

    // Get multiple objects with different properties returned for each
    @Query("artists")
    public List<Artist> getArtists(@Name("names") String[] artistNames) throws GraphQLException {
        List<Artist> toReturn = new ArrayList<>();
        List<String> missingArtists = new ArrayList<>();
        for (String artistName : artistNames) {
            try {
                toReturn.add(getArtist(artistName));
            } catch (GraphQLException e) {
                missingArtists.add(artistName);
            }
        }

        if (missingArtists.size() != 0) {
            String missingArtistNames = String.join(", ", missingArtists);
            logger.severe("Cannot find the following artists: " + missingArtistNames);
            throw new GraphQLException("Cannot find the following artists: " + missingArtistNames, toReturn);
        }
        return toReturn;
    }

    // Get a property that is not part of the object
    // Source makes return value part of schema of Source
    public int albumCount(@Source Artist artist) {
        return artist.getAlbums().size();
    }

    @Mutation
    public boolean addArtist(@Name("artist") Artist artist) throws GraphQLException {
        if (artists.containsKey(artist.getName()) || albums.containsKey(artist.getName())) {
            logger.severe("Artist already exists in map: " + artist.getName());
            throw new GraphQLException("Artist already exists in map: " + artist.getName());
        }
        try {
            artists.put(artist.getName(), artist);
            albums.put(artist.getName(), artist.getAlbums());
        } catch (Exception e) {
            logger.severe("Could not add artist " + artist.getName());
            logger.severe("Caused by: " + Arrays.toString(e.getStackTrace()));
            throw new GraphQLException("Could not add artist: " + artist.getName());
        }

        return true;
    }

    @Mutation
    public int reset() {
        int artistCount = artists.size();
        initialize();
        return artistCount - artists.size();
    }
}

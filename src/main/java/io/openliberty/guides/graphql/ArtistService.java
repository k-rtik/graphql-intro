package io.openliberty.guides.graphql;

import io.openliberty.guides.graphql.models.Artist;
import org.eclipse.microprofile.graphql.*;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.logging.Level;
import java.util.logging.Logger;

@GraphQLApi
@ApplicationScoped
public class ArtistService {

    private Logger logger = Logger.getLogger(getClass().getName());
    private Map<String, Artist> artistMap = new HashMap<>();

    @PostConstruct
    public void initialize() {
        artistMap = JsonService.getArtistMap();
    }

    // Get one object
    // Query means top level
    @Query("artist")
    public Artist getArtist(@Name("artist") String artistName) throws GraphQLException {
        if (!artistMap.containsKey(artistName)) {
            logger.log(Level.SEVERE, "Cannot find " + artistName);
            throw new GraphQLException("Cannot find " + artistName);
        }
        return artistMap.get(artistName);
    }

    // Get multiple objects with different properties returned for each
    @Query("artists")
    public List<Artist> getArtists(@Name("artists") String[] artistNames) throws GraphQLException {
        List<Artist> artists = new ArrayList<>();
        for (String artistName : artistNames) {
            if (!artistMap.containsKey(artistName)) {
                logger.log(Level.SEVERE, "Cannot find " + artistName);
                throw new GraphQLException("Cannot find " + artistName, artists);
            }
            artists.add(artistMap.get(artistName));
        }
        return artists;
    }

    // Get a property that is not part of the object
    // Source makes return value part of schema of Source
    public int albumCount(@Source @Name("artist") Artist artist) {
        return artist.getAlbums().size();
    }

    @Mutation("addArtist")
    public boolean addArtist(@Name("artist") Artist artist) throws GraphQLException {
        if (artistMap.containsKey(artist.getName())) {
            logger.log(Level.SEVERE, "Artist already exists in map: " + artist.getName());
            throw new GraphQLException("Artist already exists in map: " + artist.getName());
        }
        try {
            artistMap.put(artist.getName(), artist);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Could not add artist " + artist.getName() + ":", e);
            throw new GraphQLException("Could not add artist: " + artist.getName());
        }

        return true;
    }

    @Mutation
    public int reset() {
        int artistCount = artistMap.size();
        initialize();
        return artistCount;
    }
}

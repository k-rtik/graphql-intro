package io.openliberty.guides.graphql;

import io.openliberty.guides.graphql.models.Album;
import io.openliberty.guides.graphql.models.Artist;
import org.eclipse.microprofile.graphql.*;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import java.util.*;
import java.util.logging.Logger;

// tag::graphQLApi[]
@GraphQLApi
// end::graphQLApi[]
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
    // tag::getArtist[]
    // tag::queryAnnotationGetArtist[]
    @Query("artist")
    // end::queryAnnotationGetArtist[]
    // tag::nameAnnotationGetArtist[]
    public Artist getArtist(@Name("name") String artistName) throws GraphQLException {
    // end::nameAnnotationGetArtist[]
        // tag::graphQlExceptionGetArtist[]
        if (!artists.containsKey(artistName) || !albums.containsKey(artistName)) {
            logger.severe("Cannot find " + artistName);
            throw new GraphQLException("Cannot find " + artistName);
        }
        // end::graphQlExceptionGetArtist[]

        Artist toReturn = artists.get(artistName);
        toReturn.setAlbums(albums.get(artistName));

        return toReturn;
    }
    // end::getArtist[]

    // Get multiple objects with different properties returned for each
    // tag::getArtists[]
    // tag::queryAnnotationGetArtists[]
    @Query("artists")
    // end::queryAnnotationGetArtists[]
    // tag::nameAnnotationGetArtists[]
    public List<Artist> getArtists(@Name("names") String[] artistNames) throws GraphQLException {
    // end::nameAnnotationGetArtists[]
        List<Artist> toReturn = new ArrayList<>();

        List<String> missingArtists = new ArrayList<>();
        for (String artistName : artistNames) {
            try {
                toReturn.add(getArtist(artistName));
            } catch (GraphQLException e) {
                missingArtists.add(artistName);
            }
        }

        // tag::graphQlExceptionGetArtists[]
        if (missingArtists.size() != 0) {
            String missingArtistNames = String.join(", ", missingArtists);
            logger.severe("Cannot find the following artists: " + missingArtistNames);
            throw new GraphQLException("Cannot find the following artists: " + missingArtistNames, toReturn);
        }
        // end::graphQlExceptionGetArtists[]
        return toReturn;
    }
    // end::getArtists[]

    // Get a property that is not part of the object
    // Source makes return value part of schema of Source
    // tag::albumCount[]
    // tag::signatureAlbumCount[]
    public int albumCount(@Source Artist artist) {
    // end::signatureAlbumCount[]
        return artist.getAlbums().size();
    }
    // end::albumCount[]

    // tag::addArtist[]
    // tag::mutationAnnotationAddArtist[]
    @Mutation
    // end::mutationAnnotationAddArtist[]
    // tag::nameAnnotationAddArtist[]
    public boolean addArtist(@Name("artist") Artist artist) throws GraphQLException {
    // end::nameAnnotationAddArtist[]
        // tag::graphQlExceptionArtistExists[]
        if (artists.containsKey(artist.getName()) || albums.containsKey(artist.getName())) {
            logger.severe("Artist already exists in map: " + artist.getName());
            throw new GraphQLException("Artist already exists in map: " + artist.getName());
        }
        // end::graphQlExceptionArtistExists[]
        try {
            artists.put(artist.getName(), artist);
            albums.put(artist.getName(), artist.getAlbums());
        // tag::graphQlExceptionAddArtist[]
        } catch (Exception e) {
            logger.severe("Could not add artist " + artist.getName());
            logger.severe("Caused by: " + Arrays.toString(e.getStackTrace()));
            throw new GraphQLException("Could not add artist: " + artist.getName());
        }
        // tag::graphQlExceptionAddArtist[]

        return true;
    }
    // end::addArtist[]

    // tag::reset[]
    // tag::mutationAnnotationReset[]
    @Mutation
    // end::mutationAnnotationReset[]
    // tag::methodSignatureReset[]
    public int reset() {
    // end::methodSignatureReset[]
        int artistCount = artists.size();
        initialize();
        return artistCount - artists.size();
    }
    // end::reset[]
}

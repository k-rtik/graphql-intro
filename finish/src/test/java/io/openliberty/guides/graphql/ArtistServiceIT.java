package io.openliberty.guides.graphql;

import io.openliberty.guides.graphql.client.ArtistServiceAPI;
import io.openliberty.guides.graphql.client.models.ArtistWithAlbumCount;
import io.openliberty.guides.graphql.models.Album;
import io.openliberty.guides.graphql.models.Artist;
import io.smallrye.graphql.client.typesafe.api.GraphQlClientBuilder;
import io.smallrye.graphql.client.typesafe.api.GraphQlClientException;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ArtistServiceIT {

    private static ArtistServiceAPI artistServiceAPI;

    private static Map<String, Artist> expectedArtistMap;
    private static Map<String, List<Album>> expectedAlbumsMap;

    // tag::setupClientAndArtists[]
    @BeforeAll
    public static void setupClientAndArtists() {

        artistServiceAPI = GraphQlClientBuilder
                .newBuilder()
                .endpoint("http://localhost:9080/api/graphql")
                .build(ArtistServiceAPI.class);

        artistServiceAPI.reset();

        expectedArtistMap = JsonService.getArtists();
        expectedAlbumsMap = JsonService.getAlbums();
    }
    // end::setupClientAndArtists[]

    // tag::getArtist[]
    // tag::testAnnotationTestGetArtist[]
    @Test
    // end::testAnnotationTestGetArtist[]
    @Order(1)
    public void testGetArtist() {
        Artist drake = artistServiceAPI.getArtist("Drake");
        verifyArtist(drake, expectedArtistMap.get("Drake"));
        verifyAlbums(drake, expectedAlbumsMap.get("Drake"));
    }
    // end::getArtist[]

    // tag::getUnknownArtist[]
    // tag::testAnnotationTestGetUnknownArtist[]
    @Test
    // end::testAnnotationTestGetUnknownArtist[]
    @Order(2)
    public void testGetUnknownArtist() {
        Assertions.assertThrows(GraphQlClientException.class, () ->
                artistServiceAPI.getArtist("UnknownArtist"));
    }
    // tag::getUnknownArtist[]

    // tag::getArtists[]
    // tag::testAnnotationTestGetArtists[]
    @Test
    // end::testAnnotationTestGetArtists[]
    @Order(3)
    public void testGetArtists() {
        List<String> expectedArtistNames = Arrays.asList("Drake", "The Beatles", "Billie Holiday");
        List<Artist> artists = artistServiceAPI.getArtists(expectedArtistNames);
        Assertions.assertEquals(3, artists.size(),
                "Expected three artists to be returned, got " + artists.size());
        for (Artist artist: artists) {
            if (!expectedArtistNames.remove(artist.getName())) {
                Assertions.fail("Artist name " + artist.getName() + " not found in the list: " +
                        expectedArtistNames.toString());
            }
            verifyArtist(artist, expectedArtistMap.get(artist.getName()));
            verifyAlbums(artist, expectedAlbumsMap.get(artist.getName()));
        }
    }
    // end::getArtists[]

    // tag::getArtistWithAlbumCount[]
    // tag::testAnnotationTestGetArtistWithAlbumCount[]
    @Test
    // end::testAnnotationTestGetArtistWithAlbumCount[]
    @Order(4)
    public void testGetArtistWithAlbumCount() {
        ArtistWithAlbumCount rihanna = artistServiceAPI.getArtistWithAlbumCount("Rihanna");
        verifyArtist(rihanna, expectedArtistMap.get("Rihanna"));
        verifyAlbums(rihanna, expectedAlbumsMap.get("Rihanna"));
    }
    // end::getArtistWithAlbumCount[]

    // tag::addArtistMutation[]
    // tag::testAnnotationTestAddArtistMutation[]
    @Test
    //  end::testAnnotationTestAddArtistMutation[]
    @Order(5)
    public void testAddArtistMutation() {
        Artist newArtist = new Artist("New Artist", "Electronic", new ArrayList<>());
        Assertions.assertTrue(artistServiceAPI.addArtist(newArtist));

        List<ArtistWithAlbumCount> artistsWithAlbumCount = artistServiceAPI.getArtistsWithAlbumCounts(
                Arrays.asList("Drake", "The Beatles", "Billie Holiday", "New Artist"));

        for (ArtistWithAlbumCount artistWithAlbumCount : artistsWithAlbumCount) {
            if (artistWithAlbumCount.getName().equals(newArtist.getName())) {
                verifyArtist(artistWithAlbumCount, newArtist);
                verifyAlbums(artistWithAlbumCount, newArtist.getAlbums());
            } else {
                verifyArtist(artistWithAlbumCount, expectedArtistMap.get(artistWithAlbumCount.getName()));
                verifyAlbums(artistWithAlbumCount, expectedAlbumsMap.get(artistWithAlbumCount.getName()));
            }
        }

        Assertions.assertThrows(GraphQlClientException.class, () ->
                artistServiceAPI.addArtist(newArtist));
    }
    // end::addArtistMutation[]

    // tag::resetMutation[]
    // tag::testAnnotationTestResetMutation[]
    @Test
    // end::testAnnotationTestResetMutation[]
    @Order(6)
    public void testResetMutation() {
        int artistCount = artistServiceAPI.reset();
        Assertions.assertEquals(artistCount, 1);
    }
    // end::resetMutation[]

    // tag::verifyArtist[]
    private void verifyArtist(Artist actualArtist, Artist expectedArtist) {
        Assertions.assertEquals(expectedArtist.getName(), actualArtist.getName(),
                "Returned artist does not have the correct name");
        Assertions.assertEquals( expectedArtist.getGenres(), actualArtist.getGenres(),
                "Returned artist does not have the correct genres");
    }
    // end::verifyArtist[]

    // tag::verifyAlbums[]
    private void verifyAlbums(Artist actualArtist, List<Album> expectedAlbums) {
        Assertions.assertIterableEquals(expectedAlbums, actualArtist.getAlbums(),
                "Returned artist does not have the correct albums");
        if (actualArtist instanceof ArtistWithAlbumCount) {
            Assertions.assertEquals(expectedAlbums.size(),
                    ((ArtistWithAlbumCount) actualArtist).getAlbumCount(),
                    "Returned artist does not have the correct album count");
        }
    }
    // end::verifyAlbums[]
}

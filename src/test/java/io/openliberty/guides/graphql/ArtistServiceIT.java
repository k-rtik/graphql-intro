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

    @BeforeAll
    public static void setupClientAndArtists() {
        artistServiceAPI = GraphQlClientBuilder
                .newBuilder()
                .endpoint("http://localhost:9080/graphql-intro/graphql")
                .build(ArtistServiceAPI.class);
        artistServiceAPI.reset();

        expectedArtistMap = JsonService.getArtistMap();
    }

    @Test
    @Order(1)
    public void testGetArtist() {
        Artist drake = artistServiceAPI.getArtist("Drake");
        verifyArtist(drake, expectedArtistMap.get("Drake"));
    }

    @Test
    @Order(2)
    public void testGetUnknownArtist() {
        Assertions.assertThrows(GraphQlClientException.class, () ->
                artistServiceAPI.getArtist("UnknownArtist"));
    }

    @Test
    @Order(3)
    public void testGetArtists() {
        List<Artist> artists = artistServiceAPI.getArtists(
                Arrays.asList("Drake", "The Beatles", "Billie Holiday"));
        for (Artist artist: artists) {
            verifyArtist(artist, expectedArtistMap.get(artist.getName()));
        }
    }

    @Test
    @Order(4)
    public void testGetArtistWithAlbumCount() {
        ArtistWithAlbumCount rihanna = artistServiceAPI.getArtistWithAlbumCount("Rihanna");
        verifyArtist(rihanna, expectedArtistMap.get("Rihanna"));
    }

    @Test
    @Order(5)
    public void testAddArtistMutation() {
        Artist newArtist = new Artist("New Artist", "Electronic", new ArrayList<>());
        Assertions.assertTrue(artistServiceAPI.addArtist(newArtist));

        List<ArtistWithAlbumCount> artistsWithAlbumCount = artistServiceAPI.getArtistsWithAlbumCounts(
                Arrays.asList("Drake", "The Beatles", "Billie Holiday", "New Artist"));

        for (ArtistWithAlbumCount artistWithAlbumCount : artistsWithAlbumCount) {
            if (artistWithAlbumCount.getName().equals(newArtist.getName()))
                verifyArtist(artistWithAlbumCount, newArtist);
            else
                verifyArtist(artistWithAlbumCount, expectedArtistMap.get(artistWithAlbumCount.getName()));
        }

        Assertions.assertThrows(GraphQlClientException.class, () ->
                artistServiceAPI.addArtist(newArtist));
    }

    @Test
    @Order(6)
    public void testResetMutation() {
        int artistCount = artistServiceAPI.reset();
        Assertions.assertEquals(artistCount, expectedArtistMap.size() + 1);
    }

    private void verifyArtist(Artist actualArtist, Artist expectedArtist) {
        Assertions.assertEquals(expectedArtist.getName(), actualArtist.getName(),
                "Returned artist does not have the correct name");
        Assertions.assertEquals( expectedArtist.getGenres(), actualArtist.getGenres(),
                "Returned artist does not have the correct genres");
        Assertions.assertIterableEquals(actualArtist.getAlbums(), expectedArtist.getAlbums(),
                "Returned artist does not have the correct albums");

        if (actualArtist instanceof ArtistWithAlbumCount) {
            Assertions.assertEquals(expectedArtist.getAlbums().size(),
                    ((ArtistWithAlbumCount) actualArtist).getAlbumCount(),
                    "Returned artist does not have the correct album count");
        }
    }
}

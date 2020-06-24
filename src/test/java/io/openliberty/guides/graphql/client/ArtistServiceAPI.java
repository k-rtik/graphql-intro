package io.openliberty.guides.graphql.client;

import io.openliberty.guides.graphql.client.models.ArtistWithAlbumCount;
import io.openliberty.guides.graphql.models.Artist;
import io.smallrye.graphql.client.typesafe.api.GraphQlClientApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Name;
import org.eclipse.microprofile.graphql.Query;

import java.util.List;

@GraphQlClientApi
public interface ArtistServiceAPI {
    @Query("artist")
    Artist getArtist(@Name("name") String artistName);

    @Query("artist")
    ArtistWithAlbumCount getArtistWithAlbumCount(@Name("name") String artistName);

    @Query("artists")
    List<Artist> getArtists(@Name("artists") List<String> artistNames);

    @Query("artists")
    List<ArtistWithAlbumCount> getArtistsWithAlbumCounts(@Name("artists") List<String> artistNames);

    @Mutation("addArtist")
    boolean addArtist(@Name("artist") Artist artist);

    @Mutation
    int reset();
}

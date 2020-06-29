package io.openliberty.guides.graphql.client;

import io.openliberty.guides.graphql.client.models.ArtistWithAlbumCount;
import io.openliberty.guides.graphql.models.Artist;
import io.smallrye.graphql.client.typesafe.api.GraphQlClientApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Name;
import org.eclipse.microprofile.graphql.Query;

import java.util.List;

// tag::artistServiceApi[]
@GraphQlClientApi
public interface ArtistServiceAPI {
    // tag::getArtist[]
    @Query("artist")
    Artist getArtist(@Name("name") String artistName);
    // end::getArtist[]

    // tag::getArtistWithAlbumCount[]
    @Query("artist")
    ArtistWithAlbumCount getArtistWithAlbumCount(@Name("name") String artistName);
    // end::getArtistWithAlbumCount[]

    // tag::getArtists[]
    @Query("artists")
    List<Artist> getArtists(@Name("names") List<String> artistNames);
    // end::getArtists[]

    // tag::getArtistsWithAlbumCount[]
    @Query("artists")
    List<ArtistWithAlbumCount> getArtistsWithAlbumCounts(@Name("names") List<String> artistNames);
    // end::getArtistsWithAlbumCount[]

    // tag::addArtist[]
    @Mutation("addArtist")
    boolean addArtist(@Name("artist") Artist artist);
    // end::addArtist[]

    // tag::reset[]
    @Mutation("reset")
    int reset();
    // end::reset[]
}
// end::artistServiceApi[]

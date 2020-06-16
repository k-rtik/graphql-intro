package io.openliberty.guides.graphql.client.models;

import io.openliberty.guides.graphql.models.Album;
import io.openliberty.guides.graphql.models.Artist;

import java.util.List;

public class ArtistWithAlbumCount extends Artist {
    private int albumCount;

    public ArtistWithAlbumCount() {}

    public ArtistWithAlbumCount(String name, String genres, int albumCount, List<Album> albums) {
        super(name, genres, albums);
        this.albumCount = albumCount;
    }

    public int getAlbumCount() {
        return albumCount;
    }

    public void setAlbumCount(int albumCount) {
        this.albumCount = albumCount;
    }
}

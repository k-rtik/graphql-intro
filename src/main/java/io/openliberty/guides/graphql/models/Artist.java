package io.openliberty.guides.graphql.models;

import javax.validation.constraints.NotNull;
import java.util.List;

public class Artist {
    @NotNull
    private String name;

    @NotNull
    private String genres;

    @NotNull
    private List<Album> albums;

    public Artist() {}

    public Artist(String name, String genres, List<Album> albums) {
        this.name = name;
        this.genres = genres;
        this.albums = albums;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public List<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(List<Album> albums) {
        this.albums = albums;
    }
}

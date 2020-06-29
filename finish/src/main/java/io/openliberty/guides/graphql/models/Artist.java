package io.openliberty.guides.graphql.models;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class Artist {
    // tag::attributes[]
    // tag::notNull[]
    @NotNull
    private String name;
    // end::notNull[]
    // tag::notNull[]
    @NotNull
    private String genres;
    // end::notNull[]
    private List<Album> albums;
    // end::attributes[]

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Artist artist = (Artist) o;

        if (getName() != null ? !getName().equals(artist.getName()) : artist.getName() != null) return false;
        if (getGenres() != null ? !getGenres().equals(artist.getGenres()) : artist.getGenres() != null) return false;
        return getAlbums() != null ? getAlbums().equals(artist.getAlbums()) : artist.getAlbums() == null;

    }

    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result + (getGenres() != null ? getGenres().hashCode() : 0);
        result = 31 * result + (getAlbums() != null ? getAlbums().hashCode() : 0);
        return result;
    }
}

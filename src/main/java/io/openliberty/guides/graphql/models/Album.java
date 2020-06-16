package io.openliberty.guides.graphql.models;

import org.eclipse.microprofile.graphql.Name;

import javax.json.bind.annotation.JsonbProperty;

public class Album {
    private String title;
    private String year;

    @Name("ntracks")
    private int trackCount;

    public Album() {}

    public Album(String title, String year, int trackCount) {
        this.title = title;
        this.year = year;
        this.trackCount = trackCount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public int getTrackCount() {
        return trackCount;
    }

    @JsonbProperty("ntracks")
    public void setTrackCount(int trackCount) {
        this.trackCount = trackCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Album album = (Album) o;

        if (getTrackCount() != album.getTrackCount()) return false;
        if (!getTitle().equals(album.getTitle())) return false;
        return getYear().equals(album.getYear());
    }

    @Override
    public int hashCode() {
        int result = getTitle().hashCode();
        result = 31 * result + getYear().hashCode();
        result = 31 * result + getTrackCount();
        return result;
    }
}

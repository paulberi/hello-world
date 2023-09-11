package entities;

public class Song {
    private int songId;
    private String title;
    private int artistId;
    private int duration;
    private String genre;
    private String description;

    public Song() {
        // Weee
    }

    public Song(int songId, String title, int artistId, int duration) {
        this.songId = songId;
        this.title = title;
        this.artistId = artistId;
        this.duration = duration;
    }

    public Song(String title, int artistId, int duration) {
        this.title = title;
        this.artistId = artistId;
        this.duration = duration;
    }

    public int getSongId() {
        return songId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getArtistId() {
        return artistId;
    }

    public void setArtistId(int artistId) {
        this.artistId = artistId;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Song{" + "songId=" + songId + ", title='" + title + '\'' + ", artistId='" + artistId + '\''
                + ", duration='" + duration + '\'' + '}';
    }
}

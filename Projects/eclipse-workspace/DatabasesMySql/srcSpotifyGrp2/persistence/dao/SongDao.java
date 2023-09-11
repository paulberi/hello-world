package persistence.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import entities.Song;
import persistence.ConnectionFactory;

public class SongDao implements Dao<Song> {

    @Override
    public int create(Song song) throws SQLException {
        Connection connection = ConnectionFactory.getConnection();
        PreparedStatement statement = connection
                .prepareStatement("insert into song(title, artist_id, duration, genre, description) values(?,?,?,?,?)");
        statement.setString(1, song.getTitle());
        statement.setInt(2, song.getArtistId());
        statement.setInt(3, song.getDuration());
        statement.setString(4, song.getGenre());
        statement.setString(5, song.getDescription());
        return statement.executeUpdate();
    }

    @Override
    public List<Song> findAll() throws SQLException {
        Connection connection = ConnectionFactory.getConnection();
        PreparedStatement statement = connection.prepareStatement("select * from song");
        ResultSet rs = statement.executeQuery();
        List<Song> songs = new ArrayList<>();
        while (rs.next()) {
            Song song = new Song();
            song.setSongId(rs.getInt("song_id"));
            song.setTitle(rs.getString("title"));
            song.setArtistId(rs.getInt("artist_id"));
            song.setDuration(rs.getInt("duration"));
            song.setGenre(rs.getString("genre"));
            song.setDescription(rs.getString("description"));
            songs.add(song);
        }
        return songs;
    }

    @Override
    public Song find(Object id) throws SQLException {
        Connection connection = ConnectionFactory.getConnection();
        PreparedStatement statement = connection.prepareStatement("select * from song where song_id = ?");
        statement.setInt(1, (int) id);
        ResultSet rs = statement.executeQuery();
        Song song = null;
        if (rs.next()) {
            song = new Song();
            song.setSongId(rs.getInt("song_id"));
            song.setTitle(rs.getString("title"));
            song.setArtistId(rs.getInt("artist_id"));
            song.setDuration(rs.getInt("duration"));
            song.setGenre(rs.getString("genre"));
            song.setDescription(rs.getString("description"));
        }
        return song;
    }

    @Override
    public int update(Song song) throws SQLException {
        Connection connection = ConnectionFactory.getConnection();
        PreparedStatement statement = connection.prepareStatement(
                "update song set title=?, artist_id=?, duration=?, genre=?, description=? where song_id = ?");
        statement.setString(1, song.getTitle());
        statement.setInt(2, song.getArtistId());
        statement.setInt(3, song.getDuration());
        statement.setString(4, song.getGenre());
        statement.setString(5, song.getDescription());
        statement.setInt(6, song.getSongId());
        return statement.executeUpdate();
    }

    @Override
    public int delete(Song song) throws SQLException {
        Connection connection = ConnectionFactory.getConnection();
        PreparedStatement statement = connection.prepareStatement("delete from song where song_id = ?");
        statement.setInt(1, song.getSongId());
        return statement.executeUpdate();
    }

}

package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

import entities.Song;
import persistence.dao.SongDao;

public class Main {

    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws SQLException, IOException {

        SongDao songDao = new SongDao();

        String input = "";

        while (!input.equals("exit")) {
            System.out.println("What you want to do?");
            input = reader.readLine();
            input = input.toLowerCase();
            switch (input) {
                case "list":
                    for (Song song : songDao.findAll()) {
                        System.out.println(song.getSongId() + " | " + song.getTitle() + " | " + song.getArtistId()
                                + " | " + song.getDuration() + " | " + song.getGenre() + " | " + song.getDescription());
                    }
                    break;
                case "find":
                    System.out.println("What song_id?");
                    int id = Integer.parseInt(reader.readLine());
                    Song song = songDao.find(id);
                    System.out.println(song.getSongId() + " | " + song.getTitle() + " | " + song.getArtistId() + " | "
                            + song.getDuration() + " | " + song.getGenre() + " | " + song.getDescription());
                    break;
                case "add":
                    System.out.println("What title?");
                    String title = reader.readLine();
                    System.out.println("What artist_id?");
                    int artistId = Integer.parseInt(reader.readLine());
                    System.out.println("What duration?");
                    int duration = Integer.parseInt(reader.readLine());
                    songDao.create(new Song(title, artistId, duration));
                    break;
                default:
                    System.out.println("Invalid input");
                    break;
            }
        }
    }
}


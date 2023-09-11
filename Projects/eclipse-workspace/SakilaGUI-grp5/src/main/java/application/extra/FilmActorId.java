package application.extra;

import java.io.Serializable;

import application.entities.Actor;
import application.entities.Film;

public class FilmActorId implements Serializable {

    private static final long serialVersionUID = 1L;

    private Film film;

    private Actor actor;

    public FilmActorId() {
        // Tom
    }

    public FilmActorId(Film film, Actor actor) {
        this.film = film;
        this.actor = actor;
    }

    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }

    public Actor getActor() {
        return actor;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((film == null) ? 0 : film.hashCode());
        result = prime * result + ((actor == null) ? 0 : actor.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        FilmActorId other = (FilmActorId) obj;
        if (film == null) {
            if (other.film != null) {
                return false;
            }
        } else if (!film.equals(other.film)) {
            return false;
        }
        if (actor == null) {
            if (other.actor != null) {
                return false;
            }
        } else if (!actor.equals(other.actor)) {
            return false;
        }
        return true;
    }

}

package application.extra;

import java.io.Serializable;

import application.entities.Category;
import application.entities.Film;

public class FilmCategoryId implements Serializable {

    private static final long serialVersionUID = 1L;

    private Film film;

    private Category category;

    public FilmCategoryId() {
        // Tom
    }

    public FilmCategoryId(Film film, Category category) {
        this.film = film;
        this.category = category;
    }

    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((film == null) ? 0 : film.hashCode());
        result = prime * result + ((category == null) ? 0 : category.hashCode());
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
        FilmCategoryId other = (FilmCategoryId) obj;
        if (film == null) {
            if (other.film != null) {
                return false;
            }
        } else if (!film.equals(other.film)) {
            return false;
        }
        if (category == null) {
            if (other.category != null) {
                return false;
            }
        } else if (!category.equals(other.category)) {
            return false;
        }
        return true;
    }
}
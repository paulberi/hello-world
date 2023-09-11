package application.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.UpdateTimestamp;

import application.extra.GetId;
import application.extra.Rating;
import application.extra.RatingAttributeConverter;

@Entity
@Table(name = "film")
public class Film implements GetId, Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "film_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "release_year")
    private int releaseYear;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "language_id")
    private Language language;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "original_language_id")
    private Language originalLanguage;

    @Column(name = "rental_duration")
    private int rentalDuration;

    @Column(name = "rental_rate")
    private double rentalRate;

    @Column(name = "length")
    private int length;

    @Column(name = "replacement_cost")
    private double replacementCost;

    @Column(name = "rating")
    @Convert(converter = RatingAttributeConverter.class)
    private Rating rating;

    @Column(name = "special_features")
    private String specialFeatures;

    @Column(name = "last_update")
    @UpdateTimestamp
    private LocalDateTime lastUpdate;
    
    /*
     * TEST TEST TEST TEST TEST TES
     */
    @JoinTable(name = "film_actor",
            joinColumns = @JoinColumn(name = "film_id"),
            inverseJoinColumns = @JoinColumn(name = "actor_id")
    )
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Actor> filmActor;
    
    public List<Actor> getFilmActor() {
		return filmActor;
	}

	public void setFilmActor(List<Actor> filmActor) {
		this.filmActor = filmActor;
	}

	@JoinTable(name = "film_category",
	joinColumns = @JoinColumn(name = "category_id"),
	inverseJoinColumns = @JoinColumn(name = "film_id")
			)
	@ManyToMany(fetch = FetchType.LAZY)
	private List<Category> filmCategory;
	public List<Category> getFilmCategory() {
		return filmCategory;
	}

	public void setFilmCategory(List<Category> filmCategory) {
		this.filmCategory = filmCategory;
	}
    /*
     * SLUT PÅ TEST SLUT PÅ TEST SLUT PÅ TEST
     */

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Language getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(Language originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public int getRentalDuration() {
        return rentalDuration;
    }

    public void setRentalDuration(int rentalDuration) {
        this.rentalDuration = rentalDuration;
    }

    public double getRentalRate() {
        return rentalRate;
    }

    public void setRentalRate(double rentalRate) {
        this.rentalRate = rentalRate;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public double getReplacementCost() {
        return replacementCost;
    }

    public void setReplacementCost(double replacementCost) {
        this.replacementCost = replacementCost;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getSpecialFeatures() {
        return specialFeatures;
    }

    public void setSpecialFeatures(String specialFeatures) {
        this.specialFeatures = specialFeatures;
    }
    
    @Override
    public String toString() {
    	return title;
    }
}
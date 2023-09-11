package application.entities;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.UpdateTimestamp;

import application.extra.GetId;


@Entity
@Table(name = "country")
public class Country implements GetId {

    @Id
    @Column(name = "country_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "country")
    private String countryName;

    @Column(name = "last_update")
    @UpdateTimestamp
    private LocalDateTime lastUpdate;

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the country_name
     */
    public String getCountryName() {
        return countryName;
    }

    /**
     * @param countryName the country_name to set
     */
    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    /**
     * @return the lastUpdate
     */
    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    /**
     * @param lastUpdate the lastUpdate to set
     */
    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
    
    @Override
    public String toString() {
    	return countryName;
    }

}

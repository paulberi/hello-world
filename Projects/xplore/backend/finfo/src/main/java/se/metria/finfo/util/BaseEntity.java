package se.metria.finfo.util;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity<T extends Serializable> {
    @Id
    @GeneratedValue
    private T id;

    /* Jag följer Vlad Mihaelca senpai
    * https://vladmihalcea.com/hibernate-facts-equals-and-hashcode/*/
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof BaseEntity)) {
            return false;
        }

        var other = (BaseEntity) o;

        return id != null && id.equals(other);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

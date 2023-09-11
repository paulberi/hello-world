package se.metria.mapcms.commons.utils;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.text.Normalizer;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;


/**
 * Används för att ta bort whitespace och att göra inskickat slug läsvängligt
 * **/
public class SlugGenerator {

    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");
    private static final Pattern EDGESDHASHES = Pattern.compile("(^-|-$)");



    public static String toSlug(String input) {
        String nowhitespace = WHITESPACE.matcher(input).replaceAll("-");
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
        String slug = NONLATIN.matcher(normalized).replaceAll("");
        slug = EDGESDHASHES.matcher(slug).replaceAll("");
        return slug.toLowerCase(Locale.ENGLISH);
    }

    public static <T> T getObjectWithSlug(Class<T> t,String slug){
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        CriteriaBuilder cb = session.getCriteriaBuilder();

        CriteriaQuery<T> cr = cb.createQuery(t);
        Root<T> root = cr.from(t);
        cr.select(root).where(cb.equal(root.get("slug"), slug));  //here you pass a class field, not a table column (in this example they are called the same)

        Query query = session.createQuery(cr);
        query.setMaxResults(1);
        List<T> resultList = query.getResultList();

        T result = resultList.get(0);

        return result;
    }
}

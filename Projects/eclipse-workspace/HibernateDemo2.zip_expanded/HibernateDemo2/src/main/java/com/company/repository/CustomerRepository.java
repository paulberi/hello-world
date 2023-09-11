package com.company.repository;

import com.company.entities.Address;
import com.company.entities.Customer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import javax.persistence.criteria.*;
import java.util.List;
import java.util.Queue;

public class CustomerRepository {

    private SessionFactory sessionFactory;

    public CustomerRepository() {
        setup();
    }

    // Configuration, setup and shutdown
    public void setup() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()
                .loadProperties("hibernate.cfg.xml")
                .build();
        sessionFactory = new Configuration().buildSessionFactory(registry);
    }

    public void shutdown() {
        sessionFactory.close();
    }

    // CRUD
    private static <T> List<T> loadAllData(Class<T> type, Session session) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<T> criteria = builder.createQuery(type);
        criteria.from(type);
        List<T> data = session.createQuery(criteria).getResultList();
        return data;
    }

    public void create(Customer customer) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        for(Address address : customer.getAddresses()) {
            address.setCustomer(customer);
        }

        session.save(customer);

        session.getTransaction().commit();
        session.close();
    }

    public List<Customer> findAll() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        List<Customer> customers = loadAllData(Customer.class, session);

        session.getTransaction().commit();
        session.close();
        return customers;
    }

    public Customer getById(long id) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Customer customer = session.get(Customer.class, id);

        session.getTransaction().commit();
        session.close();

        return customer;
    }

    public void update(Customer customer) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        session.update(customer);

        session.getTransaction().commit();
        session.close();
    }

    public void delete(Customer customer) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        session.delete(customer);

        session.getTransaction().commit();
        session.close();
    }

    // Additional methods
    public List<Customer> getByFirstName(String name) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Customer> criteriaQuery = builder.createQuery(Customer.class);
        Root<Customer> root = criteriaQuery.from(Customer.class);

        criteriaQuery.select(root).where(builder.equal(root.get("firstName"), name));
        Query<Customer> query = session.createQuery(criteriaQuery);

        List<Customer> customers = query.getResultList();

        session.getTransaction().commit();
        session.close();

        return customers;
    }

    public List<Customer> getCustomerByFirstnameWithLastname(String firstName) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Customer> criteriaQuery = builder.createQuery(Customer.class);
        Root<Customer> root = criteriaQuery.from(Customer.class);

        Predicate predicate1 = builder.equal(root.get("firstName"), firstName);
        Predicate predicate2 = builder.isNotNull(root.get("lastName"));

        criteriaQuery.select(root).where(builder.and(predicate1, predicate2));

        Query<Customer> query = session.createQuery(criteriaQuery);
        List<Customer> customers = query.getResultList();

        session.getTransaction().commit();
        session.close();

        return customers;
    }


    public void selectiveUpdate(String name) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaUpdate<Customer> criteriaUpdate = builder.createCriteriaUpdate(Customer.class);
        Root<Customer> root = criteriaUpdate.from(Customer.class);

        criteriaUpdate.set("lastName", name);
        criteriaUpdate.where(builder.isNull(root.get("lastName")));

        session.createQuery(criteriaUpdate).executeUpdate();

        session.getTransaction().commit();
        session.close();
    }
}

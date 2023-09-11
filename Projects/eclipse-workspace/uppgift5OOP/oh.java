package HibernateDemo1.src.main.java.com.company;

import com.company.entities.Address;
import com.company.entities.Customer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.ArrayList;
import java.util.List;


public class Program {

    private SessionFactory sessionFactory;
    

    public static void main(String[] args) {
        Program program = new Program();
        program.setup();

        // Create customer
        Customer customer1 = new Customer("Anna", "Al");
        List<HibernateDemo1.src.main.java.com.company.entities.Address> addresses = new ArrayList<Address>();
        addresses.add(new HibernateDemo1.src.main.java.com.company.entities.Address("Storgatan", 16));
        addresses.add(new HibernateDemo1.src.main.java.com.company.entities.Address("Byvägen", 3));
        customer1.setAddresses(addresses);
        Customer customer2 = new Customer("Urban", "Ek");
        program.create(customer1);
        program.create(customer2);

        // Get all customers
        List<Customer> customers = program.findAll();
        for(Customer c : customers) {
            System.out.println(c.getFirstName() + " " + c.getLastName());
        }

        // Read customer by id
        Customer customer = program.getById(1);

        // Update customer
        customer.setLastName("Johansson");
        program.update(customer);

        // Delete customer
        program.delete(customer);

        program.shutdown();
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


}

package application.core;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import application.dao.Dao;
import application.dao.HibernateDao;
import application.entities.Actor;
import application.entities.Address;
import application.entities.Category;
import application.entities.City;
import application.entities.Country;
import application.entities.Customer;
import application.entities.Film;
import application.entities.FilmActor;
import application.entities.FilmCategory;
import application.entities.Inventory;
import application.entities.Language;
import application.entities.Payment;
import application.entities.Rental;
import application.entities.Staff;
import application.entities.Store;

public class DaoFactory {

    private HibernateDao<Category> categoryDao;
    private HibernateDao<Language> languageDao;
    private HibernateDao<Actor> actorDao;
    private HibernateDao<Film> filmDao;
    private HibernateDao<FilmActor> filmActorDao;
    private HibernateDao<FilmCategory> filmCategoryDao;
    private HibernateDao<Country> countryDao;
    private HibernateDao<Staff> staffDao;
    private HibernateDao<Store> storeDao;
    private HibernateDao<Customer> customerDao;
    private HibernateDao<City> cityDao;
    private HibernateDao<Address> addressDao;
    private HibernateDao<Inventory> inventoryDao;
    private HibernateDao<Payment> paymentDao;
    private HibernateDao<Rental> rentalDao;

    private Session session;

    private static DaoFactory daoFactory;

    private DaoFactory() {
    }

    public static DaoFactory getInstance() {
        if (daoFactory == null) {
            daoFactory = new DaoFactory();
        }
        return daoFactory;
    }

    // // HashMap med HibernateDaos
    // private Map<Class<?>, HibernateDao<?>> daoMap = new HashMap<>();
    //
    // // och generisk get metod
    // public HibernateDao<Object> getDao(Class<?> clazz) {
    // daoMap.putIfAbsent(clazz, new HibernateDao<>(clazz));
    // return (HibernateDao<Object>) daoMap.get(clazz);
    // }

    public Session getSession() {
        if (session == null) {
            StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure()
                    .loadProperties("hibernate.cfg.xml").build();
            SessionFactory sessionFactory = new Configuration().buildSessionFactory(registry);
            session = sessionFactory.openSession();
        }
        return session;
    }

    public void endSession() {
        session.close();
    }

    public Dao<Category> getCategoryDao() {
        if (categoryDao == null) {
            categoryDao = new HibernateDao<>(Category.class);
        }
        return categoryDao;
    }

    public Dao<Language> getLanguageDao() {
        if (languageDao == null) {
            languageDao = new HibernateDao<>(Language.class);
        }
        return languageDao;
    }

    public Dao<Actor> getActorDao() {
        if (actorDao == null) {
            actorDao = new HibernateDao<>(Actor.class);
        }
        return actorDao;
    }

    public Dao<Film> getFilmDao() {
        if (filmDao == null) {
            filmDao = new HibernateDao<>(Film.class);
        }
        return filmDao;
    }

    public Dao<FilmActor> getFilmActorDao() {
        if (filmActorDao == null) {
            filmActorDao = new HibernateDao<>(FilmActor.class);
        }
        return filmActorDao;
    }

    public Dao<FilmCategory> getFilmCategoryDao() {
        if (filmCategoryDao == null) {
            filmCategoryDao = new HibernateDao<>(FilmCategory.class);
        }
        return filmCategoryDao;
    }

    public Dao<Country> getCountryDao() {
        if (countryDao == null) {
            countryDao = new HibernateDao<>(Country.class);
        }
        return countryDao;
    }

    public Dao<Staff> getStaffDao() {
        if (staffDao == null) {
            staffDao = new HibernateDao<>(Staff.class);
        }
        return staffDao;
    }

    public Dao<Customer> getCustomerDao() {
        if (customerDao == null) {
            customerDao = new HibernateDao<>(Customer.class);
        }
        return customerDao;
    }

    public Dao<Store> getStoreDao() {
        if (storeDao == null) {
            storeDao = new HibernateDao<>(Store.class);
        }
        return storeDao;
    }

    public Dao<City> getCityDao() {
        if (cityDao == null) {
            cityDao = new HibernateDao<>(City.class);
        }
        return cityDao;
    }

    public Dao<Address> getAddressDao() {
        if (addressDao == null) {
            addressDao = new HibernateDao<>(Address.class);
        }
        return addressDao;
    }

    public Dao<Inventory> getInventoryDao() {
        if (inventoryDao == null) {
            inventoryDao = new HibernateDao<>(Inventory.class);
        }
        return inventoryDao;
    }

    public Dao<Payment> getPaymentDao() {
        if (paymentDao == null) {
            paymentDao = new HibernateDao<>(Payment.class);
        }
        return paymentDao;
    }

    public Dao<Rental> getRentalDao() {
        if (rentalDao == null) {
            rentalDao = new HibernateDao<>(Rental.class);
        }
        return rentalDao;
    }
}
package cg.wbd.grandemonstration.service;

import cg.wbd.grandemonstration.model.Customer;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.Collections;
import java.util.List;

public class HibernateCustomerServiceImpl implements CustomerService {
    private static SessionFactory sessionFactory;
    private static EntityManager entityManager;


    static {
        try {
            sessionFactory = new Configuration()
                    .configure("hibernate.conf.xml")
                    .buildSessionFactory();
            entityManager = sessionFactory.createEntityManager();


        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Customer> findAll() {
        String queryStr = "select c from Customer  AS c";
        TypedQuery<Customer> query = entityManager.createQuery(queryStr, Customer.class);
        try {
            return query.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Customer findOne(Long id) {
        String queryStr = "select c from Customer AS c where c.id= :id";
        TypedQuery<Customer> query = entityManager.createQuery(queryStr, Customer.class);
        query.setParameter("id", id);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public void delete(Long id) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            Customer origin = findOne(id);
            session.delete(origin);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }

    }
    @Override
    public void create(Customer customer){
        Session session=null;
        Transaction transaction=null;
        try {
            session=sessionFactory.openSession();
            transaction=session.beginTransaction();
            session.save(customer);
            transaction.commit();
        }catch (Exception e){
            e.printStackTrace();
            if (transaction!=null){
                transaction.rollback();
            }
        }finally {
            if (session!=null){
                session.close();
            }
        }
    }

    @Override
    public Customer save(Customer customer) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            Customer origin = findOne(customer.getId());
            origin.setName(customer.getName());
            origin.setEmail(customer.getEmail());
            origin.setAddress(customer.getAddress());
            session.saveOrUpdate(origin);
            transaction.commit();
            return origin;
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return null;
    }


//
//    @Override
//    public List<Customer> save(List<Customer> customers) {
//        return null;
//    }

    @Override
    public boolean exists(Long id) {
        return false;
    }

    @Override
    public List<Customer> findAll(List<Long> ids) {
        return Collections.emptyList();
    }

    @Override
    public long count() {
        return 0;
    }


    @Override
    public void delete(Customer customer) {

    }

    @Override
    public void delete(List<Customer> customers) {

    }

    @Override
    public void deleteAll() {

    }
}

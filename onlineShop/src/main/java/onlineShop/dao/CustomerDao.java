
package onlineShop.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import onlineShop.model.Authorities;
import onlineShop.model.Customer;
import onlineShop.model.User;

@Repository
public class CustomerDao {

    @Autowired
    private SessionFactory sessionFactory;

    public void addCustomer(Customer customer) {
        // add customer to authorities
        Authorities authorities = new Authorities();
        authorities.setAuthorities("ROLE_USER");
        authorities.setEmailId(customer.getUser().getEmailId());
        Session session = null;

        try {
            //create session fo db management
            session = sessionFactory.openSession();
            //Begin a unit of work and return the associated Transaction object.
            session.beginTransaction();
            //save() and persist() result in an SQL INSERT,
            // delete() in an SQL DELETE and update() or merge() in an SQL UPDATE
            session.save(authorities);
            session.save(customer);
            //write changes to db
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
            //close session
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public Customer getCustomerByUserName(String userName) {
        User user = null;
        try (Session session = sessionFactory.openSession()) {
            //create criteria for persistent class "users"
            Criteria criteria = session.createCriteria(User.class);
            //add() method available for Criteria object to add restriction for a criteria query
            user = (User) criteria.add(Restrictions.eq("emailId", userName)).uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (user != null)
            return user.getCustomer();
        return null;
    }
}


package cz.muni.pa165.surrealtravel.dao;

import cz.muni.pa165.surrealtravel.entity.Account;
import cz.muni.pa165.surrealtravel.entity.Customer;
import java.util.List;
import java.util.Objects;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

/**
 * Account repository implementation.
 * @author Jan Klimeš [374259]
 */
@Repository(value = "accountDao")
public class JPAAccountDAO implements AccountDAO {

    @PersistenceContext(name = "Surreal-Travel")
    private EntityManager em;

    public EntityManager getEntityManager() {
        return em;
    }

    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    @Override
    public void addAccount(Account account) {
        validate(account);
        em.persist(account);
    }

    @Override
    public Account getAccountById(long id) {
        if(id < 0) throw new IllegalArgumentException("Id < 0");
        return em.find(Account.class, id);
    }

    @Override
    public Account getAccountByUsername(String username) {
        Objects.requireNonNull(username, "Username is null.");
        if(username.isEmpty()) throw new IllegalArgumentException("Username is empty.");
        Account retrieved;
        try {
            retrieved =  em.createQuery("SELECT a FROM Account a WHERE a.username = :username", Account.class).setParameter("username", username).getSingleResult();
        } catch(NoResultException e) {
            // well, we prefer null to exception, when no result is found
            retrieved = null;
        }
        return retrieved;
    }

    @Override
    public List<Account> getAccountByCustomer(Customer customer) {
        Objects.requireNonNull(customer, "Customer object is null.");
        List<Account> retrieved = em.createQuery("SELECT a FROM Account a WHERE a.customer = :cid", Account.class).setParameter("cid", customer).getResultList();
        return retrieved;
    }

    @Override
    public List<Account> getAllAccounts() {
        return em.createQuery("SELECT a FROM Account a").getResultList();
    }

    @Override
    public Account updateAccount(Account account) {
        validate(account);
        return em.merge(account);
    }

    @Override
    public void deleteAccountById(long id) {
        if(id < 0) throw new IllegalArgumentException("Id < 0");
        em.createNativeQuery("DELETE FROM ACCOUNT_ROLES ar WHERE ar.account_id = :id").setParameter("id", id).executeUpdate();
        em.createQuery("DELETE FROM Account a WHERE a.id = :id").setParameter("id", id).executeUpdate();
    }

    /**
     * Utility method - check validity of arg.
     * @param account
     */
    private void validate(Account account) {
        if(account == null) throw new NullPointerException("Account object is null.");
        if(account.getId() < 0) throw new IllegalArgumentException("Account object is not valid - id < 0");
        if(account.getUsername() == null) throw new NullPointerException("Username is null.");
        if(account.getUsername().isEmpty()) throw new IllegalArgumentException("Username is empty string.");
        if(account.getPassword() == null) throw new NullPointerException("Password is null.");
        if(account.getPassword().isEmpty()) throw new IllegalArgumentException("Password is empty string.");
    }

}

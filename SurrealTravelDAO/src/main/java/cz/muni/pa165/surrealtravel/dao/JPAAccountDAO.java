package cz.muni.pa165.surrealtravel.dao;

import cz.muni.pa165.surrealtravel.entity.Account;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Jan Klimeš [374259]
 */
@Repository(value = "accountDao")
public class JPAAccountDAO implements AccountDAO {

    @PersistenceContext
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

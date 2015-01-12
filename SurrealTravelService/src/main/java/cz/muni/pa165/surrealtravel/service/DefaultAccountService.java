package cz.muni.pa165.surrealtravel.service;

import cz.muni.pa165.surrealtravel.dao.AccountDAO;
import cz.muni.pa165.surrealtravel.dto.AccountDTO;
import cz.muni.pa165.surrealtravel.entity.Account;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default service for handling accounts.
 * @author Jan Klimeš [374259]
 */
@Service(value = "accountService")
public class DefaultAccountService implements AccountService {

    @Autowired
    private AccountDAO accountDao;

    @Autowired
    private DozerBeanMapper mapper;
    
    @Transactional
    @Override
    public void addAccount(AccountDTO account) {
        Objects.requireNonNull(account, "Account data transfer object is null.");
        Account entity = mapper.map(account, Account.class);
        accountDao.addAccount(entity);
        account.setId(entity.getId());
    }

    @Transactional(readOnly = true)
    @Override
    public AccountDTO getAccountById(long id) {
        Account customer = accountDao.getAccountById(id);
        return customer == null ? null : mapper.map(customer, AccountDTO.class);
    }

    @Transactional(readOnly = true)
    @Override
    public List<AccountDTO> getAllAccounts() {
        List<AccountDTO> accountsDTO = new ArrayList<>();
        for(Account a : accountDao.getAllAccounts())
            accountsDTO.add(mapper.map(a, AccountDTO.class));
        return accountsDTO;
    }

    @Transactional
    @Override
    public AccountDTO updateAccount(AccountDTO account) {
        Objects.requireNonNull(account, "Account data transfer object is null.");
        Account updated = accountDao.updateAccount(mapper.map(account, Account.class));
        return mapper.map(updated, AccountDTO.class);
    }

    @Transactional
    @Override
    public void deleteAccountById(long id) {
        accountDao.deleteAccountById(id);
    }

    public void setAccountDao(AccountDAO accountDao) {
        this.accountDao = accountDao;
    }

    public void setMapper(DozerBeanMapper mapper) {
        this.mapper = mapper;
    }

}

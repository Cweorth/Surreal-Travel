package cz.muni.pa165.surrealtravel.utils;

import cz.muni.pa165.surrealtravel.dto.AccountDTO;

/**
 *
 * @author Roman Lacko [396157]
 */
public class AccountWrapper {
    
    private AccountDTO account;
    private String     passwd1;
    private String     passwd2;
    private boolean    customer;

    public AccountWrapper() 
    { }
    
    public AccountWrapper(AccountDTO account) {
        this.account  = account;
        this.customer = (account != null) && (account.getCustomer() != null);
    }
    
    public AccountDTO getAccount()                   { return account;         }
    public void       setAccount(AccountDTO account) { this.account = account; }
    
    public String getPasswd1()               { return passwd1;         }
    public void   setPasswd1(String passwd1) { this.passwd1 = passwd1; }

    public String getPasswd2()               { return passwd2;         }
    public void   setPasswd2(String passwd2) { this.passwd2 = passwd2; }
 
    public boolean isCustomer()              { return customer;      }
    public void    setCustomer(boolean flag) { this.customer = flag; }
    
}

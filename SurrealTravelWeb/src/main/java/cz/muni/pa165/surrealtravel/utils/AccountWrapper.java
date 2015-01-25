package cz.muni.pa165.surrealtravel.utils;

import cz.muni.pa165.surrealtravel.dto.AccountDTO;

/**
 * The class that wraps {@code AccountDTO} and extra login information.
 * These extra information are password fields (new, retype), permission
 * to change roles and password verification flag.
 *
 * @author Roman Lacko [396157]
 */
public class AccountWrapper {

    private AccountDTO account;   // the account
    private String     passwd1;   // password field 1 (new password)
    private String     passwd2;   // password field 2 (retype)
    private boolean    customer;  // [add]  contains customer
    private boolean    reqpasswd; // [edit] required password verification
    private boolean    modperm;   // [edit] can change permissions

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

    public boolean isReqpasswd()                  { return reqpasswd;           }
    public void    setReqpasswd(boolean reqpasswd) { this.reqpasswd = reqpasswd; }

    public boolean isModperm()                 { return modperm;         }
    public void    setModperm(boolean modperm) { this.modperm = modperm; }


}

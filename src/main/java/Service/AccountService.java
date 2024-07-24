package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService() {
        this.accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public Account registerAccount(Account account) {
        if (account.getPassword() == null || account.getPassword().length() < 4
                || account.getUsername() == null || account.getUsername() == "") {
            // System.out.println("hello");
            return null;
        }
        if (accountDAO.getAccountByUsername(account.getUsername()) != null) {
            // System.out.println("here");
            // System.out.println(account);
            return null;
        }
        return accountDAO.insertAccount(account);
    }
    public Account loginAccount(Account account){
        return accountDAO.getAccountByUsernameAndPassword(account.getUsername(), account.getPassword());
    }

}

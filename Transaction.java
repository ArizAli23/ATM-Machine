package ATMSimulation;

public class Transaction extends Account implements ITransaction {
    public Transaction(int accountID, String accountName, int accountPIN, int balance) {
        super(accountID, accountName, accountPIN, balance);
    }

    @Override
    public int checkBalance() throws InvalidPINException{
        int a = getBalance();
        return a;
    }

    @Override
    public void withdraw(int amount) throws InsufficientFundsException {

            if (amount > getBalance()) {
                throw new InsufficientFundsException("Insufficient Balance");
            } else if (amount <= 0) {
                throw new InsufficientFundsException("Invalid Amount Entered");
            } else {
                setBalance(getBalance() - amount);
            }

    }

    @Override
    public void deposit(int amount) throws InsufficientFundsException {
        if (amount <= 0) {
            throw new InsufficientFundsException("Invalid Amount Entered");
        } else {
            setBalance(getBalance() + amount);
        }
    }


    @Override
    public void transfer(int amount, int accountID) throws AccountNotFoundException, InsufficientFundsException {
        if (amount > 0) {
            if (!(searchAccount(accountID))) { //When Account Not Found
                throw new AccountNotFoundException("Account not Found");
            } else {
                setBalance(getBalance() - amount);
                updateBalanceList(amount, accountID);
            }
        } else {
            throw new InsufficientFundsException("Invalid Amount Enter");
        }
    }
}



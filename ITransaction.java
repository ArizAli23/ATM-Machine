package ATMSimulation;

public interface ITransaction {
    int checkBalance();
    void withdraw(int amount);
    void deposit(int amount);
    void transfer(int amount, int accountID);
}

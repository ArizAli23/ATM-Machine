package ATMSimulation;

import java.util.ArrayList;
import java.util.HashMap;

public class Account extends Branch{
    private int accountID;
    private String accountName;
    private int accountPIN;
    private int balance;
    private static HashMap<Integer, Integer> idToBalance = new HashMap<>();
    private static ArrayList<Integer> accountIdList = new ArrayList<>();
    public Account(int accountID,String accountName ,int accountPIN, int balance){
        this.accountID = accountID;
        this.accountName = accountName;
        this.accountPIN = accountPIN;
        this.balance = balance;
        accountIdList.add(this.accountID);
        idToBalance.put(this.accountID, this.balance);
    }

    public static void getIdToBalance() {
        for (int id : idToBalance.keySet()) {
            System.out.println(id + ": " + idToBalance.get(id));
        }
    }

    public boolean checkPIN(int PIN) throws InvalidPINException{
        if(this.accountPIN == PIN) {
            return true;
        }
        throw new InvalidPINException("Invalid PIN");
    }

    public void updateBalanceList(int amount,int id){
        int newBalance = idToBalance.get(id) + amount;
        idToBalance.put(id, newBalance);
    }

    public boolean searchAccount(int AccountID){
        for(int id : accountIdList){
            if(id==AccountID){
                return true; //Account Found
            }
        }
        return false;
    }

    public void setAccountPIN(int accountPIN) {
        this.accountPIN = accountPIN;
    }

    public String getAccountName() {
        return accountName;
    }

    public int getBalance() {
        return this.balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
        idToBalance.put(this.accountID, balance);
    }
}

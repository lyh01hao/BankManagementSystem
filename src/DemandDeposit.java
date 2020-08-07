public class DemandDeposit {
    double cash; //活期账户的钱数
    public void DemandDeposit (double cash){
        this.cash = cash;
    }
    public void addMoney(double money){
        cash += money;
    }
    public double getCash(){
        return cash;
    }
    public void withdraw(double money){
        cash = cash - money;
    }
}

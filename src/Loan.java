public class Loan {
    double quota = 0.5;//贷款额度
    double dueBalance = 0;//应还金额

    public void Loan(double quota){
        this.quota = quota;//设置贷款额度
    }

    public void lend(double money){
        if(money >= quota)
        {
            quota = quota - money;
            dueBalance += money;
        }else{
            System.out.println("穷鬼给爷爬！");
        }
    }

    public synchronized void run(){
        while(true){
            if(dueBalance == 0){
                break;
            }
            try{
                Thread.currentThread().sleep(10000);//10秒算一期
                dueBalance *= 1.0025;
            }catch (Exception e){
                e.getStackTrace();
            }

        }
    }

    public void payBack(double dueBalance){
        this.dueBalance -= dueBalance;
    }

}

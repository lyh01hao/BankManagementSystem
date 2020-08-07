import java.io.Serializable;

/**
 *
 */
public class TimeDeposit implements Serializable {
//    private static final long serialVersionUID = 1L;
    private double cash = 0.0;
    private double principal = 0.0;//本金，只是保存最开始投入的金额，用于未到期取钱
    private int timeCount = 0;
    private int period = 0;//存款年期
    private double interestRate = 0;//存款利率
    private boolean isDue = false;//是否到期
    public TimeDeposit(double cash,int period){
        this.cash = cash;
        this.period = period;
        this.principal = cash;
        if(period <=5){
            this.interestRate = 1.025 + period * 0.05;//按该公式算利息
        }
        if(period > 5){
            System.out.println("You should enter a number not bigger than 5.");
        }
    }
    public synchronized void run(){
        while(true){
            if(timeCount >= period){
                isDue = true;
                break;
            }
            try{
                Thread.currentThread().sleep(60000);//60秒算一年
                increase();
            }catch (Exception e){
                e.getStackTrace();
            }

        }
    }
    public void increase(){
        cash *= interestRate;
        timeCount++;
    }
    public double withdraw (){
        double result;//最终能取出来的钱数
        if (isDue){
            result = cash;
        }
        else{
            result = principal * Math.pow(1.0003, timeCount);//未到期就把钱取出来了
        }
        cash = 0;
        principal = 0;
        return result;
    }
    public double getCash(){
        return cash;
    }
    public int getPeriod(){
        return period;
    }
    public int getTimeCount(){
        return timeCount;
    }
    public boolean IsDue(){
        return isDue;
    }
}

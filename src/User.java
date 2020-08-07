import java.io.Serializable;
import java.util.ArrayList;
//    private static final long serialVersionUID = 1L;
public class User {

    private ArrayList<TimeDeposit> timeDeposits = new ArrayList<TimeDeposit>();//一个用户有多个定期存款账户，分开计息
    private DemandDeposit demandDeposit = new DemandDeposit();//不想弄成private的了,麻烦
    private Loan loan = new Loan();
    private String username;

    public void User(String username){
        this.username = username;
        this.demandDeposit.username = username;
        this.loan.username = username;
    }

    public void showInfo(){
        System.out.println("尊敬的" + username + "用户:");
        System.out.println("您的账户存款情况为:");
        System.out.print("活期存款：");
        System.out.println(demandDeposit.cash);
        System.out.print("定期存款：");
        int timeDepositCount = 0;
        for(TimeDeposit i :timeDeposits){
            timeDepositCount++;
            String isDue ;
            if(i.IsDue()){
                isDue = "已到期";
            }else{
                isDue = "未到期";
            }
            System.out.println("定期账户（" + timeDepositCount + ")：" + "现金为：" + i.getCash() + "存款期数为：" + i.getPeriod() + "是否到期：" + isDue);
        }
        System.out.println(" ");
        System.out.print("您的贷款情况：");
        System.out.println("剩余额度:" + loan.quota +" 待还金额：" + loan.dueBalance);
    }

    public Loan getLoan() {
        return loan;
    }

    public ArrayList<TimeDeposit> getTimeDeposits() {
        return timeDeposits;
    }

    public DemandDeposit getDemandDeposit() {
        return demandDeposit;
    }

    public static void main(String[] args) {
//        User user1 = new User();
//        user1.timeDeposits.add(new TimeDeposit("zuihoule1",85.28,3));
//        new Thread(new Runnable() {
//            @Override
//            public void run(){
//                user1.timeDeposits.get(0).run();
//            }
//        }).start();

    }
}

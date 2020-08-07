import java.io.Serializable;
import java.util.ArrayList;
//    private static final long serialVersionUID = 1L;
public class User {

    private ArrayList<TimeDeposit> timeDeposits = new ArrayList<TimeDeposit>();//一个用户有多个定期存款账户，分开计息
    private DemandDeposit demandDeposit = new DemandDeposit();
    private Loan loan = new Loan();
    private String userName;

    public void showInfo(){
        System.out.println("尊敬的" + userName + "用户:");
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

}

import org.springframework.jdbc.core.JdbcTemplate;

public class Loan {
    String username;//User的构造方法对其进行了初始化
    double quota = 0.5;//贷款额度
    double dueBalance = 0;//应还金额
    JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
    double usedQuota = 0;

    public void Loan(){

    }

    public void Loan(double quota){
        this.quota = quota;//设置贷款额度
        String sql = "update user SET loan_quota =" + quota +"Where username =?";
        template.update(sql,username);
    }

    public void lend(double money){
        if(money <= quota)
        {
            quota = quota - money;
            usedQuota += money;
            dueBalance += money;
            String sql = "update user SET balance =" + dueBalance +"WHERE username =?";
            template.update(sql,username);
            String sql2 = "update user SET used_quota =" + usedQuota +"WHERE username =?";
            template.update(sql2,username);
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
                String sql = "update user SET balance =" + dueBalance +"WHERE username =?";
                template.update(sql,username);
            }catch (Exception e){
                e.getStackTrace();
            }

        }
    }

    public void payBack(double dueBalance){
        this.dueBalance -= dueBalance;
        String sql = "update user SET balance =" + this.dueBalance +"WHERE username =?";
        template.update(sql,username);
    }

}

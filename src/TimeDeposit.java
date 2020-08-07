import org.springframework.jdbc.core.JdbcTemplate;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 */
public class TimeDeposit implements Serializable {
    private String username;
    private double cash = 0.0;
    private double principal = 0.0;//本金，只是保存最开始投入的金额，用于未到期取钱
    private int timeCount = 0;
    private int period = 0;//存款年期
    private double interestRate = 0;//存款利率
    private boolean isDue = false;//是否到期
    private Integer id = 0;//mysql数据库中的该条定期存款的编号
    JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
//    Connection conn;
//
//    {
//        try {
//            conn = JDBCUtils.getConnection();
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//    }

    public TimeDeposit(String username,double cash,int period){
        this.username = username;
        this.cash = cash;
        this.period = period;
        this.principal = cash;
        if(period <=5){
            this.interestRate = 1.025 + period * 0.05;//按该公式算利息
        }
        if(period > 5){
            System.out.println("You should enter a number not bigger than 5.");
        }
        String sql = "INSERT into time_deposit VALUES (NULL, '" +username +"',"+ principal +","+cash + "," + period + ","
                + isDue + ")";
        template.update(sql);
//        String sql2 = "SELECT last_insert_id() FROM time_deposit";
        String sql2 = "SELECT last_insert_id() ";//用这个函数可能会有些问题,但是找不到替代的了

        id = template.queryForObject(sql2,Integer.class);
        System.out.println("ID是:"+id);
    }
    public synchronized void run(){//运行方法
        while(true){
            if(timeCount >= period){
                isDue = true;
                String sql = "update time_deposit SET is_due = ? WHERE id= ?" ;
                template.update(sql,isDue,id);
                break;
            }
            try{
                Thread.currentThread().sleep(6000);//60秒算一年
                increase();
                String sql = "update time_deposit SET cash = ? WHERE id= ?" ;
                template.update(sql,cash,id);
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
        String sql = "update time_deposit SET principal = ?,cash = ?, is_due = ? WHERE id= ?" ;
        template.update(sql,principal,cash,isDue,id);
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

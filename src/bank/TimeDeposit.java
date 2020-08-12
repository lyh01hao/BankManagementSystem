package bank;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 *
 */
public class TimeDeposit implements Serializable, ActionListener {
    private String username;
    private double cash = 0.0;
    private double principal = 0.0;//本金，只是保存最开始投入的金额，用于未到期取钱
    private int timeCount = 0;
    private int period = 0;//存款年期
    private double interestRate = 0;//存款利率
    private boolean isDue = false;//是否到期
    private int id = 0;//mysql数据库中的该条定期存款的编号
    private int userID;
    private Date date;
    private java.sql.Date sqlDate ;

    JFrame jf ;
    JLabel jl1, jl2;
    JTextField jtf1,jtf2;
    JButton jb;

    JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    public TimeDeposit(){

    }

    public TimeDeposit(int id,String username,int userID,double principal,double cash,int period,Date date){
        this.id = id;
        this.username = username;
        this.userID = userID;
        this.cash = cash;
        this.period = period;
        this.principal = principal;
        this.date = date;


    }

    public void addMoney(){
        if(jtf1.getText().isEmpty()||jtf2.getText().isEmpty()){
            JOptionPane.showMessageDialog(null,"金额为空,请重新输入!","提示信息",JOptionPane.WARNING_MESSAGE);
        }else if (DemandDeposit.isDoubleOrFloat(jtf1.getText())){
            double money =  Double.parseDouble(jtf1.getText());
            int inputPeriod = Integer.parseInt(jtf2.getText());
            if(money>=0&&inputPeriod>0&&inputPeriod<=5){
                principal = money;
                cash = money;
                period = inputPeriod;
                this.interestRate = 1.025 + period * 0.05;//按该公式算利息
                date = new Date();
                sqlDate = new java.sql.Date(date.getTime());
                jtf1.setText("");
                jtf2.setText("");
                String sql = "INSERT INTO t_timedeposit VALUES (NULL, '" +username +"'," + userID +","+ principal +","+cash + "," + period + ","
                        + "'" + sqlDate +"')";
                template.update(sql);
                String sql2 = "SELECT last_insert_id() ";//用这个函数可能会有些问题,但是找不到替代的了
                id = template.queryForObject(sql2,Integer.class);
                JOptionPane.showMessageDialog(null, "存款成功!","提示信息",JOptionPane.WARNING_MESSAGE);
            }else{
                JOptionPane.showMessageDialog(null, "输入不合法!","提示信息",JOptionPane.WARNING_MESSAGE);
            }
        }else{
            JOptionPane.showMessageDialog(null, "输入不合法!","提示信息",JOptionPane.WARNING_MESSAGE);
        }
    }

//    public synchronized void run(){//运行方法
//        while(true){
//            if(timeCount >= period){
//                isDue = true;
//                String sql = "update time_deposit SET is_due = ? WHERE id= ?" ;
//                template.update(sql,isDue,id);
//                break;
//            }
//            try{
//                Thread.currentThread().sleep(6000);//60秒算一年
//                increase();
//                String sql = "update time_deposit SET cash = ? WHERE id= ?" ;
//                template.update(sql,cash,id);
//            }catch (Exception e){
//                e.getStackTrace();
//            }
//
//        }
//    }
    public void increase(){
        cash *= interestRate;
        timeCount++;
    }
    public double withdraw (){
        double result;//最终能取出来的钱数
        Date drawDate = new Date();
        int dateDifference = differentDays(date,drawDate);
        int realPeriod = dateDifference % 365;
        if (realPeriod >= period){
            result = principal * Math.pow(interestRate,period);
        }
        else{
            result = principal * Math.pow(1.0003, realPeriod);//未到期就把钱取出来了
        }
        cash = 0;
        principal = 0;
        String sql = "DELETE FROM t_timedeposit WHERE id = ?";
        template.update(sql,id);
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

    public void showUI(String username, int userID){
        jf = new JFrame();
        jb = new JButton();
        jl1 = new JLabel();
        jl2 = new JLabel();
        jtf1 = new JTextField();
        jtf2 = new JTextField();
        this.username = username;
        this.userID = userID;
        jf.setTitle("定期存款");
        jf.setLayout(null);
        jf.setSize(300,300);


        jb.addActionListener(this);
        jb.setText("确认存款");
        jb.setBounds(170,200,100,28);
        jf.add(jb);


        jl1.setText("定期金额:");
        jl1.setBounds(15,60,140,40);
        jl2.setText("定期期数:");
        jl2.setBounds(15,100,140,40);
        jf.add(jl2);
        jf.add(jl1);


        jtf1.setBounds(70,70,160,30);
        jtf2.setBounds(70,110,160,30);
        jf.add(jtf1);
        jf.add(jtf2);
        jf.setVisible(true);
//        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);



    }


    public boolean isDue() {
        Date date2 = new Date();
        int dateDifference = differentDays(date,date2);
        if(dateDifference %365 >= period){
            return true;
        }else{
            return false;
        }
    }

    public java.sql.Date getSqlDate() {
        return sqlDate;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==jb){
            addMoney();
        }
    }

    public static int differentDays(Date date1,Date date2)
    {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int day1= cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if(year1 != year2)   //同一年
        {
            int timeDistance = 0 ;
            for(int i = year1 ; i < year2 ; i ++)
            {
                if(i%4==0 && i%100!=0 || i%400==0)    //闰年
                {
                    timeDistance += 366;
                }
                else    //不是闰年
                {
                    timeDistance += 365;
                }
            }

            return timeDistance + (day2-day1) ;
        }
        else    //不同年
        {

            return day2-day1;
        }
    }
}

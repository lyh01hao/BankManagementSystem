import com.mysql.cj.util.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

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
    private Integer id = 0;//mysql数据库中的该条定期存款的编号

    JFrame jf ;
    JLabel jl1, jl2;
    JTextField jtf1,jtf2;
    JButton jb;

    JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    public TimeDeposit(){

    }

    public TimeDeposit(String username,double cash,int period){
//        this.username = username;
//        this.cash = cash;
//        this.period = period;
//        this.principal = cash;
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
                jtf1.setText("");
                jtf2.setText("");
//                String sql = "INSERT into time_deposit VALUES (NULL, '" +username +"',"+ principal +","+cash + "," + period + ","
//                        + isDue + ")";
//                template.update(sql);
//                String sql2 = "SELECT last_insert_id() ";//用这个函数可能会有些问题,但是找不到替代的了
//
//                id = template.queryForObject(sql2,Integer.class);
//                System.out.println("ID是:"+id);
                JOptionPane.showMessageDialog(null, "存款成功!","提示信息",JOptionPane.WARNING_MESSAGE);
            }else{
                JOptionPane.showMessageDialog(null, "输入不合法!","提示信息",JOptionPane.WARNING_MESSAGE);
            }
        }else{
            JOptionPane.showMessageDialog(null, "输入不合法!","提示信息",JOptionPane.WARNING_MESSAGE);
        }
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
//        String sql = "update time_deposit SET principal = ?,cash = ?, is_due = ? WHERE id= ?" ;
//        template.update(sql,principal,cash,isDue,id);
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

    public void showUI(String username){
        jf = new JFrame();
        jb = new JButton();
        jl1 = new JLabel();
        jl2 = new JLabel();
        jtf1 = new JTextField();
        jtf2 = new JTextField();
        this.username = username;
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

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==jb){
            addMoney();
        }
    }
}

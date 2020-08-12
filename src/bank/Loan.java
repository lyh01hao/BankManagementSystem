package bank;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Loan implements ActionListener {
    String username;//User的构造方法对其进行了初始化
    int userID;
    double quota = 100;//贷款额度
    double dueBalance = 0;//应还金额
    JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
    double usedQuota = 0;

    JFrame jf_lend;
    JButton jb1_lend  ;//取款界面用
    JLabel jl1_lend, jl2_lend,jl3_lend;//取款界面用
    JTextField jtf1_lend;//取款界面用

    JFrame jf_back;
    JButton jb1_back  ;//取款界面用
    JLabel jl1_back, jl2_back,jl3_back;//取款界面用
    JTextField jtf1_back;//取款界面用

    public void Loan(){

    }

    public void Loan(double quota){
        this.quota = quota;//设置贷款额度
        String sql = "update user SET loan_quota =" + quota +"Where username =?";
        template.update(sql,username);
    }

    public void lend(){
        if(jtf1_lend.getText().isEmpty()){
            JOptionPane.showMessageDialog(null,"金额为空,请重新输入!","提示信息",JOptionPane.WARNING_MESSAGE);
        }else if (DemandDeposit.isDoubleOrFloat(jtf1_lend.getText())){
            double money =  Double.parseDouble(jtf1_lend.getText());
            if(money<=quota){
                quota = quota - money;
                usedQuota += money;
                dueBalance = dueBalance + money *1.1;
                mysqlUpdate();
                String balance = "您的剩余额度为:" + quota;
                String duebalanc = "您的待还金额为:" + dueBalance;
                jl3_lend.setText(duebalanc);
                jl1_lend .setText(balance);
                jtf1_lend.setText("");
                JOptionPane.showMessageDialog(null, "贷款成功!","提示信息",JOptionPane.WARNING_MESSAGE);
            }else{
                JOptionPane.showMessageDialog(null, "输入金额不合法!","提示信息",JOptionPane.WARNING_MESSAGE);
            }
        }else{
            JOptionPane.showMessageDialog(null, "输入金额不合法!","提示信息",JOptionPane.WARNING_MESSAGE);
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

    public void payBack(){
        if(jtf1_back.getText().isEmpty()){
            JOptionPane.showMessageDialog(null,"金额为空,请重新输入!","提示信息",JOptionPane.WARNING_MESSAGE);
        }else if (DemandDeposit.isDoubleOrFloat(jtf1_back.getText())){
            double money =  Double.parseDouble(jtf1_back.getText());
            if(!(money>dueBalance|| money <0)){
                double difference = dueBalance - usedQuota; //应还金额减去已用额度等于利息
                dueBalance -= money;
                if(money > difference){//还的钱比利息多,额度会增加,把利息还完,剩下还的钱会加额度
                    quota += money- difference;
                    usedQuota -= money - difference;
                }

                mysqlUpdate();
                String balance = "您的剩余额度为:" + quota;
                String duebalanc = "您的待还金额为:" + dueBalance;
                jl3_back.setText(duebalanc);
                jl1_back .setText(balance);
                jtf1_back.setText("");
                JOptionPane.showMessageDialog(null, "还款成功!","提示信息",JOptionPane.WARNING_MESSAGE);
            }else{
                JOptionPane.showMessageDialog(null, "输入金额不合法!","提示信息",JOptionPane.WARNING_MESSAGE);
            }
        }else{
            JOptionPane.showMessageDialog(null, "输入金额不合法!","提示信息",JOptionPane.WARNING_MESSAGE);
        }
        this.dueBalance -= dueBalance;
        String sql = "update user SET balance =" + this.dueBalance +"WHERE username =?";
        template.update(sql,username);
    }

    public void showLendUI(){
        jf_lend = new JFrame();
        jb1_lend = new JButton("确认贷款");
        jl1_lend = new JLabel();
        jl2_lend = new JLabel();
        jl3_lend = new JLabel();

        jf_lend.setTitle("贷款");
        jf_lend.setLayout(null);
        jf_lend.setSize(300,300);


        jb1_lend.addActionListener(this);
        jb1_lend.setBounds(170,200,100,28);
        jf_lend.add(jb1_lend);

        String balance = "您的剩余额度为:" + quota;
        jl1_lend.setText(balance);
        jl1_lend.setBounds(5,5,200,20);
        String duebalanc = "您的待还金额为:" + dueBalance;
        jl3_lend.setText(duebalanc);
        jl3_lend.setBounds(5,20,200,20);
        jf_lend.add(jl1_lend);
        jf_lend.add(jl3_lend);
        jl2_lend.setText("请输入您要贷款的金额:");
        jl2_lend.setBounds(80,50,140,30);
        jf_lend.add(jl2_lend);

        jtf1_lend = new JTextField();
        jtf1_lend.setBounds(70,100,160,30);
        jf_lend.add(jtf1_lend);
        jf_lend.setVisible(true);
    }

    public void showPayBackUI(){
        jf_back = new JFrame();
        jb1_back = new JButton("确认还款");
        jl1_back = new JLabel();
        jl2_back = new JLabel();
        jl3_back = new JLabel();

        jf_back.setTitle("还款");
        jf_back.setLayout(null);
        jf_back.setSize(300,300);


        jb1_back.addActionListener(this);
        jb1_back.setBounds(170,200,100,28);
        jf_back.add(jb1_back);

        String balance = "您的剩余额度为:" + quota;
        jl1_back.setText(balance);
        jl1_back.setBounds(5,5,200,20);
        String duebalanc = "您的待还金额为:" + dueBalance;
        jl3_back.setText(duebalanc);
        jl3_back.setBounds(5,20,200,20);
        jf_back.add(jl1_back);
        jf_back.add(jl3_back);
        jl2_back.setText("请输入您要还款的金额:");
        jl2_back.setBounds(80,50,140,30);
        jf_back.add(jl2_back);

        jtf1_back = new JTextField();
        jtf1_back.setBounds(70,100,160,30);
        jf_back.add(jtf1_back);
        jf_back.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==jb1_lend){
            lend();
        }
        if (e.getSource()==jb1_back){
            payBack();
        }
    }

    public void mysqlUpdate(){
            String sql = "update t_user SET balance =" + dueBalance +"WHERE user_id =?";
            template.update(sql,userID);
            String sql2 = "update t_user SET used_quota =" + usedQuota +"WHERE user_id =?";
            template.update(sql2,userID);
    }
}

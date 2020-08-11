package bank;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DemandDeposit implements ActionListener {
    String username;
    double cash = 789; //活期账户的钱数
    JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
    JFrame jf = new JFrame();//存款界面用
    JButton jb1 = new JButton("确定存款");//存款界面用
    JLabel jl1 = new JLabel(), jl2 = new JLabel();//存款界面用
    JTextField jtf1 = new JTextField();//存款界面用

    JFrame jf_draw = new JFrame();//取款界面用
    JButton jb1_draw =new JButton("确认取款") ;//取款界面用
    JLabel jl1_draw = new JLabel(), jl2_draw = new JLabel();//取款界面用
    JTextField jtf1_draw = new JTextField();//取款界面用

    JFrame jf_trans = new JFrame();//转账界面用
    JButton jb1_trans =new JButton("确认转账") ;//转账界面用
    JLabel jl1_trans = new JLabel(), jl2_trans = new JLabel(), jl3_trans = new JLabel();//转账界面用
    JTextField jtf1_trans = new JTextField(), jtf2_trans = new JTextField();//转账界面用



    public void DemandDeposit (double cash){
        this.cash = cash;
    }

    public void add (double money) {
        cash += money;
        mysqlUpdate();
    }

    public void addMoney( ) throws IOException {
        if(jtf1.getText().isEmpty()){
            JOptionPane.showMessageDialog(null,"金额为空,请重新输入!","提示信息",JOptionPane.WARNING_MESSAGE);
        }else if (isDoubleOrFloat(jtf1.getText())){
            double money =  Double.parseDouble(jtf1.getText());
            if(money>=0){
                cash += money;
//                mysqlUpdate();
                String balance = "您的账户余额为:" + cash;
                jl1 .setText(balance);
                jtf1.setText("");
                JOptionPane.showMessageDialog(null, "存款成功!","提示信息",JOptionPane.WARNING_MESSAGE);
            }else{
                JOptionPane.showMessageDialog(null, "存入金额不合法!","提示信息",JOptionPane.WARNING_MESSAGE);
            }
        }else{
            JOptionPane.showMessageDialog(null, "存入金额不合法!","提示信息",JOptionPane.WARNING_MESSAGE);
        }



    }

    public double getCash(){
        return cash;
    }

    public void withdraw() throws IOException{
        if(jtf1_draw.getText().isEmpty()){
            JOptionPane.showMessageDialog(null,"金额为空,请重新输入!","提示信息",JOptionPane.WARNING_MESSAGE);
        }else if (isDoubleOrFloat(jtf1_draw.getText())){
            double money =  Double.parseDouble(jtf1_draw.getText());
            if(money <= cash ){
                cash -= money;
//                mysqlUpdate();
                String balance = "您的账户余额为:" + cash;
                jl1_draw.setText(balance);
                jtf1_draw.setText("");
                JOptionPane.showMessageDialog(null, "取款成功!","提示信息",JOptionPane.WARNING_MESSAGE);
            }else{
                JOptionPane.showMessageDialog(null, "输入金额不合法!","提示信息",JOptionPane.WARNING_MESSAGE);
            }
        }else{
            JOptionPane.showMessageDialog(null, "输入金额不合法!","提示信息",JOptionPane.WARNING_MESSAGE);
        }
    }

    public void transfer() throws IOException{
        if(jtf1_trans.getText().isEmpty()||jtf2_trans.getText().isEmpty()){
            JOptionPane.showMessageDialog(null,"输入为空,请重新输入!","提示信息",JOptionPane.WARNING_MESSAGE);
        }else if (isDoubleOrFloat(jtf2_trans.getText())){
            String reciever = jtf1_trans.getText();
            if(UI.userExist(reciever)){
                double money =  Double.parseDouble(jtf2_trans.getText());
                if(money<=cash){
                    cash -= money;
//                    mysqlUpdate();
                    User rec = UI.findUser();
                    rec.getDemandDeposit().add(money);
                    /*
                    加入数据库,加入数据库
                     */
                    String balance = "您的账户余额为:" + cash;
                    jl1_trans .setText(balance);
                    jtf1_trans.setText("");
                    jtf2_trans.setText("");
                    JOptionPane.showMessageDialog(null, "转账成功!","提示信息",JOptionPane.WARNING_MESSAGE);
                }else{
                    JOptionPane.showMessageDialog(null, "存入金额不合法!","提示信息",JOptionPane.WARNING_MESSAGE);
                }
            }else{
                JOptionPane.showMessageDialog(null,"收款人不存在,请重新输入!","提示信息",JOptionPane.WARNING_MESSAGE);
            }

        }else{
            JOptionPane.showMessageDialog(null, "存入金额不合法!","提示信息",JOptionPane.WARNING_MESSAGE);
        }
    }

    public void showUI1(){
        jf.setTitle("活期存款");
        jf.setLayout(null);
        jf.setSize(300,300);


        jb1.addActionListener(this);
        jb1.setBounds(170,200,100,28);
        jf.add(jb1);

        String balance = "您的账户余额为:" + cash;
        jl1.setText(balance);
        jl1.setBounds(5,5,200,20);
        jf.add(jl1);
        jl2.setText("请输入您要存储的金额:");
        jl2.setBounds(80,50,140,30);
        jf.add(jl2);


        jtf1.setBounds(70,100,160,30);
        jf.add(jtf1);
        jf.setVisible(true);

    }

    public void showUI2(){
        jf_draw.setTitle("活期取款");
        jf_draw.setLayout(null);
        jf_draw.setSize(300,300);


        jb1_draw.addActionListener(this);
        jb1_draw.setBounds(170,200,100,28);
        jf_draw.add(jb1_draw);

        String balance = "您的账户余额为:" + cash;
        jl1_draw.setText(balance);
        jl1_draw.setBounds(5,5,200,20);
        jf_draw.add(jl1_draw);
        jl2_draw.setText("请输入您要取出的的金额:");
        jl2_draw.setBounds(80,50,140,30);
        jf_draw.add(jl2_draw);


        jtf1_draw.setBounds(70,100,160,30);
        jf_draw.add(jtf1_draw);
        jf_draw.setVisible(true);
    }

    public void showUI3(){
        jf_trans.setTitle("转账");
        jf_trans.setLayout(null);
        jf_trans.setSize(300,300);


        jb1_trans.addActionListener(this);
        jb1_trans.setBounds(170,200,100,28);
        jf_trans.add(jb1_trans);

        String balance = "您的账户余额为:" + cash;
        jl1_trans.setText(balance);
        jl1_trans.setBounds(5,5,200,20);
        jf_trans.add(jl1_trans);
        jl2_trans.setText("收款人:");
        jl2_trans.setBounds(15,60,140,40);
        jl3_trans.setText("转账金额:");
        jl3_trans.setBounds(15,100,140,40);
        jf_trans.add(jl2_trans);
        jf_trans.add(jl3_trans);


        jtf1_trans.setBounds(70,70,160,30);
        jtf2_trans.setBounds(70,110,160,30);
        jf_trans.add(jtf1_trans);
        jf_trans.add(jtf2_trans);
        jf_trans.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==jb1)
        try {
            addMoney();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        if(e.getSource()== jb1_draw)
        try {
            withdraw();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        if(e.getSource()== jb1_trans)
            try {
                transfer();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
    }


    public void mysqlUpdate(){
        String sql = "update user SET demand_deposit =" +cash+"Where username =?";
        template.update(sql,username);
    }

    public static boolean isDoubleOrFloat(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");
        return pattern.matcher(str).matches();
    }

    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

}

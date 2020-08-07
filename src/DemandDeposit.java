import org.springframework.jdbc.core.JdbcTemplate;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.regex.Pattern;

public class DemandDeposit implements ActionListener {
    String username;
    double cash = 0; //活期账户的钱数
    JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
    JFrame jf = new JFrame();
    JButton jb1 ;
    JLabel jl1, jl2;
    JTextField jtf1;

    public void DemandDeposit (double cash){
        this.cash = cash;
    }

    public void addMoney( ) throws IOException {
        if(jtf1.getText().isEmpty()){
            JOptionPane.showMessageDialog(null,"金额为空,请重新输入!","提示信息",JOptionPane.WARNING_MESSAGE);
        }else if (isDoubleOrFloat(jtf1.getText())){
            double money =  Double.parseDouble(jtf1.getText());
            if(money>=0){
                cash += money;
//                String sql = "update user SET demand_deposit =" +cash+"Where username =?";
//                template.update(sql,username);//加入数据库= = ,测试就没用了
                String balance = "您的账户余额为:" + cash;
                jl1 .setText(balance);
                jtf1.setText("");
                JOptionPane.showMessageDialog(null, "存款成功!","提示信息",JOptionPane.WARNING_MESSAGE);
            }
        }else{
            JOptionPane.showMessageDialog(null, "存入金额不合法!","提示信息",JOptionPane.WARNING_MESSAGE);
        }



    }

    public double getCash(){
        return cash;
    }

    public void withdraw(double money){
        if(cash >= money){
            cash = cash - money;
            String sql = "update user SET demand_deposit =" +cash+"Where username =?";
            template.update(sql);
        }else{
            System.out.println("穷鬼给爷爬");
        }
    }

    public void showUI1(){
        jf.setTitle("活期存款");
        jf.setLayout(null);
        jf.setSize(300,300);

        jb1 = new JButton("确定存款");
        jb1.addActionListener(this);
        jb1.setBounds(170,200,100,28);
        jf.add(jb1);

        String balance = "您的账户余额为:" + cash;
        jl1 = new JLabel(balance);
        jl1.setBounds(5,5,200,20);
        jf.add(jl1);
        jl2 = new JLabel("请输入您要存储的金额:");
        jl2.setBounds(80,50,140,30);
        jf.add(jl2);

        jtf1 = new JTextField();
        jtf1.setBounds(70,100,160,30);
        jf.add(jtf1);
        jf.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand()=="确定存款")
        try {
            addMoney();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }


    public static boolean isDoubleOrFloat(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");
        return pattern.matcher(str).matches();
    }
    public static void main(String[] args) {
        DemandDeposit demandDeposit = new DemandDeposit();
//        System.out.println(demandDeposit.isDoubleOrFloat("abc12568900"));
        demandDeposit.showUI1();

    }
}

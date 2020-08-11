import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    @Override
    public String toString() {
        return "User{" +
                "timeDeposits=" + timeDeposits +
                ", demandDeposit=" + demandDeposit +
                ", loan=" + loan +
                ", username='" + username + '\'' +
                '}';
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

    public void showInfoUI(){
        JFrame jf = new JFrame();
        jf.setLayout(null);
        jf.setSize(500,500);
        jf.setTitle("账户信息");

        JTextArea jta = new JTextArea();
        String info = "尊敬的" + username + "用户:" + "\n"
                    + "您的账户存款情况为:"+"\n"
                    +"活期存款："+demandDeposit.cash +"元\n"
                    +"定期存款：\n";
        int timeDepositCount = 0;
        for(TimeDeposit i :timeDeposits){
            timeDepositCount++;
            String isDue ;
            if(i.IsDue()){
                isDue = "已到期";
            }else{
                isDue = "未到期";
            }
            info +="定期账户（" + timeDepositCount + ")：" + " 现金为：" + i.getCash() + " 存款期数为：" + i.getPeriod() + " 是否到期：" + isDue +"\n";
        }
        info += "\n"+"您的贷款情况：" + "剩余额度:" + loan.quota +" 待还金额：" + loan.dueBalance;

        jta.setText(info);
        jta.setBounds(0,0,500,500);
        jta.setEditable(false);
        jf.add(jta);
        jf.setVisible(true);
    }

    public void createTimeDeposit(){
        TimeDeposit account = new TimeDeposit();
        timeDeposits.add(account);
        account.showUI(username);
    }

    public void showTimeDeposit(){
        JFrame jf = new JFrame();
        jf.setLayout(null);
        jf.setSize(500,500);
        jf.setTitle("取出定期存款");

        int timeDepositCount = 0;
        String info="定期存款信息:\n";
        for(TimeDeposit i :timeDeposits){
            timeDepositCount++;
            String isDue ;
            if(i.IsDue()){
                isDue = "已到期";
            }else{
                isDue = "未到期";
            }
            info +="定期账户（" + timeDepositCount + ")：" + " 现金为：" + i.getCash() + " 存款期数为：" + i.getPeriod() + " 是否到期：" + isDue +"\n";
        }

        JTextArea jta = new JTextArea();
        JTextField jtf = new JTextField();
        JLabel jl = new JLabel();
        JButton jb = new JButton();
        jta.setText(info);
        jta.setEditable(false);
//        jta.setBounds(0,0,300,500);
        JScrollPane jsp=new JScrollPane(jta);    //将文本域放入滚动窗口
        jsp.setBounds(0,0,500,270);
        jf.add(jsp);
        jl.setText("请输入您要取出存款的定期账户编号:");
        jl.setBounds(150,280,200,30);
        jtf.setBounds(140,320,220,30);
        jb.setBounds(400,420,70,30);
        jb.setText("确定");

        jf.add(jl);
        jf.add(jtf);
        jf.add(jb);

        jf.setVisible(true);
        int size = timeDeposits.size();
        jb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(jtf.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null,"输入为空,请重新输入!","提示信息",JOptionPane.WARNING_MESSAGE);
                }else if(DemandDeposit.isNumeric(jtf.getText())){
                    int num = Integer.parseInt(jtf.getText())-1;
                    if(num>=0){
                        TimeDeposit currentAccount = timeDeposits.get(num);
                        double result = currentAccount.withdraw();
                        String output = "恭喜,您已成功取出:" + result +"元";
                        JOptionPane.showMessageDialog(null,output,"提示信息",JOptionPane.WARNING_MESSAGE);
                        timeDeposits.remove(currentAccount);

                    }else{
                        JOptionPane.showMessageDialog(null,"输入数字错误,请重新输入!","提示信息",JOptionPane.WARNING_MESSAGE);
                    }
                }else{
                    JOptionPane.showMessageDialog(null,"非法输入!","提示信息",JOptionPane.WARNING_MESSAGE);
                }

            }
        });
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
        new User().showTimeDeposit();
    }

}

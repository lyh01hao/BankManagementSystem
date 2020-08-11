
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class UI extends JFrame implements ActionListener{

    JButton jb1, jb2, jb3,jb4,jb5,jb6,jb7, jb8;
    JLabel jlb1, jlb2, jlb3;
    User currentUser = new User() ;

    public static void main(String[] args) {
        new UI();
    }

    public UI()
    {
        jb1 = new JButton("查询");
        jb2 = new JButton("贷款");
        jb3 = new JButton("还贷");
        jb4 = new JButton("转账");
        jb5 = new JButton("存活期");
        jb6 = new JButton("取活期");
        jb7 = new JButton("存定期");
        jb8 = new JButton("取定期");


        jlb1 = new JLabel("Java大作业银行");
        jlb1.setFont(new   java.awt.Font("Dialog",   1,   23));
        jlb2 = new JLabel("欢迎您");
        jlb2.setFont(new   java.awt.Font("Dialog",   1,   20));
        jlb3 = new JLabel("请您选择服务");
        jlb3.setFont(new   java.awt.Font("Dialog",   1,   22));

        jb1.addActionListener(this);   //事件监听
        jb2.addActionListener(this);
        jb3.addActionListener(this);
        jb4.addActionListener(this);
        jb5.addActionListener(this);
        jb6.addActionListener(this);
        jb7.addActionListener(this);
        jb8.addActionListener(this);

        this.setTitle("银行管理系统");
        this.setSize(450, 500);
        this.setLocation(400, 200);
        this.setLayout(null);

        //设置按钮的位置和大小
        jb1.setBounds( 0,50,90,60);
        jb2.setBounds( 0,150,90,60);
        jb3.setBounds( 0,250,90,60);
        jb4.setBounds(0,350,90,60);
        jb5.setBounds( 354,50,90,60);
        jb6.setBounds( 354,150,90,60);
        jb7.setBounds( 354,250,90,60);
        jb8.setBounds(354,350,90,60);

        //设置标签的位置和大小
        jlb1.setBounds(135,120,250,50);
        jlb2.setBounds(190,160,150,50);
        jlb3.setBounds(150,250,150,50);

        this.add(jb1);   //加入窗体
        this.add(jb2);
        this.add(jb3);
        this.add(jb4);
        this.add(jb5);
        this.add(jb6);
        this.add(jb7);
        this.add(jb8);
        this.add(jlb1);
        this.add(jlb2);
        this.add(jlb3);

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setResizable(false);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        if (e.getActionCommand()=="查询")
        {
            currentUser.showInfoUI();
        }
        else if (e.getActionCommand()=="贷款")
        {
            currentUser.getLoan().showLendUI();
        }
        else if (e.getActionCommand()=="还贷")
        {
            currentUser.getLoan().showPayBackUI();
        }
        else if (e.getActionCommand()=="转账")
        {
            currentUser.getDemandDeposit().showUI3();
        }
        else if (e.getActionCommand()=="存活期")
        {
            currentUser.getDemandDeposit().showUI1();
        }
        else if (e.getActionCommand()=="取活期")
        {
            currentUser.getDemandDeposit().showUI2();
        }
        else if (e.getActionCommand()=="存定期")
        {
            currentUser.createTimeDeposit();
        }
        else if (e.getActionCommand()=="取定期")
        {
            currentUser.showTimeDeposit();
        }


    }

    static public boolean userExist(String username){
        return false;
    }

    public static User findUser(){
        return null;
    }
}

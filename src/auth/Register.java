package auth;

import bank.JDBCUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

public class Register extends JFrame {
    private Connection connection = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultset = null;

    private int RegisterWidth = 400;
    private int RegisterHeight = 400;
    private int x = 600;
    private int y = 250;

    Random random = new Random(System.currentTimeMillis());

    public Register() {
        setBounds(x, y, RegisterWidth, RegisterHeight);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setTitle("Java大作业银行注册界面");

        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        JLabel userNameLable = new JLabel("用户昵称");
        userNameLable.setBounds(50, 50, 50, 50);
        contentPane.add(userNameLable);

        JTextField userNameField = new JTextField();
        userNameField.setBounds(100, 50, 200, 50);
        contentPane.add(userNameField);

        JLabel userNameNotice = new JLabel("长度：（0，10]");
        userNameNotice.setBounds(300, 50, 200, 50);
        contentPane.add(userNameNotice);

        JLabel userPasswordLable = new JLabel("密码");
        userPasswordLable.setBounds(50, 100, 50, 50);
        contentPane.add(userPasswordLable);

        JPasswordField passwordField1 = new JPasswordField();
        passwordField1.setBounds(100, 100, 200, 50);
        contentPane.add(passwordField1);

        JLabel userPasswordNotice = new JLabel("长度：[10，18]");
        userPasswordNotice.setBounds(300, 100, 200, 50);
        contentPane.add(userPasswordNotice);

        JLabel userPasswordLable2 = new JLabel("确认密码");
        userPasswordLable2.setBounds(50, 150, 50, 50);
        contentPane.add(userPasswordLable2);

        JPasswordField passwordField2 = new JPasswordField();
        passwordField2.setBounds(100, 150, 200, 50);
        contentPane.add(passwordField2);

        JButton resigsterButton = new JButton("确认注册");
        resigsterButton.setBounds(50, 230, 100, 50);
        contentPane.add(resigsterButton);

        JButton cancelButton = new JButton("取消注册");
        cancelButton.setBounds(200, 230, 100, 50);
        contentPane.add(cancelButton);

        cancelButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new Login();
            }
        });

        resigsterButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userName = userNameField.getText();
                String userPassword1 = passwordField1.getText();
                String userPassword2 = passwordField2.getText();
                int userID = random.nextInt(1000000000) + 1000000000;//生成十亿到二十亿的随机数，用来当作用户id
                Boolean flag = true;
                try {
                    connection = JDBCUtils.getConnection();
                    String sql = "select user_id from t_user where user_id = ?";
                    preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setInt(1, userID);

                    resultset = preparedStatement.executeQuery();
                    if (resultset.next()) {
                        flag = !flag;
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } finally {
                    JDBCUtils.close(resultset, preparedStatement, connection);
                }
                System.out.println(userID);

                if (userPassword1.equals(userPassword2) && userName.length() <= 10 && userName.length() >= 0 && flag && userPassword2.length() >= 10 && userPassword2.length() <= 18) {
                    String sql = "insert into t_user (user_id,user_name,user_password) values (?,?,?)";
                    try {
                        connection = JDBCUtils.getConnection();
                        preparedStatement = connection.prepareStatement(sql);
                        preparedStatement.setInt(1, userID);
                        preparedStatement.setString(2, userName);
                        String userPassword = MD5.executeMD5(userPassword1);
                        preparedStatement.setString(3, userPassword);

                        new RegisterDialog(userID);
                        preparedStatement.execute();
                    } catch (SQLException | NoSuchAlgorithmException throwables) {
                        throwables.printStackTrace();
                    } finally {
                        JDBCUtils.close(resultset, preparedStatement, connection);
                    }
                }
            }
        });
        setVisible(true);
    }

    public static void main(String[] args) {
        new Register();
    }


    class RegisterDialog extends JDialog {
        public RegisterDialog(long userID) {
            setBounds(x + 100, y + 100, 200, 200);
            setTitle("java大作业银行");
            Container contentPane = getContentPane();
            contentPane.setLayout(null);

            JButton jButton = new JButton("确认");
            jButton.setBounds(50, 125, 100, 25);
            JLabel jLabel = new JLabel("你的账户是" + String.valueOf(userID));
            jLabel.setBounds(25, 50, 150, 25);

            jButton.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            });
            contentPane.add(jLabel);
            contentPane.add(jButton);

            setVisible(true);
        }
    }
}

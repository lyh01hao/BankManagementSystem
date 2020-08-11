package auth;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Login extends JFrame {
    private Connection connection = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultset = null;

    final int loginWidth = 400;
    final int loginHeight = 400;
    final int x = 600;
    final int y = 250;

    public Login() {
        setBounds(x, y, loginWidth, loginHeight);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Java大作业银行登录界面");
        setResizable(false);

        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        JLabel userLable = new JLabel();
        userLable.setBounds(50, 50, 50, 50);
        userLable.setIcon(new ImageIcon("src/resource/user.png"));
        contentPane.add(userLable);

        JLabel lockLable = new JLabel();
        lockLable.setBounds(50, 150, 50, 50);
        lockLable.setIcon(new ImageIcon("src/resource/lock.png"));
        contentPane.add(lockLable);

        JButton loginButton = new JButton("登录");
        loginButton.setBounds(50, 250, 100, 50);
        contentPane.add(loginButton);

        JButton registButton = new JButton("注册");
        registButton.setBounds(200, 250, 100, 50);
        contentPane.add(registButton);

        JTextField userField = new JTextField();
        userField.setBounds(100, 50, 200, 50);
        contentPane.add(userField);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(100, 150, 200, 50);
        contentPane.add(passwordField);


        loginButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    connection = DButil.getConnection();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                String userID = userField.getText();
                String userPassword = passwordField.getText();
                String sql = "select user_id from t_user t where t.user_id = ? and t.user_password = ?";
                if (userID != null && userPassword != null && userID.length() != 0 && userPassword.length() != 0) {
                    try {
                        preparedStatement = connection.prepareStatement(sql);

                        preparedStatement.setInt(1, Integer.parseInt(userID));
                        preparedStatement.setString(2, MD5.executeMD5(userPassword));
                        resultset = preparedStatement.executeQuery();

                        if (resultset.next()) {
                            System.out.println("登录成功");
                        } else {
                            System.out.println("登录失败");
                        }

                    } catch (SQLException | NoSuchAlgorithmException throwables) {
                        throwables.printStackTrace();
                    } finally {
                        DButil.close(connection, preparedStatement, resultset);
                    }
                }

            }
        });


        registButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new Register();
            }
        });
        setVisible(true);
    }

    public static void main(String[] args) {
        new Login();
    }


}

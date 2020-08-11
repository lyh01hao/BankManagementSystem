package auth;

import javax.swing.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class test extends JDialog {
    public test(){

    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        String testpassword = "1asd1516qw";
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(testpassword.getBytes());
        byte[] digest = md.digest();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < digest.length; i++) {
            if((digest[i] & 0xff ) < 0x10){
                sb.append("0");
            }
            sb.append(Long.toString(digest[i] & 0xff,16));
        }

        System.out.println(sb);
        System.out.println(testpassword.getBytes());


    }
}
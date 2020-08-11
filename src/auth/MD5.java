package auth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class MD5 {
    private MD5(){

    }

    public static String executeMD5(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());
        byte[] digest = md.digest();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < digest.length; i++) {
            if((digest[i] & 0xff ) < 0x10){
                sb.append("0");
            }
            sb.append(Long.toString(digest[i] & 0xff,16));
        }


        return sb.toString();
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        //用作测试
        Scanner scanner = new Scanner(System.in);
        System.out.println(executeMD5(scanner.nextLine()));
    }
}

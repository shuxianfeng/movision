package com.movision.utils;

import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


/**
 * 密码加密 hash md5 2times
 */
@Service
public class PasswordHelper {

    private RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();

    @Value("${password.algorithmName}")
    private String algorithmName = "md5";
    @Value("${password.hashIterations}")
    private int hashIterations = 2;
    @Value("${accesstoken}")
    private String accesstoken;




    public void setRandomNumberGenerator(
            RandomNumberGenerator randomNumberGenerator) {
        this.randomNumberGenerator = randomNumberGenerator;
    }

    public void setAlgorithmName(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    public void setHashIterations(int hashIterations) {
        this.hashIterations = hashIterations;
    }

    public RandomNumberGenerator getRandomNumberGenerator() {
        return randomNumberGenerator;
    }

    public String getAlgorithmName() {
        return algorithmName;
    }

    public int getHashIterations() {
        return hashIterations;
    }
    public String getAccesstoken() {
        return accesstoken;
    }

    public void setAccesstoken(String accesstoken) {
        this.accesstoken = accesstoken;
    }


//    public void encryptPassword(User user) {
//
//        user.setSalt(randomNumberGenerator.nextBytes().toHex());
//
//        String newPassword = new SimpleHash(algorithmName, user.getUserPwd(),
//                ByteSource.Util.bytes(user.getSalt()), hashIterations).toHex();
//
//        user.setUserPwd(newPassword);
//    }



    public String encryptPayPassword(String password, String salt) {
        String ecncyptPwd = new SimpleHash(algorithmName, password,
                ByteSource.Util.bytes(salt), hashIterations)
                .toHex();
        return ecncyptPwd;
    }



    public static void main(String[] args) {
        PasswordHelper ph = new PasswordHelper();
        String salt = ph.randomNumberGenerator.nextBytes().toHex();//"cd38925fcb400292b4e823b1baa455f8";//
        System.out.println(salt);
        String s = new SimpleHash("md5", "123456",
                null, 2)
                .toHex();
        System.out.println(s);
        System.out.println(new Md5Hash("123456","aaa0af3b28f8376cb7263a6e2cefdefe",2).toString());
//4280d89a5a03f812751f504cc10ee8a5
//        String s1 = DigestUtils.shaHex("`!@#123456,.`/");
//        System.out.println(s1);
    }
}

package in.kpmg.auro.project.config;

import org.jasypt.util.text.AES256TextEncryptor;
import org.springframework.stereotype.Component;

@Component
public class AesUtil {

    AES256TextEncryptor aesEncryptor = new AES256TextEncryptor();
    static private String keystring = "498aa575016fedc251b677315eb";

    public AesUtil(){
        aesEncryptor.setPassword(keystring);
    }

    public String encrypt(String plainText){

        return aesEncryptor.encrypt(plainText);
    }

    public String decrypt(String ecriptedString){
        System.out.println("decrypt function   "+ecriptedString);
        return aesEncryptor.decrypt(ecriptedString);
    }


}

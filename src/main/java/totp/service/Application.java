package totp.service;

import io.micronaut.runtime.Micronaut;

public class Application {

    public static void main(String[] args) {
        Micronaut.run(Application.class, args);

//        String secretKey = "SUPERSECRETKEY";

//        String token = TOTP.generateTOTP(secretKey);

//        Scanner sc = new Scanner(System.in);
//
//        System.out.println("Enter token");
//
//        String tokenInput = sc.nextLine();
//
//        boolean isValid = TOTP.validate(secretKey, tokenInput);
//
//        System.out.println(isValid ? "Valid" : "Invalid");
    }
}
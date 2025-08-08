package space.quinoaa.lexlist;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class Hash {

    public static String hash(String password) {
        Argon2 argon2 = Argon2Factory.create();

        return argon2.hash(10, 65536, 1, password.toCharArray());
    }

    public static boolean compare(String password, String hash){
        Argon2 argon2 = Argon2Factory.create();

        return argon2.verify(hash, password.toCharArray());
    }

}

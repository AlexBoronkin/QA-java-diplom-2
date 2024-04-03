package Praktikum;

import org.apache.commons.lang3.RandomStringUtils;

public class User implements Cloneable {

    private String email;
    private String password;
    private String name;

    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }


    public User() {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static User getStaticUser() {
        return new User("test@google.com", "13456", "Александр");
    }

    public static User getRandomUser() {
        return new User(RandomStringUtils.randomAlphabetic(15) + "@yandex.ru", "1234567", "Александр");
    }

    @Override
    public User clone() {
        User cloned = new User();
        cloned.setName(this.name);
        cloned.setEmail(this.email);
        cloned.setPassword(this.password);
        return cloned;

    }


}

package org.grumpyf0x48.gradle_quickstart;

public class Application {

    private final Hello hello;

    public Application() {
        this.hello = new HelloService();
    }

    public static void main(String[] args) {
        System.out.println(new Application().getGreeting());
    }

    public String getGreeting() {
        return hello.getGreeting();
    }
}

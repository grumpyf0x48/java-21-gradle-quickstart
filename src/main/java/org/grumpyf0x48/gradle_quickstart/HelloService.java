package org.grumpyf0x48.gradle_quickstart;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static java.util.stream.Collectors.joining;

public class HelloService implements Hello {

    @Override
    public String getGreeting() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/greeting.txt")))) {
            return reader.lines().filter(line -> !line.isEmpty()).collect(joining("\n"));
        } catch (IOException exception) {
            return "Hello World!";
        }
    }
}

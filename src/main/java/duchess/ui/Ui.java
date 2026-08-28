package duchess.ui;

import java.util.Scanner;

class Ui {
    private final String displayName;
    private final String banner;
    private final String separator;
    private final Scanner input;

    Ui(String name, String banner, String separator) {
        this.displayName = name;
        this.banner = banner;
        this.separator = separator;

        this.input = new Scanner(System.in);
    }

    public String listen() {
        return input.nextLine().toLowerCase();
    }

    public void say(String message) {
        System.out.println(this.separator);
        System.out.println(message);
        System.out.println(this.separator);
    }


    public void greet() {
        say(this.banner
                + "Hello! I am " + this.displayName + ". \n"
                + "What can I do for you?");
    }

    public void exit() {
        say("Bye! Hope to see you again soon!");
    }
}

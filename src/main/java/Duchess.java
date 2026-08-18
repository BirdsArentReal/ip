public class Duchess {
    public static void main(String[] args) {
        printSeparator();
        greet();
        printSeparator();
        exit();
        printSeparator();
    }

    private static void printSeparator() {
        // 40 underscores
        final String separator = "*________________________________________*";
        System.out.println(separator);

    }

    private static void greet() {
        final String name = "Duchess";
        printBanner();
        System.out.println("Hello! I am " + name + ".");
        System.out.println("What can I do for you?");

    }

    private static void exit() {
        System.out.println("Bye! Hope to see you again soon!");
    }

    private static void printBanner() {
        String banner =
                " ____  _   _  _____    _ ____  ___  ___\n"
                        + "|  _ \\| | | |/ __/ |  | | ___|/   \\/   \\\n"
                        + "| | | | | | | |  | |__| | |__|  | |  | |\n"
                        + "| | | | | | | |  |  __  |  __|\\  \\/\\  \\/\n"
                        + "| |_| | \\_/ | |__| |  | | |__ /\\  \\/\\  \\\n"
                        + "|____/ \\___/ \\___\\_|  |_|____|\\___/\\___/\n";
        System.out.println(banner);
    }
}

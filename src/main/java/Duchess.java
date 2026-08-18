public class Duchess {
    public static void main(String[] args) {
        greet();
    }

    private static void greet() {
        final String name = "Duchess";
        printBanner();
        System.out.println("Hello! I am " + name + ".");
        System.out.println("What can I do for you?");

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

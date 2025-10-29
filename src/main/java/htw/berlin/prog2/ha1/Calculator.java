package htw.berlin.prog2.ha1;

public class Calculator {

    private String screen = "0";
    private double latestValue;
    private String latestOperation = "";
    private boolean waitingForNewInput = false;

    public String readScreen() {
        return screen;
    }

    public void pressDigitKey(int digit) {
        if (digit > 9 || digit < 0) throw new IllegalArgumentException();

        if (waitingForNewInput || screen.equals("0")) {
            screen = "";
            waitingForNewInput = false;
        }

        screen = screen + digit;
    }

    public void pressClearKey() {
        screen = "0";
        latestValue = 0.0;
        latestOperation = "";
        waitingForNewInput = false;
    }

    public void pressBinaryOperationKey(String operation) {
        latestValue = Double.parseDouble(screen);
        latestOperation = operation;
        waitingForNewInput = true;
    }

    public void pressUnaryOperationKey(String operation) {
        latestValue = Double.parseDouble(screen);
        latestOperation = operation;

        var result = switch (operation) {
            case "√" -> {
                double value = Double.parseDouble(screen);
                if (value < 0) yield Double.NaN;
                else yield Math.sqrt(value);
            }
            case "%" -> Double.parseDouble(screen) / 100;
            case "1/x" -> 1 / Double.parseDouble(screen);
            default -> throw new IllegalArgumentException();
        };

        screen = Double.toString(result);
        if (screen.equals("NaN")) screen = "Error";
        if (screen.endsWith(".0")) screen = screen.substring(0, screen.length() - 2);
        if (screen.contains(".") && screen.length() > 11) screen = screen.substring(0, 10);

        waitingForNewInput = true;
    }

    public void pressDotKey() {
        if (waitingForNewInput) {
            screen = "0.";
            waitingForNewInput = false;
        } else if (!screen.contains(".")) {
            screen = screen + ".";
        }
    }

    public void pressNegativeKey() {
        screen = screen.startsWith("-") ? screen.substring(1) : "-" + screen;
    }

    public void pressEqualsKey() {
        if (latestOperation.isEmpty()) return;

        var result = switch (latestOperation) {
            case "+" -> latestValue + Double.parseDouble(screen);
            case "-" -> latestValue - Double.parseDouble(screen);
            case "x" -> latestValue * Double.parseDouble(screen);
            case "/" -> Double.parseDouble(screen) == 0 ? Double.POSITIVE_INFINITY : latestValue / Double.parseDouble(screen);
            default -> throw new IllegalArgumentException();
        };

        screen = Double.toString(result);

        if (screen.equals("Infinity")) screen = "Error";
        if (screen.endsWith(".0")) screen = screen.substring(0, screen.length() - 2);
        if (screen.contains(".") && screen.length() > 11) screen = screen.substring(0, 10);

        waitingForNewInput = true;
    }
}
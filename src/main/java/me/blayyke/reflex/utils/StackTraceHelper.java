package me.blayyke.reflex.utils;

public class StackTraceHelper {
    private final String basePackage;

    public StackTraceHelper(String basePackage) {
        this.basePackage = basePackage;
    }

    public String handle(StackTraceElement[] arr) {
        StringBuilder builder = new StringBuilder(arr[0].toString() + "\nInteresting lines:");
        for (StackTraceElement e : arr) {
            if (e.getClassName().startsWith(basePackage)) {
                builder.append(e.getFileName()).append(":").append(e.getLineNumber()).append("\n");
            }
        }
        builder.append("Causes:");
        for (StackTraceElement e : arr) {
            if (e.toString().startsWith("Caused by")) {
                builder.append(e.toString().substring("Caused by: ".length()));
            }
        }
        return builder.toString();
    }
}

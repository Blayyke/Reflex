package me.blayyke.reflex.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;
import java.util.stream.Collectors;

public class Version {
    public static String VERSION;

    public static String getMajor() {
        return getVersion().split(".")[0];
    }

    public static String getMinor() {
        return getVersion().split(".")[0];
    }

    public static String getVersion() {
        return VERSION;
    }

    public static void loadVersion() throws IOException {
        File f = new File(Objects.requireNonNull(Version.class.getClassLoader().getResource("version.txt")).getFile());

        VERSION = Files.lines(f.toPath()).collect(Collectors.joining("\n"));
    }
}
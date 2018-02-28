package me.blayyke.reflex.utils;

public class TextUtils {
    public static boolean isAllLetters(CharSequence charSequence) {
        return charSequence.chars().allMatch(Character::isLetter);
    }

    public static String escapeFormatting(String prefix) {
        if (prefix == null) return null;
        return prefix
                .replace("`", "\\`")
                .replace("*", "\\*")
                .replace("_", "\\_");
    }

    public static String uppercaseFirst(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private static String replaceCursive(char input) {
        switch (input) {
            case 'a':
                return "𝒶";
            case 'b':
                return "𝒷";
            case 'c':
                return "𝒸";
            case 'd':
                return "𝒹";
            case 'e':
                return "𝑒";
            case 'f':
                return "𝒻";
            case 'g':
                return "𝑔";
            case 'h':
                return "𝒽";
            case 'i':
                return "𝒾";
            case 'j':
                return "𝒿";
            case 'k':
                return "𝓀";
            case 'l':
                return "𝓁";
            case 'm':
                return "𝓂";
            case 'n':
                return "𝓃";
            case 'o':
                return "𝑜";
            case 'p':
                return "𝓅";
            case 'q':
                return "𝓆";
            case 'r':
                return "𝓇";
            case 's':
                return "𝓈";
            case 't':
                return "𝓉";
            case 'u':
                return "𝓊";
            case 'v':
                return "𝓋";
            case 'w':
                return "𝓌";
            case 'x':
                return "𝓍";
            case 'y':
                return "𝓎";
            case 'z':
                return "𝓏";

            case 'A':
                return "𝒜";
            case 'B':
                return "𝐵";
            case 'C':
                return "𝒞";
            case 'D':
                return "𝒟";
            case 'E':
                return "𝐸";
            case 'F':
                return "𝐹";
            case 'G':
                return "𝒢";
            case 'H':
                return "𝐻";
            case 'I':
                return "𝐼";
            case 'J':
                return "𝒥";
            case 'K':
                return "𝒦";
            case 'L':
                return "𝐿";
            case 'M':
                return "𝑀";
            case 'N':
                return "𝒩";
            case 'O':
                return "𝒪";
            case 'P':
                return "𝒫";
            case 'Q':
                return "𝒬";
            case 'R':
                return "𝑅";
            case 'S':
                return "𝒮";
            case 'T':
                return "𝒯";
            case 'U':
                return "𝒰";
            case 'V':
                return "𝒱";
            case 'W':
                return "𝒲";
            case 'X':
                return "𝒳";
            case 'Y':
                return "𝒴";
            case 'Z':
                return "𝒵";

            case '1':
                return "𝟣";
            case '2':
                return "𝟤";
            case '3':
                return "𝟥";
            case '4':
                return "𝟦";
            case '5':
                return "𝟧";
            case '6':
                return "𝟨";
            case '7':
                return "𝟩";
            case '8':
                return "𝟪";
            case '9':
                return "𝟫";
            case '0':
                return "𝟢";
            default:
                return String.valueOf(input);
        }
    }

    public static String toCursive(String input) {
        StringBuilder out = new StringBuilder();
        for (char c : input.toCharArray()) {
            out.append(replaceCursive(c));
        }
        return out.toString();
    }
}
package com.promcteam.genesis.managers.misc;

import net.md_5.bungee.api.chat.BaseComponent;

import java.util.List;

public class StringManipulationLib {
    /**
     * Replace placeholders in a string
     *
     * @param s               input string
     * @param placeholderName name of placeholder
     * @param replacement     replacement string
     * @param fromIndex       index
     * @return replaced string
     */
    public static String replacePlaceholder(String s, String placeholderName, String replacement, int fromIndex) {
        String complete = getCompleteVariable(s, placeholderName, fromIndex);
        if (complete != null) {
            return s.replace(complete, replacement);
        }
        return s;
    }

    public static String figureOutVariable(String s, int fromIndex, String... placeholderNames) {
        for (String placeholderName : placeholderNames) {
            String variable = figureOutVariable(s, placeholderName, fromIndex);
            if (variable != null) {
                return variable;
            }
        }
        return null;
    }

    public static String figureOutVariable(String s, String placeholderName, int fromIndex) {
        String symbol   = "%";
        String start    = symbol + placeholderName + "_";
        String complete = getCompleteVariable(s, placeholderName, fromIndex);
        if (complete != null) {
            String variable = complete.substring(start.length(), complete.length() - symbol.length());
            return variable;
        }
        return null;
    }

    public static String getCompleteVariable(String s, String placeholderName, int fromIndex) {
        String symbol = "%";
        String start  = symbol + placeholderName + "_";
        if (s.contains(start)) {
            int firstOccurrenceStart = s.indexOf(start, fromIndex);
            if (firstOccurrenceStart != -1) {
                int firstOccurrenceEnd = s.indexOf(symbol, firstOccurrenceStart + 1);
                if (firstOccurrenceEnd != -1) {
                    String complete = s.substring(firstOccurrenceStart, firstOccurrenceEnd + 1);
                    return complete;
                }
            }
        }
        return null;
    }

    public static int getIndexOfVariableEnd(String s, String placeholderName, int fromIndex) {
        String symbol = "%";
        String start  = symbol + placeholderName + "_";
        if (s.contains(start)) {
            int firstOccurrenceStart = s.indexOf(start, fromIndex);
            int firstOccurrenceEnd   = s.indexOf(symbol, firstOccurrenceStart + 1);
            return firstOccurrenceEnd;
        }
        return -1;
    }

    public static String formatList(List<String> list) {
        if (list == null || list.isEmpty()) {
            return "";
        }
        StringBuilder output = null;
        for (String s : list) {
            if (output == null) {
                output = new StringBuilder(s);
            } else {
                BaseComponent baseComponent = InputReader.readChatComponent(s);
                output.append("\n").append(baseComponent == null ? s : baseComponent.toString());
            }
        }
        return output.toString();
    }


    public static String getBlock(String s, String beginning, String end, int fromIndex) {
        if (s.contains(beginning) && s.contains(end)) {
            int firstOccurrenceStart = s.indexOf(beginning, fromIndex);
            if (firstOccurrenceStart != -1) {
                int firstOccurrenceEnd = s.indexOf(end, firstOccurrenceStart + 1);
                if (firstOccurrenceEnd != -1) {
                    String complete = s.substring(firstOccurrenceStart, firstOccurrenceEnd + 1);
                    return complete;
                }
            }
        }
        return null;
    }

    public static int getIndexOfBlockEnd(String s, String beginning, String end, int fromIndex) {
        if (s.contains(beginning) && s.contains(end)) {
            int firstOccurrenceStart = s.indexOf(beginning, fromIndex);
            if (firstOccurrenceStart != -1) {
                int firstOccurrenceEnd = s.indexOf(end, firstOccurrenceStart + 1);
                return firstOccurrenceEnd;
            }
        }
        return -1;
    }


}

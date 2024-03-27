package org.black_ixx.bossshop.misc;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatUT {
    public static final MiniMessage Serializer = MiniMessage.builder().build();

    public static Component hexComp(String message) {
        message = message.replace("§", "&");
        message = parseHexColorCodes(message);
        message = parseNativeColorCodes(message);
        return Serializer.deserialize(message);
    }

    private static String parseHexColorCodes(String message) {
        Pattern pattern = Pattern.compile("&#[a-fA-F0-9]{6}");
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            String hexCode = message.substring(matcher.start(), matcher.end());
            String replaceSharp = hexCode.replace("&", "");

            message = message.replace(hexCode, "<color:" + replaceSharp + ">");
            matcher = pattern.matcher(message);
        }
        return message;
    }

    private static String parseNativeColorCodes(String message) {
        Pattern pattern = Pattern.compile("&[a-flmnokrA-F0-9]");
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            String colorCode = message.substring(matcher.start(), matcher.end());
            String replaceSharp = colorCode.replace("&", "");

            message = message.replace(colorCode, getNativeColor(replaceSharp.toCharArray()[0]));
            matcher = pattern.matcher(message);
        }
        return message;
    }

    private static String getNativeColor(char color) {
        String reset = "<!b><!i><!obf><!u><!st>";
        String colorCode = "<color:white>";
        if (color == 'r') return reset + colorCode;

        // TODO using java 14 or greater might be better for this switch statement in future
        switch (color) {
            case 'a':
                colorCode = "<color:green>";
                break;
            case 'b':
                colorCode = "<color:aqua>";
                break;
            case 'c':
                colorCode = "<color:red>";
                break;
            case 'd':
                colorCode = "<color:light_purple>";
                break;
            case 'e':
                colorCode = "<color:yellow>";
                break;
            case 'f':
                colorCode = "<color:white>";
                break;
            case '0':
                colorCode = "<color:black>";
                break;
            case '1':
                colorCode = "<color:dark_blue>";
                break;
            case '2':
                colorCode = "<color:dark_green>";
                break;
            case '3':
                colorCode = "<color:dark_aqua>";
                break;
            case '4':
                colorCode = "<color:dark_red>";
                break;
            case '5':
                colorCode = "<color:dark_purple>";
                break;
            case '6':
                colorCode = "<color:gold>";
                break;
            case '7':
                colorCode = "<color:gray>";
                break;
            case '8':
                colorCode = "<color:dark_gray>";
                break;
            case '9':
                colorCode = "<color:blue>";
                break;
            case 'l':
                colorCode = "<bold>";
                break;
            case 'm':
                colorCode = "<strikethrough>";
                break;
            case 'n':
                colorCode = "<u>";
                break;
            case 'o':
                colorCode = "<italic>";
                break;
            case 'k':
                colorCode = "<obf>";
                break;
            default:
                colorCode = reset + colorCode;
                break;
        }
        return colorCode;
    }
}

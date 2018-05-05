package com.hantong.util;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

import static org.fusesource.jansi.Ansi.ansi;

public class Echo {
    public static void println(String str) {
        System.out.println(str);
    }

    public static void color(Ansi.Color color,String str) {
        System.out.println(ansi().eraseScreen().fg(color).a(str).reset());
    }

    public static void red(String str) {
        Echo.color(Ansi.Color.RED,str);
    }

    public static void green(String str) {
        Echo.color(Ansi.Color.GREEN,str);
    }

    public static void yellow(String str) {
        Echo.color(Ansi.Color.YELLOW,str);
    }

    public static void blue(String str) {
        Echo.color(Ansi.Color.BLUE,str);
    }
}

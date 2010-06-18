/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.cs.rgb.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author fedo
 */
public class TestRegex {

    public static void main(String[] args) {
        String encoding = "error";
        Pattern p = Pattern.compile(".*<\\?.*encoding=.(.*).\\?>.*", Pattern.DOTALL);

        Matcher matcher = p.matcher("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");

        if (matcher.matches()) {
            encoding = matcher.group(1);
            System.out.println(matcher.group(1));
        }

         p = Pattern.compile(
                "<row><column>(.*)</column></row>",
                Pattern.DOTALL);

         matcher = p.matcher(
                "<row><column>Header\n\n\ntext</column></row>");

        if (matcher.matches()) {
            System.out.println(matcher.group(1));
        }


    }
}

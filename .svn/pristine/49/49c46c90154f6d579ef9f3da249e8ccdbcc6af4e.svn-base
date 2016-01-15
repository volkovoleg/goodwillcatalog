package ru.goodfil.catalog.process;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: Process.java 93 2012-09-23 06:43:51Z sazonovkirill $
 */
public class Process {
    public static boolean exists(String name) throws IOException, InterruptedException {
        java.lang.Process process = Runtime.getRuntime().exec("tasklist /v /fo csv");
        BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        boolean found = false;
        do {
            line = stdout.readLine();
            if (line != null && line.contains(name)) found = true;
        }
        while (line != null);
        int exitCode = process.waitFor();
        return found;
    }
}

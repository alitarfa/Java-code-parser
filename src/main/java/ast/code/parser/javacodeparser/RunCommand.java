package ast.code.parser.javacodeparser;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class RunCommand {

    public static void main(String[] args) throws Exception {
        Runtime runtime = Runtime.getRuntime();
        Process p = runtime.exec("jdeps -v /home/tarfa/MainActivity.class");
        List<String> list = new ArrayList<>();
        new Thread(new Runnable() {
            public void run() {
                BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line = null;
                try {
                    while ((line = input.readLine()) != null)
                        //System.out.println(line);
                        list.add(line);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        p.waitFor();


        list.stream()
                .filter(s -> s.contains("com.carl.touch"))
                .filter(s -> !s.contains("java.lang"))
                .filter(s -> !s.contains("android.os"))
                .filter(s -> !s.contains("android.app"))
                .filter(s -> !s.contains("android.content.Context"))
                .filter(s -> !s.contains("android.content.Intent"))
                .map(s -> s.split("->"))
                .map(strings -> strings[1])
                .map(s -> s.split("\\."))
                .map(strings -> strings[strings.length - 1])
                .map(s -> s.split(" "))
                .map(strings -> strings[0])
                .forEach(System.out::println);
    }

}

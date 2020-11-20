package ast.code.parser.javacodeparser.service.jsonhandler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class JsonHandler<T> {

    public void generateJson(T object) {
        try {
            Writer writer = new FileWriter("/home/tarfa/clusters/clusterA_v1.json");
            Gson gson = new GsonBuilder().create();
            gson.toJson(object, writer);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package pecunia_22.services.chartServices;

import com.google.gson.Gson;
import jakarta.json.JsonArray;
import lombok.AllArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;
import pecunia_22.chartClass.DeclaredMethod;
import pecunia_22.chartClass.ReportMethod;
import pecunia_22.chartClass.Variable;
import utils.JsonUtils;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class ChartServiceImpl implements ChartService {

    @Override
    public Map<String, Object> getYamlToObj(String reportName) {
        Yaml yaml = new Yaml();
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream("static/reportsChart/reports/" + reportName + ".yml");
        Map<String, Object> mapObject = yaml.load(inputStream);

        return mapObject;
    }

    @Override
    public List<DeclaredMethod> declaredMethodList(Class c) {
        List<DeclaredMethod> declaredMethodList = new ArrayList<>();
        Method[] methods = c.getDeclaredMethods();
        for (Method method : methods) {
            System.out.println(method.getName() + " == " + method.getParameters().length);
            declaredMethodList.add(new DeclaredMethod(method.getName(), method.getParameters().length));
        }
        return declaredMethodList;
    }

    @Override
    public List<ReportMethod> reportMethodList(Class c) {
        //        Class<ChartRepository> c = ChartRepository.class;
//        Class claz = ChartRepository.class;
//        Method[] methods = c.getMethods();
        List<ReportMethod> reportMethods =new ArrayList<>();
//        List<Variable> variables = new ArrayList<>();
        Method[] methods = c.getDeclaredMethods();
        System.out.println("_________________________________________________________________________________________________________________________");
        System.out.printf("|- %-40s |%-20s |%-30s |%-20s |%n", "Report Name", "quality param", "Type Param", "Name Param");
        for (Method method : methods) {
//                System.out.println(method.getName() + " == " + method.getParameters().length);
            System.out.println("|-----------------------------------------------------------------------------------------------------------------------|");
            if (method.getParameters().length > 0) {
                List<Variable> variables = new ArrayList<>();
                System.out.printf("|- %-40s |%-20s |%-30s |%-20s |%n", method.getName(), method.getParameters().length, (method.getParameters()[0]).getType().getName() , (method.getParameters()[0]).getName());
                for (Parameter parameter : method.getParameters()) {
                    variables.add(new Variable(parameter.getType().getName(), parameter.getName()));
                }
                reportMethods.add(new ReportMethod(method.getName(), method.getParameters().length, variables));
            }
            else {
                System.out.printf("|- %-40s |%-20s |%n", method.getName(), method.getParameters().length );
                reportMethods.add(new ReportMethod(method.getName(), method.getParameters().length));
            }
        }
        System.out.println("|-----------------------------------------------------------------------------------------------------------------------|");

        System.out.println(JsonUtils.gsonPretty(reportMethods));
        return reportMethods;
    }

    @Override
    public Object dataToChart(String report, List<Object[]> objects) {
        Map<String, Object> mapObject = getYamlToObj(report);
        JSONObject jsonObject = new JSONObject(mapObject);

        System.out.println("*******************TEST START***************************");

        for (int i = 0; i < objects.size() ; i++) {
            System.out.println(objects.get(0).length);
            System.out.println(objects.get(i));
            for (int j = 0; j < objects.get(i).length; j++) {
//                System.out.println(objects.toString());
            }
        }

        System.out.println("*******************TEST KONIEC***************************");

        System.out.println("XXXXXXXXXXXXXXXXXXXXXXX START XXXXXXXXXXXXXXXXXXXXXXXXXXXX");
//        System.out.println(jsonObject);
        System.out.println(objects.size());
        System.out.println(JsonUtils.gsonPretty(objects));
        System.out.println("XXXXXXXXXXXXXXXXXXXXXXX STOP XXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        List<Object> labels = new ArrayList();
        List<Object> data = new ArrayList();
        List<Object> data1 = new ArrayList();
        List<Object> data2 = new ArrayList();

        List<List<Object>> data5 = new ArrayList();
        for (Object[] object : objects) {
            if (object[0] instanceof String) {
                System.out.println(object.length);
                labels.add(object[0].toString());
                data.add((object[1]));
                data1.add((object[2]));
//                data2.add((object[3]));
            } else {
                labels.add("NULL");
                data.add(Integer.valueOf(object[1].toString()));
            }

        }


        System.out.println("===================================JSONObject++++++++++++++++++++++++++++++++++++++D");
        System.out.println((JsonUtils.gsonPretty(jsonObject.getJSONObject("chart").getJSONObject("datasets"))));

        JSONArray hoverBackgroundColor = new JSONArray();
        JSONArray backgroundColor = new JSONArray();
        JSONArray label = new JSONArray();


        hoverBackgroundColor.put(jsonObject.getJSONObject("chart").getJSONObject("datasets").getJSONArray("hoverBackgroundColor"));
        backgroundColor.put(jsonObject.getJSONObject("chart").getJSONObject("datasets").getJSONArray("backgroundColor"));
        label.put(jsonObject.getJSONObject("chart").getJSONObject("datasets").getJSONArray("label"));

        System.out.println("===============KOLOR======================");
        System.out.println(hoverBackgroundColor.length());
        System.out.println(hoverBackgroundColor.getJSONArray(0));
        System.out.println(label.getJSONArray(0));
        System.out.println("===============KOLOR======================");


        JSONArray datasets = new JSONArray();
        JSONObject dataset = new JSONObject();
        JSONObject dataset1 = new JSONObject();
//        JSONObject dataset2 = new JSONObject();

        dataset.put("data", data);
        dataset.put("label", "KOLEKCJA");
        dataset.put("hoverBackgroundColor",hoverBackgroundColor.getJSONArray(0).get(1));
        dataset.put("backgroundColor", backgroundColor.getJSONArray(0).get(1));
        dataset.put("hoverOffset", 4);
        dataset.put("borderColor", backgroundColor.get(0));
//        System.out.println(dataset);
        datasets.put(dataset);

        dataset1.put("data", data1);
        dataset1.put("label", "FOR SELL");
        dataset1.put("hoverBackgroundColor",hoverBackgroundColor.getJSONArray(0).get(2));
        dataset1.put("backgroundColor", backgroundColor.getJSONArray(0).get(2));
        dataset1.put("hoverOffset", 4);
        dataset1.put("borderColor", backgroundColor.get(0));
//        dataset1.put("type","line");
        datasets.put(dataset1);
//        System.out.println(datasets);
//
//        dataset2.put("data", data2);
//        dataset2.put("label", "SOLD");
//        dataset2.put("hoverBackgroundColor",hoverBackgroundColor.getJSONArray(0).get(2));
//        dataset2.put("backgroundColor", backgroundColor.getJSONArray(0).get(2));
//        datasets.put(dataset2);
        System.out.println(JsonUtils.gsonPretty(datasets));



        jsonObject.getJSONObject("chart").put("datasets", new JSONArray());
        jsonObject.getJSONObject("chart").put("labels", labels);
        jsonObject.getJSONObject("chart").getJSONArray("datasets").put(dataset);
        jsonObject.getJSONObject("chart").getJSONArray("datasets").put(dataset1);
//        jsonObject.getJSONObject("chart").getJSONArray("datasets").put(dataset2);
//        jsonObject.getJSONObject("chart").getJSONObject("datasets").put("label", "KONTYNENTY TEST");
        System.out.println("===================================JSONObject++++++++++++++++++++++++++++++++++++++D");


        Object object = new Gson().fromJson(String.valueOf(jsonObject), Object.class);

        System.out.println("TU JESTEM");
        return object;
    }
}

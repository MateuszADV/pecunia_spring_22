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

        System.out.println(objects.size());
        System.out.println(objects.get(0).length);
        System.out.println("Jaki jest object");
        System.out.println(JsonUtils.gsonPretty(objects));

        Object[][] dane;
        if (objects.size() > 0) {
            dane = new Object[objects.get(0).length][objects.size()];
            for (int i = 0; i < objects.size(); i++) {
                for (int j = 0; objects.get(0).length > j; j++) {
                    dane[j][i] = objects.get(i)[j];
                }
            }
        } else {
            dane = null;
            System.out.println("DODANA WARTOŚĆ NULL");
        }

        System.out.println("*******************TEST KONIEC***************************");

//        List<Object> labels = new ArrayList();
//        List<Object> data = new ArrayList();
//        List<Object> data1 = new ArrayList();
//        List<Object> data2 = new ArrayList();

//        List<List<Object>> data5 = new ArrayList();
//        for (Object[] object : objects) {
//            if (object[0] instanceof String) {
//                System.out.println(object.length);
//                labels.add(object[0].toString());
//                data.add(object[1]);
//                data1.add((object[2]));
////                data2.add((object[3]));
//            } else {
//                labels.add("NULL");
//                data.add(Integer.valueOf(object[1].toString()));
//            }
//
//        }

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

        for (int i = 1; dane.length > i; i++) {
            try {
//            if (dane[0][0] instanceof String) {
                JSONObject dataset = new JSONObject();
//                System.out.println(dane.length);
                dataset.put("data", dane[i]);
//            dataset.put("label", "label- " + i);
                if (dane.length == 2) {
                    dataset.put("hoverBackgroundColor", hoverBackgroundColor.getJSONArray(0));
                    dataset.put("backgroundColor", backgroundColor.getJSONArray(0));
                    System.out.println("KOLORY DO ZMIANY");
                } else {
                    dataset.put("hoverBackgroundColor", hoverBackgroundColor.getJSONArray(0).get(i));
                    dataset.put("backgroundColor", backgroundColor.getJSONArray(0).get(i));
                }
                dataset.put("label", label.getJSONArray(0).get(i - 1));
                dataset.put("hoverOffset", 4);
                dataset.put("borderColor", backgroundColor.get(0));
                datasets.put(dataset);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
//            else {
////                labels.add("NULL");
////                data.add(Integer.valueOf(object[1].toString()));
//                System.out.println("-----------BŁĄD-----------------");
//            }
        }

        System.out.println(JsonUtils.gsonPretty(dane));
        System.out.println(datasets.length());

        jsonObject.getJSONObject("chart").put("datasets", new JSONArray());
        jsonObject.getJSONObject("chart").put("labels", dane[0]);

        for (int i = 0; datasets.length() > i; i++) {
            jsonObject.getJSONObject("chart").getJSONArray("datasets").put(datasets.get(i));
        }

        System.out.println("===================================JSONObject++++++++++++++++++++++++++++++++++++++D");

        Object object = new Gson().fromJson(String.valueOf(jsonObject), Object.class);

        System.out.println("TU JESTEM");
        return object;
    }
}

package pecunia_22.services.chartServices;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
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

        System.out.println("XXXXXXXXXXXXXXXXXXXXXXX START XXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        System.out.println(jsonObject);
        System.out.println(JsonUtils.gsonPretty(objects));
        System.out.println("XXXXXXXXXXXXXXXXXXXXXXX STOP XXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        List<String> labels = new ArrayList();
        List<Integer> data = new ArrayList();
        for (Object[] object : objects) {
            if (object[0] instanceof String) {
                System.out.println(object[0]);
                System.out.println("Powinno byc NULL");
                labels.add(object[0].toString());
                data.add(Integer.valueOf(object[1].toString()));
            } else {
                labels.add("NULL");
                data.add(Integer.valueOf(object[1].toString()));
                System.out.println("Nie powinien byc NULL");
            }

        }
        jsonObject.getJSONObject("chart").put("labels", labels);
        jsonObject.getJSONObject("chart").getJSONObject("datasets").put("data", data);

        Object object = new Gson().fromJson(String.valueOf(jsonObject), Object.class);

        System.out.println("TU JESTEM");
        return object;
    }
}

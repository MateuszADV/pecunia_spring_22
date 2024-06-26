package pecunia_22.services.chartServices;

import org.springframework.stereotype.Service;
import pecunia_22.chartClass.DeclaredMethod;
import pecunia_22.chartClass.ReportMethod;

import java.util.List;
import java.util.Map;

@Service
public interface ChartService {
    Map<String, Object> getYamlToObj (String reportName);
    List<DeclaredMethod> declaredMethodList (Class c);
    List<ReportMethod> reportMethodList(Class c);

    Object dataToChart(String report, List<Object[]> data);
}

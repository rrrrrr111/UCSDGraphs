package ozonairticket;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 */
public class StrReplacer {

    public static void main(String[] args) throws Exception {

        prepareToVilnus();
        prepareFromVilnus();
    }

    private static void putPersons(Map<String, String> replacings) {
        replacings.put("Churganova Uliana Ms", "Churganov Roman Mr");
        replacings.put("VIK515271", "715918371");
        replacings.put("Shchukina Irina Ms", "Shchukina Irina Ms");
        replacings.put("4614807193", "4614807193");
    }

    private static void prepareToVilnus() throws IOException {
        Map<String, String> replacings = new LinkedHashMap<>();

        replacings.put("05679331-0008", "05699332-0012");
        replacings.put("262-4980492974", "262-4977492374");
        replacings.put("Авиакомпания U6/NKBAKU; (Amadeus NKBAKU)", "Авиакомпания U6/NKBAKU; (Amadeus NKBAKU)");
        replacings.put("Airline U6/NKBAKU; (Amadeus NKBAKU)", "Airline U6/NKBAKU; (Amadeus NKBAKU)");

        putPersons(replacings);

        replacings.put("16:30, 2 июля 2017", "08:25, 7 августа 2017");
        replacings.put("16:30, 2 July 2017", "08:25, 7 August 2017");
        replacings.put("17:25, 2 июля 2017", "09:00, 7 августа 2017");
        replacings.put("17:25, 2 July 2017", "09:00, 7 August 2017");
        replacings.put("U6 697", "B2-801");
        replacings.put("10 кг", "20 кг");
        replacings.put("10 kg", "20 kg");

        replacings.put("Домодедово (DME)", "Минск (MSQ)");
        replacings.put("Domodedovo (DME)", "Minsk (MSQ)");
        replacings.put("Москва", "Минск");
        replacings.put("Moscow", "Minsk");
        replacings.put("Уральские авиалинии, эконом, N, NPROW", "Белавиа, U Bombardier CRJ 200, эконом-класс");
        replacings.put("Уральские авиалинии, economy, N, NPROW", "Belavia, U Bombardier CRJ 200, economy");
        replacings.put("Уральские авиалинии", "Белавиа");
        replacings.put("Храброво (KGD)", "Вильнюс (VNO)");
        replacings.put("Khrabrovo (KGD)", "Vilnius (VNO)");
        replacings.put("Калининград", "Вильнюс");
        replacings.put("Kaliningrad", "Vilnius");

        replacings.put("Минск, Чапаевский", "Москва, Чапаевский");
        replacings.put("Minsk, Russia", "Moscow, Russia");
        replacings.put("Белавиа, eco", "Belavia, eco");
        replacings.put("marker=\"en\">Белавиа", "marker=\"en\">Belavia");

        copyTemplate("_toVilnus", replacings);
    }

    private static void prepareFromVilnus() throws IOException {
        Map<String, String> replacings = new LinkedHashMap<>();

        replacings.put("05679331-0008", "05699332-0012");
        replacings.put("262-4980492974", "262-4977492375");
        replacings.put("Авиакомпания U6/NKBAKU; (Amadeus NKBAKU)", "Авиакомпания U6/NKBAKU; (Amadeus NKBAKU)");
        replacings.put("Airline U6/NKBAKU; (Amadeus NKBAKU)", "Airline U6/NKBAKU; (Amadeus NKBAKU)");

        putPersons(replacings);

        replacings.put("16:30, 2 июля 2017", "20:40, 11 августа 2017");
        replacings.put("16:30, 2 July 2017", "20:40, 11 August 2017");
        replacings.put("17:25, 2 июля 2017", "21:15, 11 августа 2017");
        replacings.put("17:25, 2 July 2017", "21:15, 11 August 2017");
        replacings.put("U6 697", "B2-804");
        replacings.put("10 кг", "20 кг");
        replacings.put("10 kg", "20 kg");

        replacings.put("Домодедово (DME)", "Вильнюс (VNO)");
        replacings.put("Domodedovo (DME)", "Vilnius (VNO)");
        replacings.put("Москва", "Вильнюс");
        replacings.put("Moscow", "Vilnius");
        replacings.put("Уральские авиалинии, эконом, N, NPROW", "Белавиа, U Bombardier CRJ 200, эконом-класс");
        replacings.put("Уральские авиалинии, economy, N, NPROW", "Belavia, U Bombardier CRJ 200, economy");
        replacings.put("Уральские авиалинии", "Белавиа");
        replacings.put("Храброво (KGD)", "Минск (MSQ)");
        replacings.put("Khrabrovo (KGD)", "Minsk (MSQ)");
        replacings.put("Калининград", "Минск");
        replacings.put("Kaliningrad", "Minsk");

        replacings.put("Вильнюс, Чапаевский", "Москва, Чапаевский");
        replacings.put("Vilnius, Russia", "Moscow, Russia");
        replacings.put("Белавиа, eco", "Belavia, eco");
        replacings.put("marker=\"en\">Белавиа", "marker=\"en\">Belavia");

        copyTemplate("_fromVilnus", replacings);
    }

    private static void copyTemplate(String suff, Map<String, String> replacings) throws IOException {
        String dir = "C:\\Users\\Roman\\Desktop\\визы_литва\\до литвы";
        String filename = "Gmail - Маршрут-квитанция по заказу № 05679331-0008 в интернет-магазине путешествий OZON.travel";
        String suffix = ".html";

        List<String> resLines =
                Files.readAllLines(Paths.get(dir, filename + suffix))
                        .stream()
                        .map(str -> {

                            for (Map.Entry<String, String> entry : replacings.entrySet()) {
                                str = str.replace(entry.getKey(), entry.getValue());
                            }
                            return str;

                        }).collect(Collectors.toList());

        Files.write(Paths.get(dir, filename + suff + suffix), resLines);
    }
}

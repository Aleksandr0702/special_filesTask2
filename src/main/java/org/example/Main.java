package org.example;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<Employee> list = parseXML("data.xml");
        String json = listToJson(list);
        writeString(json, "data2.json");
    }

    public static List<Employee> parseXML(String fileName) {
        List<Employee> employeeList = new ArrayList<>();
        try {
            File file = new File(fileName);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("employee");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    long id = Long.parseLong(getTagValue("id", element));
                    String firstName = getTagValue("firstName", element);
                    String lastName = getTagValue("lastName", element);
                    String country = getTagValue("country", element);
                    int age = Integer.parseInt(getTagValue("age", element));

                    Employee employee = new Employee(id, firstName, lastName, country, age);
                    employeeList.add(employee);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return employeeList;
    }

    public static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = nodeList.item(0);
        return node.getNodeValue();
    }

    public static String listToJson(List<Employee> list) {
        JSONArray jsonArray = new JSONArray();
        for (Employee employee : list) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", employee.id);
            jsonObject.put("firstName", employee.firstName);
            jsonObject.put("lastName", employee.lastName);
            jsonObject.put("country", employee.country);
            jsonObject.put("age", employee.age);
            jsonArray.put(jsonObject);
        }
        return jsonArray.toString(2);
    }

    public static void writeString(String json, String fileName) {
        try (FileWriter file = new FileWriter(fileName)) {
            file.write(json);
            file.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


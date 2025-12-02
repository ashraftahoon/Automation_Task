package utilities;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


public class JsonDataReader {

    public List<String> emails = new ArrayList<>();
    public List<String> passwords = new ArrayList<>();
    public List<String> userTypes = new ArrayList<>();
    public String expectedSuccessLogin;

    public void dataReader(String userType) throws IOException, org.json.simple.parser.ParseException {
        String JSON_FILE_PATH = System.getProperty("user.dir")+ "/src/test/resources/test-data.json";
        File jsonFile =new File(JSON_FILE_PATH);
        FileReader reader = new FileReader(jsonFile);
        JSONParser jsonparse = new JSONParser();
        Object obj= jsonparse.parse(reader);
        JSONObject jsonData= (JSONObject) obj;
//        JSONObject messages = (JSONObject) jsonData.get("messages");
//
//        expectedSuccessLogin =  (String) messages.get("expectedSuccessLoginMessage");


        // get users
        JSONArray array = (JSONArray)jsonData.get("users");
        for (int i=0; i< array.size(); i++)
        {
            JSONObject users= (JSONObject)array.get(i);
            String userTypeFromJson = (String) users.get("usertype");
            if (userTypeFromJson.equals(userType))
            {
                emails.add((String) users.get("email"));
                passwords.add((String) users.get("password"));
                return;
            }

        }

    }






}

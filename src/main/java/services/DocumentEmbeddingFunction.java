package services;

import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DocumentEmbeddingFunction implements HttpFunction {

  private static final DataAccess dao = new DataAccess();


  // ... other methods and dependencies ...

  @Override
  public void service(HttpRequest request, HttpResponse response) throws IOException {
    try {
      // Get request body as a Map
      String requestBody = getRequestBodyAsString(request);

      // 2. Parse the JSON string into a Map
      Map<String, Object> body = parseJsonToMap(requestBody);

      // Get document name and bucket
      String fileName = (String) body.get("fileName");
      //      String bucketName = (String) body.get("bucket");
      insertBook(fileName);
      // ... your embedding logic using fileName and bucketName ...
      response.setStatusCode(200);
    } catch (Exception e) {
      response.setStatusCode(500);
      response.getWriter().write("Invalid JSON request body");
    }
  }

  private String getRequestBodyAsString(HttpRequest request) throws IOException {
    StringBuilder stringBuilder = new StringBuilder();
    try (BufferedReader reader = request.getReader()) {
      String line;
      while ((line = reader.readLine()) != null) {
        stringBuilder.append(line);
      }
    }
    return stringBuilder.toString();
  }

  // Helper function to parse a JSON string into a Map
  private Map<String, Object> parseJsonToMap(String jsonString) {
    Map<String, Object> map = new HashMap<>();

    if (jsonString == null || jsonString.trim().isEmpty()) {
      return map; // Return empty map for null or empty input
    }

    jsonString = jsonString.trim();

    // Remove opening and closing curly braces
    if (jsonString.startsWith("{") && jsonString.endsWith("}")) {
      jsonString = jsonString.substring(1, jsonString.length() - 1);
    }

    // Split into key-value pairs
    String[] keyValuePairs = jsonString.split(",");

    for (String keyValuePair : keyValuePairs) {
      keyValuePair = keyValuePair.trim();
      // Split key and value by colon
      String[] parts = keyValuePair.split(":");
      if (parts.length == 2) {
        String key = parts[0].trim();
        String value = parts[1].trim();

        // Remove quotes from key and value if present
        key = key.replaceAll("^\"|\"$", "");
        value = value.replaceAll("^\"|\"$", "");

        map.put(key, value);
      }
    }
    return map;
  }

  public Integer insertBook(String fileName) {
    String author = FileUtility.getAuthor(fileName);
    author = SqlUtility.replaceUnderscoresWithSpaces(author);
    String title = FileUtility.getTitle(fileName);
    title = SqlUtility.replaceUnderscoresWithSpaces(title);
    String year = FileUtility.getYear(fileName);
    String publicPrivate = FileUtility.getPublicPrivate(fileName);
    Map<String, Object> book = null;
    Integer bookId = 0;
    try {
          book = dao.findBook(title);
          Map<String, Object> authorMap = dao.findAuthor(author);
          Object authorId = authorMap.get("author_id");

          if(!book.isEmpty()){
            bookId = (Integer) book.get("book_id");
          } else {
            if(authorId==null)
              authorId = dao.insertAuthor("famous author", author);
            bookId = dao.insertBook( (Integer) authorId, title, year, ScopeType.fromValue(publicPrivate));
          }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    return bookId;
  }
}
package utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;

import org.apache.commons.io.FileUtils;
import spark.Request;

public class FileHandler {

  private static final int MAX_IMAGES_NUMBER = 3;

  public static List<String> saveFile(Request request) throws ServletException, IOException {

    List<String> paths = new ArrayList<>();
    File uploadDir = new File("images");
    uploadDir.mkdir();

    for (int i = 0; i < MAX_IMAGES_NUMBER; i++) {

      String fileName = request.raw().getPart("foto" + i).getSubmittedFileName();
      if (fileName.isEmpty()) continue;

      Path pathFile = Files.createTempFile(uploadDir.toPath(), "patitas", "." + getFileExtension(fileName));

      try (InputStream input = request.raw().getPart("foto" + i).getInputStream()) {
        Files.copy(input, pathFile, StandardCopyOption.REPLACE_EXISTING);
        paths.add(pathFile.toString());
      }
    }
    return paths;
  }

  public static String getFileExtension(String fileName) {
    return fileName.split("\\.")[fileName.split("\\.").length - 1];
  }

  public static String imageToBase64(String path) throws IOException {
    byte[] fileContent = FileUtils.readFileToByteArray(new File(path));
    return Base64.getEncoder().encodeToString(fileContent);
  }
}

package ir.maktab.finalprojectphase2.validation;

import ir.maktab.finalprojectphase2.exception.NotFoundException;
import ir.maktab.finalprojectphase2.exception.ValidationException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class ImageValidator {

    private ImageValidator() {
    }

    public static void validateFileExistence(String path) {
        Path paths = Paths.get(path);
        if (!Files.exists(paths))
            throw new NotFoundException("image not found");
    }

    public static void validateFilePass(String path) {
        Optional<String> optionalPathExtension = Optional.of(path)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(path.lastIndexOf(".") + 1));

        if (optionalPathExtension.isEmpty())
            throw new NotFoundException("the file is empty");

        String pathExtension = optionalPathExtension.get();

        if (!(pathExtension.equalsIgnoreCase("jpg") || pathExtension.equalsIgnoreCase("jpeg")))
            throw new ValidationException("the image is not valid,image format must be jpg");
    }

    public static void validateImageSize(String path) throws IOException {
        Path paths = Paths.get(path);
        if (Files.size(paths) / 1024 >= 300)
            throw new ValidationException("the image is not valid,image size must be less than 300 kb");
    }
}

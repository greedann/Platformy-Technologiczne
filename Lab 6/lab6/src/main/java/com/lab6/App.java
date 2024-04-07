package com.lab6;

import org.apache.commons.lang3.tuple.Pair;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class App {

    public static Pair<String, BufferedImage> read(Path path) {
        String name = String.valueOf(path.getFileName());
        BufferedImage image = null;
        try {
            image = ImageIO.read(path.toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Pair.of(name, image);
    }

    public static Pair<String, BufferedImage> transform(Pair<String, BufferedImage> pair) {
        BufferedImage image = new BufferedImage(pair.getRight().getWidth(),
                pair.getRight().getHeight(),
                pair.getRight().getType());
        for (int i = 0; i < pair.getRight().getWidth(); i++) {
            for (int j = 0; j < pair.getRight().getHeight(); j++) {
                int rgb = pair.getRight().getRGB(i, j);
                Color color = new Color(rgb);
                int r = color.getRed();
                int g = color.getGreen();
                int b = color.getBlue();
                Color outColor = new Color(r, b, g);
                int outRgb = outColor.getRGB();
                image.setRGB(i, j, outRgb);
            }
        }
        return Pair.of(pair.getLeft(), image);
    }

    public static void processImages(Stream<Path> pathStream, final String outputPath) {
        Stream<Pair<String, BufferedImage>> outputPairStream = pathStream.map(App::read).map(App::transform).parallel();
        outputPairStream.forEach(val -> {
            try {
                ImageIO.write(val.getValue(), "jpg", new File(outputPath + val.getKey()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static void main(String[] args) {
        String inputPath;
        String outputPath;

        if (args.length == 2) {
            inputPath = args[0];
            outputPath = args[1];
        } else {
            outputPath = "/home/greedann/Desktop/Sem 4/Platformy-Technologiczne/Lab 6/images/out/";
            inputPath = "/home/greedann/Desktop/Sem 4/Platformy-Technologiczne/Lab 6/images/in/";
        }

        ForkJoinPool pool = new ForkJoinPool(25);
        List<Path> files;
        Path source = Path.of(inputPath);
        try (Stream<Path> stream = Files.list(source)) {
            files = stream.collect(Collectors.toList());
            Collection<Path> filescollection = new ArrayList<Path>(files);
            Stream<Path> pathStream = filescollection.stream();
            long time = System.currentTimeMillis();
            try {
                pool.submit(() -> processImages(pathStream, outputPath)).get();

                System.out.println(System.currentTimeMillis() - time);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            System.out.println("Unable to load images");
            e.printStackTrace();
        }
    }
}

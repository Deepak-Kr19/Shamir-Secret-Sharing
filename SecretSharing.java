import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * SecretSharing
 * 
 * This program decodes a secret value (the constant term 'c') from a hidden polynomial
 * using Lagrange Interpolation. It reads values from a JSON-like input file with varied number bases.
 */
public class SecretSharing {

    // Simple structure to hold x and y points
    static class Coordinate {
        BigInteger x;
        BigInteger y;

        Coordinate(BigInteger x, BigInteger y) {
            this.x = x;
            this.y = y;
        }
    }

    public static void main(String[] args) throws Exception {
        // Load and solve both test cases
        String answerOne = processFile("testcase1.json");
        String answerTwo = processFile("testcase2.json");

        System.out.println("Secret 1: " + answerOne);
        System.out.println("Secret 2: " + answerTwo);
    }

    // Reads a JSON file and solves for the constant term
    public static String processFile(String filePath) throws Exception {
        // Read file as one string
        String raw = Files.readString(Paths.get(filePath));
        // Remove JSON formatting chars
        raw = raw.replaceAll("[{}\"]", "");

        // Extract key-value pairs
        String[] entries = raw.split(",");
        Map<String, String> data = new HashMap<>();
        for (String entry : entries) {
            String[] parts = entry.trim().split(":", 2);
            if (parts.length == 2) {
                data.put(parts[0].trim(), parts[1].trim());
            }
        }

        // Get number of total points and required points
        int totalPoints = Integer.parseInt(data.get("keys n"));
        int requiredPoints = Integer.parseInt(data.get("keys k"));

        // Collect all valid (x, y) coordinate pairs
        List<Coordinate> allPoints = new ArrayList<>();

        for (int i = 1; i <= totalPoints; i++) {
            String baseKey = i + " base";
            String valueKey = i + " value";

            if (data.containsKey(baseKey) && data.containsKey(valueKey)) {
                int base = Integer.parseInt(data.get(baseKey));
                String encoded = data.get(valueKey);
                BigInteger yValue = new BigInteger(encoded, base);
                allPoints.add(new Coordinate(BigInteger.valueOf(i), yValue));
            }
        }

        // Use first k points only
        List<Coordinate> subset = allPoints.subList(0, requiredPoints);

        // Run interpolation to get f(0)
        BigInteger secret = interpolateConstant(subset);
        return secret.toString();
    }

    // Applies Lagrange interpolation to get the constant term (f(0))
    public static BigInteger interpolateConstant(List<Coordinate> coords) {
        BigInteger result = BigInteger.ZERO;

        for (int i = 0; i < coords.size(); i++) {
            BigInteger xi = coords.get(i).x;
            BigInteger yi = coords.get(i).y;

            BigInteger num = BigInteger.ONE;
            BigInteger denom = BigInteger.ONE;

            for (int j = 0; j < coords.size(); j++) {
                if (j == i) continue;

                BigInteger xj = coords.get(j).x;

                num = num.multiply(xj.negate());               // -x_j
                denom = denom.multiply(xi.subtract(xj));       // (x_i - x_j)
            }

            BigInteger term = yi.multiply(num).divide(denom);
            result = result.add(term);
        }

        return result;
    }
}

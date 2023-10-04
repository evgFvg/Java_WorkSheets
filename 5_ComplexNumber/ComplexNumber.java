import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ComplexNumber implements Comparable<ComplexNumber> {
    private double real;
    private double imaginary;

    public ComplexNumber() {
        real = 0.0;
        imaginary = 0.0;
    }

    public ComplexNumber(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    public static ComplexNumber parse(String str) {
        List<Double> numbers = new ArrayList<>();
        String regex  =  "(\\s*[+-]?\\d+(\\.\\d+)?)(\\s*[+-]\\s*\\d+(\\.\\d+)?i)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);

        if (matcher.matches()) {
            String num_regex = "-?\\d+(\\.\\d+)?";
            Pattern numPattern = Pattern.compile(num_regex);
            Matcher num_matcher = numPattern.matcher(str.replaceAll("\\s", ""));

            while (num_matcher.find()) {
                numbers.add(Double.parseDouble(num_matcher.group()));
            }

            return new ComplexNumber(numbers.get(0), numbers.get(1));
        }

        return null;
    }

    public double getImaginary() {
        return imaginary;
    }

    public double getReal() {
        return real;
    }

    public void setImaginary(double imaginary) {
        this.imaginary = imaginary;
    }

    public void setReal(double real) {
        this.real = real;
    }

    public ComplexNumber add(ComplexNumber cn) {
        double rl = real + cn.getReal();
        double img = imaginary + cn.getImaginary();

        return new ComplexNumber(rl, img);
    }

    public ComplexNumber subtract(ComplexNumber cn) {
        double rl = real - cn.getReal();
        double img = imaginary - cn.getImaginary();

        return new ComplexNumber(rl, img);
    }

    public boolean isImaginary() {
        return real == 0;
    }

    public boolean isReal() {
        return imaginary == 0;
    }

    @Override
    public String toString() {
        return imaginary < 0 ? (real + "" + imaginary + "i") : (real + "+" + imaginary + "i");
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ComplexNumber that = (ComplexNumber) o;
        return Double.compare(that.getReal(), real) == 0 && Double.compare(that.getImaginary(), imaginary) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(real, imaginary);
    }


    @Override
    public int compareTo(ComplexNumber user_n) {
        double first = Math.sqrt(Math.pow(real, 2) + Math.pow(imaginary, 2));
        double other = Math.sqrt(Math.pow(user_n.getReal(), 2) + Math.pow(user_n.getImaginary(), 2));

        return (int) (first - other);
    }
}


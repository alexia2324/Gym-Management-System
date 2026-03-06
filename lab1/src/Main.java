public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please provide some numbers as arguments");
            return;
        }

        double max = Double.parseDouble(args[0]);

        for (int i = 1; i < args.length; i++) {
            double value = Double.parseDouble(args[i]);
            if (value > max) {
                max = value;
            }
        }

        System.out.println("Maximum value: " + max);
    }
}

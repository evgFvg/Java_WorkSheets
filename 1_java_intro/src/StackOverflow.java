public class StackOverflow {
    private static final int AVG_STACK_NUM = 4500;

    public static void overflow(int counter) {
        if (counter == AVG_STACK_NUM) {
            return;
        }
        System.out.printf("stack number is %d%n", counter);
        overflow(counter + 1);
    }
}

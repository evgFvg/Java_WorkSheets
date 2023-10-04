public class InitializationOnDemand {
    private InitializationOnDemand() {
    }

    private static class LazyHolder {
        private static final InitializationOnDemand INSTANCE = new InitializationOnDemand();
    }

    public static InitializationOnDemand getInstance() {
        return LazyHolder.INSTANCE;
    }
}



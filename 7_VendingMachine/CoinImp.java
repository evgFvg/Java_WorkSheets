public enum CoinImp implements CoinI {
    EURO {
        @Override
        public double getCurrencyInShekel(double money) {
            return money * 4;
        }
    },
    DOLLAR {
        @Override
        public double getCurrencyInShekel(double money) {
            return money * 3.5;
        }
    },
    SHEKEL {
        @Override
        public double getCurrencyInShekel(double money) {
            return money;
        }
    }
}

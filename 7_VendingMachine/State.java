enum State {
    IDLE {
        @Override
        void on(VM vm) {
            //empty on purpose
        }

        @Override
        void off(VM vm) {
            vm.rejectHandler("Good bye", State.OFF);
        }

        @Override
        void cancel(VM vm) {
            vm.getChangeAndSetState(State.IDLE);
        }

        @Override
        void insertMoney(VM vm) {
            vm.getMonitor().print("Your current balance is " + vm.getBalance());
        }

        @Override
        void chooseProduct(VM vm) {
            vm.sellHandler();
        }
    },
    WAIT {
        @Override
        void on(VM vm) {
            //empty on purpose
        }

        @Override
        void off(VM vm) {
            vm.rejectHandler("Good bye", State.OFF);
        }

        @Override
        void cancel(VM vm) {
            vm.rejectHandler("Back to main menu...", State.IDLE);
        }

        @Override
        void insertMoney(VM vm) {
            vm.sellHandler();
        }

        @Override
        void chooseProduct(VM vm) {
            vm.sellHandler();
        }
    },
    OFF {
        @Override
        void on(VM vm) {
            vm.setCurrState(State.IDLE);
            vm.getMonitor().print("Please choose an Item or insert money");
        }

        @Override
        void off(VM vm) {
            //empty on purpose
        }

        @Override
        void cancel(VM vm) {
            //empty on purpose
        }

        @Override
        void insertMoney(VM vm) {
            //empty on purpose
        }

        @Override
        void chooseProduct(VM vm) {
            //empty on purpose
        }
    };

    abstract void on(VM vm);

    abstract void off(VM vm);

    abstract void cancel(VM vm);

    abstract void insertMoney(VM vm);

    abstract void chooseProduct(VM vm);
}

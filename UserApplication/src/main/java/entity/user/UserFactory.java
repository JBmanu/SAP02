package entity.user;

public interface UserFactory {
    User createTest();
    User createWithoutCredit(String username, String password);
    User createWithCredit(String username, String password, float credits);

    class SimpleFactory implements UserFactory {

        @Override
        public User createTest() {
            final String mail = "prova@gmail.com";
            final String password = "password";
            return new UserImpl(mail, password);
        }

        @Override
        public User createWithoutCredit(final String username, final String password) {
            return new UserImpl(username, password);
        }

        @Override
        public User createWithCredit(final String username, final String password, final float credits) {
            return new UserImpl(username, password, credits);
        }
    }

}

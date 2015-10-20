package works.cirno.mocha.example;

public interface UserService {

	void saveUser(String userId, User user);

	User getUser(String userId);
}

package works.cirno.mocha.example;

import java.util.concurrent.ConcurrentHashMap;

public class UserServiceImpl implements UserService {

	private final ConcurrentHashMap<String, User> userStorage = new ConcurrentHashMap<>();


	@Override
	public void saveUser(String userId, User user) {
		userStorage.put(userId, user);
	}

	@Override
	public User getUser(String userId) {
		return userStorage.get(userId);
	}


}

package works.cirno.mocha.example;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.inject.Named;
import javax.inject.Singleton;

import org.apache.commons.codec.binary.Base64;

@Singleton
@Named("userService")
public class UserServiceImpl implements UserService {

	@Override
	public String getUserHash(TitledUser user) {
		try {
			String all = user.getUsername() + user.getTitle() + user.getTel() + user.getFirstName()
					+ user.getLastName();
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] result = md.digest(all.getBytes(Charset.forName("utf-8")));
			return Base64.encodeBase64String(result);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

}

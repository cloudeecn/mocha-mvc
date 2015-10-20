package works.cirno.mocha.example;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;

public class UserServiceImpl implements UserService {

	@Override
	public String getUserHash(TitledUser user) {
		try {
			if (user == null) {
				return null;
			} else {
				String all = user.getUsername() + user.getTitle() + user.getTel() + user.getFirstName()
						+ user.getLastName();
				MessageDigest md = MessageDigest.getInstance("MD5");
				byte[] result = md.digest(all.getBytes(Charset.forName("utf-8")));
				return Base64.encodeBase64String(result);
			}
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

}

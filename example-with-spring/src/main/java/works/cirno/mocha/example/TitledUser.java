package works.cirno.mocha.example;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

public class TitledUser {
	private String username;
	private String title;
	private String firstName;
	private String lastName;
	private String tel;
	private String photoUrl;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhoto(InputStream photo) throws IOException {
		byte[] content = IOUtils.toByteArray(photo);
		photoUrl = "data:;base64," + URLEncoder.encode(Base64.encodeBase64String(content), "utf-8");
	}

}

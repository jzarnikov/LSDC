package at.tuwien.lsdc.performance;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

// this is just to find out how big the serialized DummyMessage is.

public class SizeTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		DummyMessage msg = new DummyMessage();
		File f = File.createTempFile("dummy", null);
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
		oos.writeObject(msg);
		oos.close();
		System.out.println(f.getAbsolutePath());
	}

}

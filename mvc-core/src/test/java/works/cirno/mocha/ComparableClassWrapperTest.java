package works.cirno.mocha;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class ComparableClassWrapperTest {
	private Class<?>[] classes = new Class<?>[] {
			Throwable.class,
			java.awt.AWTError.class,
			java.io.IOError.class,
			java.lang.AbstractMethodError.class,
			java.lang.annotation.AnnotationFormatError.class,
			java.lang.AssertionError.class,
			java.lang.BootstrapMethodError.class,
			java.lang.ClassCircularityError.class,
			java.lang.ClassFormatError.class,
			java.lang.Error.class,
			java.lang.ExceptionInInitializerError.class,
			java.lang.IllegalAccessError.class,
			java.lang.IncompatibleClassChangeError.class,
			java.lang.InstantiationError.class,
			java.lang.InternalError.class,
			java.lang.LinkageError.class,
			java.lang.NoClassDefFoundError.class,
			java.lang.NoSuchFieldError.class,
			java.lang.NoSuchMethodError.class,
			java.lang.OutOfMemoryError.class,
			java.lang.reflect.GenericSignatureFormatError.class,
			java.lang.StackOverflowError.class,
			java.lang.UnknownError.class,
			java.lang.UnsatisfiedLinkError.class,
			java.lang.UnsupportedClassVersionError.class,
			java.lang.VerifyError.class,
			java.lang.VirtualMachineError.class,
			java.nio.charset.CoderMalfunctionError.class,
			java.rmi.ServerError.class,
			java.util.ServiceConfigurationError.class,
			java.util.zip.ZipError.class,
			java.awt.AWTException.class,
			java.awt.color.CMMException.class,
			java.awt.color.ProfileDataException.class,
			java.awt.datatransfer.MimeTypeParseException.class,
			java.awt.datatransfer.UnsupportedFlavorException.class,
			java.awt.dnd.InvalidDnDOperationException.class,
			java.awt.FontFormatException.class,
			java.awt.geom.IllegalPathStateException.class,
			java.awt.geom.NoninvertibleTransformException.class,
			java.awt.HeadlessException.class,
			java.awt.IllegalComponentStateException.class,
			java.awt.image.ImagingOpException.class,
			java.awt.image.RasterFormatException.class,
			java.awt.print.PrinterAbortException.class,
			java.awt.print.PrinterException.class,
			java.awt.print.PrinterIOException.class,
			java.beans.IntrospectionException.class,
			java.beans.PropertyVetoException.class,
			java.io.CharConversionException.class,
			java.io.EOFException.class,
			java.io.FileNotFoundException.class,
			java.io.InterruptedIOException.class,
			java.io.InvalidClassException.class,
			java.io.InvalidObjectException.class,
			java.io.IOException.class,
			java.io.NotActiveException.class,
			java.io.NotSerializableException.class,
			java.io.ObjectStreamException.class,
			java.io.OptionalDataException.class,
			java.io.StreamCorruptedException.class,
			java.io.SyncFailedException.class,
			java.io.UnsupportedEncodingException.class,
			java.io.UTFDataFormatException.class,
			java.io.WriteAbortedException.class,
			java.lang.annotation.AnnotationTypeMismatchException.class,
			java.lang.annotation.IncompleteAnnotationException.class,
			java.lang.ArithmeticException.class,
			java.lang.ArrayIndexOutOfBoundsException.class,
			java.lang.ArrayStoreException.class,
			java.lang.ClassCastException.class,
			java.lang.ClassNotFoundException.class,
			java.lang.CloneNotSupportedException.class,
			java.lang.EnumConstantNotPresentException.class,
			java.lang.Exception.class,
			java.lang.IllegalAccessException.class,
			java.lang.IllegalArgumentException.class,
			java.lang.IllegalMonitorStateException.class,
			java.lang.IllegalStateException.class,
			java.lang.IllegalThreadStateException.class,
			java.lang.IndexOutOfBoundsException.class,
			java.lang.InstantiationException.class,
			java.lang.instrument.IllegalClassFormatException.class,
			java.lang.instrument.UnmodifiableClassException.class,
			java.lang.InterruptedException.class,
			java.lang.invoke.WrongMethodTypeException.class,
			java.lang.NegativeArraySizeException.class,
			java.lang.NoSuchFieldException.class,
			java.lang.NoSuchMethodException.class,
			java.lang.NullPointerException.class,
			java.lang.NumberFormatException.class,
			java.lang.reflect.InvocationTargetException.class,
			java.lang.reflect.MalformedParameterizedTypeException.class,
			java.lang.reflect.UndeclaredThrowableException.class,
			java.lang.ReflectiveOperationException.class,
			java.lang.RuntimeException.class,
			java.lang.SecurityException.class,
			java.lang.StringIndexOutOfBoundsException.class,
			java.lang.TypeNotPresentException.class,
			java.lang.UnsupportedOperationException.class,
			java.net.BindException.class,
			java.net.ConnectException.class,
			java.net.HttpRetryException.class,
			java.net.MalformedURLException.class,
			java.net.NoRouteToHostException.class,
			java.net.PortUnreachableException.class,
			java.net.ProtocolException.class,
			java.net.SocketException.class,
			java.net.SocketTimeoutException.class,
			java.net.UnknownHostException.class,
			java.net.UnknownServiceException.class,
			java.net.URISyntaxException.class,
			java.nio.BufferOverflowException.class,
			java.nio.BufferUnderflowException.class,
			java.nio.channels.AcceptPendingException.class,
			java.nio.channels.AlreadyBoundException.class,
			java.nio.channels.AlreadyConnectedException.class,
			java.nio.channels.AsynchronousCloseException.class,
			java.nio.channels.CancelledKeyException.class,
			java.nio.channels.ClosedByInterruptException.class,
			java.nio.channels.ClosedChannelException.class,
			java.nio.channels.ClosedSelectorException.class,
			java.nio.channels.ConnectionPendingException.class,
			java.nio.channels.FileLockInterruptionException.class,
			java.nio.channels.IllegalBlockingModeException.class,
			java.nio.channels.IllegalChannelGroupException.class,
			java.nio.channels.IllegalSelectorException.class,
			java.nio.channels.InterruptedByTimeoutException.class,
			java.nio.channels.NoConnectionPendingException.class,
			java.nio.channels.NonReadableChannelException.class,
			java.nio.channels.NonWritableChannelException.class,
			java.nio.channels.NotYetBoundException.class,
			java.nio.channels.NotYetConnectedException.class,
			java.nio.channels.OverlappingFileLockException.class,
			java.nio.channels.ReadPendingException.class,
			java.nio.channels.ShutdownChannelGroupException.class,
			java.nio.channels.UnresolvedAddressException.class,
			java.nio.channels.UnsupportedAddressTypeException.class,
			java.nio.channels.WritePendingException.class,
			java.nio.charset.CharacterCodingException.class,
			java.nio.charset.IllegalCharsetNameException.class,
			java.nio.charset.MalformedInputException.class,
			java.nio.charset.UnmappableCharacterException.class,
			java.nio.charset.UnsupportedCharsetException.class,
			java.nio.file.AccessDeniedException.class,
			java.nio.file.AtomicMoveNotSupportedException.class,
			java.nio.file.attribute.UserPrincipalNotFoundException.class,
			java.nio.file.ClosedDirectoryStreamException.class,
			java.nio.file.ClosedFileSystemException.class,
			java.nio.file.ClosedWatchServiceException.class,
			java.nio.file.DirectoryIteratorException.class,
			java.nio.file.DirectoryNotEmptyException.class,
			java.nio.file.FileAlreadyExistsException.class,
			java.nio.file.FileSystemAlreadyExistsException.class,
			java.nio.file.FileSystemException.class,
			java.nio.file.FileSystemLoopException.class,
			java.nio.file.FileSystemNotFoundException.class,
			java.nio.file.InvalidPathException.class,
			java.nio.file.NoSuchFileException.class,
			java.nio.file.NotDirectoryException.class,
			java.nio.file.NotLinkException.class,
			java.nio.file.ProviderMismatchException.class,
			java.nio.file.ProviderNotFoundException.class,
			java.nio.file.ReadOnlyFileSystemException.class,
			java.nio.InvalidMarkException.class,
			java.nio.ReadOnlyBufferException.class,
			java.rmi.AccessException.class,
			java.rmi.activation.ActivateFailedException.class,
			java.rmi.activation.ActivationException.class,
			java.rmi.activation.UnknownGroupException.class,
			java.rmi.activation.UnknownObjectException.class,
			java.rmi.AlreadyBoundException.class,
			java.rmi.ConnectException.class,
			java.rmi.ConnectIOException.class,
			java.rmi.MarshalException.class,
			java.rmi.NoSuchObjectException.class,
			java.rmi.NotBoundException.class,
			java.rmi.RemoteException.class,
			java.rmi.server.ExportException.class,
			java.rmi.server.ServerCloneException.class,
			java.rmi.server.ServerNotActiveException.class,
			java.rmi.ServerException.class,
			java.rmi.StubNotFoundException.class,
			java.rmi.UnexpectedException.class,
			java.rmi.UnknownHostException.class,
			java.rmi.UnmarshalException.class,
			java.security.AccessControlException.class,
			java.security.acl.AclNotFoundException.class,
			java.security.acl.LastOwnerException.class,
			java.security.acl.NotOwnerException.class,
			java.security.cert.CertificateEncodingException.class,
			java.security.cert.CertificateException.class,
			java.security.cert.CertificateExpiredException.class,
			java.security.cert.CertificateNotYetValidException.class,
			java.security.cert.CertificateParsingException.class,
			java.security.cert.CertificateRevokedException.class,
			java.security.cert.CertPathBuilderException.class,
			java.security.cert.CertPathValidatorException.class,
			java.security.cert.CertStoreException.class,
			java.security.cert.CRLException.class,
			java.security.DigestException.class,
			java.security.GeneralSecurityException.class,
			java.security.InvalidAlgorithmParameterException.class,
			java.security.InvalidKeyException.class,
			java.security.InvalidParameterException.class,
			java.security.KeyException.class,
			java.security.KeyManagementException.class,
			java.security.KeyStoreException.class,
			java.security.NoSuchAlgorithmException.class,
			java.security.NoSuchProviderException.class,
			java.security.PrivilegedActionException.class,
			java.security.ProviderException.class,
			java.security.SignatureException.class,
			java.security.spec.InvalidKeySpecException.class,
			java.security.spec.InvalidParameterSpecException.class,
			java.security.UnrecoverableEntryException.class,
			java.security.UnrecoverableKeyException.class,
			java.sql.BatchUpdateException.class,
			java.sql.SQLClientInfoException.class,
			java.sql.SQLDataException.class,
			java.sql.SQLException.class,
			java.sql.SQLFeatureNotSupportedException.class,
			java.sql.SQLIntegrityConstraintViolationException.class,
			java.sql.SQLInvalidAuthorizationSpecException.class,
			java.sql.SQLNonTransientConnectionException.class,
			java.sql.SQLNonTransientException.class,
			java.sql.SQLRecoverableException.class,
			java.sql.SQLSyntaxErrorException.class,
			java.sql.SQLTimeoutException.class,
			java.sql.SQLTransactionRollbackException.class,
			java.sql.SQLTransientConnectionException.class,
			java.sql.SQLTransientException.class,
			java.text.ParseException.class,
			java.util.concurrent.BrokenBarrierException.class,
			java.util.concurrent.CancellationException.class,
			java.util.concurrent.ExecutionException.class,
			java.util.concurrent.RejectedExecutionException.class,
			java.util.concurrent.TimeoutException.class,
			java.util.ConcurrentModificationException.class,
			java.util.DuplicateFormatFlagsException.class,
			java.util.EmptyStackException.class,
			java.util.FormatFlagsConversionMismatchException.class,
			java.util.FormatterClosedException.class,
			java.util.IllegalFormatCodePointException.class,
			java.util.IllegalFormatConversionException.class,
			java.util.IllegalFormatException.class,
			java.util.IllegalFormatFlagsException.class,
			java.util.IllegalFormatPrecisionException.class,
			java.util.IllegalFormatWidthException.class,
			java.util.IllformedLocaleException.class,
			java.util.InputMismatchException.class,
			java.util.InvalidPropertiesFormatException.class,
			java.util.jar.JarException.class,
			java.util.MissingFormatArgumentException.class,
			java.util.MissingFormatWidthException.class,
			java.util.MissingResourceException.class,
			java.util.NoSuchElementException.class,
			java.util.prefs.BackingStoreException.class,
			java.util.prefs.InvalidPreferencesFormatException.class,
			java.util.regex.PatternSyntaxException.class,
			java.util.TooManyListenersException.class,
			java.util.UnknownFormatConversionException.class,
			java.util.UnknownFormatFlagsException.class,
			java.util.zip.DataFormatException.class,
			java.util.zip.ZipException.class
	};

	public void ensureCompare(Class<?> aw, Class<?> bw, Class<?> cw) {
		ClassComparator cc = new ClassComparator();
		Class<?> t;
		if (cc.compare(aw, bw) > 0) {
			t = aw;
			aw = bw;
			bw = t;
		}
		if (cc.compare(bw, cw) > 0) {
			t = bw;
			bw = cw;
			cw = t;
		}
		if (cc.compare(aw, bw) > 0) {
			t = aw;
			aw = bw;
			bw = t;
		}
		if (cc.compare(aw, bw) < 0 && cc.compare(bw, cw) < 0 && cc.compare(aw, cw) < 0 &&
				cc.compare(bw, aw) > 0 && cc.compare(cw, bw) > 0 && cc.compare(cw, aw) > 0) {
			if (cw.isAssignableFrom(bw)
					|| bw.isAssignableFrom(cw)) {
				Assert.assertEquals(bw.getName() + " <= " + cw.getName(),
						cw.isAssignableFrom(bw), cc.compare(bw, cw) <= 0);
			}

			if (cw.isAssignableFrom(aw)
					|| aw.isAssignableFrom(cw)) {
				Assert.assertEquals(aw.getName() + " <= " + cw.getName(),
						cw.isAssignableFrom(aw), cc.compare(aw, cw) <= 0);
			}

			if (aw.isAssignableFrom(bw)
					|| bw.isAssignableFrom(aw)) {
				Assert.assertEquals(bw.getName() + " <= " + aw.getName(),
						aw.isAssignableFrom(bw), cc.compare(bw, aw) <= 0);
			}
		} else {
			throw new IllegalStateException("Compare result for " + aw + " " + bw + " " + cw + " is wrong ("
					+ cc.compare(aw, bw) + ", " + cc.compare(bw, cw) + ", " + cc.compare(aw, cw) + ", "
					+ cc.compare(bw, aw)
					+ ", " + cc.compare(cw, bw) + ", " + cc.compare(cw, aw) + ")");
		}
	}

	@Test
	public void testCompare() {
		int max = classes.length;
		for (int i = 0; i < max; i++) {
			for (int j = 0; j < max; j++) {
				if (i != j) {
					for (int k = 0; k < max; k++) {
						if (i != k && j != k) {
							Class<?> a = classes[i];
							Class<?> b = classes[j];
							Class<?> c = classes[k];
							ensureCompare(a, b, c);
						}
					}
				}
			}
		}
	}
}

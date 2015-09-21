package works.cirno.mocha.mvc.parameter.name;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import works.cirno.mocha.mvc.InvokeTarget;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;

/**
 *
 */
public class ASMParameterAnalyzer implements ParameterAnalyzer {

	private static final Logger log = LoggerFactory.getLogger(ASMParameterAnalyzer.class);

	private HashMap<Class<?>, ClassNode> classNodeCache = new HashMap<>();

	@SuppressWarnings("unchecked")
	public Parameter[] getParameters(InvokeTarget target) {
		Class<?> controllerClass = target.getControllerClass();
		Method method = target.getMethod();
		String methodName = method.getName();

		Class<?>[] parameterTypes = method.getParameterTypes();
		int parameterCount = parameterTypes.length;

		// /////////Resolve parameter names by asm
		{
			if (log.isDebugEnabled()) {
				log.debug("Analyzing method {}.{}", controllerClass.getName(), method.getName());
			}
			ClassNode node = classNodeCache.get(controllerClass);
			if (node == null) {
				String name = controllerClass.getName();
				{
					int idx = name.lastIndexOf('.');
					if (idx > 0) {
						name = name.substring(idx + 1);
					}
				}

				try (InputStream is = controllerClass.getResourceAsStream(name + ".class")) {
					if (is == null) {
						throw new FileNotFoundException(name + ".class");
					}
					ClassReader cr = new ClassReader(is);
					node = new ClassNode();
					cr.accept(node, ClassReader.SKIP_FRAMES);
					classNodeCache.put(controllerClass, node);
				} catch (IOException e) {
					log.warn("Can't read bytecode of class " + name);
					return null;
				}
			}

			Parameter parameters[] = new Parameter[parameterCount];
			boolean methodNodeGot = false;
			for (MethodNode m : (List<MethodNode>) node.methods) {
				if (methodName.equals(m.name)) {
					String[] names = new String[parameterCount];
					// do it
					{
						IdentityHashMap<LabelNode, Integer> labelPosMap = new IdentityHashMap<>();
						int pos = 0;
						InsnList il = m.instructions;
						for (int i = 0, max = il.size(); i < max; i++) {
							AbstractInsnNode ain = il.get(i);
							if (ain.getType() == AbstractInsnNode.LABEL) {
								LabelNode label = (LabelNode) ain;
								labelPosMap.put(label, pos++);
							}
						}
						int[] poses = new int[parameterCount];
						for (int i = 0; i < parameterCount; i++) {
							poses[i] = Integer.MAX_VALUE;
						}

						for (LocalVariableNode lvn : (List<LocalVariableNode>) m.localVariables) {
							if (lvn.index > 0 && lvn.index <= parameterCount) {
								int idx = lvn.index - 1;
								int startPos = labelPosMap.get(lvn.start);
								if (poses[idx] > startPos) {
									poses[idx] = startPos;
									names[idx] = lvn.name;
								}
							}
						}
					}

					if (log.isDebugEnabled()) {
						log.debug("Method {}.{} has parameter: {}", controllerClass.getName(), method.getName(),
								Arrays.toString(names));
					}
					for (int i = 0; i < parameterCount; i++) {
						parameters[i] = new Parameter(names[i], parameterTypes[i]);
					}

					methodNodeGot = true;
					break;
				}
			}
			if (!methodNodeGot) {
				throw new IllegalStateException("Can't get method " + method.getName() + " from ASM");
			}
			return parameters;
		}
	}
}

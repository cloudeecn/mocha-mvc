/*
 * The MIT License (MIT)
 * 
 * Copyright (c) 2014-2015 Cloudee Huang ( https://github.com/cloudeecn / cloudeecn@gmail.com )
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package works.cirno.mocha.parameter.name;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import works.cirno.mocha.InvokeTarget;

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

	public static final boolean enabled;

	static {
		boolean found;
		try {
			Class.forName("org.objectweb.asm.tree.ClassNode");
			found = true;
		} catch (ClassNotFoundException e) {
			found = false;
		}
		enabled = found;
	}

	@Override
	public Parameter[] getParameters(InvokeTarget target) {
		return getParameters(target, null);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Parameter[] getParameters(InvokeTarget target, Parameter[] parameters) {
		if (!enabled) {
			return parameters;
		}
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

			if (parameters == null) {
				parameters = new Parameter[parameterCount];
			} else if (parameters.length != parameterCount) {
				throw new IllegalArgumentException("Input parameters' length is different than parameters of target");
			}

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
						if (parameters[i] == null || parameters[i].getName() == null) {
							parameters[i] = new Parameter(names[i], parameterTypes[i]);
						}
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

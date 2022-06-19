/*
 * Copyright (c) 2022 Otavio Santana and others
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the Eclipse
 * Public License v. 2.0 are satisfied: GNU General Public License, version 2
 * with the GNU Classpath Exception which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */
package jakarta.nosql;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.file.PathUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@SuppressWarnings("nls")
@Disabled
class LoaderTypeTest {

    @Test
    public void shouldReturnServiceLoader() {
        LoaderType type = LoaderType.getLoaderType();
        Assertions.assertNotNull(type);
        Assertions.assertEquals(LoaderType.SERVICE_LOADER, type);
    }

    @Test
    @Disabled
    public void shouldReadServiceLoader() {
        LoaderType type = LoaderType.SERVICE_LOADER;
        Stream<Object> stream = type.read(Machine.class);
        Assertions.assertNotNull(stream);
        List<Machine> machines = stream.map(Machine.class::cast).collect(Collectors.toList());
        Assertions.assertNotNull(machines);
        Assertions.assertEquals(2, machines.size());
    }

    /*
     * Tests the "OSGI" LoaderType implementation in a mock OSGi environment. This works
     * with heavy reflection in order to avoid contaminating the rest of the testing
     * class space with the HK2 ServiceLoader.
     */
    @Disabled
    @ParameterizedTest
    @ValueSource(strings = {"/ServiceLoaderImpl.java", "/ServiceLoaderNull.java"})
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void testOsgiLoader(String serviceLoaderResource) throws IOException, ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        ClassLoader base = ClassLoader.getSystemClassLoader();
        while(base.getParent() != null) {
            base = base.getParent();
        }
        try(JavaSourceClassLoader cl = new JavaSourceClassLoader(base)) {
        
	        // Load our stub ServiceLoader implementation and loader bytecode to have a coherent
	        //   ClassLoader space
	        cl.addClass("org.glassfish.hk2.osgiresourcelocator.ServiceLoader", serviceLoaderResource);
	        cl.addCompiledClass(LoaderType.class.getName());
	        Arrays.stream(LoaderType.values())
	        	.map(type -> type.getClass().getName())
	        	.forEach(cl::addCompiledClass);
	        cl.addCompiledClass(ServiceLoaderSort.class.getName());
	        
	        // Re-load LoaderType reflectively so that Class.forName will find it from our new ClassLoader
	        Class loaderType = Class.forName(LoaderType.class.getName(), false, cl);
	        Method getLoaderType = loaderType.getDeclaredMethod("getLoaderType");
	        getLoaderType.setAccessible(true);
	        Object type = getLoaderType.invoke(null);
	        Object osgiVal = Enum.valueOf(loaderType, "OSGI");
	        
	        Assertions.assertNotNull(type);
	        Assertions.assertEquals(osgiVal, type);
	
	        Method read = type.getClass().getDeclaredMethod("read", new Class<?>[] { Class.class });
	        read.setAccessible(true);
	        Stream<Object> stream = (Stream<Object>)read.invoke(type, Machine.class);
	        Assertions.assertNotNull(stream);
	        List<Machine> machines = stream.map(Machine.class::cast).collect(Collectors.toList());
	        Assertions.assertNotNull(machines);
	        Assertions.assertEquals(2, machines.size());
        }
    }
    
    private static class JavaSourceClassLoader extends ClassLoader implements AutoCloseable {
        private final Map<String, byte[]> compiled = new HashMap<>();
        private final JavaCompiler compiler;
        private final StandardJavaFileManager fileManager;
        private final Path temp;
        
        public JavaSourceClassLoader(ClassLoader parent) throws IOException {
            super(parent);
            this.compiler = ToolProvider.getSystemJavaCompiler();
            this.fileManager = compiler.getStandardFileManager(null, Locale.getDefault(), StandardCharsets.UTF_8);
            this.temp = Files.createTempDirectory(getClass().getSimpleName());
            this.fileManager.setLocation(StandardLocation.CLASS_OUTPUT, Arrays.asList(this.temp.toFile()));
        }
        
        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            if(compiled.containsKey(name)) {
                byte[] bytes = compiled.get(name);
                return defineClass(name, bytes, 0, bytes.length);
            }
            
            // Failing that, look to the surrounding environment
            try {
                return Class.forName(name, false, LoaderTypeTest.class.getClassLoader());
            } catch(ClassNotFoundException e) {
                // Ignore
            }
            
            return super.findClass(name);
        }
        
        @Override
        protected Enumeration<URL> findResources(String name) throws IOException {
            Enumeration<URL> result = LoaderTypeTest.class.getClassLoader().getResources(name);
            return result.hasMoreElements() ? result : null;
        }
        
        @Override
        public void close() throws IOException {
        	fileManager.close();
        	PathUtils.deleteDirectory(this.temp);
        }
        
        public void addClass(String className, String resourceName) throws IOException {
            String source = IOUtils.resourceToString(resourceName, StandardCharsets.UTF_8);
            String fileName = className.substring(className.lastIndexOf('.')+1) + ".java";
            List<JavaFileObject> sources = Arrays.asList(new JavaFileObjectJavaSource(fileName, source));
            compiler.getTask(null, fileManager, null, null, null, sources).call();
            JavaFileObject out = fileManager.getJavaFileForOutput(StandardLocation.CLASS_OUTPUT, className, Kind.CLASS, sources.get(0));
            byte[] bytecode;
            try(InputStream is = out.openInputStream()) {
                bytecode = IOUtils.toByteArray(is);
            }
            compiled.put(className, bytecode);
        }
        
        public void addCompiledClass(String className) {
        	try {
	            String resourceName = '/' + className.replace('.', '/') + ".class";
	            byte[] bytecode = IOUtils.resourceToByteArray(resourceName);
	            compiled.put(className, bytecode);
        	} catch(IOException e) {
        		throw new UncheckedIOException(e);
        	}
        }
    }
    
    private static class JavaFileObjectJavaSource extends SimpleJavaFileObject {

        private CharSequence sourceCode;

        public JavaFileObjectJavaSource(String name, CharSequence sourceCode) {
            super(URI.create(name), Kind.SOURCE);
            this.sourceCode = Objects.requireNonNull(sourceCode);
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) throws UnsupportedOperationException {
            return sourceCode;
        }
    }
}
/**
 * JBoss, Home of Professional Open Source
 * Copyright Red Hat, Inc., and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.arquillian.qunit.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;

public final class FileUtilities {

    private static final Logger LOGGER = Logger.getLogger(FileUtilities.class.getName());

    private FileUtilities() {
    }

    public static String readFile(String fileNamePath) {

        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(fileNamePath));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "readFile Error", e);
        } finally {
            try {
                br.close();
            } catch (Exception ignore) {
                LOGGER.log(Level.WARNING, "readFile: resourse not closed", ignore);
            }
        }
        return sb.toString();
    }

    public static void writeToFile(File file, String str) {
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            fw = new FileWriter(file.getAbsoluteFile());
            bw = new BufferedWriter(fw);
            bw.write(str);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "writeToFile Error", e);
        } finally {
            try {
                bw.close();
            } catch (Exception ignore) {
                LOGGER.log(Level.WARNING, "writeToFile: resourse not closed", ignore);
            }
        }
    }

    public static void deleteDirectory(String path) throws IOException {
        File dir = new File(path);
        if (dir.exists()) {
            FileUtils.deleteDirectory(dir);
        }
    }

    public static File createDirectory(String path) {
        final File dir = new File(path);
        dir.mkdir();
        return dir;
    }
}

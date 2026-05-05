package org.danish.autotestgen;

import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TestFileWriter {

    public static void writeTestFile(Project project, PsiFile sourceFile, String testCode) throws Exception {
        String sourceFileName = sourceFile.getName(); // e.g. Calculator.java
        String testFileName = sourceFileName.replace(".java", "Test.java"); // e.g. CalculatorTest.java

        WriteAction.runAndWait(() -> {
            try {
                VirtualFile parent = sourceFile.getVirtualFile().getParent();
                VirtualFile testFile = parent.findChild(testFileName);

                if (testFile == null) {
                    testFile = parent.createChildData(TestFileWriter.class, testFileName);
                }

                testFile.setBinaryContent(testCode.getBytes(StandardCharsets.UTF_8));

            } catch (IOException ex) {
                throw new RuntimeException("Failed to write test file: " + ex.getMessage(), ex);
            }
        });
    }
}
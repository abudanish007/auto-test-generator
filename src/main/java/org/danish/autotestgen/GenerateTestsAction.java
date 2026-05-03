package org.danish.autotestgen;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiFile;

public class GenerateTestsAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);

        if (project == null || editor == null || psiFile == null) {
            Messages.showErrorDialog("Could not read file context.", "Auto-Test Generator");
            return;
        }

        String selectedCode = editor.getSelectionModel().hasSelection()
                ? editor.getSelectionModel().getSelectedText()
                : psiFile.getText();

        if (selectedCode == null || selectedCode.isBlank()) {
            Messages.showErrorDialog("Please select some code or open a Java file.", "Auto-Test Generator");
            return;
        }

        String finalCode = selectedCode;

        com.intellij.openapi.application.ApplicationManager.getApplication()
                .executeOnPooledThread(() -> {
                    try {
                        String generatedTests = AIService.generateTests(finalCode);
                        TestFileWriter.writeTestFile(project, psiFile, generatedTests);

                        com.intellij.openapi.application.ApplicationManager.getApplication()
                                .invokeLater(() -> Messages.showInfoMessage(
                                        "Test file generated successfully!", "Auto-Test Generator"));

                    } catch (Exception ex) {
                        com.intellij.openapi.application.ApplicationManager.getApplication()
                                .invokeLater(() -> Messages.showErrorDialog(
                                        "Error: " + ex.getMessage(), "Auto-Test Generator"));
                    }
                });
    }

    @Override
    public void update(AnActionEvent e) {
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
        boolean isJavaFile = psiFile != null
                && psiFile.getVirtualFile() != null
                && "java".equals(psiFile.getVirtualFile().getExtension());
        e.getPresentation().setEnabledAndVisible(true);
    }
}
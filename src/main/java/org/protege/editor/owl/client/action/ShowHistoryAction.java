package org.protege.editor.owl.client.action;

import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.KeyStroke;

import org.protege.editor.owl.client.api.exception.ClientRequestException;
import org.protege.editor.owl.client.api.exception.SynchronizationException;
import org.protege.editor.owl.client.ui.ChangeHistoryPanel;
import org.protege.editor.owl.model.OWLWorkspace;

public class ShowHistoryAction extends AbstractClientAction {

    private static final long serialVersionUID = -7628375950917155764L;

    @Override
    public void initialise() throws Exception {
        super.initialise();
    }

    @Override
    public void dispose() throws Exception {
        super.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        final OWLWorkspace editorWindow = getOWLEditorKit().getOWLWorkspace();
        try {
            JDialog dialog = createDialog();
            dialog.setLocationRelativeTo(editorWindow);
            dialog.setVisible(true);
        }
        catch (ClientRequestException | SynchronizationException e) {
            showErrorDialog("Show history error", "Unable to show change history: " + e.getMessage(), e);
        }
    }

    private JDialog createDialog() throws ClientRequestException, SynchronizationException {
        final JDialog dialog = new JDialog(null, "Browse Change History", Dialog.ModalityType.MODELESS);
        ChangeHistoryPanel changeHistoryPanel = new ChangeHistoryPanel(getActiveVersionOntology(), getOWLEditorKit());
        changeHistoryPanel.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "CLOSE_DIALOG");
        changeHistoryPanel.getActionMap().put("CLOSE_DIALOG", new AbstractAction()
        {
           private static final long serialVersionUID = 1L;
           @Override
           public void actionPerformed(ActionEvent e)
           {
               dialog.setVisible(false);
               dialog.dispose();
           }
        });
        dialog.addWindowListener(new WindowAdapter()
        {
           @Override
           public void windowClosing(WindowEvent e)
           {
               dialog.setVisible(false);
               dialog.dispose();
           }
        });
        dialog.setContentPane(changeHistoryPanel);
        dialog.setSize(800, 600);
        dialog.setResizable(true);
        return dialog;
    }
}

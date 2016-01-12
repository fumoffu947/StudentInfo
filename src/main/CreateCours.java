package main;

import main.Interfaces.RePackWindow;

import javax.swing.*;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class CreateCours {

	private final RePackWindow rePackWindow;
	private List<Student> students = new ArrayList<>();
    private List<String> aktiviteter = new ArrayList<>();
	private Dimension largetsActivitySize = new Dimension(1,1);

    private JPanel pageHolder = new JPanel();
    private JPanel aktivitieHolder = new JPanel();
    private JPanel addAktivityPanel = new JPanel();


    public CreateCours(RePackWindow rePackWindow) {
	    this.rePackWindow = rePackWindow;
	JTextField aktivityTextField = new JTextField(" ", 40);

	JButton addAktivityButton =  new JButton("Add aktivity");
	addAktivityButton.setAction(new AbstractAction()
	{
	    @Override public void actionPerformed(final ActionEvent e) {
		String aktivity = aktivityTextField.getText();
		aktivityTextField.setText(" ");
		aktivitieHolder.remove(addAktivityPanel);
		    final JTextField comp = new JTextField(aktivity);
		    comp.setEditable(false);
		    setDimensionIfLarger(comp.getSize());
		    aktivitieHolder.add(comp);
		aktivitieHolder.add(addAktivityPanel);
		    rePackWindow.rePackWindow();

	    }
	});

	addAktivityPanel.add(aktivityTextField);
	addAktivityPanel.add(addAktivityButton);
	aktivitieHolder.add(addAktivityPanel);

	pageHolder.add(aktivitieHolder);
    }

	public void setDimensionIfLarger(Dimension textFieldSize) {
		if (largetsActivitySize.getWidth() < textFieldSize.getWidth()) {
			largetsActivitySize = textFieldSize;
		}
	}

    public JPanel getPageHolder() {
	return pageHolder;
    }
}

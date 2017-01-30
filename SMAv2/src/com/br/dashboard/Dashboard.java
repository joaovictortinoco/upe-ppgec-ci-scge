package com.br.dashboard;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wb.swt.SWTResourceManager;

public class Dashboard {

	protected Shell shell;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());

	public static void main(String[] args) {
		try {
			Dashboard window = new Dashboard();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(709, 278);
		shell.setText("SCGE - PPGEC");

		Label lblScge = new Label(shell, SWT.NONE);
		lblScge.setBounds(10, 44, 55, 15);
		lblScge.setText("SCGE");

		Label lblDetran = new Label(shell, SWT.NONE);
		lblDetran.setBounds(10, 113, 55, 15);
		lblDetran.setText("DETRAN");

		Label lblAti = new Label(shell, SWT.NONE);
		lblAti.setBounds(10, 190, 55, 15);
		lblAti.setText("ATI");

		Label lblConvenips = new Label(shell, SWT.NONE);
		lblConvenips.setBounds(96, 10, 55, 15);
		lblConvenips.setText("Convenios");

		Label lblOramentria = new Label(shell, SWT.NONE);
		lblOramentria.setBounds(209, 10, 78, 15);
		lblOramentria.setText("Or\u00E7ament\u00E1ria");

		Label lblServidores = new Label(shell, SWT.NONE);
		lblServidores.setBounds(342, 10, 55, 15);
		lblServidores.setText("Servidores");

		Label lblDespesas = new Label(shell, SWT.NONE);
		lblDespesas.setBounds(463, 10, 55, 15);
		lblDespesas.setText("Despesas");

		Label lblAesEProgramas = new Label(shell, SWT.NONE);
		lblAesEProgramas.setBounds(571, 10, 115, 15);
		lblAesEProgramas.setText("A\u00E7\u00F5es e Programas");

		Button bntScgeConvenios = formToolkit.createButton(shell, "", SWT.NONE);
		bntScgeConvenios.setForeground(SWTResourceManager.getColor(0, 128, 0));
		bntScgeConvenios.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		bntScgeConvenios.setBounds(91, 27, 70, 48);

		Button btnScgeOrcamentaria = formToolkit.createButton(shell, "", SWT.NONE);
		btnScgeOrcamentaria.setBounds(209, 27, 70, 48);

		Button btnScgeServidores = formToolkit.createButton(shell, "", SWT.NONE);
		btnScgeServidores.setBounds(332, 27, 70, 48);

		Button btnScgeDespesas = formToolkit.createButton(shell, "", SWT.NONE);
		btnScgeDespesas.setBounds(450, 27, 75, 48);

		Button btnScgeAcoesProgramas = new Button(shell, SWT.NONE);
		btnScgeAcoesProgramas.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnScgeAcoesProgramas.setBounds(581, 27, 70, 48);
		formToolkit.adapt(btnScgeAcoesProgramas, true, true);
		btnScgeAcoesProgramas.setText("");

		Button btnDetranConvenios = formToolkit.createButton(shell, "", SWT.NONE);
		btnDetranConvenios.setBounds(91, 93, 70, 48);

		Button btnDetranOrcamentaria = formToolkit.createButton(shell, "", SWT.NONE);
		btnDetranOrcamentaria.setBounds(209, 93, 70, 51);

		Button btnDetranServidores = formToolkit.createButton(shell, "", SWT.NONE);
		btnDetranServidores.setBounds(332, 93, 70, 48);

		Button btnDetranDespesas = formToolkit.createButton(shell, "", SWT.NONE);
		btnDetranDespesas.setBounds(450, 93, 75, 48);

		Button btnDetranAcoesProgramas = new Button(shell, SWT.NONE);
		btnDetranAcoesProgramas.setBounds(581, 88, 70, 53);
		formToolkit.adapt(btnDetranAcoesProgramas, true, true);
		btnDetranAcoesProgramas.setText("");

		Button btnAtiConvenios = formToolkit.createButton(shell, "", SWT.NONE);
		btnAtiConvenios.setBounds(91, 170, 70, 51);

		Button btnAtiOrcamentaria = formToolkit.createButton(shell, "", SWT.NONE);
		btnAtiOrcamentaria.setBounds(209, 170, 70, 51);

		Button btnAtiServidores = formToolkit.createButton(shell, "", SWT.NONE);
		btnAtiServidores.setBounds(332, 170, 70, 51);

		Button btnAtiDespesas = formToolkit.createButton(shell, "", SWT.NONE);
		btnAtiDespesas.setBounds(450, 170, 75, 51);

		Button btnAtiAcoesProgramas = new Button(shell, SWT.NONE);
		btnAtiAcoesProgramas.setBounds(581, 170, 70, 51);
		formToolkit.adapt(btnAtiAcoesProgramas, true, true);
		btnAtiAcoesProgramas.setText("");

	}
}

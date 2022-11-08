package ui.mainScreen;

import model.Equation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.EquationService;
import service.EquationService_impl;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Main window of UI
 *
 * @see MainTable
 * @see ButtonsPanel
 */
public class MainScreen extends JFrame {
    private static final Logger LOGGER = LoggerFactory.getLogger(MainScreen.class);

    private static final String TITLE = "MathParser";
    private static final String OOPS = "Oops!";
    private static final String SEARCH = "Search";
    private static final String SEARCH_OFF = "Search off";

    private static final String ERROR_MESSAGE = "Something go wrong! Please try again.";

    private final EquationService service = new EquationService_impl();

    private final ArrayList<Equation>list;

    private final MainTable mainTable;
    public final ButtonsPanel buttonsPanel;

    public boolean searchOn = false;

    public MainScreen(){
        super(TITLE);

        this.list = service.getAll();

        mainTable = new MainTable(this.list, this);
        buttonsPanel = new ButtonsPanel(this);

        build();
        setReactions();

        LOGGER.debug("MainScreen was created successful");
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
        LOGGER.debug("Visibility of MainScreen was sets: {}", b);
    }

    /**
     * Sets up the size, location, appearance and content of window
     */
    private void build(){
        Dimension sizeOfScreen = new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width,
                GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().height);
        this.setSize(sizeOfScreen);
        this.setContentPane(new MainPanel());
    }

    /**
     * Assign reactions to user actions to window and content
     *
     * @see ButtonsPanel
     */
    private void setReactions(){
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    /**
     * Gets selected equation from {@link #mainTable}
     *
     * @return equation from {@link #list} by index equal selected row from {@link #mainTable}
     * or null if row wasn't selected
     *
     * @see MainTable
     */
    public Equation getSelectedEquation(){
        int selected = mainTable.getSelectedRow();
        return selected >= 0 && selected < list.size() ? list.get(selected) : null;
    }

    /**
     * Sends the command 'add' to {@link #service} and resets list in {@link #mainTable}
     * if @param == null or {@link EquationService#add(Equation)} returns false - show error dialog
     *
     * @param equation to change
     *
     * @see EquationService#set(Equation)
     * @see #resetList()
     */
    public void addEquation(Equation equation){
        if (equation != null && service.add(equation)) {
            resetList();
        }else {
            JOptionPane.showMessageDialog(this, ERROR_MESSAGE, OOPS, JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Sends the command 'remove' to {@link #service} and resets list in {@link #mainTable}
     * if @param < 0 or {@link EquationService#remove(int)} returns false - show error dialog
     *
     * @param id of equation that need to remove
     *
     * @see EquationService#remove(int id)
     * @see #resetList()
     */
    public void removeEquation(int id){
        if (id >= 0 && service.remove(id)) {
            resetList();
        }else {
            JOptionPane.showMessageDialog(this, ERROR_MESSAGE, OOPS, JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Sends the command 'set' to {@link #service} and resets list in {@link #mainTable}
     * if @param == null or {@link EquationService#set(Equation)} returns false - show error dialog
     *
     * @param equation to change
     *
     * @see EquationService#set(Equation)
     * @see #resetList()
     */
    public void setEquation(Equation equation){
        if (equation != null && service.set(equation)) {
            resetList();
        }else {
            JOptionPane.showMessageDialog(this, ERROR_MESSAGE, OOPS, JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Makes search off and resets list in table
     *
     * @see EquationService#getAll()
     * @see #searchEquations(String, double)
     */
    public void resetList(){
        searchOn = false;
        buttonsPanel.btn_search.setText(SEARCH);
        list.clear();
        list.addAll(service.getAll());
        mainTable.setList(list);
    }

    /**
     * Turns on search and show list of equations that matches search condition
     *
     * @param condition of search
     *
     * @param result for search
     *
     * @see EquationService#get(String condition, double result)
     * @see #resetList() ()
     */
    public void searchEquations(String condition, double result){
        searchOn = true;
        buttonsPanel.btn_search.setText(SEARCH_OFF);
        list.clear();
        list.addAll(service.get(condition, result));
        mainTable.setList(list);
    }

    /**
     * Panel of main window content
     */
    private class MainPanel extends JPanel {
        MainPanel(){
            super(new GridBagLayout());

            this.add(buttonsPanel, new Cell(0,0.05));
            this.add(new JScrollPane(mainTable), new Cell(1,0.95));
        }

        private class Cell extends GridBagConstraints {
            Cell(int y, double weightY){
                super();

                this.fill = BOTH;
                this.weightx = 1D;
                this.gridx = 0;
                this.insets = new Insets(20,0,10,0);

                this.gridy = y;
                this.weighty = weightY;
            }
        }
    }
}

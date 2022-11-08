package ui.mainScreen;

import model.Equation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ui.equationInfo.EquationInfoDialog;
import ui.equationSearch.EquationSearchDialog;
import ui.model.DefaultButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Buttons panel of main window
 *
 * @see MainScreen
 */
public class ButtonsPanel extends JPanel {
    private static final Logger LOGGER = LoggerFactory.getLogger(ButtonsPanel.class);

    private static final String ADD = "Add";
    private static final String CHANGE = "Change";
    private static final String REMOVE = "Remove";
    private static final String SEARCH = "Search";

    private String removeMessage(Equation equation){
        return "Delete equation \"" + equation.getEquation() + "\" with result = " + equation.getResult() + " ?";
    }

    private final MainScreen owner;

    private final JButton btn_add, btn_change, btn_remove;
    public final JButton btn_search;

    public ButtonsPanel(MainScreen owner){
        super(new GridBagLayout());

        this.owner = owner;

        btn_add = new DefaultButton(ADD);
        btn_remove = new DefaultButton(REMOVE);
        btn_change = new DefaultButton(CHANGE);
        btn_search = new DefaultButton(SEARCH);

        build();
        setReactions();

        LOGGER.debug("ButtonsPanel of MainScreen was created successful");
    }

    /**
     * Sets up content of panel
     */
    private void build(){
        this.add(btn_search, new Cell(0,0));
        this.add(btn_add, new Cell(1,0));
        this.add(btn_remove, new Cell(0,1));
        this.add(btn_change, new Cell(1,1));
    }

    /**
     * Assign reactions to user actions to buttons in the panel
     */
    private void setReactions(){
        btn_change.addActionListener(clickChange);
        btn_search.addActionListener(clickSearch);
        btn_remove.addActionListener(clickRemove);
        btn_add.addActionListener(clickAdd);
    }

    @Override
    public void setEnabled(boolean enabled) {
        btn_change.setEnabled(enabled);
        btn_remove.setEnabled(enabled);
    }

    private final ActionListener clickAdd = e -> EventQueue.invokeLater(new Runnable() {
        @Override
        public void run() {
            LOGGER.debug("btn_add was clicked");

            LOGGER.debug("""
                    Trying to create EquationInfoDialog with params:
                    MainScreen = {}
                    Equation = {}""",
                    owner, null);
            new EquationInfoDialog(owner, null).setVisible(true);
        }
    });

    private final ActionListener clickRemove = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            LOGGER.debug("btn_remove was clicked");

            Equation equation = owner.getSelectedEquation();
            if (equation != null){
                int result = JOptionPane.showConfirmDialog(owner, removeMessage(equation), REMOVE, JOptionPane.YES_NO_OPTION);
                if (result == 0) owner.removeEquation(equation.getId());
            }else {
                LOGGER.debug("No equation has been selected");
            }
        }
    };

    private final ActionListener clickSearch = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            LOGGER.debug("btn_search was clicked");

            if (owner.searchOn) {
                owner.resetList();
                LOGGER.debug("Search disabled");
            } else {
                LOGGER.debug("""
                    Trying to create EquationSearchDialog with params:
                    MainScreen = {}""",
                        owner);

                EventQueue.invokeLater(() -> new EquationSearchDialog(owner).setVisible(true));
            }
        }
    };

    private final ActionListener clickChange = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            LOGGER.debug("btn_change was clicked");

            Equation equation = owner.getSelectedEquation();

            LOGGER.debug("""
                    Trying to create EquationInfoDialog with params:
                    MainScreen = {}
                    Equation = {}""",
                    owner, equation);

            if (equation != null) new EquationInfoDialog(owner,equation).setVisible(true);
        }
    };

    private static class Cell extends GridBagConstraints {
        Cell(int x, int y){
            super();

            this.fill = BOTH;
            this.insets = new Insets(5,5,5,5);

            this.gridx = x;
            this.gridy = y;
        }
    }
}

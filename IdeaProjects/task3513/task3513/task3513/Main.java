package task3513;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Model model = new Model();

        //model.test();
        Controller controller = new Controller(model);
        JFrame game = new JFrame();
        game.setTitle("2048");
        JMenuBar jMenuBar = new JMenuBar();
        JPanel helpPanel = new JPanel();
        JPanel helpPanel1 = new JPanel();
        JPanel helpPanel2 = new JPanel();
        helpPanel.add(new JLabel("A - автоход"));
        helpPanel1.add(new JLabel("R - случайный ход"));
        helpPanel2.add(new JLabel("Z - отмена хода"));
        JMenu helpMenu = new JMenu("помощь");
        helpMenu.add(helpPanel);
        helpMenu.add(helpPanel1);
        helpMenu.add(helpPanel2);
        jMenuBar.add(helpMenu);
        game.setJMenuBar(jMenuBar);
        game.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        game.setSize(450,570);
        game.setResizable(false);

        game.add(controller.getView());
        game.setLocationRelativeTo(null);
        game.setVisible(true);



    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.ArrayList;

public class MemoryGame extends JFrame implements ActionListener {
    private JButton[][] buttons = new JButton[7][4];
    private String[][] values = new String[7][4];
    private JButton firstButton = null, secondButton = null;
    private int firstRow, firstCol, secondRow, secondCol;
    private String player1Name, player2Name;
    private int turn = 1; // Player 1 = 1, Player 2 = 2
    private int player1Score = 0, player2Score = 0;
    private JLabel statusLabel, player1Label, player2Label, turnLabel;

    public MemoryGame() {
        // Get player names
        player1Name = JOptionPane.showInputDialog(this, "Enter Player 1 Name:", "Player 1");
        player2Name = JOptionPane.showInputDialog(this, "Enter Player 2 Name:", "Player 2");

        // Set up the frame
        setTitle("Memory Game");
        setSize(600, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Set background color (Dark Teal)
        getContentPane().setBackground(new Color(58, 109, 128));

        // Set up the game board panel
        JPanel panel = new JPanel(new GridLayout(7, 4, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(243, 205, 83));  // Border color (Amber)

        ArrayList<String> valuesList = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            String value = Character.toString((char) ('L' + i));
            valuesList.add(value);
            valuesList.add(value);
        }
        Collections.shuffle(valuesList);

        int index = 0;
        for (int row = 0; row < 7; row++) {
            for (int col = 0; col < 4; col++) {
                buttons[row][col] = new JButton("");
                buttons[row][col].setFont(new Font("Arial", Font.BOLD, 16));
                buttons[row][col].setBackground(new Color(253, 248, 240)); // Default Beige Tile
                buttons[row][col].setForeground(Color.BLACK);
                buttons[row][col].setFocusPainted(false);
                buttons[row][col].addActionListener(this);
                panel.add(buttons[row][col]);
                values[row][col] = valuesList.get(index);
                index++;
            }
        }

        JPanel statusPanel = new JPanel(new GridLayout(1, 3));
        player1Label = new JLabel(player1Name + ": 0", JLabel.CENTER);
        player2Label = new JLabel(player2Name + ": 0", JLabel.CENTER);
        turnLabel = new JLabel(player1Name + "'s Turn", JLabel.CENTER);

        // Customize labels
        player1Label.setFont(new Font("Arial", Font.BOLD, 20));
        player2Label.setFont(new Font("Arial", Font.BOLD, 20));
        turnLabel.setFont(new Font("Arial", Font.BOLD, 20));
        player1Label.setForeground(Color.WHITE);
        player2Label.setForeground(Color.WHITE);
        turnLabel.setForeground(new Color(168, 218, 205));

        statusPanel.setBackground(new Color(58, 109, 128));
        statusPanel.add(player1Label);
        statusPanel.add(turnLabel);
        statusPanel.add(player2Label);

        add(statusPanel, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton clickedButton = (JButton) e.getSource();
        
        for (int row = 0; row < 7; row++) {
            for (int col = 0; col < 4; col++) {
                if (buttons[row][col] == clickedButton) {
                    handleButtonClick(row, col);
                    return;
                }
            }
        }
    }

    private void handleButtonClick(int row, int col) {
        if (firstButton == null) {
            // First button click
            firstButton = buttons[row][col];
            firstRow = row;
            firstCol = col;
            firstButton.setText(values[row][col]);
            firstButton.setBackground(new Color(206, 143, 48));
            firstButton.setForeground(new Color(168, 218, 205));
            firstButton.setEnabled(false);
        } else if (secondButton == null) {
            // Second button click
            secondButton = buttons[row][col];
            secondRow = row;
            secondCol = col;
            secondButton.setText(values[row][col]);
            secondButton.setBackground(new Color(237, 50, 36));
            secondButton.setForeground(new Color(168, 218, 205));
            secondButton.setEnabled(false);

            // Check for match
            if (values[firstRow][firstCol].equals(values[secondRow][secondCol])) {
                // Match found
                firstButton.setBackground(Color.WHITE);
                secondButton.setBackground(Color.WHITE);
                if (turn == 1) {
                    player1Score++;
                    player1Label.setText(player1Name + ": " + player1Score);
                } else {
                    player2Score++;
                    player2Label.setText(player2Name + ": " + player2Score);
                }
                resetSelection();
                checkGameEnd();
            } else {
                // No match, wait for a brief moment then flip back
                Timer timer = new Timer(500, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        firstButton.setText("");
                        firstButton.setBackground(new Color(253, 248, 240));
                        firstButton.setEnabled(true);
                        secondButton.setText("");
                        secondButton.setBackground(new Color(253, 248, 240));
                        secondButton.setEnabled(true);
                        resetSelection();

                        turn = (turn == 1) ? 2 : 1;
                        turnLabel.setText((turn == 1 ? player1Name : player2Name) + "'s Turn");
                    }
                });
                timer.setRepeats(false);
                timer.start();
            }
        }
    }

    private void resetSelection() {
        firstButton = null;
        secondButton = null;
    }

    private void checkGameEnd() {
        int totalPairs = player1Score + player2Score;
        if (totalPairs == 14) {
            String winner;
            if (player1Score > player2Score) {
                winner = player1Name + " wins!";
            } else if (player2Score > player1Score) {
                winner = player2Name + " wins!";
            } else {
                winner = "It's a tie!";
            }
            JOptionPane.showMessageDialog(this, winner);
            System.exit(0); // Close the game
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MemoryGame());
    }
}

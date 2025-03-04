import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class PuzzleGame extends JFrame implements ActionListener {
    private JButton[] buttons = new JButton[9]; // 9 buttons (8 numbered + 1 blank)
    private int[] numbers = new int[9]; // Stores the numbers 1-8, and the empty space (0)
    private final Font font = new Font("Arial", Font.BOLD, 40);
    private int moveCount = 0;
    private JLabel moveLabel;

    public PuzzleGame() {
        setTitle("9-Tile Puzzle Game");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(3, 3, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (int i = 0; i < 9; i++) {
            buttons[i] = new JButton();
            buttons[i].setFont(font);
            buttons[i].addActionListener(this);
            panel.add(buttons[i]);
        }

        moveLabel = new JLabel("Moves: 0", SwingConstants.CENTER);
        moveLabel.setFont(new Font("Arial", Font.PLAIN, 20));

        JButton shuffleButton = new JButton("Shuffle");
        shuffleButton.setFont(new Font("Arial", Font.PLAIN, 20));
        shuffleButton.addActionListener(e -> shuffleTiles());

        add(panel, BorderLayout.CENTER);
        add(moveLabel, BorderLayout.NORTH);
        add(shuffleButton, BorderLayout.SOUTH);

        shuffleTiles(); // Initialize the board in shuffled state
        setVisible(true);
    }

    // Shuffle the tiles to start the game
    private void shuffleTiles() {
        List<Integer> tileList = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            tileList.add(i); // Numbers 0-8
        }
        Collections.shuffle(tileList);
        while (!isSolvable(tileList) || isSolved(tileList)) {
            Collections.shuffle(tileList); // Ensure puzzle is solvable and not pre-solved
        }

        for (int i = 0; i < 9; i++) {
            numbers[i] = tileList.get(i);
            buttons[i].setText(numbers[i] == 0 ? "" : String.valueOf(numbers[i]));
        }

        moveCount = 0;
        moveLabel.setText("Moves: 0");
    }

    // Check if the puzzle is solvable
    private boolean isSolvable(List<Integer> list) {
        int inversions = 0;
        for (int i = 0; i < list.size(); i++) {
            for (int j = i + 1; j < list.size(); j++) {
                if (list.get(i) != 0 && list.get(j) != 0 && list.get(i) > list.get(j)) {
                    inversions++;
                }
            }
        }
        return inversions % 2 == 0; // Solvable if the number of inversions is even
    }

    // Check if the puzzle is already solved
    private boolean isSolved(List<Integer> list) {
        for (int i = 0; i < 8; i++) {
            if (list.get(i) != i + 1) {
                return false;
            }
        }
        return list.get(8) == 0; // Last tile should be empty
    }

    // Handle tile moves
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton clickedButton = (JButton) e.getSource();
        int clickedIndex = -1;
        for (int i = 0; i < 9; i++) {
            if (buttons[i] == clickedButton) {
                clickedIndex = i;
                break;
            }
        }

        // Get the index of the blank tile
        int blankIndex = findBlankTile();

        if (isAdjacent(clickedIndex, blankIndex)) {
            swapTiles(clickedIndex, blankIndex);
            moveCount++;
            moveLabel.setText("Moves: " + moveCount);

            if (checkWin()) {
                JOptionPane.showMessageDialog(this, "Congratulations! You solved the puzzle in " + moveCount + " moves.");
                shuffleTiles(); // Shuffle after winning
            }
        }
    }

    // Find the index of the blank tile (0)
    private int findBlankTile() {
        for (int i = 0; i < 9; i++) {
            if (numbers[i] == 0) {
                return i;
            }
        }
        return -1;
    }

    // Check if two tiles are adjacent (can be swapped)
    private boolean isAdjacent(int index1, int index2) {
        if (index1 == -1 || index2 == -1) return false;
        int row1 = index1 / 3, col1 = index1 % 3;
        int row2 = index2 / 3, col2 = index2 % 3;
        return (Math.abs(row1 - row2) + Math.abs(col1 - col2)) == 1;
    }

    // Swap two tiles
    private void swapTiles(int index1, int index2) {
        int temp = numbers[index1];
        numbers[index1] = numbers[index2];
        numbers[index2] = temp;

        buttons[index1].setText(numbers[index1] == 0 ? "" : String.valueOf(numbers[index1]));
        buttons[index2].setText(numbers[index2] == 0 ? "" : String.valueOf(numbers[index2]));
    }

    // Check if the player has won
    private boolean checkWin() {
        for (int i = 0; i < 8; i++) {
            if (numbers[i] != i + 1) {
                return false;
            }
        }
        return numbers[8] == 0; // The last tile should be the blank space
    }

    // The main method to start the game
    public static void main(String[] args) {
        SwingUtilities.invokeLater(PuzzleGame::new);
    }
}

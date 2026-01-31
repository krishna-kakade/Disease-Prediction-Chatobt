// File: MainUI.java
import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Disease Prediction Chatbot UI
 * - Auto-scrolls vertically to latest message
 * - No horizontal scrollbars
 * - Clean, bubble-style layout
 */
public class MainUI {
    private JFrame frame;
    private JPanel chatPanel;
    private JTextArea inputArea;
    private JButton sendBtn;
    private JLabel statusLabel;
    private JScrollPane scrollPane;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainUI().createAndShowGUI());
    }

    private void createAndShowGUI() {
        frame = new JFrame("Disease Prediction Chatbot");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(650, 520);
        frame.setLayout(new BorderLayout(8, 8));

        // Top status bar
        JPanel top = new JPanel(new BorderLayout());
        statusLabel = new JLabel("Status: Ready");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        top.add(statusLabel, BorderLayout.WEST);
        frame.add(top, BorderLayout.NORTH);

        // Chat area (scrollable vertically)
        chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
        chatPanel.setBackground(Color.WHITE);

        scrollPane = new JScrollPane(chatPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // smooth scroll speed

        frame.add(scrollPane, BorderLayout.CENTER);

        // Bottom input area
        inputArea = new JTextArea(4, 40);
        inputArea.setLineWrap(true);
        inputArea.setWrapStyleWord(true);
        inputArea.setFont(new Font("SansSerif", Font.PLAIN, 14));

        sendBtn = new JButton("Predict");
        sendBtn.addActionListener(e -> onSend());

        JPanel bottom = new JPanel(new BorderLayout(6, 6));
        bottom.add(new JScrollPane(inputArea,
                JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);
        bottom.add(sendBtn, BorderLayout.EAST);

        frame.add(bottom, BorderLayout.SOUTH);

        // Load chat history if available
        List<String> history = HistoryManager.loadLast(5);
        if (!history.isEmpty()) {
            for (String h : history) appendSystem(h);
            appendSystem("Loaded last " + history.size() + " history entries.");
        }

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void onSend() {
        String raw = inputArea.getText();
        String normalized = SymptomHelper.normalize(raw);
        if (normalized.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter symptoms.", "Input required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        appendUser(normalized);
        ChatLogger.log("You", normalized);
        statusLabel.setText("Status: Sending...");

        sendBtn.setEnabled(false);
        appendSystem("Sending to backend...");

        new Thread(() -> {
            try {
                BaseClient client = new ApiClient();
                String jsonResponse = client.sendSymptoms(normalized);
                System.out.println("RAW BACKEND RESPONSE: " + jsonResponse);

                String extracted = ResponseParser.parseMessage(jsonResponse);
                String enhanced = ResponseEnhancer.enhance(extracted);

                appendBot(enhanced);
                ChatLogger.log("Bot", enhanced);
            } catch (Exception ex) {
                String err = "Error connecting to backend: " + ex.getMessage();
                appendBot(err);
                ChatLogger.log("Bot", err);
                ex.printStackTrace();
            } finally {
                SwingUtilities.invokeLater(() -> {
                    sendBtn.setEnabled(true);
                    statusLabel.setText("Status: Ready");
                });
            }
        }).start();

        inputArea.setText("");
    }

    // Message append helpers
    private void appendUser(String text) {
        appendBubble("You", text, true, new Color(0, 102, 204));
    }

    private void appendBot(String text) {
        appendBubble("Bot", text, false, new Color(0, 128, 0));
    }

    private void appendSystem(String text) {
        appendBubble("System", text, false, Color.GRAY);
    }

    /**
     * Adds a new chat bubble to the chat panel and auto-scrolls vertically
     */
    private void appendBubble(String who, String message, boolean right, Color color) {
        SwingUtilities.invokeLater(() -> {
            JPanel wrapper = new JPanel(new BorderLayout());
            wrapper.setBackground(Color.WHITE);

            JTextArea bubble = new JTextArea();
            bubble.setText(who + " [" + java.time.LocalTime.now().withNano(0) + "]:\n" + message);
            bubble.setLineWrap(true);
            bubble.setWrapStyleWord(true);
            bubble.setEditable(false);
            bubble.setBackground(color);
            bubble.setForeground(Color.WHITE);
            bubble.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
            bubble.setFont(new Font("SansSerif", Font.PLAIN, 13));

            // Keep bubble width within window
            bubble.setMaximumSize(new Dimension(frame.getWidth() - 100, Integer.MAX_VALUE));

            if (who.equals("System")) {
                bubble.setForeground(Color.BLACK);
                wrapper.add(bubble, BorderLayout.CENTER);
            } else if (right) {
                wrapper.add(bubble, BorderLayout.EAST);
            } else {
                wrapper.add(bubble, BorderLayout.WEST);
            }

            chatPanel.add(wrapper);
            chatPanel.add(Box.createVerticalStrut(10));
            chatPanel.revalidate();
            chatPanel.repaint();

            // ✅ Vertical auto-scroll to latest message
            SwingUtilities.invokeLater(() -> {
                JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
                verticalBar.setValue(verticalBar.getMaximum());
            });
        });
    }
}

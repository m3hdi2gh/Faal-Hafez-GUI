package org.example;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Main {

    public static void main(String[] args) {
        new FaalGUI();
    }

    public static class FaalGUI extends JFrame implements ActionListener {

        private static final String GET_URL = "https://faal.spclashers.workers.dev/api";
        JButton getFaal = new JButton("دریافت فال");
        JButton backButton = new JButton("بازگشت");

        public FaalGUI() {
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setTitle("فال حافظ");
            HomePage();
            this.setSize(700, 800);
            this.setVisible(true);
        }

        private static Faal getFaal() throws IOException {

            URL url = new URL(GET_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                in.close();

                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.readValue(response.toString(), Faal.class);
            }
            return null;
        }

        public void HomePage() {

            JPanel HomePage = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    ImageIcon imageIcon = new ImageIcon("src/main/resources/homePage.jpg");
                    Image image = imageIcon.getImage();
                    g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
                }
            };
            HomePage.setLayout(null);

            getFaal.setContentAreaFilled(false);
            getFaal.setFont(new Font("Vazirmatn", Font.BOLD, 40));
            getFaal.setForeground(Color.GRAY);
            getFaal.setBounds(50, 650, 215, 50);
            getFaal.addActionListener(this);

            HomePage.add(getFaal);
            this.add(HomePage);
        }

        public void FaalPage(String poem, String interpretation) {
            JPanel faalPage = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    ImageIcon imageIcon = new ImageIcon("src/main/resources/faalPage.jpg");
                    Image image = imageIcon.getImage();
                    g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
                }
            };

            JScrollPane scrollPane = new JScrollPane(faalPage);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.getVerticalScrollBar().setUnitIncrement(16);
            this.add(scrollPane);

            faalPage.setLayout(new BoxLayout(faalPage, BoxLayout.Y_AXIS));
            faalPage.setAlignmentX(Component.CENTER_ALIGNMENT);
            faalPage.setAlignmentY(Component.CENTER_ALIGNMENT);
            faalPage.add(Box.createRigidArea(new Dimension(0, 20)));

            JLabel label1 = new JLabel("شعر");
            label1.setFont(new Font("Vazirmatn", Font.BOLD, 30));

            JLabel label3 = new JLabel("تفسیر");
            label3.setFont(new Font("Vazirmatn", Font.BOLD, 30));

            JTextArea label2 = new JTextArea(poem);
            label2.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
            label2.setEditable(false);
            label2.setOpaque(false);
            label2.setBorder(BorderFactory.createEmptyBorder(0, 100, 0, 100));
            label2.setFont(new Font("Vazirmatn", Font.BOLD, 18));

            JTextArea label4 = new JTextArea(interpretation);
            label4.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
            label4.setEditable(false);
            label4.setOpaque(false);
            label4.setLineWrap(true);
            label4.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
            label4.setFont(new Font("Vazirmatn", Font.BOLD, 18));

            backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            backButton.setFont(new Font("Vazirmatn", Font.BOLD, 14));
            backButton.setContentAreaFilled(false);
            backButton.addActionListener(this);

            faalPage.add(label1);
            faalPage.add(Box.createRigidArea(new Dimension(0, 20)));
            faalPage.add(label2);
            faalPage.add(label3);
            faalPage.add(Box.createRigidArea(new Dimension(0, 20)));
            faalPage.add(label4);
            faalPage.add(backButton);
            faalPage.add(Box.createRigidArea(new Dimension(0, 20)));
        }

        public static class Faal {
            @JsonProperty("Poem")
            private String poem;
            @JsonProperty("Interpretation")
            private String interpretation;

            public String getPoem() {
                String[] parts = poem.split("\\\\r\\\\n");
                StringBuilder poem = new StringBuilder();
                for (int i = 0; i < parts.length; i++) {
                    poem.append(parts[i]);
                    if (i != parts.length - 1) {
                        if (i % 2 == 0) {
                            poem.append("\n" + "\t".repeat(30));
                        } else {
                            poem.append("\n");
                        }
                    }
                }
                return poem.toString();
            }

            public String getInterpretation() {
                return interpretation;
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == getFaal) {
                this.getContentPane().removeAll();

                try {
                    Faal faal = getFaal();
                    assert faal != null;
                    FaalPage(faal.getPoem(), faal.getInterpretation());
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }

                this.revalidate();
                this.repaint();
            }

            if (e.getSource() == backButton) {
                this.getContentPane().removeAll();
                HomePage();
                this.revalidate();
                this.repaint();
            }
        }
    }
}
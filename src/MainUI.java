import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainUI extends JFrame{
    private JPanel main;
    private JTextField ip;
    private JTextField from;
    private JTextField to;
    private JTextField subject;
    private JTextArea message;
    private JButton sendButton;
    private JButton quitButton;
    private JTextField account;
    private JPasswordField password;
    private JTextField port;
    private JPanel mainPanel;
    public boolean containsKeyword(String text, String keyword) {
        return text.indexOf(keyword) != -1;
    }
    public MainUI(){
        setContentPane(mainPanel);
        setTitle("main");
        setSize(500,500);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String toSplit =to.getText();
                String[] parts = toSplit.split(",");
                int i = 0,j= 0;
                for(String part : parts) {
                    j++;
                    if (containsKeyword(part, "@")) {
                        String passwordString = new String(password.getPassword());
                        MailSender mailSender = new MailSender();
                        mailSender.sender(ip.getText(),Integer.parseInt(port.getText()),from.getText(),passwordString,account.getText(),part,subject.getText(),message.getText());
                        i++;
                    } else {
                        JOptionPane.showMessageDialog(null, "Email address is illegal", "Error", JOptionPane.ERROR_MESSAGE);

                    }

                }
                JOptionPane.showMessageDialog(null, "Mail Send.\r\nSend to"+toSplit+"\r\nWW計出"+i+"封", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }
    public static void main(String[] args){
        MainUI frame = new MainUI();

    }

}

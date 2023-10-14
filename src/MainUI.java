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
    public MainUI(){
        setContentPane(mainPanel);
        setTitle("main");
        setSize(500,500);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(ip.getText());
                System.out.println(port.getText());
                System.out.println(from.getText());
                System.out.println(account.getText());
                String passwordString = new String(password.getPassword());
                System.out.println(passwordString);
                System.out.println(to.getText());
                System.out.println(subject.getText());
                System.out.println(message.getText());
                MailSender mailSender = new MailSender();
                mailSender.sender(ip.getText(),Integer.parseInt(port.getText()),from.getText(),passwordString,account.getText(),to.getText(),subject.getText(),message.getText());
                JOptionPane.showMessageDialog(null, "Mail Send.", "Success", JOptionPane.INFORMATION_MESSAGE);
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

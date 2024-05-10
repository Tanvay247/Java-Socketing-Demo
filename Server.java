package WeChat.App;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.net.*;
import java.io.*;

public class Server implements ActionListener {

    // Globally declared so that rest methods can access it as well
    static JFrame f = new JFrame();
    JTextField j;
    JPanel a;
    static Box vert = Box.createVerticalBox();
    static DataOutputStream dout;

    Server() {

        JPanel head = new JPanel();
        head.setBackground(new Color(255, 164, 164));
        head.setBounds(0, 0, 450, 65);
        head.setLayout(null);
        f.add(head);

        // BackArrow Button
        ImageIcon i = new ImageIcon(ClassLoader.getSystemResource("icons/leftarrow.png"));
        Image temp = i.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT);
        ImageIcon i1 = new ImageIcon(temp);
        JLabel backbtn = new JLabel(i1);
        backbtn.setBounds(9, 20, 25, 25);
        head.add(backbtn);

        backbtn.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }
        });

        // Profile Image
        ImageIcon ii = new ImageIcon(ClassLoader.getSystemResource("icons/batman.png"));
        Image temp2 = ii.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT);
        ImageIcon i2 = new ImageIcon(temp2);
        JLabel profilephoto = new JLabel(i2);
        profilephoto.setBounds(45, 10, 50, 50);
        head.add(profilephoto);

        // Profile Name
        JLabel name = new JLabel("Batman");
        name.setBounds(110, 15, 100, 18);
        name.setFont(new Font("SAN_SERIF", Font.BOLD, 22));
        head.add(name);

        // Active Status
        JLabel active = new JLabel("Active");
        active.setBounds(110, 35, 100, 18);
        active.setFont(new Font("SAN_SERIF", Font.BOLD, 14));
        head.add(active);

        // Video Call Image
        ImageIcon iii = new ImageIcon(ClassLoader.getSystemResource("icons/video.png"));
        Image temp3 = iii.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(temp3);
        JLabel videophoto = new JLabel(i3);
        videophoto.setBounds(300, 20, 30, 30);
        head.add(videophoto);

        // Phone Call Image
        ImageIcon iiii = new ImageIcon(ClassLoader.getSystemResource("icons/phone.png"));
        Image temp4 = iiii.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT);
        ImageIcon i4 = new ImageIcon(temp4);
        JLabel phonephoto = new JLabel(i4);
        phonephoto.setBounds(365, 22, 25, 25);
        head.add(phonephoto);

        // More Button Image
        ImageIcon iiiii = new ImageIcon(ClassLoader.getSystemResource("icons/more.png"));
        Image temp5 = iiiii.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT);
        ImageIcon i5 = new ImageIcon(temp5);
        JLabel morephoto = new JLabel(i5);
        morephoto.setBounds(410, 20, 35, 35);
        head.add(morephoto);

        // MidPanel creation
        a = new JPanel();
        a.setBounds(0, 65, 450, 545);
        a.setBackground(new Color(255, 240, 240));
        f.add(a);

        // Typing Box creation
        j = new JTextField();
        j.setBounds(10, 618, 315, 45);
        j.setFont(new Font("SAN_SERIF", Font.PLAIN, 18));
        f.add(j);

        // Send Button
        Button b = new Button("Send");
        b.setBounds(335, 618, 105, 45);
        b.addActionListener(this);
        b.setFont(new Font("SAN_SERIF", Font.PLAIN, 18));
        f.add(b);

        // Frame Creation
        f.getContentPane().setBackground(new Color(255, 164, 164));
        f.setSize(450, 700);
        f.setLocation(200, 50);
        f.setLayout(null);
        f.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {

        try {

            // Storing String value from TextField in op variable
            String op = j.getText();

            JPanel jout = formatLabel(op);
            jout.setBackground(new Color(255, 240, 240));

            a.setLayout(new BorderLayout());

            // Identating sent Chat clouds to right of Frame
            JPanel right = new JPanel(new BorderLayout());
            right.add(jout, BorderLayout.LINE_END);
            right.setBackground(new Color(255, 240, 240));

            vert.add(right);
            vert.add(Box.createVerticalStrut(15));

            a.add(vert, BorderLayout.PAGE_START);

            dout.writeUTF(op);

            j.setText("");

            f.revalidate();
            f.repaint();

        } catch (Exception er) {
            er.printStackTrace();
        }

    }

    // Text Clouds creation
    public static JPanel formatLabel(String op) {
        JPanel pan = new JPanel();
        pan.setLayout(new BoxLayout(pan, BoxLayout.Y_AXIS));

        JLabel output = new JLabel("<html><p style=\"width: 130px\">" + op + "</p></html>");
        output.setFont(new Font("SAN_SERIF", Font.PLAIN, 20));
        output.setBackground(new Color(243, 58, 106));
        output.setForeground(Color.white);
        output.setOpaque(true);
        output.setBorder(new EmptyBorder(15, 15, 15, 50));

        // To get Time at which text was sent
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        // Displaying time underneath text cloud
        JLabel time = new JLabel();
        time.setText(sdf.format(cal.getTime()));
        time.setBackground(new Color(255, 240, 240));

        pan.add(output);
        pan.add(time);
        return pan;
    }

    public static void main(String[] args) {
        new Server();

        try {
            ServerSocket skt = new ServerSocket(6001);
            while (true) {
                Socket s = skt.accept();
                DataInputStream din = new DataInputStream(s.getInputStream());
                dout = new DataOutputStream(s.getOutputStream());

                while (true) {
                    String msg = din.readUTF();

                    JPanel panel = formatLabel(msg);
                    panel.setBackground(new Color(255, 240, 240));

                    // Identating sent Chat clouds to left of Frame
                    JPanel left = new JPanel(new BorderLayout());
                    left.add(panel, BorderLayout.LINE_START);
                    left.setBackground(new Color(255, 240, 240));
                    vert.setBackground(new Color(255, 240, 240));
                    vert.add(left);

                    f.revalidate();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

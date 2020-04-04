import javax.swing.*;
import java.awt.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;


public class TimerApp {

    private JTextField time = new JTextField();
    private JTextField binary = new JTextField(getBinary());
    private JLabel desc = new JLabel("Time left until " + getMaxPosixTime().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)));
    private LocalDateTime maxPosixTime = getMaxPosixTime();

    {
        desc.setForeground(Color.GREEN);
        time.setEditable(false);
        time.setHorizontalAlignment(SwingConstants.CENTER);
        time.setFont(new Font(Font.MONOSPACED, Font.BOLD, 15));
        time.setForeground(Color.GREEN);
        time.setBackground(Color.DARK_GRAY);
        binary.setEditable(false);
        binary.setHorizontalAlignment(SwingConstants.CENTER);
        binary.setFont(new Font(Font.MONOSPACED, Font.BOLD, 15));
        binary.setForeground(Color.GREEN);
        binary.setBackground(Color.DARK_GRAY);

    }

    private LocalDateTime getMaxPosixTime() {
        return Instant.ofEpochSecond(Integer.MAX_VALUE).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    private String getBinary(){
        long unixTime = LocalDateTime.of(1970,1,1,0,0,0)
                .until(LocalDateTime.now(), ChronoUnit.SECONDS);
        return Long.toBinaryString(unixTime);
    }

    private String calculateRemaining() {
        int years = maxPosixTime.getYear() - LocalDateTime.now().getYear() - 1;
        long untilNewYear = LocalDateTime.now()
                .until(
                        LocalDateTime.of(
                                (LocalDateTime.now().getYear() + 1), 1, 1, 0, 0, 0), ChronoUnit.SECONDS);
        long remainingSeconds = LocalDateTime.of(2037, 1, 1, 0, 0, 0)
                .until(maxPosixTime, ChronoUnit.SECONDS) + untilNewYear;
        if (remainingSeconds > 31536000) {
            years++;
            remainingSeconds -= 31536000;
        }

        int days = (int) remainingSeconds / 86400;
        remainingSeconds = remainingSeconds % 86400;

        int hours = (int) remainingSeconds / 3600;
        remainingSeconds = remainingSeconds % 3600;

        int minutes = (int) remainingSeconds / 60;
        remainingSeconds = remainingSeconds % 60;

        return ""
                + years + " years "
                + days + " days "
                + hours + " hours "
                + minutes + " minutes "
                + remainingSeconds + " seconds";
    }

    private TimerApp() {
        JFrame frame = new JFrame("2038 Timer");

        createUI(frame);

        frame.setMinimumSize(new Dimension(400, 180));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void createUI(JFrame frame) {
        JPanel panel = new JPanel();
        panel.setBackground(Color.BLACK);
        BoxLayout layout = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(layout);

        panel.add(desc);
        panel.add(time);
        panel.add(binary);
        time.setText(calculateRemaining());

        frame.getContentPane().add(panel);

        Timer timer = new Timer(1000, event -> {
            time.setText(calculateRemaining());
            binary.setText(getBinary());
        });
        timer.start();
    }

    public static void main(String[] args) {
        new TimerApp();
    }
}

package uz.pdp;
import lombok.SneakyThrows;
import uz.pdp.bot.BotController;

public class Main {
    @SneakyThrows
    public static void main(String[] args)  {
        BotController botController=new BotController();
        botController.start();

    }
}

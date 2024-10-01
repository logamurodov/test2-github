package uz.pdp.bot;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data@AllArgsConstructor@NoArgsConstructor
public class TgUser {
    private Long chatId;
    private TgState tgState;
}

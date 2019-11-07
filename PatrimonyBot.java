import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ChatAction;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendChatAction;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import com.pengrad.telegrambot.response.SendResponse;

import java.util.List;

public class PatrimonyBot {
    TelegramBot bot = new TelegramBot("900648613:AAH7YI39GkqN_-yD5wv2TpdYU_4WmtGvyB4");

    private static final String OPTIONS = "/help";
    private static final String OPTION_ONE = "/create";
    private static final String OPTION_TWO = "/read";
    private static final String OPTION_THREE = "/update";
    private static final String OPTION_FOUR = "/delete";

    public void commands(Long id, String message) {
        switch(message) {
            case OPTIONS:
                String commandList = "*COMANDOS*\n\n/create - para cadastrar\n/read - para ler\n/update - para atualizar\n/delete - para remover";
                sendMessageWithMarkdown(id, commandList);
                break;
            case OPTION_ONE:
                sendMessage(id, "Cadastrar...");
                break;
            case OPTION_TWO:
            case OPTION_THREE:
                sendMessage(id, "Ler ou editar...");
                break;
            case OPTION_FOUR:
                sendMessage(id, "Apagar...");
                break;
            default:
                sendMessage(id, "Operação Invalida...");
                break;
        }
    }

    public boolean sendMessage(Long id, String message) {
        SendResponse sendResponse = bot.execute(new SendMessage(id, message));
        System.out.println("LOGGER: resposta enviada com sucesso? " + sendResponse.isOk());
        return sendResponse.isOk();
    }

    public boolean sendMessageWithMarkdown(Long id, String message) {
        SendResponse sendResponse = bot.execute(new SendMessage(id, message).parseMode(ParseMode.Markdown));
        System.out.println("LOGGER: resposta com markdown enviada com sucesso? " + sendResponse.isOk());
        return sendResponse.isOk();
    }

    public boolean sendChatActionTyping(Long id) {
        BaseResponse baseResponse = bot.execute(new SendChatAction(id, ChatAction.typing.name()));
        System.out.println("LOGGER: chat action typing foi enviada com sucesso? " + baseResponse.isOk());
        return baseResponse.isOk();
    }

    public void run() {
        GetUpdatesResponse updatesResponse;

        int offset = 0;
        while (true) {
            updatesResponse = bot.execute(new GetUpdates().limit(100).offset(offset));

            List<Update> updates = updatesResponse.updates();
            for (Update update : updates) {
                Long chatId = update.message().chat().id();
                String message = update.message().text();
                offset = update.updateId() + 1;

                sendChatActionTyping(chatId);
                commands(chatId, message);
            }
        }
    }
}
